(ns rclone.routes.graphql
  (:require             [com.walmartlabs.lacinia :refer [execute]]
                        [clojure.java.io :as io]
                        [clojure.data.json :as json]
                        [com.walmartlabs.lacinia :refer [execute]]
                        [rclone.routes.home :refer [compiled-schema]]))

(defn variable-map
  "Reads the `variables` query parameter, which contains a JSON string
  for any and all GraphQL variables to be associated with this request.
  Returns a map of the variables (using keyword keys)."
  [request]
  (let [variables (condp = (:request-method request)
                    ;; We do a little bit more error handling here in the case
                    ;; where the client gives us non-valid JSON. We still haven't
                    ;; handed over the values of the request object to lacinia
                    ;; GraphQL so we are still responsible for minimal error
                    ;; handling
                    :get (try (-> request
                                  (get-in [:query-params "variables"])
                                  (json/read-str :key-fn keyword))
                              (catch Exception e nil))
                    :post (try (-> request
                                   :body
                                   (json/read-str :key-fn keyword)
                                   :variables)
                               (catch Exception e nil)))]
    (if-not (empty? variables)
      variables
      {})))

(defn extract-query
  "Reads the `query` query parameters, which contains a JSON string
  for the GraphQL query associated with this request. Returns a
  string.  Note that this differs from the PersistentArrayMap returned
  by variable-map. e.g. The variable map is a hashmap whereas the
  query is still a plain string."
  [request]
  (case (:request-method request)
    :get (if-let [query (get-in request [:query-params "query"])]
           query
           (str "mutation " (get-in request [:query-params "mutation"])))
    ;; Additional error handling because the clojure ring server still
    ;; hasn't handed over the values of the request to lacinia GraphQL
    :post (try (-> request
                   :body
                   (json/read-str :key-fn keyword)
                   :query)
               (catch Exception e ""))
    :else ""))

(defn ^:private graphql-handler
  "Accepts a GraphQL query via GET or POST, and executes the query.
  Returns the result as text/json."
  [comp-schema]
  (let [context {:cache (atom {})}]
    (fn [request]
      (let [vars (variable-map request)
            query (extract-query request) 
            result (execute comp-schema query vars context) 
            temp1            (spit "request.edn" (str "\nRequest    : " request
                                                      "\nVariables  : " vars
                                                      "\nQuery      : " query
                                                      "\nResult     : " result))
            status (if (-> result :errors seq)
                     400
                     200)]
        {:status  status
         :headers {"Content-Type" "application/json"}
         :body    (json/write-str result)}))))


(defn graphql-routes [request]
  (let [uri (:uri request)]
    (when  (= uri "/graphql") 
      ((graphql-handler compiled-schema) request))))

