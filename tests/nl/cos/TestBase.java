package nl.cos;

import nl.cos.exceptions.AssembleException;
import nl.cos.io.AssembledClass;
import nl.cos.io.JasminBytecode;

import java.util.List;


/**
 * Abstract class from which test can extend.
 * Supplies the tests with a runCode method.
 */
public abstract class TestBase {

    /**
     * Helper method that takes some compiled Jasmin byte code, assembles it and
     * runs the class. It returns the output of the execution, which you can use
     * to check in an assert.
     */
    protected List<String> runCode(JasminBytecode code ) throws AssembleException {
        // Turn the Jasmin code into a (hopefully) working class file
        if( code == null ) {
            throw new AssembleException("No valid Jasmin code to assemble");
        }
        AssembledClass aClass = AssembledClass.assemble(code);

        // Run the class and return the output
        SandBox s = new SandBox();
        s.runClass(aClass);
        return s.getOutput();
    }
}
