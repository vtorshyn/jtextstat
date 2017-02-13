# jtextstat
An simple Java application which scans a text file and prints statistics

# Requirements
- Bash shell (*nix or Cygwin)
- GNU Make
- Perl
- Java version >= 8

# Usage
To execute the application with provided sample data just type:
 make run

To remove build artefacts type:
 make clean

# Big input file test
 make run-test

# Notes
Single thread execution time:
Intel Xeon 2.9 Ghz (4 Cores, HT On) - around 4 seconds for 10 000 000 lines text file
Intel Core i5 1.7 Ghz (2 Cores, HT On) - around 14 seconds for 10 000 000 lines text file
