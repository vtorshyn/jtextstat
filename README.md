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
## Brief overview
For full set of options, please use '-help' flag as a command line parameter.
- Case can be ignored 
- Inmemory read buffer can be fine tunned depends on input file size
- Parallel processing is supported
- Simple output filters are supported:
 - minimum frequency of the word
 - limiting number of unique words
- By default output is sorted by:
 - first by word ferquency
 - second filter is alphanum sorting (a-z)
 
## Supported command line options
```
-readBufferSize <integer> - Use custom size for file io buffer. [!] (default=8192)
```
Defines a size of prealocated buffer for reading data from a file. By default 8192. If you have slow disk with high IO latencies, you want to use larger numbers. Please note, in case of concurency is in use, you could avoid many mem. allocations by increasing value of this parameter.
```
-readBufferDeltaSize <integer> - Reserve addition bytes in readers queue for read-a-head smart logic.
If current chunk is not complete, e.g. word is fragmented, reader will read addition number of bytes until delimiter is found or on overflow. [!] (default=100)
```
Defines amount of bytes used for "smart parsing". Sometimes, readBuffer could contain fragmented word in the end of the buffer. If you have long words > 100, you probably want to adjust this parameter to avoid fragmentaion.
```
-file <string> - Path to file with text. Assumed ASCII. (default=)
```
Path to input file with a text to be scanned.
```
-parallelFactor <integer> - Use single or parallel processors. (default=1)
```
Turns on/off concurrent parsing. Defaults is single threaded parsing. You could increase this value according to your hardware configuration.
```
-frequency <integer> - Simple filter for words counters. (default=5)
```
Used to limit output by frequency (a number of occurances a word in the text). For example, if you want to print only words with frequency 10 use `-frequency 10`.
```
-maxEntries <integer> - Limit output to this number of entries. (default=5)
```
Used to limit number of printed words. For example if you want to see top 2 words, use `-maxEntries 2`.
```
-debugRaw on - dump raw map before sorting (default=)
```
Use this flag if you want to dump full map before filtering. Default is off.
```
-ignoreCase on - Ignore case, e.g. "text" and "tExt" will be counted separtely (default=)
```
If you want to ignore case, use this flag. By default is off.

For example, to display top 10 words with a frequency > 4, case sensitivity if off :
```
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar -file task-text.in -ignoreCase 1 -frequency 4 -maxEntries 10
```
Result should be:
```
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
