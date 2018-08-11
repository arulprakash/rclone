(ns rclone.semanticui
  (:require [cljsjs.semantic-ui-react]))

(def semantic-ui js/semanticUIReact)

(defn component
  [k & ks]
  (if (seq ks)
    (apply goog.object/getValueByKeys semantic-ui k ks)
    (goog.object/get semantic-ui k)))

(def container (component "Container"))
(def image (component "Image"))
(def segment (component "Segment"))
(def button (component "Button"))
(def input (component "Input"))
(def label (component "Label"))
(def slist (component "List"))
(def header (component "Header"))
(def grid (component "Grid"))
(def gridc (component "Grid" "Column"))
(def gridr (component "Grid" "Row"))

(def card (component "Card"))
(def cardhdr (component "Card" "Header"))
(def cardtxt (component "Card" "Description"))
(def cardcnt (component "Card" "Content"))
(def cardgrp (component "Card" "Group"))

(def sidebar (component "Sidebar"))
(def sbpushable (component "Sidebar" "Pushable"))
(def sbpusher (component "Sidebar" "Pusher"))
(def menu (component "Menu"))
(def menuitm (component "Menu" "Item"))
(def menumenu (component "Menu" "Menu"))

(def modal (component "Modal"))
(def modalhdr (component "Modal" "Header"))
(def modalcnt (component "Modal" "Content"))
(def modalact (component "Modal" "Actions"))

(def form (component "Form"))
(def formbtn (component "Form" "Button"))
(def formfld (component "Form" "Field"))
(def forminp (component "Form" "Input"))
(def formdd (component "Form" "Dropdown"))

(def icon (component "Icon"))
(def dropdown (component "Dropdown"))
(def select (component "Select"))
