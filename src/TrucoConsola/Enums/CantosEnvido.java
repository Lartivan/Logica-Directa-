package TrucoConsola.Enums;

public enum CantosEnvido {
	NOCANTADO(0), ENVIDO(2), ENVIDOENVIDO(2), REALENVIDO(3), FALTAENVIDO(30);

	int valor;

	CantosEnvido(int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return this.valor;
	}

}

// CANTOS SINGULARES

//ENVIDO : 		[Q(2P)|NQ(1P)]
//REAL ENVIDO: 	[Q(3P)|NQ(1P)]
//FALTA ENVIDO	if((puntajeJ1<15 && puntajeJ2<15)) || jugadorQueGana.puntaje > jugadorQuePierdeFALTA.puntaje) --> GANA // EN BUENAS Y VAS PERDIENDO --> Cant. de Puntos que le faltan al rival para llegar a 30.


//CANTOS DOBLES

//REAL ENVIDO --> FALTA ENVIDO [NQ(3P)]
//ENVIDO --> FALTA ENVIDO [NQ(2P)]
//ENVIDO --> REAL ENVIDO [Q(5P)|NQ(2P)]
//ENVIDO --> ENVIDO		 [Q(4P)|NQ(2P)]

//CANTOS TRIPLES

//ENVIDO --> REAL ENVIDO --> FALTA ENVIDO [NQ(5P)]
//ENVIDO --> ENVIDO --> FALTA ENVIDO [NQ(4P)]
//ENVIDO --> ENVIDO --> REAL ENVIDO [Q(7P)|NQ(4P)]

//CANTOS CUÁDRUPLES

// ENVIDO --> ENVIDO --> REAL ENVIDO --> FALTA ENVIDO [NQ(7P)]






