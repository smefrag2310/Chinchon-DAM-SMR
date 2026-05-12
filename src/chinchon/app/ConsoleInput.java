package chinchon.app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInput {

	private Scanner keyboard;
	private static ConsoleInput console;

	private ConsoleInput() {
		this.keyboard = new Scanner(System.in);
	}

	private void cleanInput() {
		keyboard.nextLine();
	}

	public static ConsoleInput getInstance() {
		if (console == null) {
			console = new ConsoleInput();
		}
		return console;
	}
	
	public void write(String message) {
		System.out.print(message);
	}
	
	public void writeLine(String message) {
		System.out.println(message);
	}

	// --------------------------------------------------- INT -------------------------------------------------------

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

	// --------------------------------------------- DOUBLE
	// ----------------------------------------------------

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

	public String readString() {
		return keyboard.nextLine();
	}

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
