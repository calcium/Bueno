package com.pyuser.bueno;

import com.pyuser.bueno.rover.Plateau;
import com.pyuser.bueno.rover.Rover;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        char commands[] = {'R', 'R', 'M', 'M', 'M', 'L', 'M','M','M' };
//
//        char[][] layout = {
//                { 'o', 'o', 'R', 'o' },
//                { 'o', 'o', 'R', 'o' },
//                { 'o', 'o', 'o', 'o' }
//        };

        String plateauFilename = "plateau.txt";

//        Plateau plateau = new Plateau(layout);
//
//        Rover rover = new Rover(plateau.getRows() - 1,0,  'N', plateau);
//
//        System.out.println("Hello Rover");
//        for (int i = 0; i < commands.length; ++i) {
//            try {
////                System.out.println(rover.getPosition() + "; command; " + commands[i] + "; Orientation; " + rover.getOrientation());
//                rover.execute(commands[i]);
////                System.out.println("New position; " + rover.getPosition() + "; Facing " + rover.getOrientation());
//            } catch (Exception e) {
//                System.out.println("Command failed; " + e.getMessage());;
//            }
//        }
//
        try {
            Plateau plateau = new Plateau(plateauFilename);
            Rover rover = new Rover(plateau.getRows() - 1,0,  'N', plateau);

            rover.console();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
