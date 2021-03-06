(ns ku-expo.auth
  (:require [ring.util.response :refer [response resource-response redirect]]
            [ring.util.json-response :refer [json-response]]
            [ku-expo.utils.db :as db]))

;; TODO
;; 1 Collapse user registration functions
(defn login
  []
  (resource-response "login.html" {:root "public/html"}))

(defn wrong-login
  []
  (resource-response "wrong-login.html" {:root "public/html"}))

(defn registration
  []
  (resource-response "register.html" {:root "public/html"}))

(defn new-password
  []
  (resource-response "new-password.html" {:root "public/html"}))

(defn username-valid?
  [params]
  (let [{:keys [username]} params]
    (json-response {:valid 
                    (if (= (db/user-exists? username) false)
                      true
                      false)})))

(defn register-user
  [params]
  (let [{:keys [fullname username phone password]
         :or [phone nil]} params]
    (do
      (db/create-user fullname username phone password "#{:ku-expo.handler/teacher}")
      (resource-response "registration-response.html" {:root "public/html"}))))

(defn change-password
  [params]
  (let [{:keys [username password]} params]
    (if (= (db/user-exists? username) true)
      (do
        (db/new-password username password)
        (redirect "/login"))
      (resource-response "wrong-username.html" {:root "public/html"}))))

(defn register-scorer
  [params]
  (let [{:keys [fullname username phone password]
         :or [phone nil]} params]
    (json-response (db/create-user fullname username phone password "#{:ku-expo.handler/group}"))))
