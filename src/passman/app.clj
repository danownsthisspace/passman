(ns passman.app
  (:require [clojure.tools.cli :refer [parse-opts]]
            [passman.db :as db]
            [passman.stash :as stash]
            [passman.clipboard :refer [copy]]
            [passman.password :refer [generate-password]]
            [table.core :as t]))

;; https://github.com/cldwalker/table
;; https://github.com/clojure/tools.cli

(def cli-options
  [["-l" "--length Length" "Password Length"
    :default 40
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-g" "--generate" "Generate new password"]
   [nil "--list"]])

(defn password-input []
  (println "Enter your master key:")
  (String. (.readPassword (System/console))))

(defn -main [& args]
  (let [parsed-options (parse-opts args cli-options)
        url (first (:arguments parsed-options))
        username (second (:arguments parsed-options))
        options (:options parsed-options)]
    (cond
      (:generate options) (do
                            (stash/stash-init (password-input))
                            (let [password (generate-password (:length options))]
                              (db/insert-password url username)
                              (stash/add-password url username password)
                              (println "Password copied to clipboard")
                              (copy password)))

      (and url username) (do
                           (stash/stash-init (password-input))
                           (let [password (stash/find-password url username)]
                             (copy password)
                             (println "Password copied to clipboard")))

      (:list options) (t/table (db/list-passwords)))))

(comment
  (t/table (list-passwords)))