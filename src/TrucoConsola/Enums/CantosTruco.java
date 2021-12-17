package TrucoConsola.Enums;

public enum CantosTruco {
	NOCANTADO("No Cantado", 0), TRUCO("Truco", 2), RETRUCO("Retruco", 3), VALE4("Vale4", 4);

	String nombre;
	int valor;

	CantosTruco(String nombre, int valor) {
		this.nombre = nombre;
		this.valor = valor;
	}

	public String getNombre() {
		return this.nombre;
	}
	
	public int getValor() {
		return this.valor;
	}
	
}
