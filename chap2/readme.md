# Lexer / Tokenizer
This chapter contains the `lexer` for the minijava specification. This implementation uses `javacc`. Some notes: 

* Includes some generic primitive types mainly compliant with standard java. But additionally a `String` type that is `c-like` (a pointer to a **nul terminated string**). This is done so it interfaces nicely with functions provided by the c standard library for easy linking when we want to print to `STDOUT`.
* Integer literals are supported additionally with any "base". Eg. to represent `0xFFFF` we can equivalently write `16_FFFF` (denoting base 16).
* char/String literals eg. `'a'` or `"The quick brown fox jumps over the lazy dog"` as well as boolean literal `true/false`.
* Operators for expressions including arithmetic/logical/bitwise an ebject field/array access.
* Support for standard single line comments `//...` and multiline comments (including nesting) `/* ... */`
* Generic program constructs `if/then/else/while` and objects/inheritence `class/extends`.
* No discussion of variable 'scopes' so far. `public/private/abstract/static/etc...`. `public/void` are included because of how iconic `psvm` is as well as to provide a clear entry point into the program.
>Note that the language supports inner functions. Variable access within inner functions to outer scope functions are done via static linking as in the textbook.

For now we omit constructing the abstract syntax tree and verifying the syntax is compliant with the grammar (delegated to chap3 with parsing). It's design is to be covered later.

Compile/build using `make` which generates the requires helper classes in `lib/` for the token manager (namely the Token class and some error handling). The actual java `.class`(es) are generated in `bin/` compiled under `javac` (JDK 24). Notably this contains the `ParserTokenManager.class` which is a java program which reads input from `STDIN` and outputs its corresponding token (until an EOF token is read via `Ctrl-C/Z`).

Tests are within the `tests/` directory and are accompanied with a  `.mj` file (standing for `minijava` i.e the custom program we want to tokenize). The tests generate a result pair for each test case in the `out/` directory. Namely an `.out` file (the expected output for the lexer) and an `.err` file (the expected error for the lexer or empty if none). The most basic test is the sample `Factorial` program provided in the textbook. Additional tests cover these other edge cases. These tests can be generated using the testing script `./run_tests.sh` (assuming that the program is compiled using `make`). Alternatively run `java -classpath bin/ ParserTokenManager < path_to_file.mj` to see the lexer run on individual files as required.