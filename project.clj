(defproject xtnt-auth "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [com.h2database/h2 "1.4.199"]
                 [hikari-cp "2.9.0"]
                 [ring/ring-json "0.5.0"]
                 [ring "1.7.1"]
                 [ch.qos.logback/logback-classic "1.3.0-alpha4"]
                 [buddy/buddy-sign "3.1.0"]
                 [buddy/buddy-hashers "1.4.0"]
                 [ring-server "0.5.0"]]

  :repl-options {:init-ns xtnt-auth.core}
  :main xtnt-auth.core
  :profiles {:dev {:plugins [[lein-ring "0.12.5"]]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.9.9"]]
                    :test-paths ["test"]
                    :resource-paths ["test/resources"]}}
  )
