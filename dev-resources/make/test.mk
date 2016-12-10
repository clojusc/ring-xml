kibit:
	@lein with-profile +1.8,+test kibit

eastwood:
	@lein with-profile +1.8,+test eastwood \
	"{:namespaces [:source-paths]}"

lint: kibit eastwood

lint-unused:
	@lein with-profile +1.8,+test eastwood \
	"{:linters [:unused-fn-args \
	            :unused-locals \
	            :unused-namespaces \
	            :unused-private-vars \
	            :wrong-ns-form] \
	  :namespaces [:source-paths]}"

lint-ns:
	@lein with-profile +1.8,+test eastwood \
	"{:linters [:unused-namespaces :wrong-ns-form] \
	  :namespaces [:source-paths]}"

kibit-all:
	@lein with-profile +1.8,+test kibit

eastwood-all:
	@lein with-profile +1.8,+test eastwood \
	"{:namespaces [:source-paths]}"

lint-all: kibit-all eastwood-all

lint-unused-all:
	@lein with-profile +1.8,+test eastwood \
	"{:linters [:unused-fn-args \
	            :unused-locals \
	            :unused-namespaces \
	            :unused-private-vars \
	            :wrong-ns-form] \
	  :namespaces [:source-paths]}"

lint-ns-all:
	@lein with-profile +1.8,+test eastwood \
	"{:linters [:unused-namespaces :wrong-ns-form] \
	  :namespaces [:source-paths]}"

1.5-tests:
	@lein with-profile +1.7,+test test

1.6-tests:
	@lein with-profile +1.7,+test test

1.7-tests:
	@lein with-profile +1.7,+test test

1.8-tests:
	@lein with-profile +1.8,+test test

1.9-tests:
	@lein with-profile +1.9,+test test

all-tests: 1.5-tests 1.6-tests 1.7-tests 1.8-tests 1.9-tests

check: kibit 1.8-tests

check-all: lint
	@$(MAKE) all-tests
