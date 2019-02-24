Lindsay Ngo
Operating Systems: Lab 2
This project imitates how a CPU schedules processes depending on what method is used.
More information about output in the file scheduling.pdf

How to compile:
javac Lab2.java
java Lab2 <input file name/input txt file>

You can include parentheses in the input
Accepts a file "random-numbers"
The 4 scheduling processes:
  LRU = Last Recently Used
  FIFO = First in First Out
  HPRN = Highest Penalty Ratio Next
  RR = Round Robin

For a verbose output:
java Lab2 --verbose <input file name>
