Problem 1:
	To compile:
	javac ProblemOne.java ServantThread.java PresentBag.java Present.java

	To run:
	java ProblemOne

	To check correctness (after running):
	javac ProblemOneChecker.java
	java ProblemOneChecker

Problem 2:
	To compile:
	javac a

	To run:
	java a

-------------------------------------
Problem 1:
My solution is relatively straightforward. Using Java's `synchronized` keyword, I implemented a simple monitor lock for my PresentBag and Present classes. Both needed their own classes with `synchronized` methods because they were both being accessed and modified by servant threads. I implemented adding to and searching the linked list using recursive methods. My code is simplified by the fact that I used the Present class both to represent presents and for the linked list, since the linked list can be represented by a single Present and a pointer to the next present in the list. Additionally, the problem statement did not require me to be able to remove a specific Present from the list, just the first one, simplifying that function. In order to keep the linked list always shared between all threads, I made sure it always had one Present in it by making the head a placeholder Present with a num of -1. This way, even at times when the list became temporarily empty, the list was still shared between all threads.
To test the efficiency of my solution, I ran it 100 times, each time taking 1 or 2 milliseconds to run. To test the correctness of the queries when the minotaur asked whether a present was in the list, I wrote output to a file every time a present was added or removed from the list, and when the minotaur asked about a present. The output is formatted as follows:
	Line	Meaning
	0 x	Present x has been added to the list
	1 x	Present x has been removed from the list
	2 x y	The minotaur has asked about Present x, a servant has responded with y (0 indicating that Present x is not currently in the list, 1 indicating that it is).
I then wrote a simple checking program (ProblemOneChecker) that uses a TreeSet to replicate the changes to the list and checks whether each query was consistent with the state of the list at the time. For all 100 times I tested it, my tester has proven that the servants' responses were always internally consistent for all tests.


Problem 2:
a