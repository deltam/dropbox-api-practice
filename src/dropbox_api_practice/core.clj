(ns dropbox-api-practice.core
  (:require [clojure.contrib.json :as json]
            [clojure.contrib.http.agent :as ha]
            [oauth.signature :as sig]
            [oauth.client :as cl]
            [com.twinql.clojure.http :as http]))


(def *dropbox-api-root* "https://api.dropbox.com/0")
(def *dropbox-access-token* (str *dropbox-api-root* "/token"))
(def *dropbox-account-info* (str *dropbox-api-root* "/account/info"))
(def *dropbox-metadata* (str *dropbox-api-root* "/metadata/dropbox"))
(def *dropbox-files* (str *dropbox-api-root* "/files/dropbox"))
(def *dropbox-create-folder* (str *dropbox-api-root* "/fileops/create_folder"))

(def request-methods {:get "GET", :post "POST"})

(defn new-consumer [key secret]
  {:key key, :secret secret :signature-method :hmac-sha1})

(defn get-access-token [consumer email password]
  (let [api-url (str *dropbox-access-token* "?email=" email "&password=" password "&oauth_consumer_key=" (:key consumer))
        agent (ha/http-agent api-url)]
    (json/read-json (ha/string agent))))


(defn oauth-params [consumer access-token]
  (sig/oauth-params consumer (:token access-token)))

(defn base-str
  ([url params] (base-str url params :get))
  ([url params method] (sig/base-string (request-methods method) url params)))

(defn signature [base-string consumer access-token]
  (sig/sign consumer base-string (:secret access-token)))

(defn oauth-header [params]
  (cl/authorization-header params))

(defn oauth-request
  ([consumer access-token url] (oauth-request consumer access-token url :get {}))
  ([consumer access-token url method request-params]
     (let [params (oauth-params consumer access-token)
           bstr (base-str url params)
           sign (signature bstr consumer access-token)
           resp (ha/http-agent url
                               :method (request-methods method)
                               :headers {"Authorization" (oauth-header (assoc params :oauth_signature sign))})]
;                               :body (sig/url-form-encode request-params))]
       (json/read-json (ha/string resp)))))


;; API Wrapper

(defn get-account-info [consumer access-token]
  (oauth-request consumer access-token *dropbox-account-info*))

(defn get-metadata [consumer access-token path]
  (oauth-request consumer access-token (str *dropbox-metadata* path)))

(defn get-files [consumer access-token path]
  (oauth-request consumer access-token (str *dropbox-files* path)))

;(defn create-folder [consumer access-token root path]
;  (oauth-request consumer access-token *dropbox-create-folder* :post {:root root :path path}))


(comment
  ;; Dropbox API Docs
  ;; https://www.dropbox.com/developers/docs
  
  ; GET /account/info
  (def my-consumer (new-consumer "developer-token" "developer-secret"))
  (def my-access-token (get-access-token my-consumer "user mailaddress" "password"))
  (get-account-info my-consumer my-access-token)
)
