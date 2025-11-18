.PHONY: all build clean
all:	build
build:
	javacc -OUTPUT_DIRECTORY="lib" ./Lexer.jj
	javac -sourcepath lib -d bin lib/*.java
clean:
	rm -rf bin lib output
