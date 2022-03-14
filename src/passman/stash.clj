(ns passman.stash
  (:require [babashka.pods :as pods]))

;; https://github.com/rorokimdim/stash
(def ^{:doc "path to stash file"} stash-file-path "passman.stash")

(babashka.pods/load-pod 'rorokimdim/stash "0.2.0")

(require '[pod.rorokimdim.stash :as stash])

(defn stash-init
  "Initializes stash.
  If `stash-file-path` does not exist, it will be created."
  [password]
  (stash/init {"encryption-key" password
               "stash-path" stash-file-path
               "create-stash-if-missing" true}))

(defn stash-add
  "Adds a new node under a parent."
  [parent-id k v]
  (stash/add parent-id k v))

(defn add-password [url username password]
  (stash-add 0 (str url username) password))


(defn stash-nodes
  "Gets all nodes stored in stash.
  If a parent-node-id is provided, only nodes with that parent-id are returned."
  ([] (stash-nodes 0))
  ([parent-id] (stash/nodes parent-id)))

(defn find-password [url username]
  (let [nodes (stash-nodes)
        key (str url username)
        found-node (first (filter (fn [n]
                                    (= key (:key n))) nodes))]
    (:value found-node)))

(comment
  (stash-nodes)
  (add-password "facebook.com" "dan@test.com" "secret")
  (find-password "facebook.com" "dan@test.com")
  (stash-init "password"))

