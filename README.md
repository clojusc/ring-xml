# ring-xml

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]
[![Tag][tag-badge]][tag]
[![Clojure version][clojure-v]](project.clj)

*Ring middleware for XML requests and responses*

[![Project Logo][logo]][logo-large]


#### Contents

* [About](#about-)
* [Documentation](#documentation-)
* [Usage](#usage-)
* [License](#license-)


## About [&#x219F;](#contents)

`ring-xml` provides [Ring][ring] middleware functions for working with
[XML][xml]. It was inspired by and modeled after the [ring-json][ring json]
middleware.


## Documentation [&#x219F;](#contents)

The `ring-xml` API Reference and usage guides will be available here:
 * [http://clojusc.github.io/ring-xml/](http://clojusc.github.io/ring-xml/)


## Usage [&#x219F;](#contents)

Include `ring-xml` in your project with the following:

```clj
[clojusc/ring-xml "0.2.0-SNAPSHOT"]
```

The functionality of the library consists of two Ring handlers:

* `wrap-xml-request` checks the content-type of an incoming request and if it
  is XML then parses the body.

* `wrap-xml-response` converts outgoing collections or S-expressions to
  an XML string.

See the examples and documentation for more information.


## License [&#x219F;](#contents)

Copyright © 2013 IMIntel

Copyright © 2016 ClojuSc

Distributed under the MIT license.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/ring-xml
[travis-badge]: https://travis-ci.org/clojusc/ring-xml.png?branch=master
[deps]: http://jarkeeper.com/clojusc/ring-xml
[deps-badge]: http://jarkeeper.com/clojusc/ring-xml/status.svg
[logo]: resources/images/ring-xml-logo-x250.png
[logo-large]: resources/images/ring-xml-logo-x1000.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/ring-xml.svg
[tag]: https://github.com/clojusc/ring-xml/tags
[clojure-v]: https://img.shields.io/badge/clojure->=1.5.0-blue.svg
[clojars]: https://clojars.org/clojusc/ring-xml
[clojars-badge]: https://img.shields.io/clojars/v/clojusc/ring-xml.svg

[ring]: https://github.com/ring-clojure
[xml]: https://en.wikipedia.org/wiki/XML
[ring json]: https://github.com/ring-clojure/ring-json
