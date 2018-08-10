(ns rclone.assignment
  (:require [clojure.string :as str]))

(defn split-record
  [s]
  (let [v (str/split s #"\|")]
    (hash-map (Integer. (first v)) (vec (rest v)))))

(defn parse-file
  [filename]
  (->> (slurp filename)
       (str/split-lines)
       (map #(split-record %))
       (into (sorted-map))))

;;1st Question
(def customer (parse-file "cust.txt"))

;;2nd Question
(def product (parse-file "prod.txt"))

;;3rd Question
(defn format-sales
  [v]
  (let [cid (Integer. (first v))
        pid (Integer. (second v))
        cust (first (get customer cid))
        prod (first (get product pid))]
    [cust prod (last v)]))

(defn display-sales
  []
  (let [s (parse-file "sales.txt")]
    (->> s
         (map #(hash-map (first %) (format-sales (second %))))
         (into (sorted-map)))))

;;4th Question
(defn get-price
  [v]
  (let [prod (nth v 1)
        quantity (Integer. (nth v 2))]
    (->> product
         (map #(if (= prod (first (second %))) (second (second %))))
         (filter identity)
         first
         (Float.)
         (* quantity))))

(defn customer-bill
  []
  (println "Enter customer name")
  (let [name (str (read-line))]
    (->> (display-sales)
         (filter #(= name (first (second %))))
         (map #(get-price (second %)))
         (reduce +))))

;;5th Question
(defn product-sales
  []
  (println "Enter product name")
  (let [p (str (read-line))]
    (->> (display-sales)
         (filter #(= p (second (second %))))
         (map #(Integer. (last (second %))))
         (reduce +))))

(defn prompt
  []
  (println "\n*** Sales Menu ***\n------------------\n1.	Display Customer Table\n2.	Display Product Table\n3.	Display Sales Table\n4.	Total Sales for Customer\n5.	Total Count for Product\n6.	Exit\nEnter an option?")
  (let [input (Integer. (read-line))]
    (case input
      1 (println customer)
      2 (println  product)
      3 (println (display-sales))
      4 (println (customer-bill))
      5 (println (product-sales))
      6 (println "Good Bye"))
    (if (not (= input 6))
      (prompt))))
