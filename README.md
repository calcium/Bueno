# Coding exercises for Bueno
## Description
Coding exercise as per requested by the Bueno.
The Mars Rover programming exercise.

Tested using java 8.

## Usage
This will run the rover application

$ `java -classpath classes com.pyuser.bueno.App`

## Assumptions :
* No 3rd party libs, save for jUnit.
* Invalid commands does increment the command count.
* Grouped commands are not atomic, as in all commands will be attemped even if one of
the commands in the group does not succeed. Though I can see why this would
not be a good idea, I can change this behaviour reasonably easily,
by not swallowing the exception in the group commands loop.
* There is no undo facility (see possible improvements).

## Design
* Rover needs to be instantiated with a plateau. No plateau, no rover.

## Possible improvements
* Each of the commands could be made into a class viz the GOF Command pattern. This will provide an ability
to easily add new commands as well as to implement add _undo_ functionality.
* Have the exceptions have a mnemonic value, so as to be able to easily distinguish from other types of
exceptions, as opposed to having a class per type of exception.
* Plateau coordinates are (x, y). Arrays are (rows, cols) which is (y, x) or vice versa.
I had a lot of trouble with grokking this.


## Feeback received
We've just finished our code reviews, and unfortunately you weren't selected for interview. The feedback from our developers was as follows:

Pros

* README very well written

Cons

* No need to catch the exceptions in the test cases
* Limited/no use of proper visibility accessors
* We ask not to use external libraries - I don't see the point of rewriting `Pair` when you can simply introduce a class called `Position`
* Extraneous links to external references. Not that I don't see the benefits per se, when they are useful
* Tests are too big/complex and hard to read
* boolean res = false; [...] res = true; return res;
* IIRC reference comparison `==` with Integers works only with small numbers, and only with certain VMs (OpenJ9 does not work with this at all)
* The solution lacks OO design
* In general not super clean - I don't think an IDE like IntelliJ (which has warnings/suggestions built-in) was used
* `;  //we swallow` and then `throws Exception` in the signature
* Why `_env`?
