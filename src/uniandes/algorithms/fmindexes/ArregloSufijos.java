package uniandes.algorithms.fmindexes;

import java.util.ArrayList;
import java.util.Collections;

import ngsep.sequences.RawRead;


public class ArregloSufijos implements RawReadProcessor{
	
	private String sequence;
	private ArrayList<String> suffixes; //arreglo de sufijos ordenados
	private int[] positions; //guarda en la posicion i en que caracter comienza el sufijo en la posicion i de suffixes
	
	public ArregloSufijos(String sequence) {
		this.sequence = sequence;
		this.sequence += "$";
		positions = new int[sequence.length()+1]; //en la posicion i guarda el sufijo que empieza en esa pos
		suffixes = new ArrayList<String>();
	}
	
	public void buildSuffixes() {
		for (int i = 0; i < sequence.length(); i++) {
			String suffix = sequence.substring(i,sequence.length());
			suffixes.add(suffix);
		}
		Collections.sort(suffixes);
	}
	
	public void computePositions() {
		int l = this.sequence.length();
		for (int i = 0; i < suffixes.size(); i++) {
			String suffix = suffixes.get(i);
			int pos = l - suffix.length();
			positions[i]=pos;
			
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
	public ArrayList<String> getSuffixes() {
		return suffixes;
	}

	/**
	 * @return the positions
	 */
	public int[] getPositions() {
		return positions;
	}

	@Override
	public void processRead(RawRead read) {
		// TODO Auto-generated method stub
		String sseq = read.getSequenceString();
		//System.out.println(sequence);
		int indice = busquedaBinaria(sseq);
		int posOriginal = positions[indice];
		
	    //System.out.println("Sequence " + sseq + " is in pos: " + posOriginal);
		System.out.print(posOriginal+",");
	    //System.out.println("o sea "+ sequence.substring(posOriginal, posOriginal + 35));
		
		
		
	}
	
	/**
	 * Retorna el indice en el que esta la lectura en el arreglo de sufijos
	 * @param suffix
	 * @return
	 */
	public int busquedaBinaria(String suffix) {
		int index = -1;
		int low = 0;
		int high = suffixes.size();
		
		while(low <= high) {
			int mid = (low + high) / 2;
			if (suffixes.get(mid).startsWith(suffix)) { //no igual porque es parte del sufijo
	            index = mid;
	            break;
	        }
			else  if (suffixes.get(mid).compareTo(suffix) < 0) {
				//System.out.println(suffixes.get(mid) + "es menor que" + suffix);
	            low = mid + 1;
	        } else {
	            high = mid - 1;
	        } 
		}	
		return index;
	}

}
