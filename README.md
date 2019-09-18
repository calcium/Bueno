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
