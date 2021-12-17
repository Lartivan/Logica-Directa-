package TrucoConsola.Juego;

import java.util.ArrayList;

public class Mesa {

	public ArrayList<Carta> cartasJugadasJ1 = new ArrayList<Carta>(3);
	public ArrayList<Carta> cartasJugadasJ2 = new ArrayList<Carta>(3);
	public int cartasTiradas;
	public int cartasTiradasJ1, cartasTiradasJ2;

	public Mesa() {
		cartasTiradas = 0;
		limpiarMesa();
	}

	public void limpiarMesa() {
		cartasJugadasJ1.clear();
		cartasJugadasJ2.clear();
	}

	public void mostrarMesa(int nroJugador) {
		System.out.println();
		System.out.println();
		System.out.println();
		// COMPROBACIONES MANO 1
		if ((cartasTiradasJ1 == 1 && cartasTiradasJ2 == 1) || cartasTiradas >= 2)
			imprimirMano(cartasJugadasJ1.get(0), cartasJugadasJ2.get(0), 1); // SI AMBOS JUGADORES TIRARON 1 CARTA SE
																				// IMPRIMEN AMBAS
		if (cartasTiradasJ1 == 1 && cartasTiradasJ2 == 0)
			imprimirMano(cartasJugadasJ1.get(0), null, 1); // SI SE TIRO UNA SE IMPRIME UNA
		if (cartasTiradasJ1 == 0 && cartasTiradasJ2 == 1)
			imprimirMano(null, cartasJugadasJ2.get(0), 1);

		// COMPROBACIONES MANO 2
		if ((cartasTiradasJ1 == 2 && cartasTiradasJ2 == 2) || cartasTiradas >= 4)
			imprimirMano(cartasJugadasJ1.get(1), cartasJugadasJ2.get(1), 2);
		if (cartasTiradasJ1 == 2 && cartasTiradasJ2 == 1)
			imprimirMano(cartasJugadasJ1.get(1), null, 2);
		if (cartasTiradasJ1 == 1 && cartasTiradasJ2 == 2)
			imprimirMano(null, cartasJugadasJ2.get(1), 2);

		// COMPROBACIONES MANO 3
		if (cartasTiradasJ1 == 3 && cartasTiradasJ2 == 3)
			imprimirMano(cartasJugadasJ1.get(2), cartasJugadasJ2.get(2), 3);
		if (cartasTiradasJ1 == 3 && cartasTiradasJ2 == 2)
			imprimirMano(cartasJugadasJ1.get(2), null, 3);
		if (cartasTiradasJ1 == 2 && cartasTiradasJ2 == 3)
			imprimirMano(null, cartasJugadasJ2.get(2), 3);
	}

	private void imprimirMano(Carta carta1, Carta carta2, int nroMano) {
		if (carta2 == null) {
			System.out.println(
					"----------------------------------Mano " + nroMano + "----------------------------------");
			System.out.println("| (Jugador 1)[" + carta1.getNro() + " de " + carta1.getPalo()
					+ "] VS (Jugador 2)[Aún no ha jugado]|");
			System.out.println("--------------------------------------------------------------------------");
		} else if (carta1 == null) {
			System.out.println(
					"----------------------------------Mano " + nroMano + "----------------------------------");
			System.out.println("| (Jugador 1)[Aún no ha jugado] " + " VS (Jugador 2)["  +carta2.getNro() + " de "
					+ carta2.getPalo() + "]|");
			System.out.println("--------------------------------------------------------------------------");
		} else {
			System.out.println(
					"----------------------------------Mano " + nroMano + "----------------------------------");
			System.out.println("| (Jugador 1)[" + carta1.getNro() + " de " + carta1.getPalo() + "] VS (Jugador 2)["
					+ carta2.getNro() + " de " + carta2.getPalo() + "] |");
			System.out.println("--------------------------------------------------------------------------");
		}
	}

}
