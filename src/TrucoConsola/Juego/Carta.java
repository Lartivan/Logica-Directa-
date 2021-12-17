package TrucoConsola.Juego;

import TrucoConsola.Enums.Palos;

public class Carta {
	private int nro;
	private Palos palo;
	private boolean enJuego = false;
	private int valorTruco;
	private int valorEnvido;

	public Carta(int nro, Palos palo) {
		super();
		this.nro = nro;
		this.palo = palo;
		this.valorTruco = asignarValorTruco();
		this.valorEnvido = asignarValorEnvido();
	}

	private int asignarValorTruco() {
		if (this.nro == 4)
			return 1;
		if (this.nro == 5)
			return 2;
		if (this.nro == 6)
			return 3;
		if ((this.nro == 7) && (this.palo == Palos.COPA || this.palo == Palos.BASTO))
			return 4;
		if (this.nro == 10)
			return 5;
		if (this.nro == 11)
			return 6;
		if (this.nro == 12)
			return 7;
		if (this.nro == 1 && (this.palo == Palos.ORO || this.palo == Palos.COPA))
			return 8;
		if (this.nro == 2)
			return 9;
		if (this.nro == 3)
			return 10;
		if (this.nro == 7 && this.palo == Palos.ORO)
			return 11;
		if (this.nro == 7 && this.palo == Palos.ESPADA)
			return 12;
		if (this.nro == 1 && this.palo == Palos.BASTO)
			return 13;
		if (this.nro == 1 && this.palo == Palos.ESPADA)
			return 14;
		return 0;
		/*
		 * 1ESPADA, --> VALOR TRUCO: 14 
		 * 1BASTO --> VALOR TRUCO: 13 
		 * 7ESPADA --> VALOR TRUCO: 12 
		 * 7ORO --> VALOR TRUCO: 11 
		 * TODOS LOS 3 --> VALOR TRUCO: 10 
		 * TODOS LOS 2 --> VALOR TRUCO: 9 
		 * 1 ORO / 1 COPA --> VALOR TRUCO: 8 
		 * TODOS LOS 12 --> VALOR TRUCO: 7 
		 * TODOS LOS 11 --> VALOR TRUCO: 6 
		 * TODOS LOS 10 --> VALOR TRUCO: 5
		 * 7COPA / 7BASTO --> VALOR TRUCO: 4 
		 * TODOS LOS 6 --> VALOR TRUCO: 3 
		 * TODOS LOS 5 --> VALOR TRUCO: 2 
		 * TODOS LOS 4 --> VALOR TRUCO: 1
		 * 
		 */
	}

	private int asignarValorEnvido() {
		if(this.nro == 12 || this.nro == 11 || this.nro ==10) return 0;
		else return this.nro;
	}
	
	public int getNro() {
		return nro;
	}

	public String getPalo() {
		return palo.toString();
	}
	
	public Palos getPaloEnum() {
		return this.palo;
	}

	public boolean isEnJuego() {
		return enJuego;
	}

	public void setEnJuego(boolean enJuego) {
		this.enJuego = enJuego;
	}

	public int getValorTruco() {
		return valorTruco;
	}

	public int getValorEnvido() {
		return valorEnvido;
	}

	
}
