package com.pyuser.bueno;

import com.pyuser.bueno.exceptions.InvalidMoveException;
import com.pyuser.bueno.helper.Pair;
import com.pyuser.bueno.rover.Plateau;
import com.pyuser.bueno.rover.Rover;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 * Map used for tests unless specified otherwise
 * ooooooRRRR
 * ooRooooooo
 * ooooooRRoo
 * ooRooooooo
 * oooooRoooo
 * oooooRRRoo
 */
public class AppTest {
    Plateau _plateau;
    Rover _rover;

    @BeforeEach
    public void init() {
        String plateauFilename = "plateau.txt";

        try {
            _plateau = new Plateau(plateauFilename);
            _rover = new Rover(_plateau.getRows() - 1, 0, 'E', _plateau);
            System.out.println(_plateau);
        } catch (Exception e) {
            System.err.println("Exception in creating the rover/plateau. Cannot proceed.");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMoveCommand() {
        /* Tests moving in each direction, N, E, S, W
        * (x, y) = (col, row)
        * */
        init();
        Pair<Integer, Integer> position = _rover.getPosition();
        Pair<Integer, Integer> newPosition = _rover.getPosition();

        assertTrue(position.getLeft() == _plateau.getRows() - 1);
        assertTrue(position.getRight() == 0);
        assertTrue(_rover.getOrientation() == 'E');

        try {
            _rover.execute("M");  // facing east, x
            newPosition = _rover.getPosition();
            assertTrue(newPosition.getLeft() == position.getLeft(), "Y axis coordinate is wrong.");
            assertTrue(newPosition.getRight() == position.getRight() + 1, "X axis coordinate is wrong.");
            position = newPosition;

            _rover.execute("RM");  // facing south, move
            assertTrue(_rover.getOrientation() == 'S', "Should be orienteering South.");
            newPosition = _rover.getPosition();
            assertTrue(newPosition.getLeft() == position.getLeft() - 1, "Y axis coordinate is wrong.");
            assertTrue(newPosition.getRight() == position.getRight(), "X axis coordinate is wrong.");
            position = newPosition;

            _rover.execute("RM");  // facing east, x
            assertTrue(_rover.getOrientation() == 'W', "Should be orienteering South.");
            newPosition = _rover.getPosition();
            assertTrue(newPosition.getLeft() == position.getLeft(), "Y axis coordinate is wrong.");
            assertTrue(newPosition.getRight() == position.getRight() - 1, "X axis coordinate is wrong.");
            position = newPosition;

            _rover.execute("RM");  // facing east, x
            assertTrue(_rover.getOrientation() == 'N', "Should be orienteering South.");
            newPosition = _rover.getPosition();
            assertTrue(newPosition.getLeft() == position.getLeft() + 1, "Y axis coordinate is wrong.");
            assertTrue(newPosition.getRight() == position.getRight(), "X axis coordinate is wrong.");

            // we are back at where we are.
        } catch (Exception e) {
            assertTrue(false, "This should never happen; " + e.getMessage());
        }
    }

    @Test
    /**
     * Tests rotation clockwise and anti clockwise.
     */
    public void testRotation() {
        init();
        assertTrue(_rover.getOrientation() == 'E', "Initial orientation should be East.");

        char[] compass = {'N', 'E', 'S', 'W'};
        try {
            for (int i = 0; i < compass.length; ++i) {
                _rover.execute("R");  // going clockwise
                char newOrientation = compass[((i + 4) + 2) % 4];  // + 2 cos start is 'E'
                System.out.println(i + "; " + _rover.getOrientation());
                assertTrue(_rover.getOrientation() == newOrientation, "Expected orientation is " + newOrientation);
            }
        } catch (Exception e) {
            assertTrue(false, "This should never happen; " + e.getMessage());
        }

        assertEquals('E', _rover.getOrientation(), "Initial anti clockwise orientation should be East.");
        try {
            for (int i = compass.length - 1; i > 0; --i) {
                _rover.execute("L");  // going anti clockwise
                char newOrientation = compass[(i + 4) % 4 + 2];
                assertTrue(_rover.getOrientation() == newOrientation, "Expected orientation is " + newOrientation);
            }
        } catch (Exception e) {
            ;  // swallow
        }

    }

    @Test
    public void testSuccessfulNavigation() {
        /**
         * Will navigate from
         * XoooooRRRR
         * ooRooooooo
         * ooooooRRoo
         * ooRooooooo
         * oooooRoooo
         * oooooRRRoo
         * to
         * ooooooRRRR
         * ooRooooooo
         * ooooooRRoo
         * ooRooooooo
         * oooooRoooo
         * oooooRRRoX
         */
// assume starting point - top left, facing east
        String commandsS = "MMMMMRMMMLMMMMRMM";
        boolean isNavigationSuccessful = false;
        init();
        try {
            _rover.execute(commandsS);
            isNavigationSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(isNavigationSuccessful, "Navigation is expected to have passed.");
        System.out.println(_plateau.generatePrintout(_rover.getPosition()));
        System.out.println(_rover.getPosition().getRight());
        System.out.println(_rover.getPosition().getLeft());
        System.out.println(_plateau.getColumns());

        assertTrue(isNavigationSuccessful &&
                _rover.getPosition().getLeft() == 0
                && _rover.getPosition().getRight() == _plateau.getColumns() - 1 ,
                "Position should be at N, 0.");

        System.out.println(_plateau.generatePrintout(_rover.getPosition()));
    }

    @Test
    public void testOutOfBounds() {
        /**
         *  Am using the map as per in supplied
         *  */
        boolean isOutOfBounds = false;

        init();
        try {
            _rover.execute("MMLMMM");
        } catch (Exception e) {
            System.out.println(String.format("Exception '%s' as expected;", e.getMessage()));
            isOutOfBounds = true;
        }
        assertTrue(isOutOfBounds, "Expectation was that rover goes out of bounds.");
    }

    @Test
    public void testInvalidCommand() {
        boolean isInvalidCommand = false;

        init();
        try {
            _rover.execute("MMMKJMM");
        } catch (Exception e) {
            System.out.println(String.format("Exception '%s' as expected;", e.getMessage()));

            isInvalidCommand = true;
        }
        assertTrue(isInvalidCommand, "Expectation was that some invalid commands was found.");
    }

    @Test
    public void testBumpingIntoR() {
        String[] newMap = {
                "ooo",
                "ooo",
                "oRo"
        };

        boolean isBumpingIntoR = false;
        init();
        _rover.setPlateau(new Plateau(newMap)); // where X is top left, facing East
        // To bump into R we need to go either MRMM, or RMMLM
        String commands = "MRMM";
        try {
            _rover.execute(commands);
        } catch (InvalidMoveException e) {
            isBumpingIntoR = true;
        } catch (Exception e) {
            assertTrue(false, "This was unexpected.");
        }

        assertTrue(isBumpingIntoR, "Expectation is to bump into an 'R'.");
    }

    @Test
    public void testCommandMetrics() {
        /**
         * Using the given map, sending it N = 10 commands of which we know 5 will pass
         */
        String commands = "MMMMMMMMMM";  // in original map, will bump into 'R' after 5
        int expectedSuccessfulCommandCount = 5;
        init();
        try {
            _rover.execute(commands);
        } catch (InvalidMoveException e) {
            System.out.println(String.format("Exception '%s' as expected;", e.getMessage()));
        } catch (Exception e) {
            assertTrue(false, "This was unexpected.");
        }
        Map<String, Integer> metrics = _rover.getMetrics();
        assertTrue(commands.length() == metrics.get(Rover.TOTAL_COMMANDS_COUNT),
                "Total commands count is wrong.");
        assertEquals(commands.length() - expectedSuccessfulCommandCount,
                metrics.get(Rover.FAILED_COMMANDS_COUNT),
                "Failed commands count is wrong.");
    }

    @Test
    public void testPlateau() {
        /**
         * Just a simple test for plateau to make sure it stores it's maps properly.
         * length = 4 rows, width = 3 columns
         */
        String[] newMap = {
                "ooo",
                "ooo",
                "ooo",
                "oRo"
        };

        init();
        Plateau plateau = new Plateau(newMap);
        assertEquals(newMap.length, plateau.getRows(), "Number of rows is wrong.");
        assertEquals(newMap[0].length(), plateau.getColumns(), "Number of columns is wrong.");
    }
}
