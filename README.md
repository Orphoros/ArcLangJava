# Arc Compiler

> By Orphoros and [SarenDev](https://github.com/SarenDev)

---

## Project Structure

### Docs

- Contains the [Language.md](./docs/Language.md) file that describes the Arc language
- Example Arc codes
  - [Example1.arc](./docs/Example1.arc) - First 10 Fibonacci numbers
  - [Example2.arc](./docs/Example2.arc) - Check if a number is prime or not
  - [Example3.arc](./docs/Example3.arc) - Functions as parameters
  - [Example4.arc](./docs/Example4.arc) - User input
  - [Example5.arc](./docs/Example5.arc) - Print the first 10 prime numbers
- Example bad Arc codes that won't compile
  - [EdgeCaseExample1.arc](./docs/EdgeCaseExample1.arc) - Call function that is not yet initialized
  - [EdgeCaseExample2.arc](./docs/EdgeCaseExample2.arc) - If statement on the return value of a function that does not return a boolean value
  - [EdgeCaseExample3.arc](./docs/EdgeCaseExample3.arc) - Too big integer literal
  - [EdgeCaseExample4.arc](./docs/EdgeCaseExample4.arc) - Try to reach a variable from a function that is in its outer scope
  - [EdgeCaseExample5.arc](./docs/EdgeCaseExample5.arc) - Pass a function to an other function that expects an other type of function

### Src

- __Compiler package:__ Holds the [TypeChecker](./src/nl/cos/compiler/TypeChecker.java) and the [CodeGenerator](./src/nl/cos/compiler/CodeGenerator.java)
- __Exceptions package:__ Holds Arc Lang's custom [CompilerException](./src/nl/cos/exceptions/CompilerException.java)
- __Jasmin package:__ Holds the [DataType](./src/nl/cos/jasmin/DataType.java) enum (used to check the expression types) and the [MathInstr](./src/nl/cos/jasmin/MathInstr.java) enum. These enums hold jasmin specific instructions
- __typing__ package: Holds the [SymbolTable](./src/nl/cos/typing/SymbolTable.java) and the models of the various symbols in the symbols package

### Tests

The [CompilerTest](./tests/nl/cos/CompilerTest.java) java class holds all of our black box unit tests. These tests contain bad weather and good weather unit tests and tests for the example Arc files found in the docs folder.

A [TestReport](./tests/testreport/Test%20Results%20-%20CompilerTest.html) file can be found in the test folder that shows the result of our tests.

### Parser grammar

Arc Lang's Antlr grammer file can be located in [ArcLang.g4](./src/nl/cos/ArcLang.g4) file.

### VS Code syntax highlighting plugin

The util folder holds the files for a custom VS Code extension that aims to give syntax highlighting for ArcLang.

## Examples

The [Language.md](./docs/Language.md) in the docs folder shows structural examples of constructing various ArcLang statements. The docs folder holds more concrete examples in the `.arc` files. Code comments are used there to explain how different actions were used.
