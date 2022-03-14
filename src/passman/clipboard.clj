(ns passman.clipboard
  (:require [babashka.process :refer [sh]]))

;; https://github.com/babashka/process
(defn copy [text]
  (-> (sh ["echo" text]) (sh ["pbcopy"]) :out))

(comment
  (copy "hello"))

