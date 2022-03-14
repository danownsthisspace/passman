(ns passman.password)

;; https://github.com/danownsthisspace/clojure-command-line-password-generator/blob/main/src/onthecodeagain/password.clj
(defn generate-password [length]
  (let [available-chars (reduce (fn [acc val]
                                  (str acc (char val))) "" (range 33 123))]
    (loop [password ""]
      (if (= (count password) length)
        password
        (recur (str password (rand-nth available-chars)))))))

(comment
  (generate-password 50))