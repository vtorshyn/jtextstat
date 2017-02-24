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

# Executing
Main class is added into manifest so jar can be executed directly by:
```
  java -jar ./target/jtextstat-0.0.1-SNAPSHOT.jar <application options>
```
For full list of supported command line options please use '-help' switch when executing jar. 

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
- Intel Xeon 2.9 Ghz (4 Cores, HT On) - around 4 seconds for 130 000 000 words in text file
- Intel Core i5 1.7 Ghz (2 Cores, HT On) - around 14 seconds for 130 000 000 words in text file
