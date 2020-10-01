# FMIndexes

Para correr se necesitan 2 argumentos, el archivo fasta con la secuencia original y el archivo fastq con lecturas de ese archivo fasta. 
Ejemplo de comando para generar lecturas:

  * **java -Xmx4g -cp lib\NGSEPcore_3.2.0.jar;bin uniandes.algorithms.fmindexes.SimpleReadsSimulator data\seqVirus.fa 100 10000 salida.fastq > test.out**

Ejemplo de comando para correr el índice:

  * **java -Xmx4g -cp lib\NGSEPcore_3.2.0.jar;bin uniandes.algorithms.fmindexes.FMindexesExample data\seqVirus.fa data\salida.fastq > test.out**

