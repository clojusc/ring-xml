# ring-xml

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]
[![Tag][tag-badge]][tag]
[![Clojure version][clojure-v]](project.clj)

*Ring middleware for XML requests and responses*


## About

`ring-xml` provides [Ring][ring] middleware functions for working with
[XML][xml]. It was inspired by and modeled after the [ring-json][ring json]
middleware.


## Installation

Add the following to your project.clj:

```clj
[clojusc/ring-xml "0.1.1"]
```


## Usage

The functionality of the library consists of two Ring handlers:

* `wrap-xml-request` checks the content-type of an incoming request and if it
  is XML then parses the body.

* `wrap-xml-response` converts outgoing collections or S-expressions to
  an XML string.


## License

Copyright © 2013 IMIntel

Copyright © 2016 ClojuSc

Distributed under the MIT license.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/ring-xml
[travis-badge]: https://travis-ci.org/clojusc/ring-xml.png?branch=master
[deps]: http://jarkeeper.com/clojusc/ring-xml
[deps-badge]: http://jarkeeper.com/clojusc/ring-xml/status.svg
[logo]: https://avatars1.githubusercontent.com/u/18177940?v=3&s=200
[logo-large]: https://avatars1.githubusercontent.com/u/18177940?v=3&s=1000
[tag-badge]: https://img.shields.io/github/tag/clojusc/ring-xml.svg
[tag]: https://github.com/clojusc/ring-xml/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/clojusc/ring-xml
[clojars-badge]: https://img.shields.io/clojars/v/clojusc/ring-xml.svg

[ring]: https://github.com/ring-clojure
[xml]: https://en.wikipedia.org/wiki/XML
[ring json]: https://github.com/ring-clojure/ring-json