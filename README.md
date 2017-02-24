# jtextstat
An simple Java application which scans a text file and prints statistics

## Requirements
- Bash shell (*nix or Cygwin)
- GNU Make
- Perl
- Java version >= 8
- Maven

# Building
Use maven to compile the project:
```
  mvn clean install
```
As a result you should have a jar file in ./target folder.

## GNU Make

Alternatively you can use GNU Make:

```
make clean all
```

# Executing
Main class is added into manifest so jar can be executed directly by:
```
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar <application options>
```
For full list of supported command line options please use '-help' switch when executing jar. 

## GNU Make

Makefile contains simple perl script which generates big input file. To execute test with generated input file use the following command:
```
make com.vtorshyn.Main
```

### Changing Read Buffer size 

It is possible to change in application file read buffer size by passing new value to make command:
```
make all com.vtorshyn.Main java_application_read_buffer=$((2*8*1024))
```

### Using multiple threads for processing

Use `java_application_thread_count` make variable to change number of threads used for processing:
```
make all com.vtorshyn.Main java_application_thread_count=8
```

## Quick start
```
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar -file task-text.in
```
# Supported options
- Case can be ignored 
- Inmemory read buffer can be fine tunned depends on input file size
- Parallel processing is supported
- Simple output filters are supported:
-- minimum occurance
-- maximum unique words
- By default output is sorted by:
-- occurance
-- then by word

For example, to display top 10 words with a frequency > 4:
```
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar -file task-text.in -ignoreCase 1 -minimum 4 -maxEntries 10
```
Result should be:
```
$ java -jar target/jtextstat-0.0.1-SNAPSHOT.jar -file task-text.in -ignoreCase 1 -minimum 4 -maxEntries 10
...
*** Completed succesfully ***
Total word count: 212
Total number of unique words: 66
Result: {merry=16, christmas=8, their=8, to=8, and=6, cheer=6, ding=6, dong=6, good=6, is=6}
```

# Notes
Single thread execution time:
- Intel Xeon 2.9 Ghz (4 Cores, HT On) - around 3.5 seconds for 130 000 000 words in text file
Still slow, but better
```
Fri Feb 24 17:05:28 GMT 2017
com.vtorshyn.word.processor.WordProcessorBuilder.parallelFactor=8
com.vtorshyn.word.processor.WordProcessor.maxEntries=5
com.vtorshyn.word.processor.WordProcessor.minimum=5
com.vtorshyn.word.processor.WordsRangeReader.readBufferSize=16384
com.vtorshyn.word.processor.WordsRangeReader.readBufferDeltaSize=100
com.vtorshyn.word.processor.WordsRangeReader.file=build/input.txt
com.vtorshyn.WordMapBuilder.ignoreCase=

*** Completed succesfully ***
Total word count: 130000000
Total number of unique words: 8
Result: {a=30000000, test=30000000, text=20000000, This=10000000, by=10000000}

real	0m3.329s
user	0m22.050s
sys	0m1.063s
Fri Feb 24 17:05:31 GMT 2017
```
- Intel Core i5 1.7 Ghz (2 Cores, HT On) - around 6 (was 14) seconds for 130 000 000 words in text file
```
com.vtorshyn.word.processor.WordProcessorBuilder.parallelFactor=4
com.vtorshyn.word.processor.WordProcessor.maxEntries=5
com.vtorshyn.word.processor.WordProcessor.minimum=5
com.vtorshyn.word.processor.WordsRangeReader.readBufferSize=3276800
com.vtorshyn.word.processor.WordsRangeReader.readBufferDeltaSize=100
com.vtorshyn.word.processor.WordsRangeReader.file=build/input.txt
com.vtorshyn.WordMapBuilder.ignoreCase=

*** Completed succesfully ***
Total word count: 130000000
Total number of unique words: 8
Result: {a=30000000, test=30000000, text=20000000, This=10000000, by=10000000}

real	0m6.626s
user	0m24.373s

```
