package com.pyuser.bueno.rover;

import com.pyuser.bueno.exceptions.InvalidMoveExeption;
import com.pyuser.bueno.helper.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Plateau {
    int _columns = 0;
    int _rows = 0;
    char[][] _plateau;

    public Plateau(String[] thePlateau) {
        _init(thePlateau);
    }

    private void _init(String[] thePlateau) {
        /** https://stackoverflow.com/questions/36241039/splitting-the-string-array-into-two-dimensional-character-array **/
        char[][] plateau = new char[thePlateau.length][];
        for(int i = 0; i< thePlateau.length; i++){
            plateau[i] = thePlateau[i].toCharArray();
        }

        _init(plateau);
    }

    public Plateau(String plateauFilename) throws Exception {
        loadFile(plateauFilename);
    }

    public Plateau(char[][] plateau) {
        _init(plateau);
    }

    private void _init(char[][] thePlateau) {
        _plateau = thePlateau;
        _rows = _plateau.length;
        _columns = _plateau[0].length;
    }
    public int getColumns() {
        return _columns;
    }

    public int getRows() {
        return _rows;
    }

    @Override
    public String toString() {
        StringBuffer grid = new StringBuffer();

        for (int i = _rows - 1; i >= 0; --i) {
            for (int j = 0; j < _columns; ++j) {
//                System.out.println("" + Integer.toString(i) + " " +  Integer.toString(j));
                grid.append(Character.toString(_plateau[i][j]));
            }
            grid.append("\n");
        }
//        System.out.println(grid.toString());
        return "Plateau{\n" + grid + '}';
    }

    /**
     * Given row, col, checks if out of bounds.
     * @param row
     * @param col
     * @return
     * @throws Exception
     */
    public boolean isPositionLegitimate(int row, int col) throws Exception {
        boolean res = false;

        if (row < 0 || col < 0 || row + 1 > _rows || col + 1 > _columns) {
            throw new InvalidMoveExeption("Out of bounds; " + col + ", " + row);
        }

        if (_plateau[row][col] == 'R') {
            throw new InvalidMoveExeption("Occupied by 'R'; " + col + ", " + row);
        }

        res = true;

        return res;
    }

    public void loadFile(String filename) throws Exception{
        /* https://www.mkyong.com/java/java-read-a-file-from-resources-folder/ */
        List<String> result = new ArrayList<>();

        File infn = getFileFromResources(filename);

        try (BufferedReader br = new BufferedReader(new FileReader(infn))) {
            while (br.ready()) {
                result.add(br.readLine());
            }
        }

        // https://javaconceptoftheday.com/reverse-an-arraylist-in-java/
        // Need to reverse cos input sequence is bottom up.
        Collections.reverse(result);

        String plateau[] = new String[result.size()];
        plateau = result.toArray(plateau);

        _init(plateau);
    }


    public String generatePrintout(Pair<Integer, Integer> xMarksTheSpot) {
        StringBuffer grid = new StringBuffer();

        for (int i = _rows - 1; i >= 0; --i) {
            for (int j = 0; j < _columns; ++j) {
//                System.out.println("" + Integer.toString(i) + " " +  Integer.toString(j));
                if (i == xMarksTheSpot.getLeft() && j == xMarksTheSpot.getRight()) {
                    grid.append('X');
                } else {
                    grid.append(Character.toString(_plateau[i][j]));
                }
            }
            grid.append("\n");
        }
//        System.out.println(grid.toString());
        return grid.toString();
    }

    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }


    public static void main(String[] args) {
        char[][] plateau = {
                { 'o', 'o', 'R', 'o' },
                { 'o', 'o', 'R', 'o' },
                { 'o', 'o', 'R', 'o' }
        };
        Plateau myObj = new Plateau(plateau);
        System.out.println(myObj);
    }
}
