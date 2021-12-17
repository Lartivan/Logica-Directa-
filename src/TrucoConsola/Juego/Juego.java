package TrucoConsola.Juego;

import TrucoConsola.Enums.CantosEnvido;
import TrucoConsola.Enums.CantosTruco;
import TrucoConsola.Enums.Palos;
import TrucoConsola.Utilidades.Utiles;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Juego {

    // ATRIBUTOS GENERALES RONDAS Y PARTIDA
    private Mesa mesa; // Es la mesa dónde se tienen las cartas jugadas
    private boolean rondaFinalizada, partidaFinalizada = false;
    private boolean[] pardas;
    private int contRondas;
    private boolean manoJ1 = false, turnoJ1;
    private boolean victoriaJ1;
    private int[] ganadorMano = {0, 0, 0};

    // ATRIBUTOS PUNTAJES
    private int valorEnvidoAnterior = 0, valorEnvidoActual = 0;
    private int puntajes[] = new int[2];

    // ATRIBUTOS ENVIDO
    private CantadoEnvido cantadoEnvidoActual = new CantadoEnvido(CantosEnvido.NOCANTADO);
    private boolean envidoJugado = false;

    // ATRIBUTOS TRUCO
    private CantadoTruco cantadoTrucoActual = new CantadoTruco(CantosTruco.NOCANTADO);

    // ATRIBUTOS CARTAS Y JUGADORES
    private Jugador[] jugadores;
    private ArrayList<Carta> cartas = new ArrayList<Carta>(6);

    public Juego(String nombreJ1, String nombreJ2) {
        jugadores = new Jugador[2];
        jugadores[0] = new Jugador(nombreJ1);
        jugadores[1] = new Jugador(nombreJ2);
        jugarPartida();
    }

    private void jugarPartida() {
        contRondas = 0;
        do {
            // ITERACION PARA LAS RONDAS
            contRondas++;
            iniciarRonda();
            do {
                // ITERACION PARA LAS MANOS y Acciones Dentro de una ronda
                if (puntajes[0] >= 30 || puntajes[1] >= 30) {
                    rondaFinalizada = true;
                    partidaFinalizada = true;
                } else
                    jugarRonda();
            } while (!rondaFinalizada);
            cartas.clear();
        } while (!partidaFinalizada);
        mostrarPuntaje();
        Utiles.printbrk();
        System.out.println(
                Utiles.espacio + "¡HA GANADO EL JUGADOR " + jugadores[((victoriaJ1) ? 0 : 1)].getNombre() + "!");
        System.out.println(Utiles.espacio + "PUNTAJES");
        System.out.println(Utiles.espacio + jugadores[0].getNombre() + ": " + puntajes[0] + " PUNTOS");
        System.out.println(Utiles.espacio + jugadores[1].getNombre() + ": " + puntajes[1] + " PUNTOS");
        Utiles.printbrk();
    }

    // MÉTODOS POR RONDA

    private void iniciarRonda() {
        cantadoTrucoActual = new CantadoTruco(CantosTruco.NOCANTADO);
        cantadoEnvidoActual = new CantadoEnvido(CantosEnvido.NOCANTADO);
        repartirRonda();
        manoJ1 = !manoJ1;
        turnoJ1 = manoJ1;
        mesa = new Mesa();
        pardas = new boolean[3];
        mesa.cartasTiradas = 0;
        rondaFinalizada = false;
        valorEnvidoActual = 0;
        envidoJugado = false;
    }

    private void repartirRonda() {
        System.out.println("\n\n--------------------------COMIENZA LA RONDA " + contRondas + "--------------------------");
        mostrarPuntaje();
        for (int i = 0; i < jugadores.length; i++) {
            System.out.println("JUGADOR " + (i + 1) + ": " + jugadores[i].getNombre());
        }
        repartirCartas();
    }

    private void repartirCartas() {

        // GENERANDO LA MANO DEL JUGADOR1
        for (int i = 0; i < 3; i++) {
            cartas.add(generarCarta());
        }

        // GENERANDO LA MANO DEL JUGADOR2
        for (int i = 3; i < 6; i++) {
            cartas.add(generarCarta());
        }

        mostrarCartas(1);
        mostrarCartas(2);

    }

    private Carta generarCarta() {
        boolean repetida = false;
        int palo;
        Palos paloCarta;
        int nroCarta;
        do {
            palo = Utiles.r.nextInt(3) + 1;

            if (palo == 1) {
                // Espada
                paloCarta = Palos.ESPADA;
            } else if (palo == 2) {
                paloCarta = Palos.BASTO;
            } else if (palo == 3) {
                paloCarta = Palos.ORO;
            } else {
                paloCarta = Palos.COPA;
            }
            nroCarta = (Utiles.r.nextInt(9) + 1);
            // SI EL NÚMERO DE CARTA ESTÁ ENTRE 8 Y 10, le suma 2. 8 --> 10 // 9--> 11 //
            // 10--> 12
            if (nroCarta >= 8)
                nroCarta += 2;

            repetida = chequearManos(paloCarta, nroCarta);
        } while (repetida);

        Carta cartaGenerada = new Carta(nroCarta, paloCarta);
        return cartaGenerada;
    }

    private boolean chequearManos(Palos palo, int nro) {
        for (int i = 0; i < cartas.size(); i++) {
            if (cartas.get(i).getNro() == nro && cartas.get(i).getPalo() == palo.toString())
                return true;
        }
        return false;
    }

    private void jugarRonda() {
        mostrarPuntaje();
        System.out.print("TURNO DEL JUGADOR " + jugadores[((turnoJ1) ? 0 : 1)].getNombre());
        rondaFinalizada = opcionesJugador();
    }

    private void mostrarPuntaje() {
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("PUNTAJE DEL JUGADOR 1 - " + jugadores[0].getNombre() + ": [" + puntajes[0] + " PUNTOS]");
        System.out.println("PUNTAJE DEL JUGADOR 2 - " + jugadores[1].getNombre() + ": [" + puntajes[1] + " PUNTOS]");
        System.out.println("-----------------------------------------------------------------------");
    }

    private boolean opcionesJugador() {
        System.out.println();
        System.out.println();
        if (mesa.cartasTiradas < 2) {
            System.out.print("1) Cantar Envido"); // SIEMPRE QUE SE ESTÉ EN PRIMERA TIRADA
            if (envidoJugado)
                System.out.print(" (Ya se ha jugado el Envido esta ronda)");
            System.out.println();
        }
        if (cantadoTrucoActual.getCanto() != CantosTruco.VALE4) {
            System.out.println("2) Cantar Truco");
        }
        System.out.println("3) Jugar Carta");
        System.out.println("4) Irse al Mazo");
        System.out.println("5) Guardar y Salir");
        int rta = Utiles.validarDato(1, 5);
        switch (rta) {
            case 1:
                if (mesa.cartasTiradas >= 2) {
                    System.out.println();
                    System.out
                            .println("EL ENVIDO SÓLO SE PUEDE CANTAR DURANTE LA PRIMERA MANO. Ya no puedes cantar envido");
                    System.out.println();
                } else if (!envidoJugado) {
                    // OPCIONES DE ENVIDO
                    if (cantadoEnvidoActual.getCantor().getNombre() == jugadores[(turnoJ1) ? 0 : 1].getNombre()) {
                        System.out.println("No podés volver a cantar el ENVIDO. Sólo tu rival puede.");
                    } else {
                        if (envido()) {
                            System.out.println("El jugador " + jugadores[(turnoJ1) ? 0 : 1].getNombre() + " ha cantado "
                                    + cantadoEnvidoActual.getCanto().toString() + ".");
                            return responderCantoEnvido((turnoJ1) ? 2 : 1);
                        } else {
                            return false;
                        }
                    }
                } else {
                    System.out.println("Ya se ha jugado el envido esta ronda. No puede volver a jugarse.");
                }

                return false;
            case 2:
                // OPCIONES DE TRUCO
                if (cantadoTrucoActual.getCantor().getNombre() == jugadores[(turnoJ1) ? 0 : 1].getNombre()) {
                    System.out.println("No podés volver a cantar el Truco. Sólo tu rival puede.");
                } else {
                    if (truco()) {
                        System.out.println("El jugador " + jugadores[(turnoJ1) ? 0 : 1].getNombre() + " ha cantado "
                                + cantadoTrucoActual.getCanto().toString() + ".");
                        return responderCantoTruco((turnoJ1) ? 2 : 1);
                    } else {
                        return false;
                    }
                }
                return false;
            case 3:
                // JUGAR CARTA
                jugarCarta(((turnoJ1) ? 1 : 2));
                mesa.cartasTiradas++;
                if (turnoJ1)
                    mesa.cartasTiradasJ1 += 1;
                if (!turnoJ1)
                    mesa.cartasTiradasJ2 += 1;
                mesa.mostrarMesa(((turnoJ1) ? 1 : 2));
                boolean retorno = comprobarGanadorMano();
                if (retorno)
                    actualizarPuntajeObtenidoMano();
                return retorno;
            case 4:
                // IRSE AL MAZO
                irseMazo();
                return true;
            case 5:
                // GUARDAR Y SALIR
                guardado();
                return true;
        }

        return false;
    }

    // MÉTODOS ENVIDO

    private boolean responderCantoEnvido(int nroJugadorResponde) {
        Utiles.printbrk();
        System.out.println("-----------------------" + cantadoEnvidoActual.getCanto() + "-----------------------");
        Utiles.printbrk();
        envidoJugado = true;
        System.out.println("SE REQUIERE UNA RESPUESTA DEL JUGADOR " + jugadores[nroJugadorResponde - 1].getNombre());
        System.out.println("1) QUIERO");
        System.out.println("2) NO QUIERO");

        CantosEnvido[] cantosEnvido = CantosEnvido.values();
        int rtaCanto = 0;
        CantosEnvido canto = CantosEnvido.ENVIDO;
        if (cantadoEnvidoActual.getCanto() == CantosEnvido.ENVIDO) {
            System.out.println("3) ENVIDO-ENVIDO.");
            System.out.println("4) REAL ENVIDO");
            System.out.println("5) FALTA ENVIDO");
            rtaCanto = Utiles.validarDato(1, 5);
            if (rtaCanto >= 3)
                canto = cantosEnvido[rtaCanto - 1];
        } else if (cantadoEnvidoActual.getCanto() == CantosEnvido.ENVIDOENVIDO) {
            System.out.println("3) REAL ENVIDO.");
            System.out.println("4) FALTA ENVIDO.");
            rtaCanto = Utiles.validarDato(1, 4);
            if (rtaCanto >= 3)
                canto = cantosEnvido[rtaCanto];
        } else if (cantadoEnvidoActual.getCanto() == CantosEnvido.REALENVIDO) {
            System.out.println("3) FALTA ENVIDO.");
            rtaCanto = Utiles.validarDato(1, 3);
            if (rtaCanto == 3)
                canto = cantosEnvido[rtaCanto + 1];
        } else {
            rtaCanto = Utiles.validarDato(1, 2);
        }

        if (rtaCanto == 1) {
            int ganador = calcularEnvido();
            System.out.println("¡GANA EL JUGADOR " + jugadores[ganador].getNombre() + "!");
            puntajes[ganador] += valorEnvidoActual;
        } else if (rtaCanto == 2) { // SI --> NO QUIERO
            puntajes[(turnoJ1) ? 0 : 1] += valorEnvidoAnterior;
            System.out.println("-----¡El JUGADOR " + jugadores[((turnoJ1) ? 1 : 0)].getNombre() + " NO QUIERE!-----\n");
            System.out.println("-----¡EL JUGADOR " + jugadores[((turnoJ1) ? 0 : 1)].getNombre() + " HA GANADO "
                    + valorEnvidoAnterior + " PUNTOS!-----");
            envidoJugado = true;
            return false;
        } else if (rtaCanto >= 3) {
            System.out.println("-----EL JUGADOR " + jugadores[(nroJugadorResponde - 1)].getNombre() + " HA CANTADO "
                    + canto.toString() + "-----\n");
            turnoJ1 = !turnoJ1;
            actualizarCanto(canto);
            actualizarValorEnvido(canto);
            boolean retorno = responderCantoEnvido((nroJugadorResponde == 1) ? 2 : 1);
            turnoJ1 = !turnoJ1;
            return retorno;
        }
        return false;
    }

    private boolean envido() {
        int rta = 0;
        int numeroVolver = 2;
        CantosEnvido[] cantosEnvido = CantosEnvido.values();
        CantosEnvido canto = CantosEnvido.NOCANTADO;
        System.err.println("-----OPCIONES DE CANTO DEL ENVIDO ACTUALES-----");
        if (cantadoEnvidoActual.getCanto() == CantosEnvido.NOCANTADO) {
            System.out.println("1) Cantar ENVIDO.");
            System.out.println("2) Cantar REAL-ENVIDO.");
            System.out.println("3) Cantar FALTA-ENVIDO.");
            System.out.println("4) Volver");
            rta = Utiles.validarDato(1, 4);
            numeroVolver = 4;
            if (rta == 1)
                canto = cantosEnvido[rta];
            else if (rta != 4)
                canto = cantosEnvido[rta + 1];
        } else if (cantadoEnvidoActual.getCanto() == CantosEnvido.ENVIDO) {
            System.out.println("1) Cantar ENVIDO-ENVIDO.");
            System.out.println("2) Cantar REAL-ENVIDO.");
            System.out.println("3) Cantar FALTA-ENVIDO.");
            System.out.println("4) Volver");
            rta = Utiles.validarDato(1, 4);
            numeroVolver = 4;
            if (rta != 4)
                canto = cantosEnvido[rta + 1];

        } else if (cantadoEnvidoActual.getCanto() == CantosEnvido.ENVIDOENVIDO) {
            System.out.println("1) Cantar REAL-ENVIDO.");
            System.out.println("2) Cantar FALTA-ENVIDO.");
            System.out.println("3) Volver");
            rta = Utiles.validarDato(1, 3);
            numeroVolver = 3;
            if (rta != 3)
                canto = cantosEnvido[rta + 2];

        } else if (cantadoEnvidoActual.getCanto() == CantosEnvido.REALENVIDO) {
            System.out.println("1) Cantar FALTA-ENVIDO.");
            System.out.println("2) Volver");
            numeroVolver = 2;
            canto = CantosEnvido.FALTAENVIDO;
        } else {
            System.out.println("No puede cantar envido si ya se encuentra en estado de FALTA-ENVIDO.");
            return false;
        }
        if (rta != numeroVolver) {
            actualizarCanto(canto);
            actualizarValorEnvido(canto);
            return true;
        }
        return false;

    }

    private void actualizarValorEnvido(CantosEnvido canto) {
        if (valorEnvidoAnterior == 0) {
            valorEnvidoAnterior = 1;
        } else {
            valorEnvidoAnterior = valorEnvidoActual;
        }
        valorEnvidoActual += canto.getValor();
        if (canto == CantosEnvido.FALTAENVIDO)
            faltaEnvido();
    }

    private void faltaEnvido() {
        // SI AMBOS JUGADORES ESTÁN EN MALAS, EL FALTA ENVIDO GANA EL PARTIDO
        if (puntajes[0] < 15 && puntajes[1] < 15)
            valorEnvidoActual = 30;
            // SI ESTÁ GANANDO EL JUGADOR 1 Y NO ESTÁN AMBOS EN MALAS, EL FALTA ENVIDO VALE
            // LO QUE LE FALTA AL JUGADOR 2 PARA LLEGAR A 30
        else if (puntajes[0] > puntajes[1])
            valorEnvidoActual = 30 - puntajes[1];
            // Lo mismo pero si gana el jugador 2 --> O si van iguales vale lo que le falta
            // a un jugador para llegar a 30 (si no están ambos en malas)
        else if (puntajes[0] <= puntajes[1])
            valorEnvidoActual = 30 - puntajes[0];
    }

    private void actualizarCanto(CantosEnvido canto) {
        cantadoEnvidoActual = new CantadoEnvido(canto, jugadores[(turnoJ1) ? 0 : 1]);
    }

    private int calcularEnvido() {
        // ENVIDO MANO 1
        int m1 = calculadorInternoEnvido(0, 2);
        int m2 = calculadorInternoEnvido(3, 5);
        System.out.println("-----EL JUGADOR " + jugadores[0].getNombre() + " TIENE " + m1 + " PUNTOS DE ENVIDO-----");
        System.out.println("-----EL JUGADOR " + jugadores[1].getNombre() + " TIENE " + m2 + " PUNTOS DE ENVIDO-----");
        return (m1 > m2) ? 0 : 1;
    }

    private int calculadorInternoEnvido(int indiceMin, int indiceMax) {
        int[] valorEnvido = new int[4];
        int[] contPalos = new int[4];
        for (int i = indiceMin; i < indiceMax + 1; i++) {
            switch (cartas.get(i).getPaloEnum()) {
                case BASTO:
                    valorEnvido[0] += cartas.get(i).getValorEnvido();
                    contPalos[0]++;
                    break;
                case ORO:
                    valorEnvido[1] += cartas.get(i).getValorEnvido();
                    contPalos[1]++;
                    break;
                case ESPADA:
                    valorEnvido[2] += cartas.get(i).getValorEnvido();
                    contPalos[2]++;
                    break;
                case COPA:
                    valorEnvido[3] += cartas.get(i).getValorEnvido();
                    contPalos[3]++;
                    break;
            }
        }

        //EL ENVIDO TOMA LA SUMA DE LAS 3 CARTAS
        for (int i = 0; i < contPalos.length; i++) {
            if (contPalos[i] > 1) {
                valorEnvido[i] += 20;
            }
        }

        int max = 0, iMax = 0;
        for (int i = 0; i < valorEnvido.length; i++) {
            if (valorEnvido[i] > max) {
                max = valorEnvido[i];
                iMax = i;
            }
        }
        return valorEnvido[iMax];
    }

    // MÉTODOS TRUCO

    private boolean responderCantoTruco(int nroJugadorResponde) {
        System.out.println("SE REQUIERE UNA RESPUESTA DEL JUGADOR " + jugadores[nroJugadorResponde - 1].getNombre());
        System.out.println("1) QUIERO");
        System.out.println("2) NO QUIERO");
        int rtaCanto = 0;
        CantosTruco canto = CantosTruco.TRUCO;
        if (cantadoTrucoActual.getCanto() == CantosTruco.TRUCO) {
            System.out.println("3) QUIERO RETRUCO.");
            canto = CantosTruco.RETRUCO;
            rtaCanto = Utiles.validarDato(1, 3);
        } else if (cantadoTrucoActual.getCanto() == CantosTruco.RETRUCO) {
            System.out.println("3) QUIERO VALE 4.");
            canto = CantosTruco.VALE4;
            rtaCanto = Utiles.validarDato(1, 3);
        } else {
            rtaCanto = Utiles.validarDato(1, 2);
        }
        if (rtaCanto == 2) { // SI --> NO QUIERO
            puntajes[(turnoJ1) ? 0 : 1] += cantadoTrucoActual.getCanto().getValor() - 1;
            return true;
        } else if (rtaCanto == 3) {
            System.out.println("EL JUGADOR " + jugadores[(nroJugadorResponde - 1)].getNombre() + " HA CANTADO "
                    + canto.toString());
            turnoJ1 = !turnoJ1;
            actualizarCanto(canto);
            boolean retorno = responderCantoTruco((nroJugadorResponde == 1) ? 2 : 1);
            turnoJ1 = !turnoJ1;
            return retorno;
        }
        return false;
    }

    private boolean truco() {
        int rta;
        CantosTruco canto = CantosTruco.NOCANTADO;
        if (cantadoTrucoActual.getCanto() == CantosTruco.NOCANTADO) {
            System.out.println("1) Cantar Truco");
            canto = CantosTruco.TRUCO;
        }
        if (cantadoTrucoActual.getCanto() == CantosTruco.TRUCO) {
            System.out.println("1) Cantar Retruco");
            canto = CantosTruco.RETRUCO;
        } else if (cantadoTrucoActual.getCanto() == CantosTruco.RETRUCO) {
            System.out.println("1) Cantar Vale Cuatro");
            canto = CantosTruco.VALE4;
        }
        System.out.println("2) Volver");
        rta = Utiles.validarDato(1, 2);
        if (rta == 1) {
            actualizarCanto(canto);
            return true;
        }
        return false;
    }

    private void actualizarCanto(CantosTruco canto) {
        cantadoTrucoActual = new CantadoTruco(canto, jugadores[(turnoJ1) ? 0 : 1]);
    }

    // MÉTODOS DE JUGAR CARTA

    private void jugarCarta(int nroJugador) {
        // MOSTRAMOS CARTAS DEL JUGADOR
        mostrarCartas(nroJugador);

        mostrarOpcionesCartas(nroJugador);

        // CONTROLAMOS QUE ELIJA UNA CARTA QUE SE PUEDA JUGAR
        boolean correcta;
        do {
            correcta = true;
            int rta = Utiles.validarDato(1, 3);
            if (cartas.get(((nroJugador == 1) ? (rta - 1) : (rta + 2))).isEnJuego()) {
                correcta = false;
                System.out.println("No puede elegir una carta en juego.");
                System.out.println("Elija otra carta.");
                mostrarCartas(nroJugador);
                mostrarOpcionesCartas(nroJugador);
            }
            if (correcta) {
                cartas.get(((nroJugador == 1) ? (rta - 1) : (rta + 2))).setEnJuego(true);
                if (nroJugador == 1)
                    mesa.cartasJugadasJ1.add(cartas.get(rta - 1));
                else
                    mesa.cartasJugadasJ2.add(cartas.get(rta + 2));
            }

        } while (!correcta);
        System.out.println(
                "El jugador " + nroJugador + " (" + jugadores[nroJugador - 1].getNombre() + ") tira una carta.\n");
    }

    private boolean comprobarGanadorMano() {
        if (mesa.cartasTiradas == 2) {
            if (mesa.cartasJugadasJ1.get(0).getValorTruco() == mesa.cartasJugadasJ2.get(0).getValorTruco()) {
                pardas[0] = true;
                System.out.println("-----PARDA PRIMERA-----");
                turnoJ1 = !turnoJ1;
            } else {
                ganadorMano[0] = ((mesa.cartasJugadasJ1.get(0).getValorTruco() > mesa.cartasJugadasJ2.get(0)
                        .getValorTruco()) ? 1 : 2);
                System.out.println("El ganador de la mano 1 es: " + jugadores[ganadorMano[0] - 1].getNombre());
                turnoJ1 = (ganadorMano[0] == 1) ? true : false;
            }
            return false;
        } else if (mesa.cartasTiradas == 4) {
            if (mesa.cartasJugadasJ1.get(1).getValorTruco() == mesa.cartasJugadasJ2.get(1).getValorTruco()) {
                // SI EMPARDAN SEGUNDA
                System.out.println("-----PARDA SEGUNDA-----");
                if (pardas[0]) { // SI EMPARDAN SEGUNDA Y ADEMÁS EMPARDARON PRIMERA, SE VA A LA TERCERA MANO
                    pardas[1] = true;
                    System.out.println("-----PARDA SEGUNDA----- PASAMOS A TERCERA");
                    turnoJ1 = !turnoJ1;
                    return false;
                } else if (!pardas[0]) {
                    // SI EMPARDAN SEGUNDA SIN HABER EMPARDADO PRIMERA, GANA LA RONDA EL QUE GANÓ
                    // PRIMERA
                    ganadorMano[1] = ganadorMano[0];
                    System.out.println("GANA EL GANADOR DE PRIMERA: " + jugadores[ganadorMano[0] - 1].getNombre());
                    return true;
                }
            } else {// SI NO EMPARDAN SEGUNDA
                ganadorMano[1] = ((mesa.cartasJugadasJ1.get(1).getValorTruco() > mesa.cartasJugadasJ2.get(1)
                        .getValorTruco()) ? 1 : 2);
                turnoJ1 = (ganadorMano[1] == 1) ? true : false;
                // SI EL MISMO JUGADOR GANÓ 2 MANOS, O SE HABÍA EMPARDADO PRIMERA PEOR HAY
                // GANADOR EN SEGUNDA
                if (ganadorMano[0] == ganadorMano[1] || pardas[0]) {
                    return true;
                }
            }
        } else if (mesa.cartasTiradas == 6) {
            // SI SE EMPARDA LA TERCERA
            if (mesa.cartasJugadasJ1.get(2).getValorTruco() == mesa.cartasJugadasJ2.get(2).getValorTruco()) {
                System.out.println("----- PARDA TERCERA -----");
                if (pardas[0] && pardas[1]) {// SI SE EMPARDAN LAS 3 MANOS GANA LA MANO
                    pardas[2] = true;
                    System.out.println("GANA LA RONDA LA MANO:" + jugadores[(manoJ1) ? 0 : 1].getNombre());
                } else {// SI SE EMPARDA TERCERA, SIN HABER EMPARDADO NI PRIMERA NI SEGUNDA, GANA QUIEN
                    // GANÓ LA PRIMERA
                    System.out.println("GANA EL GANADOR DE PRIMERA: " + jugadores[ganadorMano[0] - 1].getNombre());
                }
            } else {
                ganadorMano[2] = ((mesa.cartasJugadasJ1.get(2).getValorTruco() > mesa.cartasJugadasJ2.get(2)
                        .getValorTruco()) ? 1 : 2);
            }
            return true;
        } else {
            turnoJ1 = !turnoJ1;
        }
        return false;
    }

    private void actualizarPuntajeObtenidoMano() {
        int acum1 = 0, acum2 = 0;
        for (int i = 0; i < ganadorMano.length; i++) {
            if (ganadorMano[i] == 1)
                acum1++;
            if (ganadorMano[i] == 2)
                acum2++;
        }
        puntajes[(acum1 > acum2) ? 0 : 1] += cantadoTrucoActual.getCanto().getValor();
    }

    private void mostrarCartas(int nroJugador) {
        int indiceCarta = 0;
        if (nroJugador == 2)
            indiceCarta += 3;
        System.out.println("\nMANO DEL JUGADOR " + nroJugador);
        for (int i = indiceCarta; i < indiceCarta + 3; i++) {
            System.out.println("Carta " + ((nroJugador == 1) ? (i + 1) : (i - 2)) + ": " + cartas.get(i).getNro()
                    + " de " + cartas.get(i).getPalo().toString());
        }
        System.out.println();
        System.out.println();
    }

    private void mostrarOpcionesCartas(int nroJugador) {
        int indiceCarta = 0;
        if (nroJugador == 2)
            indiceCarta += 3;
        // LE PEDIMOS AL JUGADOR QUE ELIJA UNA CARTA PARA TIRAR
        for (int i = indiceCarta; i < indiceCarta + 3; i++) {
            System.out.print(((nroJugador == 1) ? (i + 1) : (i - 2)) + ") Jugar Carta "
                    + ((nroJugador == 1) ? (i + 1) : (i - 2)));
            if (cartas.get(i).isEnJuego()) {
                System.out.print(" (EN JUEGO - NO DISPONIBLE) ");
            }
            System.out.println();
        }
    }

    // MÉTODOS IRSE AL MAZO Y TERMINAR RONDA

    private void irseMazo() {
        System.out.println("El jugador " + ((turnoJ1) ? "1" : "2") + " (" + jugadores[((turnoJ1) ? 0 : 1)].getNombre()
                + ") se ha ido al mazo.");
        int puntos;
        // SI ME VOY AL MAZO DURANTE LA PRIMERA RONDA Y NO SE CANTÓ NADA SON 2 PUNTOS
        if (mesa.cartasTiradas < 2) {
            puntos = 2;
            puntajes[(turnoJ1) ? 1 : 0] += 2;
        }
        // SI ME VOY AL MAZO DURANTE LA SEGUNDA O TERCERA RONDA Y NO SE CANTÓ NADA ES 1
        // PUNTO
        else if (mesa.cartasTiradas >= 2 && cantadoTrucoActual.getCanto() == CantosTruco.NOCANTADO) {
            puntos = 1;
            puntajes[(turnoJ1) ? 1 : 0] += 1;
        }
        // SI ME VOY AL MAZO DURANTE LA SEGUNDA O TERCERA RONDA Y SE CANTARON COSAS, SON
        // LOS PUNTOS DEL TRUCO CANTADO HASTA EL MOMENTO
        else {
            puntos = cantadoTrucoActual.getCanto().getValor();
            puntajes[(turnoJ1) ? 1 : 0] += puntos;
        }
        Utiles.printbrk();
        System.out.println(Utiles.espacio + "¡EL JUGADOR " + jugadores[((turnoJ1) ? 1 : 0)].getNombre() + " HA GANADO " + puntos + " PUNTOS!");
        Utiles.printbrk();
    }

    // MÉTODOS GUARDADO

    private void guardado() {

        JSONArray pardas = new JSONArray();
        for (boolean parda : this.pardas) {
            pardas.add(parda);
        }
        JSONArray cartasJugadasJ1 = new JSONArray();
        for (Carta carta : this.mesa.cartasJugadasJ1) {
            JSONObject carta1 = new JSONObject();
            carta1.put("nro", carta.getNro());
            carta1.put("palo", carta.getPalo());
            carta1.put("enJuego", carta.isEnJuego());
            carta1.put("valorTruco", carta.getValorTruco());
            carta1.put("valorEnvido", carta.getValorEnvido());
            cartasJugadasJ1.add(carta1);
        }
        JSONArray cartasJugadasJ2 = new JSONArray();
        for (Carta carta : this.mesa.cartasJugadasJ2) {
            JSONObject carta2 = new JSONObject();
            carta2.put("nro", carta.getNro());
            carta2.put("palo", carta.getPalo());
            carta2.put("enJuego", carta.isEnJuego());
            carta2.put("valorTruco", carta.getValorTruco());
            carta2.put("valorEnvido", carta.getValorEnvido());
            cartasJugadasJ2.add(carta2);
        }
        JSONObject mesa = new JSONObject();
        mesa.put("cartasJugadasJ1", cartasJugadasJ1);
        mesa.put("cartasJugadasJ2", cartasJugadasJ2);
        mesa.put("cartasTiradas", this.mesa.cartasTiradas);
        mesa.put("cartasTiradasJ1", this.mesa.cartasTiradasJ1);
        mesa.put("cartasTiradasJ2", this.mesa.cartasTiradasJ2);


        JSONArray ganadorMano = new JSONArray();
        for (int ganaMano : this.ganadorMano) {
            ganadorMano.add(ganaMano);
        }

        JSONArray puntajes = new JSONArray();
        for (int puntaje : this.puntajes) {
            puntajes.add(puntaje);
        }
        JSONObject jugadorEnvido = new JSONObject();
        jugadorEnvido.put("nombre", this.cantadoEnvidoActual.getCantor().getNombre());

        JSONObject cantadoEnvido = new JSONObject();
        cantadoEnvido.put("canto", this.cantadoEnvidoActual.getCanto());
        cantadoEnvido.put("cantor", jugadorEnvido);

        JSONObject jugadorTruco = new JSONObject();
        jugadorTruco.put("nombre", this.cantadoTrucoActual.getCantor().getNombre());

        JSONObject cantadoTruco = new JSONObject();
        cantadoTruco.put("canto", this.cantadoTrucoActual.getCanto());
        cantadoTruco.put("cantor", jugadorTruco);

        JSONArray jugadores2 = new JSONArray();
        for (Jugador jugador : this.jugadores) {
            JSONObject j1 = new JSONObject();
            j1.put("nombre", jugador.getNombre());
            jugadores2.add(j1);
        }


        JSONArray cartas = new JSONArray();
        for (Carta carta : this.cartas) {
            JSONObject carta1 = new JSONObject();
            carta1.put("nro", carta.getNro());
            carta1.put("palo", carta.getPalo());
            carta1.put("enJuego", carta.isEnJuego());
            carta1.put("valorTruco", carta.getValorTruco());
            carta1.put("valorEnvido", carta.getValorEnvido());
            cartas.add(carta1);
        }
        JSONObject obj = new JSONObject();
        obj.put("mesa", mesa);
        obj.put("rondaFinalizada", this.rondaFinalizada);
        obj.put("partidaFinalizada", this.partidaFinalizada);
        obj.put("pardas", pardas);
        obj.put("contRondas", this.contRondas);
        obj.put("manoJ1", this.manoJ1);
        obj.put("turnoJ1", this.turnoJ1);
        obj.put("victoriaJ1", this.victoriaJ1);
        obj.put("ganadorMano", ganadorMano);
        obj.put("valorEnvidoActual", valorEnvidoActual);
        obj.put("valorEnvidoAnterior", valorEnvidoAnterior);
        obj.put("puntajes", puntajes);
        obj.put("cantadoEnvidoActual", cantadoEnvido);
        obj.put("envidoJugado", envidoJugado);
        obj.put("cantadoTrucoActual", cantadoTruco);
        obj.put("jugadores", jugadores2);
        obj.put("cartas", cartas);


        try (FileWriter file = new FileWriter("C:\\Users\\Andres\\Desktop\\TrucoFinal\\src\\guardado.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rondaFinalizada = true;
        partidaFinalizada = true;
    }


}