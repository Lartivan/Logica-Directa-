package TrucoConsola.Juego;

import TrucoConsola.Enums.CantosTruco;

public class CantadoTruco {
	private CantosTruco canto;
	private Jugador cantor;

	public CantadoTruco(CantosTruco canto, Jugador cantor) {
		this.canto = canto;
		this.cantor = cantor;
	}

	public CantadoTruco(CantosTruco canto) {// ESTE ES PARA EL CANTO POR DEFECTO, CUANDO TODAVIA NO SE CANTÓ EL TRUCO
		this.canto = canto;
		this.cantor = new Jugador("EMPTY");
	}

	public CantosTruco getCanto() {
		return canto;
	}

	public Jugador getCantor() {
		return cantor;
	}

}
