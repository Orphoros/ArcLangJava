package nl.cos.io;

import jasmin.ClassFile;
import nl.cos.exceptions.AssembleException;

import java.io.*;

public class AssembledClass {
	/** After compilation, this contains the class data. Write that to a file and
	 * Java can execute your program. */
	private byte[] classBytes;

	private String className;

	/**
	 * Constructor. Not for public use: use AssembledClass.assemble to assemble
	 * a piece of Jasmin-code.
	 */
	private AssembledClass( byte[] classBytes, String className ) {
		this.classBytes = classBytes;
		this.className = className;
	}

	/**
	 * Assembles Jasmin code into a (hopefully valid) JVM-compatible class file.
	 *
	 * @throws AssembleException if Jasmin code was not valid
	 */
	public static AssembledClass assemble( JasminBytecode jasminBytecode )
			throws AssembleException {
		try {
			ClassFile classFile = new ClassFile();

			// Merge all Jasmin lines into a single string
			StringBuilder combinedCode = new StringBuilder();
			for( String line : jasminBytecode.getLines() ) {
				combinedCode.append(line);
				combinedCode.append('\n');
			}

			// Read in the jasmin code
			BufferedReader input = new BufferedReader(new StringReader(combinedCode.toString()));
			classFile.readJasmin(input, jasminBytecode.getClassName() + ".j", true);
			input.close();

			// Write to a class
			ByteArrayOutputStream classOut = new ByteArrayOutputStream();
			classFile.write(classOut);
			byte[] classBytes = classOut.toByteArray();
			return new AssembledClass(classBytes, jasminBytecode.getClassName());
		} catch ( Exception e ) {
			// Jasmin just uses Exception in the exception specification, which is quite ugly
			// Let's wrap it in a more specific Exception.
			throw new AssembleException(e.getMessage(), e);
		}
	}

	/**
	 * Write the assembled class file - runnable by the JVM - to a file.
	 * @param classFileName  The full path where to write the class file to.
	 * @throws IOException     if the file could not be written, e.g. because of
	 *                         security rights.
	 */
	public void writeClassToFile( String classFileName ) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(classFileName);
		fileOut.write(this.classBytes);
		fileOut.close();
	}

	/**
	 * Returns if the Jasmin-code has been successfully assembled into an actual class file.
	 * The binary data for this class can then be retrieved using getClassBytes().
	 */
	public boolean isAssembled() {
		return (classBytes != null);
	}

	/**
	 * Get the compiled class as a byte array. This is exactly how the bytes would be stored
	 * on disc.
	 */
	public byte[] getClassBytes() {
		return classBytes;
	}

	public String getClassName() {
		return className;
	}
}
