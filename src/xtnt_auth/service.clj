(ns xtnt-auth.service
  (:require [buddy.hashers      :as hs]
            [xtnt-auth.store    :as store]
            [buddy.sign.generic :as sign]
            [buddy.sign.jws     :as jws]
            [buddy.core.keys    :as ks]
            [clj-time.core      :as t]
            [clojure.java.io    :as io]))

;; Adding a user
(defn add-user! [ds user]
  (store/add-user! ds (update-in user [:password] #(hs/encrypt %))))

;;Authenticating a user
(defn auth-user [ds credentials]
  (let [user (store/find-user ds (:username credentials))
        unauthed [false {:message "Invalid username or password"}]]
    (if user
      (if (hs/check (:password credentials) (:password user))
        [true {:user (dissoc user :password)}]
        unauthed)
      unauthed)))

(defn- pkey [auth-conf]
  (ks/private-key
   (io/resource (:privkey auth-conf))
   (:passphrase auth-conf)))

(defn create-auth-token [ds auth-conf credentials]
  (let [[ok? res] (auth-user ds credentials)
        exp (-> (t/plus (t/now) (t/days 1)) (jws/to-timestamp))]
    (if ok?
      [true {:token (jws/sign res
                              (pkey auth-conf)
                              {:alg :rs256 :exp exp})}]
      [false res])))
