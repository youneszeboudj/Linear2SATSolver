# Linear2SATSolver
A Java program to solve 2-SAT problem using Aspvall, Plass &amp; Tarjan algorithm 

The program test weither a 2-sat set is satisfiable or not, if the set is satisfiable, the program doesn't give the variables values that makes the  set satisfiable, it is not complicated to do so, it just requires to program the last Aspvall, Plass &amp; Tarjan algorithm step, I didn't do it because this program was just a part of a work of mine.

The set file structure is simple, each line contains a clause; clause's literals are separated by space(s), variable negation is represented by (-) sign. Example :

a -b

a c

b -c

a -c

c b
