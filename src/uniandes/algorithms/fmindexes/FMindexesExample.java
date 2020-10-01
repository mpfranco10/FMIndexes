package uniandes.algorithms.fmindexes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.QualifiedSequenceList;
import ngsep.sequences.RawRead;
import ngsep.sequences.io.FastaSequencesHandler;
import ngsep.sequences.io.FastqFileReader;

/**
 * Example program to build simple overlap graphs or to build k-mer tables from
 * raw reads
 * 
 * @author Jorge Duitama
 *
 */
public class FMindexesExample {
	public static final String COMMAND_OVERLAP = "Overlap";
	public static final String COMMAND_KMERS = "Kmers";

	/**
	 * Main class that executes the program
	 * 
	 * @param args Array of arguments: args[0]: Command to execute. It can be
	 *             "Overlap" or "Kmers" args[1]: Fastq file with the reads to be
	 *             processed args[2]: Optional. For "Overlap" is the minimum overlap
	 *             length. For "Kmers" is the k-mer length
	 * @throws Exception If the reads can not be loaded
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Command and input file are mandatory parameters");
			return;
		}
		String fastaFilename = args[0];
		String fastqFilename = args[1];
		long time = System.currentTimeMillis();
		// Assemble sequence
		

		FastaSequencesHandler handler = new FastaSequencesHandler();
		handler.setSequenceType(StringBuilder.class);
		QualifiedSequenceList sequences = handler.loadSequences(fastaFilename);
		if (sequences.size() == 0)
			throw new Exception("No sequences found in file: " + fastaFilename);
		QualifiedSequence seq = sequences.get(0);
		String sequence = seq.getCharacters().toString();
		int seqLength = sequence.length();
		System.out.println("Length of the sequence read: " + seqLength);
		time = System.currentTimeMillis();
		ArregloSufijos as = new ArregloSufijos(sequence);
		
		as.buildSuffixes();
		ArrayList<String> s = as.getSuffixes();
		System.out.println("Number of suffixes: " + s.size());
		as.computePositions();
//		int[] pos = as.getPositions();
//		for (int i = 0; i < pos.length; i++) {
//			System.out.println(i);
//			System.out.println(pos[i]);
//			System.out.println(s.get(i));
//			System.out.println("----");
//		}
		time = System.currentTimeMillis() - time;
		System.out.println("Time building array (ms): " + time);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		time = System.currentTimeMillis();
		processFastq(fastqFilename, as); //lee el fastq
		time = System.currentTimeMillis() - time;
		System.out.println("Time searching each sequence in Suffix array (ms): " + time);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		time = System.currentTimeMillis();
		IndiceFM ifm = new IndiceFM(as);
		ifm.calcularBWT();
		ifm.calcularTally();
		processFastq(fastqFilename,ifm);
		time = System.currentTimeMillis() - time;
		System.out.println("Time searching each sequence in FMIndex (ms): " + time);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}
	/**
	 * Process the reads stored in the given fastq file
	 * @param filename Name of the file to load. The file can be gzip compressed but then the
	 * extension must finish with ".gz"
	 * @param processor Object able to operate with raw reads
	 * @throws IOException If there is an error reading the file
	 */
	public static void processFastq(String filename, RawReadProcessor processor) throws IOException {
		try (FastqFileReader reader = new FastqFileReader(filename)) {
			reader.setLoadMode(FastqFileReader.LOAD_MODE_MINIMAL);
			Iterator<RawRead> it = reader.iterator();
			while(it.hasNext()) {
				RawRead read = it.next();
				processor.processRead(read);
			}
		}
	}
}
