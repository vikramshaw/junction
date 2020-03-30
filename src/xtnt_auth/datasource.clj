(ns xtnt-auth.datasource
  (:require [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]))

;;https://github.com/tomekw/hikari-cp
;; :url- This url is from the H2 Database Login box
(def datasource-options {:adapter "h2"
                         :url     "jdbc:h2:tcp://localhost/~/test"
                         :username ""
                         :password "abc"})


(defn get-ds []
  (defonce ds (make-datasource datasource-options))
  {:datasource ds})
