package uniandes.algorithms.fmindexes;

import java.util.ArrayList;

import ngsep.sequences.RawRead;

public class IndiceFM implements RawReadProcessor{

	private String[] bwt;
	private ArregloSufijos as;
	private int[][] tally; // filas columnas
	private int largobwt;
	private int[] conteo; //mantiene el conteo total ordenado
	private int[] primeraOcurrencia; //mantiene el numero de la primera ocurrencia de cada letra

	public IndiceFM(ArregloSufijos as) {
		this.as = as;
		int largo = this.as.getSuffixes().size();
		bwt = new String[largo];
		largobwt = largo;
		tally = new int[largo][5]; // $ A C G T
		conteo = new int[5];
		primeraOcurrencia = new int[5];
	}

	public void calcularBWT() {
		String seq = as.getSequence();
		int[] positions = as.getPositions();

		for (int i = 0; i < positions.length; i++) {
			int pos = positions[i];
			int nueva = 0;
			if (pos == 0) {
				nueva = positions.length - 1;
			} else {
				nueva = pos - 1;
			}
			bwt[i] = seq.substring(nueva, nueva + 1);
		}
	}

	public void calcularTally() {
		int contpesos = 0;
		int contas = 0;
		int contcs = 0;
		int contgs = 0;
		int contts = 0;

		for (int i = 0; i < bwt.length; i++) {
			String caracter = bwt[i];

			if (caracter.equals("$")) {
				contpesos += 1;
			} else if (caracter.equals("A")) {
				contas += 1;
			} else if (caracter.equals("C")) {
				contcs += 1;
			} else if (caracter.equals("G")) {
				contgs += 1;
			} else if (caracter.equals("T")) {
				contts += 1;
			}

			tally[i][0] = contpesos;
			tally[i][1] = contas;
			tally[i][2] = contcs;
			tally[i][3] = contgs;
			tally[i][4] = contts;

			if (i == bwt.length - 1) {
				conteo[0] = tally[i][0];
				conteo[1] = tally[i][1];
				conteo[2] = tally[i][2];
				conteo[3] = tally[i][3];
				conteo[4] = tally[i][4];

				System.out.println("-------------------");
				System.out.println("Letra | Veces");
				System.out.println("$     | " + tally[i][0]);
				System.out.println("A     | " + tally[i][1]);
				System.out.println("C     | " + tally[i][2]);
				System.out.println("G     | " + tally[i][3]);
				System.out.println("T     | " + tally[i][4]);
				System.out.println("-------------------");
			}
		}
	}

	public int devolverColumna(String letra) {
		int r = -1;
		if (letra.equals("$")) {
			r = 0;
		} else if (letra.equals("A")) {
			r = 1;
		} else if (letra.equals("C")) {
			r = 2;
		} else if (letra.equals("G")) {
			r = 3;
		} else if (letra.equals("T")) {
			r = 4;
		}
		return r;
	}
	
	public void calcularOcurrencias() {
		int indice = 0;
		for (int i = 0; i < conteo.length; i++) {
			primeraOcurrencia[i] = indice;
			//System.out.println(i + ":" + primeraOcurrencia[i]);
			int cuantas = conteo[i];
			indice += cuantas;
		}
	}
	
	public int[] operacionLF(int lower, int higher, String letra) {
		int[] posicionesNuevas = new int[2];
		int indiceLetra = devolverColumna(letra);
		int low = -1; //por si no encontre
		int high = -1; //por si no encontre
		
		for (int i = lower; i < higher; i++) { //ahora vamos a buscar la letra en la bwt
			String bw = bwt[i];
			if(bw.equals(letra)) { //encontre una ocurrencia de la letra en este rango
				high = tally[i][indiceLetra]; //aqui guarda la ultima ocurrencia
				if(low==-1) {
					low = tally[i][indiceLetra]; //primera ocurrencia
				}
			}
		}
		if(low==-1) { //no encontre
			posicionesNuevas[0] = -1;
			posicionesNuevas[1] = -1;
			return posicionesNuevas;
		}
		else { //si encontre
			low = primeraOcurrencia[indiceLetra] + low -1 ;
			high = primeraOcurrencia[indiceLetra] + high ; //exclusivo
			posicionesNuevas[0] = low;
			posicionesNuevas[1] = high;
			
		}
		
		return posicionesNuevas;
	}

	/**
	 * Retorna el indice de inicio de la seq real en el que est� la seq buscada xd
	 * @param buscada
	 * @return
	 */
	public int calcularCoincidencias(String buscada) {
		
		int largoBuscada = buscada.length();
		calcularOcurrencias();
		int respuesta = 0;
		String letra = buscada.substring(buscada.length() - 1, buscada.length()); // empezamos por ultima letra
		int indice = devolverColumna(letra); // buscamos el indice de la matriz de la letra
		int lower = primeraOcurrencia[indice]; //rango menor
		int higher = lower + conteo[indice]; //rango mayor, exclusivo
		int[] nuevasPos = new int[2];
		
		for (int i = buscada.length() - 2; i >= 0; i--) { // recorremos la cadena al reves
			letra = buscada.substring(i, i + 1); //ahora buscamos en esos indices esta letra
			nuevasPos = operacionLF(lower, higher, letra); //buscamos nuevo rango
			lower = nuevasPos[0];
			higher = nuevasPos[1];
			if(lower == -1 && higher == -1) {
				return -1;
			}
		}
		
		respuesta = as.getPositions()[lower];
		//Descomentar aqui para ver seqs
		System.out.print("Sequence: " + buscada);
		System.out.print(" is in pos: "+respuesta);
		
		
		//String cadenaEncontrada = as.getSequence().substring(respuesta,respuesta+largoBuscada);
		//System.out.print(", Found: " + cadenaEncontrada);
		System.out.println(" ");
		return respuesta;
	}

	@Override
	public void processRead(RawRead read) {
		String sseq = read.getSequenceString();
		int i = calcularCoincidencias(sseq);
		if(i==-1) {
			System.out.println("No se encontro la lectura xd");
		}
		// TODO Auto-generated method stub
		
	}

}
