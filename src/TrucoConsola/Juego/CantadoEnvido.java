package TrucoConsola.Juego;

import TrucoConsola.Enums.CantosEnvido;

public class CantadoEnvido {
		private CantosEnvido canto;
		private Jugador cantor;

		public CantadoEnvido(CantosEnvido canto, Jugador cantor) {
			this.canto = canto;
			this.cantor = cantor;
		}

		public CantadoEnvido(CantosEnvido canto) {// ESTE ES PARA EL CANTO POR DEFECTO, CUANDO TODAVIA NO SE CANTÓ EL ENVIDO
			this.canto = canto;
			this.cantor = new Jugador("EMPTY");
		}

		public CantosEnvido getCanto() {
			return canto;
		}

		public Jugador getCantor() {
			return cantor;
		}

}
