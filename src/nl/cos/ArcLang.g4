grammar ArcLang;

//Program is multiple statements. An expression is also a statement
program: statement* EOF;

//Statement that has no return values
statement: systemReturn S_SEMICOLON             #SystemReturnStatement
         | systemInput S_SEMICOLON              #SystemInputStatement
         | loop                                 #LoopStatement
         | condition                            #IfStatement
         | variableDeclaration S_SEMICOLON      #VariableDeclarationStatement
         | expression S_SEMICOLON               #ExpressionStatement;

//Expressions that has a return value
expression: variableInitialization                                              #VariableInitExpr
          | left=expression MATH_MUL right=expression                           #MathMulExpr
          | left=expression MATH_DIV right=expression                           #MathDivExpr
          | left=expression MATH_MOD right=expression                           #MathModExpr
          | left=expression MATH_ADD right=expression                           #MathAddExpr
          | left=expression MATH_SUB right=expression                           #MathSubExpr
          | left=expression booleanLogicalComperator right=expression           #BoolLogicalComprExpr
          | left=expression booleanComparator right=expression                  #BoolComprExpr
          | B_NOT expression                                                    #BoolNotExpr
          | systemExp                                                           #SystemExpr
          | variable                                                            #VariableExpr
          | functionInit                                                        #FunctionInitExpr
          | functionCall                                                        #FunctionCallExpr
          | literal                                                             #LiteralExpr
          | S_PAREN_OPEN expression S_PAREN_CLOSE                               #ParenedExpr;

// ==========================[ VARIABLES ]==========================
variableDeclaration: E_IDENTIFIER S_COLON varType                           #VariableEmptyDec
                   | E_IDENTIFIER S_COLON varType S_FLOW_LEFT expression    #VariableInitDec;
variableDecList: E_IDENTIFIER S_COLON varType (S_COMMA E_IDENTIFIER S_COLON varType)*;
varTypeList: varType (S_COMMA varType)*;
variableInitialization: variable S_FLOW_LEFT expression;
variable: E_IDENTIFIER;

// ==========================[ IF STATEMENT ]==========================
condition: E_COND S_PAREN_OPEN expression S_PAREN_CLOSE S_FLOW_RIGHT conditionBranchTrue conditionBranchFalse?;
conditionBranchTrue: E_COND_BRANCH_TRUE codeBlock;
conditionBranchFalse: E_COND_BRANCH_FALSE codeBlock
                      | E_COND_BRANCH_FALSE condition;

// ==========================[ FUNCTIONS ]==========================
functionInit: S_PAREN_OPEN variableDecList? S_PAREN_CLOSE S_COLON (varType | E_NULL) S_FUNC_ARROW codeBlockFunc;
functionCall: S_FUNC_CALL E_IDENTIFIER S_PAREN_OPEN expressionList? S_PAREN_CLOSE;
codeBlockFunc: S_C_PAREN_OPEN statement* (returnScope S_SEMICOLON)? S_C_PAREN_CLOSE;
returnScope: S_PAREN_OPEN S_PAREN_CLOSE S_FLOW_LEFT expression;

// ==========================[ I/O ]==========================
systemReturn: systemExp S_FLOW_LEFT expression;
systemInput: left=systemExp S_FLOW_RIGHT right=variable;
systemExp: S_SYS S_PAREN_OPEN S_PAREN_CLOSE;

// ==========================[ LOOPS ]==========================
loop: E_LOOP S_PAREN_OPEN expression S_PAREN_CLOSE codeBlock;

// ==========================[ SETS ]==========================
codeBlock: S_C_PAREN_OPEN statement* S_C_PAREN_CLOSE;
expressionList: expression (S_COMMA expression)*;

// ==========================[ DEFINITIONS ]==========================
varType: T_INT                                                                               #VarTypeInt
       | T_FLOAT                                                                             #VarTypeFloat
       | T_STRING                                                                            #VarTypeString
       | T_BOOLEAN                                                                           #VarTypeBoolean
       | T_FUNCTION                                                                          #VarTypeFunction
       | T_FUNCTION S_BRACKET_OPEN varTypeList? S_BRACKET_CLOSE S_HASH (varType | E_NULL)    #VarTypeFunctionParam;
literal: VAL_STRING   #LiteralString
       | number       #LiteralNumber
       | E_TRUE       #LiteralTrue
       | E_FALSE      #LiteralFalse
       | E_NULL       #LiteralNull;
booleanComparator: B_LESS | B_GRATER | B_EQUAL | B_NOT_EQUAL | B_GREATER_EQUAL | B_LESS_EQUAL;
booleanLogicalComperator: B_OR | B_AND;
number: MATH_SUB VAL_FLOAT          #NegativeFloat
      | MATH_SUB VAL_INT            #NegativeInt
      | VAL_INT                     #PositiveInt
      | VAL_FLOAT                   #PositiveFloat;

//Types
T_INT: 'num';
T_FLOAT: 'frac';
T_STRING: 'text';
T_BOOLEAN: 'logic';
T_FUNCTION: 'func';

//Reserved expressions
E_TRUE: 'val';
E_FALSE: 'ival';
E_NULL: 'nil';
E_LOOP: 'rep';
E_COND: 'is';
E_COND_BRANCH_TRUE: 'yes';
E_COND_BRANCH_FALSE: 'no';
E_IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

//Values
VAL_INT: [1-9][0-9]* | '0';
VAL_FLOAT: ([0-9]*[.])[0-9]+;
VAL_STRING: UNTERMINATED_STRING_LITERAL '"';

//Math operators
MATH_ADD: '+';
MATH_MUL: '*';
MATH_SUB: '-';
MATH_DIV: '/';
MATH_MOD: '%';

//Boolean operators
B_NOT: '~';
B_OR: '||';
B_AND: '&&';
B_LESS: '<';
B_GRATER: '>';
B_EQUAL: '==';
B_NOT_EQUAL: '~=';
B_GREATER_EQUAL: '>=';
B_LESS_EQUAL: '<=';

//Symbols
S_SEMICOLON: ';';
S_PAREN_OPEN: '(';
S_PAREN_CLOSE: ')';
S_C_PAREN_OPEN: '{';
S_C_PAREN_CLOSE: '}';
S_BRACKET_OPEN: '[';
S_BRACKET_CLOSE: ']';
S_FLOW_LEFT: '<-';
S_FLOW_RIGHT: '->';
S_FUNC_ARROW: '=>';
S_COLON: ':';
S_HASH: '#';
S_COMMENT: '//';
S_COMMA: ',';
S_SYS: '$';
S_FUNC_CALL: '@';

UNTERMINATED_STRING_LITERAL: '"' (~["\\\r\n] | '\\' (. | EOF))*;

WS: [\r\n\t ]+ -> skip;
LINE_COMMENT: S_COMMENT ~[\r\n]* -> skip;