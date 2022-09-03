package nl.cos.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JasminBytecode {
    /** After successful compilation, this contains all Jasmin commands. */
    private ArrayList<String> jasminCode;

    /** Name of the compiled class */
    private String className;

    /**
     * Create an instance with an empty list of Jasmin lines.
     */
    public JasminBytecode( String className ) {
        this.className = className;
        this.jasminCode = new ArrayList<>();
    }

    /**
     * Create an instance that keeps track of the compiled Jasmin code. You can
     * pass this to AssembledClass::assemble() to actually build a class file
     * from this.
     *
     * @param className   The name of the class that was compiled.
     * @param jasminCode  A list of Jasmin instructions.
     */
    public JasminBytecode(String className, List<String> jasminCode ) {
        this.className = className;
        this.jasminCode = new ArrayList<>(jasminCode);
    }

    /**
     * Add an empty line to this byte code.
     */
    public JasminBytecode add() {
        return add("");
    }

    /**
     * Add a line of Jasmin code.
     *
     * @param line  A single line of Jasmin code.
     * @return      A reference to this object, so that you can chain calls to add like this:
     *              <pre>
     *              jasminCode.add( "ldc 3" )
     *                        .add( "ldc 5" )
     *                        .add( "iadd" );
     *              </pre>
     */
    public JasminBytecode add( String line ) {
        jasminCode.add(line);
        return this;
    }

    /**
     * Add a whole number of lines to this JasminBytecode.
     * Pro-tip: you can also use this to combine two JasminBytecode instances:
     * <pre>
     *     jasminCode1.addAll( jasminCode2.getLines() );
     * </pre>
     *
     * @param lines  The lines to add.
     * @return       A reference to this object, so you can chain calls.
     * @see          #add(String)
     */
    public JasminBytecode addAll( List<String> lines ) {
        jasminCode.addAll(lines);
        return this;
    }

    /**
     * Write the jasmin byte code (in text form) to a file. You can use this to
     * debug your code.
     *
     * @param jasminFileName   Path to write the Jasmin code to.
     * @throws IOException     if the file could not be written, e.g. because of
     *                         security rights.
     */
    public void writeJasminToFile( String jasminFileName ) throws IOException {
        PrintWriter jasminOut = new PrintWriter(new FileWriter(jasminFileName));
        for( String line : jasminCode )
            jasminOut.println(line);
        jasminOut.close();
    }

    public String getClassName() {
        return className;
    }

    public List<String> getLines() {
        return jasminCode;
    }
}
