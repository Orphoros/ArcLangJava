package nl.cos;

import nl.cos.exceptions.CompilerException;
import nl.cos.io.JasminBytecode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This file shows a few different ways you can do automated tests:
 *
 *  - checkByteCode():        Shows how to compile a file and check that the bytecode that was
 *                            generated matches your expectations.
 *  - checkByteCode2():       Same as above, but now the output is stored in a file.
 *  - syntaxErrorsAreFound(): Checks that a file with syntax errors stops compilation.
 *  - checkOutputFile():      This test shows how to compile a file, run it and check if the output
 *                            matches your expectations.
 *  - checkOutputString():    Same as above, but now the source code is coming from a string within
 *                            the test.
 *
 * Not shown is a test where the file contains no syntax errors, but the checker should find some
 * error. You can of course add that yourself.
 */
class CompilerTest extends TestBase {

	@Test
	@DisplayName("GoodWeather - Output string")
	void checkOutputString() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-\"Hello World\";", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"Hello World"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Output number")
	void checkOutputNumber() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-12345;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"12345"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Output boolean")
	void checkOutputBoolean() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-ival;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Output null")
	void checkOutputNull() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-nil;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"null"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Output float")
	void checkOutputFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-1234.1234;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"1234.1234"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Basic addition")
	void checkAddition() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5+4;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"9"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Basic subtraction")
	void checkDivision() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5-4;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"1"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Basic modulus")
	void checkModulus() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-3%2;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"1"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Basic multiplication")
	void checkMultiplication() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7*3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"21"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Basic division")
	void checkSubtraction() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-8/2;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"4"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Math priority operations")
	void checkMathPriorityOperations() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7+2*4/2-1;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"10"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Math priority operations with brackets")
	void checkMathPriorityOperationsBrackets() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-(7+2)*4/2-1;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"17"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Float addition")
	void checkFloatAddition() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7.4+8.5;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"15.9"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Float subtraction")
	void checkFloatDivision() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-8.2-5.6;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"2.6"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Float modulus")
	void checkFloatModulus() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-8.0%2.0;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"0.0"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Float multiplication")
	void checkFloatMultiplication() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5.8*2.5;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"14.5"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Float division")
	void checkFloatSubtraction() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-8.4/3.5;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"2.3999999"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Math float priority operations")
	void checkMatFloatPriorityOperations() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-8.3+5.6*4.2/2.6-3.1;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"14.246153"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Math float priority operations with brackets")
	void checkMathPriorityOperationsFloatBrackets() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-(8.3+5.6)*4.2/2.6-3.1;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"19.353846"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Division by zero")
	void checkDivisionByZero() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("$()<-6/0;", "ArcTest");
				});
		assertEquals("Cannot do division by zero!", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Different number types in math")
	void checkDifferentNumbersInMath() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("$()<-5+6.5;", "ArcTest");
				});
		assertEquals("Can only do operation on the same type", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Boolean equal on numbers")
	void checkBoolEqual() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5==5;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean equal on float")
	void checkBoolEqualFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7.2==7.2;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean equal on booleans")
	void checkBoolEqualBooleans() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-ival==ival;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean not equal on numbers")
	void checkBoolNotEqual() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5~=5;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean not equal on float")
	void checkBoolNotEqualFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7.2~=7.2;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean not equal on booleans")
	void checkBoolNotEqualBooleans() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-ival~=ival;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater on ints")
	void checkBoolGrater() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-4>3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater on ints - false")
	void checkBoolGraterFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-2>3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater on floats")
	void checkBoolGraterFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-4.2>4.1;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater on floats - false")
	void checkBoolGraterFloatFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7.2>7.3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less on ints")
	void checkBoolLess() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-3<6;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less on ints - false")
	void checkBoolLessFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-7<3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less on floats")
	void checkBoolLessFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-3.2<3.6;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less on floats - false")
	void checkBoolLessFloatFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5.6<5.3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater-equal on ints")
	void checkBoolGraterE() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-4>=3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater-equal on ints - false")
	void checkBoolGraterEFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-2>=3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater-equal on floats")
	void checkBoolGraterEFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-4.6>=4.4;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean grater-equal on floats - false")
	void checkBoolGraterEFloatFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-1.5>=1.6;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less-equal on ints")
	void checkBoolLessE() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-2<=3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less-equal on ints - false")
	void checkBoolLessEFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-6<=3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less-equal on floats")
	void checkBoolLessEFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-3.5<=3.6;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean less-equal on floats - false")
	void checkBoolLessEFloatFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-5.4<=5.3;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Boolean compare on incompatible types")
	void checkBoolCompareIncompatible() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("$()<-5.4>6;", "ArcTest");
				});
		assertEquals("Can only do operation on the same type", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Boolean OR")
	void checkBoolOr() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-val||val;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean OR - 2nd")
	void checkBoolOr2() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-ival||val;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean OR - false")
	void checkBoolOrFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-ival||ival;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Boolean OR with numbers")
	void checkBoolOrWithNumbers() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("$()<-5||val;", "ArcTest");
				});
		assertEquals("Can only do boolean logical compare on boolean values!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Boolean AND")
	void checkBoolAnd() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-val&&val;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Boolean AND - false")
	void checkBoolAndFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("$()<-val&&ival;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Boolean AND with numbers")
	void checkBoolAndWithNumbers() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("$()<-5&&val;", "ArcTest");
				});
		assertEquals("Can only do boolean logical compare on boolean values!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Declare int variable")
	void checkVariableDeclarationInt() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:num;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"ldc 0",
				"istore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("GoodWeather - Declare float variable")
	void checkVariableDeclarationFloat() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:frac;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"ldc 0.0",
				"fstore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("GoodWeather - Declare string variable")
	void checkVariableDeclarationString() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:text;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"aconst_null",
				"astore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("GoodWeather - Declare boolean variable")
	void checkVariableDeclarationBool() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:logic;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"ldc 0",
				"istore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("GoodWeather - Declare multiple variables")
	void checkMultipleVarDeclare() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:text;b:num;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"aconst_null",
				"astore 2",
				"ldc 0",
				"istore 3",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("BadWeather - Declare the same variable twice")
	void checkDeclareSameVariable() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:num;a:num;", "ArcTest");
				});
		assertEquals("Variable 'a' already exists in current scope and cannot be added!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Variable declare and init")
	void checkVarDecAndInit() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:frac<-2.3;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"ldc 2.3",
				"fstore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("BadWeather - Declare and init var with wrong type")
	void checkDeclareAndInitVarWithWrongType() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:logic<-9.5;", "ArcTest");
				});
		assertEquals("Cannot assign FLOAT to BOOLEAN", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Init var that doesn't exit")
	void checkInitNonExistingVar() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a<-9.5;", "ArcTest");
				});
		assertEquals("Could not resolve variable 'a'!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Variable declare and later init")
	void checkVarDecAndInitLater() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:num;a<-6;", "ArcTest");
		assertNotNull(code);

		assertArrayEquals(new String[] {
				".bytecode 49.0",
				".class public ArcTest",
				".super java/lang/Object",
				"",
				".method public static main([Ljava/lang/String;)V",
				".limit stack 99",
				".limit locals 99",
				"ldc 0",
				"istore 2",
				"ldc 6",
				"istore 2",
				"return",
				".end method",

		}, code.getLines().toArray());
	}

	@Test
	@DisplayName("GoodWeather - Resolve variable value")
	void checkResolveVariableValue() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:frac;a<-6.12;$()<-a;", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"6.12"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Store incompatible number in var")
	void checkIncompatibleNumInVar() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a<-9.5;", "ArcTest");
				});
		assertEquals("Could not resolve variable 'a'!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - If statement true branch")
	void checkIfStatementTrue() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("is(val) -> yes {$() <- val;} no {$() <- ival;}", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"true"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - If statement false branch")
	void checkIfStatementFalse() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("is(ival) -> yes {$() <- val;} no {$() <- ival;}", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"false"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - If statement has a non-boolean condition")
	void checkIfStatementNonBool() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("is(6+2) -> yes {$() <- val;}", "ArcTest");
				});
		assertEquals("Condition statement expected a boolean!", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Variable not in scope")
	void checkVarNotInScope() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("is(val) -> yes {a:num <- 6;} a<-7;", "ArcTest");
				});
		assertEquals("Could not resolve variable 'a'!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Variable in outer scope")
	void checkVarInOuterScope() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("a:num <- 5; $()<-a;is(val) -> yes {a<-10; $()<-a;}", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"5","10"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Loop")
	void checkLoop() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("index:num <- 1; rep(index <= 5) {$()<-index; index <- index + 1;}", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[] {"1","2","3","4","5"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Function returns when type is void")
	void checkFuncVoidReturn() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func<-():nil=>{()<-1;};", "ArcTest");
				});
		assertEquals("Cannot return from a function with a return type of NIL", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Function return when mismatch")
	void checkFuncMismatchReturn() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func<-():num=>{()<-4.6;};", "ArcTest");
				});
		assertEquals("Function expected to return INT, but found FLOAT", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Function does not return when expected")
	void checkFuncNoReturn() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func<-():num=>{$()<-1;};", "ArcTest");
				});
		assertEquals("Function is supposed to return INT, but does not", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Assign function to an other type variable")
	void checkFuncToNonFuncVariable() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:logic<-():num=>{()<-1;};", "ArcTest");
				});
		assertEquals("Cannot assign function to non-function variable", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Assign function to an other type variable")
	void checkForUninitFunc() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func; @a();", "ArcTest");
				});
		assertEquals("Cannot execute function variable 'a', as it is not initialized", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Access var in func from outer scope")
	void checkFuncReachOuterVar() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("b:logic<-val;a:func<-():logic=>{()<-b;};", "ArcTest");
				});
		assertEquals("Could not resolve variable 'b'!", e.getMessage());
	}

	@Test
	@DisplayName("BadWeather - Function returns Function with signature")
	void checkFuncRetFuncWithSign() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func<-():func[num,num]#logic=>{()<-val;};", "ArcTest");
				});
		assertEquals("Cannot return a Function Parameter", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Function normal execution")
	void checkFunctionCall() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("f:func<-(n:num):num=>{()<-n+1;};$()<-@f(4);", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"5"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Function declare and then init, then call")
	void checkFuncDecInitCall() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("f:func;f<-(n:num):num=>{()<-n+1;};$()<-@f(9);", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"10"}, output.toArray());
	}

	@Test
	@DisplayName("GoodWeather - Function inside a function")
	void checkFuncInFunc() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("x:func<-(n:num):nil=>{addOne:func<-(t:num):num=>{()<-t+1;};$()<-@addOne(n);};@x(5);", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"6"}, output.toArray());
	}

	@Test
	@DisplayName("BadWeather - Redeclare function parameter")
	void checkFuncParamRedeclare() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					c.compileString("a:func<-(n:num):logic=>{n:frac<-3.2;()<-val;};", "ArcTest");
				});
		assertEquals("Variable 'n' already exists in current scope and cannot be added!", e.getMessage());
	}

	@Test
	@DisplayName("GoodWeather - Pass function as parameter")
	void checkFuncAsParameter() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileString("add:func <- (n1:num,n2:num,callBack:func[num,num]#nil):num => {@callBack(n1,n2);()<-n1+n2;};printer:func <- (n1:num,n2:num):nil => {$()<-n1;$()<-n2;};$()<-@add(7,3,printer);", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"7","3","10"}, output.toArray());
	}

	@Test
	@DisplayName("Complex test - File: Example1.arc")
	void example1() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileFile("docs/Example1.arc", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"0","1","1","2","3","5","8","13","21","34"}, output.toArray());
	}

	@Test
	@DisplayName("Complex test - File: Example2.arc")
	void example2() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileFile("docs/Example2.arc", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"Your number is a prime number!"}, output.toArray());
	}

	@Test
	@DisplayName("Complex test - File: Example3.arc")
	void example3() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileFile("docs/Example3.arc", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"2","Cannot divide by 0!", "0"}, output.toArray());
	}

	@Test
	@DisplayName("Complex test - File: Example5.arc")
	void example5() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileFile("docs/Example5.arc", "ArcTest");
		assertNotNull(code);

		List<String> output = runCode(code);
		assertArrayEquals(new String[]{"2","3", "5", "7", "11", "13", "17", "19", "23", "29"}, output.toArray());
	}

	@Test
	@DisplayName("Complex test - File: EdgeCaseExample1.arc")
	void exampleEC1() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					JasminBytecode code = c.compileFile("docs/EdgeCaseExample1.arc", "ArcTest");
					assertNotNull(code);
				});
		assertEquals("Cannot execute function variable 'myFunc', as it is not initialized", e.getMessage());
	}

	@Test
	@DisplayName("Complex test - File: EdgeCaseExample2.arc")
	void exampleEC2() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					JasminBytecode code = c.compileFile("docs/EdgeCaseExample2.arc", "ArcTest");
					assertNotNull(code);
				});
		assertEquals("Condition statement expected a boolean!", e.getMessage());
	}

	@Test
	@DisplayName("Complex test - File: EdgeCaseExample3.arc")
	void exampleEC3() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					JasminBytecode code = c.compileFile("docs/EdgeCaseExample3.arc", "ArcTest");
					assertNotNull(code);
				});
		assertEquals("Number literal is incorrect!", e.getMessage());
	}

	@Test
	@DisplayName("Complex test - File: EdgeCaseExample4.arc")
	void exampleEC4() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					JasminBytecode code = c.compileFile("docs/EdgeCaseExample4.arc", "ArcTest");
					assertNotNull(code);
				});
		assertEquals("Could not resolve variable 'index'!", e.getMessage());
	}

	@Test
	@DisplayName("Complex test - File: EdgeCaseExample5.arc")
	void exampleEC5() {
		Exception e = assertThrows(CompilerException.class,
				()->{
					Compiler c = new Compiler();
					JasminBytecode code = c.compileFile("docs/EdgeCaseExample5.arc", "ArcTest");
					assertNotNull(code);
				});
		assertEquals("Can only do operation on the same type", e.getMessage());
	}
}
