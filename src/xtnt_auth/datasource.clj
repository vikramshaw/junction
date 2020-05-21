(ns xtnt-auth.datasource
  (:require [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]))

;;https://github.com/tomekw/hikari-cp
(def datasource-options {:dbtype        "mysql"
                         :jdbc-url      "jdbc:mysql://localhost:3306/test"
                         :username      "root"
                         :password      "xtntserver"})
(defn get-ds []
  (defonce ds (make-datasource datasource-options))
  {:datasource ds})
