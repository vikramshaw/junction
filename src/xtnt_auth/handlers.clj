(ns xtnt-auth.handlers
  (:require [xtnt-auth.service :as service]))

(defn create-auth-token [req]
  (let [[ok? res] (service/create-auth-token (:datasource req)
                                             (:auth-conf req)
                                             (:params req))]
    (if ok?
      {:status 201 :body res}
      {:status 401 :body res})))

(defn sign-up [req]
  (let [[ok? res] (service/sign-up (:datasource req)
                                   (:params req))]
    (if ok?
      {:status 201 :body res}
      {:status 401 :body res})))

(defn password-recovery [req]
  (let [[ok? res] (service/password-recovery (:datasource req)
                                             (:params req))]
    (if ok?
      {:status 201 :body res}
      {:status 401 :body res})))

(defn refresh-auth-token [req]
  (let [refresh-token (-> req :params :refresh-token)
        [ok? res] (service/refresh-auth-token (:datasource req)
                                              (:auth-conf req)
                                              refresh-token)]
    (if ok?
      {:status 201 :body res}
      {:status 401 :body res})))

(defn invalidate-refresh-token [req]
  (let [refresh-token (-> req :params :refresh-token)
        [ok? res] (service/invalidate-refresh-token (:datasource req)
                                                    (:auth-conf req)
                                                    refresh-token)]
    (if ok?
      {:status 200 :body res}
      {:status 401 :body res})))
