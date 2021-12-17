package TrucoConsola.Utilidades;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public abstract class Utiles {

	public static Scanner s = new Scanner(System.in);
	public static Random r = new Random();
	public static String espacio = "                                            ";

	public static int validarDato(int min, int max) {
		int valor = 0;
		boolean error = false;
		do {
			error = false;
			try {
				valor = s.nextInt();
				s.nextLine();
				if (!(valor >= min && valor <= max)) {
					error = true;
					System.out.println("El valor debe estar entre " + min + " y " + max + ".\nIngrese otro valor.");
				}
			} catch(InputMismatchException e) {
				System.out.println(Utiles.espacio + "Error: Tipo de Dato Ingresado inválido. \nIngrese otro valor.");
				error = true;
				s.nextLine();
			} 
			catch (Exception e) {
				System.out.println(Utiles.espacio + "Error: " + e + ".\nIngrese otro valor.");
				error = true;
				s.nextLine();
			}
		} while (error);
		return valor;
	}

	public static String validarString(int longMin, int longMax) {
		String cadena;
		boolean error = false;
		do {
			error = false;
			cadena = s.nextLine();
			if ((cadena.length() < longMin || cadena.length() > longMax)) {
				error = true;
				System.err.println("                El dato ingresado debe tener entre " + longMin + " y " + longMax
						+ " caracteres. Ingrese otro valor.\n");
				System.err.print(Utiles.espacio + "Ingresar Nombre: ");
			}
		} while (error);
		return cadena;

	}

	public static void esperar(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void redBar() {
		System.err.print("|");
	}

	public static void oneLine() {
		System.out.println();
	}

	public static void printbrk() {
		System.out.println(
				" ========================================================================================================");

	}

}
