package com.pyuser.bueno;

import com.pyuser.bueno.rover.Plateau;
import com.pyuser.bueno.rover.Rover;

/**
 * Main app for the Mars Rover Programming problem for Bueno
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        String plateauFilename = "plateau.txt";

        try {
            Plateau plateau = new Plateau(plateauFilename);
            Rover rover = new Rover(plateau.getRows() - 1,0,  'N', plateau);

            rover.console();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}