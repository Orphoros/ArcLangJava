package nl.cos;

import nl.cos.io.AssembledClass;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SandBox {
	private ArrayList<String> output = new ArrayList<>();

	/**
	 * Java loads classes (and all of their dependencies) using a class loader.
	 * We inherit from this, so we can use defineClass() to parse the class data
	 * and load our program.
	 */
	private static class SandBoxClassLoader extends SecureClassLoader {
		Class<?> loadFromData( byte[] classData, String className ) {
			Class<?> c = defineClass(className, classData, 0, classData.length);
			resolveClass(c);
			return c;
		}
	}

	public void runClass( AssembledClass aClass ) {
		// Load the class in a separate ClassLoader
		SandBoxClassLoader classLoader = new SandBoxClassLoader();
		Class<?> loadedClass = classLoader.loadFromData(aClass.getClassBytes(), aClass.getClassName());

		// Try to invoke the main method
		PrintStream oldSystemOut = System.out;
		ByteArrayOutputStream outputData = new ByteArrayOutputStream();
		PrintStream outputStream = new PrintStream(outputData);

		try {
			System.setOut(outputStream);

			Method mainMethod = loadedClass.getMethod("main", String[].class);
			String[] args = new String[0];
			Object[] methodArgs = {args};
			mainMethod.invoke(null, methodArgs);

			System.out.flush();
			splitOutputIntoLines(outputData);

		} catch( NoSuchMethodException nsme ) {
			throw new IllegalArgumentException("Class file has no main");
		} catch( IllegalAccessException | InvocationTargetException e ) {
			throw new IllegalArgumentException("Failed to start main");
		} finally {
			// Reset System.out to the original stream
			System.setOut(oldSystemOut);
		}
	}

	private void splitOutputIntoLines( ByteArrayOutputStream baos ) {
		String allOutput = baos.toString();
		output.clear();
		output.addAll(Arrays.asList(allOutput.split("(\r)?\n")));
	}

	public List<String> getOutput() {
		return output;
	}
}
