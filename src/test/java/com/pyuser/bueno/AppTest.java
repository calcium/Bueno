package com.pyuser.bueno;

import com.pyuser.bueno.rover.Plateau;
import com.pyuser.bueno.rover.Rover;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
//    extends TestCase
{
    Plateau _plateau;
    Rover _rover;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
//    public AppTest( String testName )
//    {
//        super( testName );
//    }

    /**
     * @return the suite of tests being tested
     */
//    public static Test suite()
//    {
//        return new TestSuite( AppTest.class );
//    }

    /**
     * Rigourous Test :-)
     */
    @BeforeEach
    public void init() {
        char[][] layout = {
                { 'o', 'o', 'o', 'o' },
                { 'o', 'R', 'o', 'o' },
                { 'o', 'o', 'R', 'o' },
                { 'R', 'o', 'R', 'o' },
                { 'o', 'o', 'R', 'o' },
                { 'o', 'R', 'R', 'o' },
        };
        _plateau = new Plateau(layout);
        _rover = new Rover(_plateau.getRows() - 1, 0, 'E', _plateau);
        System.out.println(_plateau);
    }

    @Test
    public void testSuccessfulNavigation() {
        // assume starting point - top left, facing east
        char commands[] = {'R', 'M', 'L', 'M', 'R', 'M', 'M', 'R', 'M','M', 'L', 'M', 'M', 'M', 'L', 'M', 'M', 'M', 'M', 'M' };
        String commandsS = "RMLMRMMRMLMMLMMMLMMMMM";
        System.out.println(commands);
        try {
            _rover.execute(commandsS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(_plateau.generatePrintout(_rover.getPosition()));
    }

@Test
    public void testOutOfBounds()
    {
        String[] blank2x3 = {
                "ooo",
                "ooo",
        };
        boolean isOK = false;

        _rover.reset();
        _rover.setPlateau(new Plateau(blank2x3));
        try {
            _rover.execute("MMMMM");
        } catch (Exception e) {
            System.out.println("Expected");
            isOK = true;
        }
        assertTrue( isOK );
    }

    public void testInvalidCommand()
    {
        assertTrue( false );
    }


    public void testBumpingIntoR()
    {
        assertTrue( false );
    }

    public void testCommandMetrics()
    {
        assertTrue( false );
    }



    @Test
    public void testFailedNavigation() {

        try {
            System.out.println("Old; " + _rover.getPosition());
            _rover.move(1);
            System.out.println("New; " + _rover.getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Old; " + _rover.getPosition());
            _rover.rotate('R');
            _rover.move(1);
            System.out.println("New; " + _rover.getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
