# jtextstat
An simple Java application which scans a text file and prints statistics for every word.

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
  java -jar ./target/jtextstat-0.0.x-SNAPSHOT.jar <application options>
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
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar -file data/test.task.text
```
# Supported options
## Brief overview
For full set of options, please use '-help' flag as a command line parameter.
- Case can be ignored 
- Inmemory read buffer can be fine tunned depends on input file size
- Asyn parallel processing is supported
- Simple output filters are supported:
 - minimum frequency of the word
 - limiting number of unique words
- By default output is sorted by:
 - first by word ferquency
 - second filter is alphanum sorting (a-z)
 
## Supported command line options
```
-blockSize <integer> - Use custom size for a file IO buffer. [!] (default=8192)
```
Defines a size of prealocated buffer for reading data from a file. By default value is 8192. 
```
-file <string> - Path to file with text. Assumed ASCII. (default=)
```
Path to input file with a text to be scanned.
```
-threads <integer> - Specifies a number of threads for processing. (default=1)
```
Turns on/off concurrent parsing. Defaults is single thread parsing. You could increase this value according to your hardware configuration.
```
-frequency <integer> - Simple filter for word counters value. Smaller values will be ignored. (default=5)
```
Used to limit output by frequency (a number of occurances a word in the text). For example, if you want to print only words with frequency 10 use `-frequency 10`.
```
-limit <integer> - Limit output to this number of entries. (default=5)
```
Used to limit number of printed words. For example if you want to see top 2 words, use `-limit 2`.
```
-ignoreCase true | false - Ignore case, e.g. "text" and "tExt" will be counted separtely (default=)
```
If you want to ignore case, use this flag. By default is off.

For example, to display top 10 words with a frequency > 4, case sensitivity if off :
```
  java -jar ./target/jtextstat-0.0.x-SNAPSHOT.jar -file task-text.in -ignoreCase 1 -frequency 4 -limit 10
```
Result should be:
```
...
*** Completed succesfully ***
Total word count: 212
Total number of unique words: 66
Result: 
merry=16
christmas=8
their=8
to=8
and=6
cheer=6
ding=6
dong=6
good=6
is=6
```

# Notes
- Intel Core i5 1.7 Ghz (2 Cores, HT On) - around 4.5 (was 6 in 0.0.1) seconds for 130 000 000 words in text file
