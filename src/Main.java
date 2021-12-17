import TrucoConsola.Juego.Juego;
import TrucoConsola.Utilidades.Utiles;
public class Main {
    private static boolean finalizado = false;
    private static Juego juego;

    public static void main(String[] args) {

        while (!finalizado) {
            Utiles.printbrk();
            System.out.println("||" + Utiles.espacio + "MENU DE TRUCO" + Utiles.espacio + " ||");
            System.out.println("||                                                                                                      ||");
            System.out.println("||                                    1) Nuevo Juego Local 1vs1                                         ||");
            System.out.println("||                                    2) Cargar Partida Guardada                                        ||");
            System.out.println("||                                    3) Opciones                                                       ||");
            System.out.println("||                                    4) Salir                                                          ||");
            Utiles.printbrk();

            System.out.print(Utiles.espacio + "Ingresar opcion: ");
            funcionMenu(Utiles.validarDato(1, 4));

        }
        Utiles.s.close();
    }

    private static void funcionMenu(int opcion) {
        if (opcion == 1) {
            //Creo un nuevo juego
            crearJuego();
        } else if (opcion == 2) {
            //Cargo una partida
        } else if (opcion == 3) {
            //Men� Opciones
        } else {
            finalizado = true;
        }
    }

    private static void crearJuego() {
        Utiles.printbrk();
        System.out.print("                                      Ingrese el nombre el jugador 1.\n");
        System.out.print(Utiles.espacio + "Ingresar Nombre: ");
        String jugador1 = Utiles.validarString(3, 20);
        Utiles.printbrk();
        System.out.println();
        System.out.println();
        Utiles.printbrk();

        System.out.println("                                      Ingrese el nombre del jugador 2");
        System.out.print(Utiles.espacio + "Ingresar Nombre: ");
        String jugador2 = Utiles.validarString(3, 20);
        Utiles.printbrk();

        juego = new Juego(jugador1, jugador2);
    }
    private void cargado(){

    }
}

