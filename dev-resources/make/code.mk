build: clean
	@lein compile
	@lein uberjar

repl:
	@lein with-profile +1.8 repl

clean-all: clean clean-docs

clean:
	@rm -rf target pom.xml

mvn-tree:
	@lein pom
	@mvn dependency:tree

deps-tree:
	@lein with-profile +1.8,+test deps :tree
	@lein with-profile +1.8,+test deps :plugin-tree

loc:
	@find src -name "*.clj" -exec cat {} \;|wc -l
