# Arc Lang

Arc Lang is a statically typed language designed to carry out math-based tasks and operations.
Arc is a language designed to be simple to write with as few characters as possible.

Arc supports:

- The basic five math operations
- Boolean logical operations
- Variables
- User inputs
- Printing to the screen
- Loops
- Conditionals with branches
- Functions in variables

## Syntax

### Usage of semicolon

An arc program is a collection of statements (that do not return/have a value) and expressions (that do return or have values). Expressions also count as statements.

All statements must end with a `;`. If an expression is not part of another expression and stands by itself, it has to end with a `;` too!

### Dataflow

Arc uses three symbols to mark the flow of data:

- `->`: Data flows from left to right
  - Same as `=`
  - Currently used explicitly for user input statements
  - Value on the left will be assigned to the value on the right
- `<-` Data flows from right to left
  - Same as `=`
  - Used everywhere where a value needs to be assigned/returned
  - Value on the right will be assigned to the value on the left
- `=>`: Flow parameters into a code block
  - Gives parameters into the scope of a code block
  - Currently used explicitly for function parameters
  - Gives the parameters on the left to the code block on the right

### Scope/System

Arc uses empty parentheses `()` to indicate a scope above the current one.

To return a data from the current scope to the outer scope, Arc uses the following syntax:

```arc
() <- EXPRESSION;
```

- This will exit the scope with the value of the expression.

To mark the system scope, use the `$` sign in front of the scope symbol. Thus, we can directly return a value to the system scope (console) and get a value from there.

We can return a value to the system scope from any scope (print a value) using the following syntax:

```arc
$() <- EXPRESSION;
```

### Type define

Once variables are declared, their type must be indicated. This can be done using the type define symbol: `:`.

Thus, we can define the type of a variable using the following syntax:

```Arc
VARNAME: TYPE;
```

This type define symbol is also used to describe the return type of a function.

### Function parameter

When we want to pass a function as a parameter for another function, Arc expects to have all details of a function as a parameter: return type and parameters.

We can define the parameters of a function parameter using the following syntax:

```Arc
FUNCTION_PARAM_NAME[TYPE, TYPE, TYPE]
```

- Where the brackets contain a list of expected types, such as `num` or `text`.

We can then define the expected return type of the function parameter using `#` in the following syntax:

```arc
FUNCTION_PARAM_NAME[]#TYPE
```

- Where `TYPE` is a variable type, such as `num` or `text`.

### Function execution - executor

When we want to instruct Arc to interpret the value of a variable as a piece of code to run, we can use the `@` symbol before the variable name. The executer symbol can only be used with type `func` variables.

## Types

### Special types

- `nil`: Equates to void and null.
  - Nil is used to indicate emptiness
  - Can be used as a return type for functions that do not return anything
  - Can be assigned to `text` types when text is not defined

### Common types

- `num`: Whole number
  - Default value: `0`
  - Can be any positive or negative whole number
- `frac`: Floating number
  - Default value: `0.0`
  - Can be any positive or negative floating point number
- `text`: String
  - Default value: `nil`
  - Can be any text between `"`
- `logic`: Boolean
  - Default value: `ival`
  - Can be `val` (true) or `ival` (false)
- `func`: Function
  - Default value: `nil`
  - Can be a piece of code that can be executed

## Math operators

Arc supports five math operations. Arc prioritizes Division, Multiplication, and Modulus by default over Addition and Subtraction. It is possible to change the default order of operations using brackets (`()`).

Math operators must be used with the following syntax:

```Arc
EXPRESSION OPERATOR EXPRESSION
```

The `EXPRESSION` can be of the following values:

- `num`
- `frac`

The operator can be of the following values:

- `+` Addition
- `-` Subtraction
- `/` Division
- `*` Multiplication
- `%` Modulus

## Boolean operators

Boolean operators turn the result of an expression into an expression of a type `logic` (boolean).

Arc has two values that represent a logical value:

- `val`: Short for valid (true)
- `ival`: Short for invalid (false)

### Not operator

The "not" logical operator can invert a logical value.

This operator can be used with the following syntax:

```Arc
~ EXPRESSION
```

- Where the expression is a type of `logic`
- The `~` symbol means "not"

### Logical comparator (And, Or)

It is possible to check two logical operators and convert them into a new logical operator using logical comparator operators.

Logical comparator operators can be used with the following syntax:

```Arc
EXPRESSION COMPARATOR EXPRESSION
```

- Where the expression is a type of `logic`
- Where the comparator can be:
  - `||`: The logical "or" comparator
  - `&&`: The logical "and" comparator

### Logical math comparator

It is also possible to compare two expressions with the same type (`num` / `frac`) into a logical value.

Logical comparator operators on numbers can be used with the following syntax:

```Arc
EXPRESSION COMPARATOR EXPRESSION
```

- Where the comparator can be:
  - `<` Less
  - `>` Greater
  - `==` Is equal
  - `~=` Not equal
  - `>=` Greater or equal
  - `<=` Less or equal
- Both expressions have to be of the same type
- The expression can be a type of `num` or `frac`

## Comments

It is possible to write a single line comments using the following syntax:

```arc
// Your comment goes here
```

## Console outputs

Using Arc, it is possible to print expressions to the console.

Using the following syntax, Arc will print to the console:

```arc
$() <- EXPRESSION;
```

- Where `EXPRESSION` is a type of: `num`, `frac`, `logic` or `text`

## User input

Arc can prompt the console to ask for user input and then store it in a variable. The user input is always a type of `text`

Using the following syntax, Arc will save a user input in a variable:

```arc
$() -> VARIABLE;
```

- Where the `VARIABLE` is a type of `text`

## Loop

We can repeat a code multiple times using the following expression:

```arc
rep (EXPRESSION) {
    STATEMENTS
}
```

- Where `EXPRESSION` returns a logical `val` or `ival`.

## Condition

We can branch our code based on a logical value using the `is` statement ("if" statement).

```arc
is (EXPRESSION) -> 
yes{
    STATEMENTS
}
no is (EXPRESSION) -> 
    yes {
        STATEMENTS
    }
    no {
        STATEMENTS
    }
```

The expression in the parentheses must always return a type of `logic`. If the expression is `val`, the `yes` block will be executed. If the expression is `ival`, the `no` statement will be executed. The `no` block is optional and does not have to be defined. The `yes` block must be present at all times.

It is also possible to chain `no` blocks with other `is` statements (like else-if statements) just by putting `is` after `no`.

## Variables

In Arc, a variable is a piece of text that begins with a letter and then has numbers or other letters. What makes a variable not just a bit of text is that it has a type too defined by `:`.

A variable can have a type of `num`, `logic`, `text`, `func`, or `frac`.

1. Declare a variable:

```Arc
myVariable: num;
```

2. Declare and initialize a variable at the same time:

```arc
index: num <- 6;
```

3. Initialize a variable:

```arc
result <- 2.3531;
```

## Functions

In Arc, a function cannot exist without being assigned to a variable with a type of `func`.

There are three steps to take when we want to use functions.

1. Declaration

First, when we want to create a function, we must declare a variable with the type of `func`. This variable will be our function.

```arc
VAR_NAME:func;
```

2. Initialization

After declaration, we can assign a piece of code to the variable that can be executed. During the initialization step, we can give zero, one or more parameters for our function to be used inside the parentheses.

For function, a return type must be defined at all times. This can be `num`, `text`, etc. If the function does not return anything, `nil` can be used as a return type to indicate that the function is "void".

When a function has a return type defined, the function must always end with a return scope statement. This has to be the last instruction of a function's code block. If the function returns `nil`, a return scope statement must not be defined.

```arc
VAR_NAME <- (PARAMETER, PARAMETER):FUNCTION_RETURN_TYPE => {
    STATEMENTS;
    STATEMENTS;
    () <- VALUE_TO_RETURN;
}
```

The parameters of a function must be variable names with type definitions, such as `a:num`.

The code block of a function has an entirely separate scope. This means that statements inside a function code block can only access the function parameters at the max.

It is also possible to collapse both this step and the previous into one instruction by replacing `VAR_NAME` with `VAR_NAME:func`.

3. Execution with output

When we want to execute a piece of code stored in a `func` type variable, we can use the executer `@` symbol before the function variable to run the code. Function executions is an expression. If the function has a return value, the variable will be resolved to the return value of the function and not to the type of the variable (that would be `func`)

```Arc
@VAR_NAME_OF_TYPE_FUNC(VALUE,VALUE);
```
