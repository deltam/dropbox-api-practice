# dropbox-api-practice

Practice for using Dropbox API on Clojure.

Dependes `clj-oauth` version 1.2.10.


## Usage

0. Make Account on Dropbox.

1. Create "My Apps" on [Dropbox for Developers page](https://www.dropbox.com/developers/apps).

2. Get API Keys(`developer-token` and `developer-secret`).

3. `lein deps`, `lein repl`


## Examples

* Getting Dropbox account info.

    (use 'dropbox-api-practice.core)
    (def my-consumer (new-consumer "developer-token" "developer-secret"))
    (def my-access-token (get-access-token my-consumer "user mailaddress" "password"))
    (get-account-info my-consumer my-access-token)


* Getting file metadata.

Before run this code,  make test file `~/Dropbox/api_test/test.txt`.

    (get-metadata my-consumer myaccess-token "/api_test/test.txt")

Edit test file, after run same code, check to update metadata.


## References

http://d.hatena.ne.jp/teru_kusu/20110120/1295520678

https://www.dropbox.com/developers/docs

https://github.com/mattrepl/clj-oauth

http://tzmtk.pbworks.com/w/page/7618696/OAuthCore10aJP

https://github.com/aria42/clj-dropbox


## License

Copyright (c) 2010 deltam (MISUMI Masaru).

Licensed under the MIT License (http://www.opensource.org/licenses/mit-license.php)