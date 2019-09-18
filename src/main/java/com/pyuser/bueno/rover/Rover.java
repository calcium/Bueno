package com.pyuser.bueno.rover;

import com.pyuser.bueno.exceptions.InvalidCommandException;
import com.pyuser.bueno.exceptions.InvalidMoveException;
import com.pyuser.bueno.helper.ArrayUtils;
import com.pyuser.bueno.helper.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Class implementing the behaviour for the Mars rover.
 * Has a console.
 */
public class Rover {
    public static String TOTAL_COMMANDS_COUNT = "TotalCommandsCount";
    public static String FAILED_COMMANDS_COUNT= "FailedCommandsCount";

    private String _env = "";  // DTAP - dev, test, acceptance, prod
    private Pair<Integer, Integer> _position;
    private Pair<Integer, Integer> _initialPosition;
    private char _orientation;
    private char _initialOrientation;
    private Plateau _plateau;
    private int _totalCommands = 0;
    private int _failedCommands = 0;
    private char _lastCommand;
    private String _version = "v1.0";
    final private char[] compass = {'N', 'E', 'S', 'W'};

    public Rover(int row, int col, char orientation, Plateau plateau) {
        _env = System.getenv("DTAP");

        _position = new Pair<Integer, Integer>(row, col);
        _initialPosition = _position;
        _orientation = orientation;
        _initialOrientation = _orientation;
        _plateau = plateau;
    }

    public Rover(Pair<Integer, Integer> initialPosition, char orientation, Plateau plateau) {
        this(initialPosition.getLeft(), initialPosition.getRight(), orientation, plateau);
    }

    public void setPlateau(Plateau _plateau) {
        this._plateau = _plateau;
    }

    public char getOrientation() {
        return _orientation;
    }

    public Pair<Integer, Integer> getPosition() {
        return _position;
    }

    private Pair<Integer, Integer> figureOutNewPosition(Pair<Integer, Integer> currentPosition,
                                      int numGridPoints, char direction)
            throws Exception {
        int movement = 0;
        Pair<Integer, Integer> newPosition;

        switch (Character.toUpperCase(direction)) {
            case 'N':
                movement = numGridPoints;
                newPosition = new Pair<Integer, Integer>(
                        currentPosition.getLeft() + movement,
                        currentPosition.getRight());
                break;
            case 'W':
                movement = -numGridPoints;
                newPosition = new Pair<Integer, Integer>(
                        currentPosition.getLeft(),
                        currentPosition.getRight() + movement);

                break;
            case 'S':
                movement = -numGridPoints;
                newPosition = new Pair<Integer, Integer>(
                        currentPosition.getLeft() + movement,
                        currentPosition.getRight());
                break;
            case 'E':
                movement = numGridPoints;
                newPosition = new Pair<Integer, Integer>(
                        currentPosition.getLeft(),
                        currentPosition.getRight() + movement);
                break;
            default:
                throw new Exception("Unkwn direction; " + direction);
        }

        return newPosition;
    }

    private Pair<Integer, Integer> move(int numGridPoints) throws Exception {
        Pair<Integer, Integer> newPosition = figureOutNewPosition(
                _position, numGridPoints, _orientation);

        try {
            if (_plateau.isPositionLegitimate(newPosition.getLeft(), newPosition.getRight())) {
                _position = newPosition;
            }
        } catch (InvalidMoveException | InvalidCommandException e) {
            _failedCommands++;
            throw e;
        }

        return _position;
    }

    /**
     * has compass
     * @param direction
     * @return
     * @throws Exception
     */
    private char rotate(char direction) throws Exception {

        if (direction != 'L' && direction != 'R') {
            throw new InvalidCommandException("Bad rotation specified; " + direction);
        }

        int rotation = 1;  // default is right
        if (direction == 'L') {
            rotation = -1;
        }

        // https://www.techiedelight.com/find-index-element-array-java/
        int currentOrientation = ArrayUtils.indexOf(compass, _orientation);
        // adding the 4 in case we go negative. The mod discards this added 4.
        int newOrientation = (currentOrientation + rotation + 4) % 4;

        _orientation = compass[newOrientation];
        return _orientation;
    }

    public Pair<Integer, Integer> execute(String commands) throws Exception {
        char[] commandsC = commands.trim().toCharArray();
        Exception caughtException = null;
        for (int i = 0; i < commandsC.length; ++i) {
            try {
                execute(commandsC[i]);
            } catch (Exception e) {
                if (caughtException == null) {  // only keeping first exception
                    caughtException = e;
                }
            }
        }
        if (caughtException != null) {
            throw caughtException;
        }

        return _position;
    }

    public Pair<Integer, Integer> execute(char command) throws Exception {
        return execute(command, 1);
    }

    public Pair<Integer, Integer> execute(char command, int iterations) throws Exception {
        _totalCommands++;  // bad/invalid commands are counted.
        _lastCommand = command;
        try {
            switch (command) {
                case 'R':
                    rotate('R');
                    break;
                case 'L':
                    rotate('L');
                    break;
                case 'M':
                    move(iterations);
                    break;
                case 'O':
                    if ("dev".equalsIgnoreCase(_env)) {
                        _totalCommands--;  // not counting this one.
                        out.println("Rover is facing; " + getOrientation());
                        break;
                    }
                default:
                    throw new InvalidCommandException("Unknown command; " + command);
            }
        } catch (Exception e) {
            if ("dev".equalsIgnoreCase(_env)) {
                String mesg = String.format("Last move (%s:%s). We have a problem; %s",
                        _lastCommand, _orientation, e.getMessage());
                out.println(mesg);
            }
            throw e;
        }
        return _position;
    }

    public void console() throws Exception {
        out.println("Mars Rover v1.0 running, plateau configuration is:");
        out.println(_plateau.generatePrintout(_position));
        out.print("Waiting for commands.\n> ");
        String commands = "";

        Scanner scanner = new Scanner(System.in);
        commands = scanner.nextLine().toUpperCase();
        while (!commands.trim().equalsIgnoreCase("x")) {
            try {
                execute(commands);
            } catch (Exception e) {
                ;  //we swallow
            }
            printPlateau();
            out.print("Waiting for commands.\n> ");
            commands = scanner.nextLine().toUpperCase();
        }

        displayMetrics();
    }

    public Map getMetrics() {
        Map res = new HashMap<String, Integer>();

        res.put(TOTAL_COMMANDS_COUNT, _totalCommands);
        res.put(FAILED_COMMANDS_COUNT, _failedCommands);

        return res;
    }

    private void displayMetrics() {
        String mesg = "Sent %d command(s) / %d failed.\n\n" +
                "Mars Rover %s closed.\n";
        out.println(mesg.format(mesg, _totalCommands, _failedCommands, _version));
    }

    @Override
    public String toString() {
        return "Rover{" +
                "_position=" + _position +
                ", _orientation=" + _orientation +
                '}';
    }

    public void reset() {
        _position = _initialPosition;
        _orientation = _initialOrientation;
        _totalCommands = 0;
        _failedCommands = 0;
    }

    public void printPlateau() {
        System.out.println(_plateau.generatePrintout(getPosition()));
    }
}
