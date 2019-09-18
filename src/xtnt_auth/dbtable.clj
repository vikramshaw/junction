(ns xtnt-auth.dbtable
  (:require [clojure.java.jdbc :as jdbc]
            [xtnt-auth.service :as service]))

(defn create-db [ds]
  (jdbc/with-db-connection [conn ds]
    (jdbc/db-do-commands conn
                         "create table user (
                           id integer auto_increment primary key,
                           username varchar(255) not null,
                           password varchar(255) not null)"

                         "create table application (
                           id integer auto_increment primary key,
                           name varchar(255) not null)"

                         "create table role (
                           id integer auto_increment primary key,
                           application_id integer not null,
                           name varchar(255) not null,
                           foreign key (application_id) references application (id))"

                         "create table user_role (
                           id integer auto_increment primary key,
                           role_id integer not null,
                           user_id integer not null,
                           foreign key (role_id) references role (id),
                           foreign key (user_id) references user (id))"

                         "create table refresh_token (
                           id integer auto_increment primary key,
                           user_id integer not null,
                           valid boolean default true not null,
                           issued long not null,
                           token varchar2(512) not null,
                           foreign key (user_id) references user (id),
                           unique (user_id, issued))")))


(defn seed [ds]
  (jdbc/with-db-transaction [conn ds]
    (jdbc/insert! conn :application
                  [:id :name]
                  [10 "webstore"]
                  [20 "crome"]
                  [30 "xtnt-admin"]
                  [40 "catalog"])
    (jdbc/insert! conn :role
                  [:id :application_id :name]
                  [10 10 "customer"]
                  [11 10 "store-admin"]
                  [20 20 "customer-support"]
                  [21 20 "accounting"]
                  [30 30 "sysadmin"]
                  [40 40 "catalog-admin"]
                  [41 40 "customer"]))

  ;; add a couple of users for testing/demo
  (service/add-user! ds {:username "test"
                         :password "secret"
                         :user-roles [{:role-id 10} {:role-id 41}]})
  (service/add-user! ds {:username "admin"
                         :password "secret"
                         :user-roles [{:role-id 11} {:role-id 40}]}))
