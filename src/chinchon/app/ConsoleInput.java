package chinchon.app;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class responsible for handling console input and output operations.
 * 
 * This class centralizes all user interaction through the console, ensuring
 * that the data entered matches the expected format and constraints. It follows
 * the Singleton design pattern to guarantee a single shared instance.
 */

public class ConsoleInput {

	private Scanner keyboard;
	private static ConsoleInput console;

	/**
     * Private constructor to prevent external instantiation.
     * Initializes the internal {@link Scanner} used for reading console input.
     */
	
	private ConsoleInput() {
		this.keyboard = new Scanner(System.in);
	}

	/**
     * Clears the input buffer to avoid residual data affecting subsequent reads.
     */
	
	private void cleanInput() {
		keyboard.nextLine();
	}
	
	/**
     * Returns the single instance of {@code ConsoleInput} following the
     * Singleton design pattern.
     *
     * @return the shared {@code ConsoleInput} instance
     */

	public static ConsoleInput getInstance() {
		if (console == null) {
			console = new ConsoleInput();
		}
		return console;
	}
	
	/**
     * Writes a message to the console without a trailing newline.
     *
     * @param message the text to display
     */
	
	public void write(String message) {
		System.out.print(message);
	}
	

    /**
     * Writes a message to the console followed by a newline.
     *
     * @param message the text to display
     */
	
	public void writeLine(String message) {
		System.out.println(message);
	}

	// --------------------------------------------------- INT -------------------------------------------------------

	/**
     * Reads an integer from the console, ensuring that the input is valid.
     * If the user enters invalid data, the method prompts again.
     *
     * @return a valid integer value
     */
	
	public int readInt() {
		int value = 0;
		boolean error;
		do {
			try {
				value = keyboard.nextInt();
				error = false;
			} catch (InputMismatchException e) {
				System.out.printf("Debes introducir un número entero y que no se salga de rango %d, %d: ",
						Integer.MIN_VALUE, Integer.MAX_VALUE);
				error = true;
			} finally {
				cleanInput();
			}
		} while (error);
		return value;
	}
	
	/**
     * Reads an integer strictly less than the specified upper bound.
     *
     * @param upperBound the exclusive upper limit
     * @return a valid integer less than {@code upperBound}
     */

	public int readIntLessThan(int upperBound) {
		int value;
		do {
			value = readInt();
			if (value >= upperBound) {
				System.out.printf("%d debe ser menor que %d, Introduce otro número: ", value, upperBound);
			}
		} while (value >= upperBound);
		return value;
	}
	
	/**
     * Reads an integer less than or equal to the specified upper bound.
     *
     * @param upperBound the inclusive upper limit
     * @return a valid integer ≤ {@code upperBound}
     */

	public int readIntLessOrEqualThan(int upperBound) {
		int value;
		do {
			value = readInt();
			if (value > upperBound) {
				System.out.printf("%d debe ser menor o igual a %d, Introduce otro número: ", value, upperBound);
			}
		} while (value > upperBound);
		return value;
	}
	
	 /**
     * Reads an integer strictly greater than the specified lower bound.
     *
     * @param lowerBound the exclusive lower limit
     * @return a valid integer greater than {@code lowerBound}
     */

	public int readIntGreaterThan(int lowerBound) {
		int value;
		do {
			value = readInt();
			if (value <= lowerBound) {
				System.out.printf("%d debe ser mayor que %d, Introduce otro número", value, lowerBound);
			}
		} while (value <= lowerBound);
		return value;
	}
	
	 /**
     * Reads an integer greater than or equal to the specified lower bound.
     *
     * @param lowerBound the inclusive lower limit
     * @return a valid integer ≥ {@code lowerBound}
     */

	public int readIntGreaterOrEqualThan(int lowerBound) {
		int value;
		do {
			value = readInt();
			if (value < lowerBound) {
				System.out.printf("%d debe ser mayor o igual a %d, Introduce otro número: ", value, lowerBound);
			}
		} while (value < lowerBound);
		return value;
	}
	
	 /**
     * Reads an integer within a specified inclusive range.
     *
     * @param lowerBound the minimum allowed value
     * @param upperBound the maximum allowed value
     * @return a valid integer within the range [{@code lowerBound}, {@code upperBound}]
     */

	public int readIntInRange(int lowerBound, int upperBound) {
		int value;
		do {
			value = readInt();
			if (value < lowerBound || value > upperBound) {
				System.out.printf("%d está fuera del rango %d - %d, Introduce otro número: ", value, lowerBound,
						upperBound);
			}
		} while (value < lowerBound || value > upperBound);
		return value;
	}
	
	/**
     * Reads an integer that must match one of two allowed options.
     *
     * @param firstOption  the first valid option
     * @param secondOption the second valid option
     * @return the chosen valid option
     */

	public int readIntTwoOptions(int firstOption, int secondOption) {
		int value;
		do {
			value = readInt();
			if (value != firstOption && value != secondOption) {
				System.out.printf("%d no es una de las opciones dadas, introduce %d o %d: ", value, firstOption,
						secondOption);
			}
		} while (value != firstOption && value != secondOption);
		return value;

	}

	// --------------------------------------------- DOUBLE ----------------------------------------------------

	 /**
     * Reads a double value from the console, validating the input format.
     *
     * @return a valid double value
     */
	
	public double readDouble() {
		double value = 0;
		boolean error;
		do {
			try {
				value = keyboard.nextDouble();
				error = false;
			} catch (InputMismatchException e) {
				System.out.printf("Debes introducir un número decimal y que no se salga de rango %d, %d: ",
						Double.MIN_VALUE, Double.MAX_VALUE);
				error = true;
			} finally {
				cleanInput();
			}
		} while (error);
		return value;
	}
	
	 /**
     * Reads a double strictly less than the specified upper bound.
     *
     * @param upperBound the exclusive upper limit
     * @return a valid double less than {@code upperBound}
     */

	public double readDoubleLessThan(double upperBound) {
		double value;
		do {
			value = readDouble();
			if (value >= upperBound) {
				System.out.printf("%d debe ser menor que %d, Introduce otro número: ", value, upperBound);
			}
		} while (value >= upperBound);
		return value;
	}
	
	 /**
     * Reads a double less than or equal to the specified upper bound.
     *
     * @param upperBound the inclusive upper limit
     * @return a valid double ≤ {@code upperBound}
     */

	public double readDoubleLessOrEqualThan(double upperBound) {
		double value;
		do {
			value = readDouble();
			if (value > upperBound) {
				System.out.printf(".2f debe ser menor o igual a %.2f, Introduce otro número: ", value, upperBound);
			}
		} while (value > upperBound);
		return value;
	}
	
	 /**
     * Reads a double strictly greater than the specified lower bound.
     *
     * @param lowerBound the exclusive lower limit
     * @return a valid double greater than {@code lowerBound}
     */

	public double readDoubleGreaterThan(double lowerBound) {
		double value;
		do {
			value = readDouble();
			if (value <= lowerBound) {
				System.out.printf("%.2f debe ser mayor que %.2f, Introduce otro número", value, lowerBound);
			}
		} while (value <= lowerBound);
		return value;
	}
	
	/**
     * Reads a double greater than or equal to the specified lower bound.
     *
     * @param lowerBound the inclusive lower limit
     * @return a valid double ≥ {@code lowerBound}
     */

	public double readDoubleGreaterOrEqualThan(double lowerBound) {
		double value;
		do {
			value = readDouble();
			if (value < lowerBound) {
				System.out.printf("%.2f debe ser mayor o igual a %.2f, Introduce otro número", value, lowerBound);
			}
		} while (value < lowerBound);
		return value;
	}
	
	 /**
     * Reads a double within a specified inclusive range.
     *
     * @param lowerBound the minimum allowed value
     * @param upperBound the maximum allowed value
     * @return a valid double within the range [{@code lowerBound}, {@code upperBound}]
     */

	public double readDoubleInRange(double lowerBound, double upperBound) {
		double value;
		do {
			value = readDouble();
			if (value < lowerBound || value > upperBound) {
				System.out.printf("%.2f está fuera del rango %.2f - %.2f, Introduce otro número: ", value, lowerBound,
						upperBound);
			}
		} while (value < lowerBound || value > upperBound);
		return value;
	}

	// ------------------------------------------ CHAR ---------------------------------------------------------------

	 /**
     * Reads a single character from the console.
     * If the user enters more than one character, the method prompts again.
     *
     * @return the character entered by the user
     */
	
	public char readChar() {
		String input;
		do {
			input = keyboard.next();
			if (input.trim().length() != 1) {
				System.out.println("**Cadena inválida** Por favor, introduce un único carácter: ");
			}
		} while (input.length() != 1);
		cleanInput();
		return input.charAt(0);
	}

	// ------------------------------------------ STRING ------------------------------------------------------------

	 /**
     * Reads a full line of text from the console.
     *
     * @return the string entered by the user
     */
	
	public String readString() {
		return keyboard.nextLine();
	}

	 /**
     * Reads a string with a maximum allowed length.
     *
     * @param maxLength the maximum number of characters permitted
     * @return a valid string with length ≤ {@code maxLength}
     */
	
	public String readString(int maxLength) {
		String input;
		do {
			input = readString();
			cleanInput();
			if (input.length() > maxLength) {
				System.out.printf("El máximo de carácteres es %d, por favor, introduce otra cadena: ", maxLength);
			}
		} while (input.length() > maxLength);
		return input;
	}

	// ------------------------------------------ BOOLEAN ---------------------------------------------------------

	 /**
     * Reads a boolean value using two character options (e.g., 'y'/'n').
     * The comparison is case-insensitive.
     *
     * @param affirmativeValue the character representing {@code true}
     * @param negativeValue    the character representing {@code false}
     * @return {@code true} if the user enters {@code affirmativeValue},
     *         {@code false} if the user enters {@code negativeValue}
     */
	
	public boolean readBooleanUsingChar(char affirmativeValue, char negativeValue) {
		char input;
		boolean character;
		character = true;

		do {
			input = readChar();
			input = Character.toLowerCase(input);
			if (input == Character.toLowerCase(affirmativeValue)) {
				character = true;
			} else if (input == Character.toLowerCase(negativeValue)) {
				character = false;
			} else {
				System.out.printf("**Carácter inválido** Introduce %s o %s para continuar: ", affirmativeValue,
						negativeValue);
			}
		} while (input != affirmativeValue && input != negativeValue);
		return character;
	}
}
