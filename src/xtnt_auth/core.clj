(ns xtnt-auth.core
  (:require [compojure.core                 :refer [defroutes ANY POST]]
            [ring.adapter.jetty     :as jetty]
            [ring.middleware.params         :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json           :refer [wrap-json-response wrap-json-params]]
            [xtnt-auth.datasource           :refer [get-ds]]
            [xtnt-auth.handlers             :as    handlers]))

(defroutes app-routes
  (POST "/create-auth-token" [] handlers/create-auth-token))


(defn wrap-datasource [handler]
  (fn [req]
    (handler (assoc req :datasource (get-ds)))))

(defn wrap-config [handler]
  (fn [req]
    (handler (assoc req :auth-conf {:privkey "auth_privkey.pem"
                                    :passphrase "secret-key"}))))

(def app
  (-> app-routes
      wrap-datasource
      wrap-config
      wrap-keyword-params
      wrap-json-params
      wrap-json-response))
(defn -main
	"A very simple web server using Ring and Jetty"
	[port-number]
	(jetty/run-jetty app
                   {:port 6001}))
