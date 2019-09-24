(ns xtnt-auth.datasource
  (:require [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]))


(def datasource-options {:adapter "h2"
                         :url     "jdbc:h2:tcp://localhost/~/test"
                         :username ""
                         :password ""})


(defn get-ds []
  (defonce ds (make-datasource datasource-options))
  {:datasource ds})
