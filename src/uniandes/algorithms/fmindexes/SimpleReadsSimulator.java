package uniandes.algorithms.fmindexes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.QualifiedSequenceList;
import ngsep.sequences.io.FastaSequencesHandler;

/**
 * Simple script that simulates error free reads from a text in fasta format
 * 
 * @author Jorge Duitama
 *
 */
public class SimpleReadsSimulator {

	/**
	 * Main class that executes the program
	 * 
	 * @param args Array of arguments: args[0]: Source sequence in fasta format. If
	 *             many sequences are present, it only takes the first sequence
	 *             args[1]: Length of the reads to simulate args[2]: Number of reads
	 *             to simulate args[3]: Path to the output file args[4]: "E"
	 *             indicates if the simulation contains errors
	 * @throws Exception If the fasta file can not be loaded
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 4) {
			System.err.println("Please fill all the parameters");
			return;
		}
		Random random = new Random();
		String filename = args[0];
		int readLength = Integer.parseInt(args[1]);
		int numReads = Integer.parseInt(args[2]);
		String outFile = args[3];
		FastaSequencesHandler handler = new FastaSequencesHandler();
		handler.setSequenceType(StringBuilder.class);
		QualifiedSequenceList sequences = handler.loadSequences(filename);
		if (sequences.size() == 0)
			throw new Exception("No sequences found in file: " + filename);
		QualifiedSequence seq = sequences.get(0);
		String sequence = seq.getCharacters().toString();
		int seqLength = sequence.length();
		System.out.println("Length of the sequence to simulate reads: " + seqLength);
		double averageRD = ((double) numReads * readLength) / seqLength;
		System.out.println("Expected average RD: " + averageRD);
		char[] fixedQS = new char[readLength];
		Arrays.fill(fixedQS, '5');
		String fixedQSStr = new String(fixedQS);

		try (PrintStream out = new PrintStream(outFile)) {
			// TODO: Generar lecturas aleatorias. Utilizar el objeto random para generar una
			// posicion aleatoria de inicio
			// en la cadena sequence. Extraer la lectura de tamanho readLength e imprimirla
			// en formato fastq.
			// Utilizar la cadena fixedQSStr para generar calidades fijas para el formato
			int contador = 0;

			while (contador < numReads) {
				int inicio = random.nextInt(seqLength - (readLength + 1));
				String cadena = sequence.substring(inicio, inicio + readLength);
				out.println("@" + contador + "-" + inicio);
				out.println(cadena);
				out.println("+");
				out.println(fixedQSStr);
				contador++;
			}

			out.close();

		}
	}
}
