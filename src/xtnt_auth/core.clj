(ns xtnt-auth.core
  (:require [compojure.core                 :refer [defroutes ANY POST]]
            [ring.middleware.reload         :refer [wrap-reload]]
            [ring.middleware.params         :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json           :refer [wrap-json-response
                                                    wrap-json-params]]
            [xtnt-auth.datasource           :refer [get-ds]]
            [xtnt-auth.dbtable              :refer [create-db seed]]
            [xtnt-auth.handlers             :as     handlers]
            [ring.server.standalone         :as     server]))

(defn bootstrap []
  (println "Bootstrapping....")
  (let [ds (get-ds)]
    (create-db ds)
    (seed ds)))

(defroutes app-routes
  (ANY "/" [] (fn [req] (println req) "Index of xtnt auth"))
  (POST "/create-auth-token" [] handlers/create-auth-token)
  (POST "/refresh-auth-token" [] handlers/refresh-auth-token)
  (POST "/invalidate-refresh-token" [] handlers/invalidate-refresh-token)
  (POST "/sign-up" [] handlers/sign-up)
  (resources "/")
	(not-found "<h1>Page not found</h1>"))

(defn wrap-datasource [handler]
  (fn [req]
    (handler (assoc req :datasource (get-ds)))))

(defn wrap-config [handler]
  (fn [req]
    (handler (assoc req :auth-conf {:privkey "xtnt_privkey.pem"
                                    :pubkey "xtnt_pubkey.pem"
                                    :passphrase "secret-key"}))))

(def app
  (-> app-routes
      wrap-datasource
      wrap-config
      wrap-keyword-params
      wrap-json-params
      wrap-json-response))

(defn -main []
     (server/serve app {:port 4000
                        :init bootstrap})
  (println (str "Server is running on port 4000")))
