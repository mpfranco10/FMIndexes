package uniandes.algorithms.fmindexes;

import java.util.ArrayList;
import java.util.Collections;

public class ArregloSufijosMejorado {

	private String sequence;
	private ArrayList<CaracterArreglo> suffixes; // arreglo de sufijos ordenados
	private int[] positions; // guarda en la posicion i en que caracter comienza el sufijo en la posicion i
								// de suffixes

	public ArregloSufijosMejorado(String sequence) {
		this.sequence = sequence;
		this.sequence += "$";
		positions = new int[sequence.length() + 1]; // en la posicion i guarda el sufijo que empieza en esa pos
		suffixes = new ArrayList<CaracterArreglo>();
	}

	public void buildSuffixes() {
		
		for (int i = 0; i < sequence.length(); i++) {
			String c = String.valueOf(sequence.charAt(i));
			CaracterArreglo ca = new CaracterArreglo(c, i);
			suffixes.add(ca);
		}
		Collections.sort(suffixes);
	}

	public void computePositions() {
		
		for (int i = 0; i < suffixes.size(); i++) {
			int indice = suffixes.get(i).getIndice();
			positions[i] = indice;
		
		}
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @return the suffixes
	 */
	public ArrayList<CaracterArreglo> getSuffixes() {
		return suffixes;
	}
	
	public int getSuffixesSize() {
		return suffixes.size();
	}

	/**
	 * @return the positions
	 */
	public int[] getPositions() {
		return positions;
	}

}

class CaracterArreglo implements Comparable< CaracterArreglo >{
	
	private String caracter;
	private int indice;
	
	public CaracterArreglo(String caracter, int indice) {
		this.caracter = caracter;
		this.indice = indice;
	}

	/**
	 * @return the caracter
	 */
	public String getCaracter() {
		return caracter;
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}
	
	@Override
	public int compareTo(CaracterArreglo o) {
        return this.getCaracter().compareTo(o.getCaracter());
    }

}
