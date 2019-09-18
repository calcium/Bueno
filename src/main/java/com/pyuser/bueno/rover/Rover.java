package com.pyuser.bueno.rover;

import com.pyuser.bueno.exceptions.InvalidCommandException;
import com.pyuser.bueno.exceptions.InvalidMoveExeption;
import com.pyuser.bueno.helper.ArrayUtils;
import com.pyuser.bueno.helper.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

public class Rover {
    private Pair<Integer, Integer> _position;
    private Pair<Integer, Integer> _initialPosition;
    private char _orientation;
    private char _initialOrientation;
    private Plateau _plateau;
    private int _totalCommands = 0;
    private int _failedCommands = 0;
    private String _version = "v1.0";

    public void setPlateau(Plateau _plateau) {
        this._plateau = _plateau;
    }

    public char getOrientation() {
        return _orientation;
    }

    public Rover(int row, int col, char orientation, Plateau plateau) {
        _position = new Pair<Integer, Integer>(row, col);
        _initialPosition = _position;
        _orientation = orientation;
        _initialOrientation = _orientation;
        _plateau = plateau;
    }


    private Pair figureOutNewPosition(Pair<Integer, Integer> currentPosition,
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
                throw new Exception("Unknown direction; " + direction);
        }

        return newPosition;
    }

    public Pair move(int numGridPoints) throws Exception {
        Pair<Integer, Integer> newPosition = figureOutNewPosition(
                _position, numGridPoints, _orientation);

        try {
            if (_plateau.isPositionLegitimate(newPosition.getLeft(), newPosition.getRight())) {
                _position = newPosition;
            }
        } catch (InvalidMoveExeption | InvalidCommandException e) {
            _failedCommands++;
            throw e;
        }

        return _position;
    }

    public Pair getPosition() {
        return _position;
    }

    /**
     * has compass
     * @param direction
     * @return
     * @throws Exception
     */
    public char rotate(char direction) throws Exception {
        char[] compass = {'N', 'E', 'S', 'W'};

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

    public void execute(String commands) throws Exception {
        char[] commandsC = commands.trim().toCharArray();
        for (int i = 0; i < commandsC.length; ++i) {
            execute(commandsC[i]);
        }
    }

    public void execute(char command) throws Exception {
        execute(command, 1);
    }

    public void execute(char command, int iterations) throws Exception {
        _totalCommands++;  // bad/invalid commands are counted.
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
                _totalCommands--;  // not counting this one.
                out.println("Rover is facing; " + getOrientation());
                break;
            default:
                throw new InvalidCommandException("Unknown command; " + command);
        }
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

        res.put("TotalCommands", _totalCommands);
        res.put("FailedCommands", _failedCommands);
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
    }

    public void printPlateau() {
        System.out.println(_plateau.generatePrintout(getPosition()));
    }
}
