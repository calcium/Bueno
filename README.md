# Coding exercises for Bueno
# Description
Coding exercise as`per requested by the Bueno.

The Mars Rover programming exercise.

Assumptions :
* No 3rd party libs, save for jUnit.
* Invalid commands does increment the command count.
* Grouped commands are not atomic, as in all commands will be attemped even if a command does not succeed.
* There is no undo facility (see possible improvements).


Possible improvements :
* Each of the commands could be made into a class viz the GOF Command pattern. This will provide an ability
to easily add new commands as well as to implement add _undo_ functionality.
* Have the exceptions have a mnemonic value, so as to be able to easily distinguish from other types of
exceptions, as opposed to having a class per type of exception.



