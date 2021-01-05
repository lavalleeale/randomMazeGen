package com.axlav;

import java.util.Arrays;
import java.util.Random;

public class backend {
    /**
     * Generates a <code>boolean[][]</code> with specified dimensions
    * @Param x The height of the maze to be generated
    * @Param y The width of the maze to be generated
    **/
    public static boolean[][] genMaze(int x, int y){
        boolean[][] maze = new boolean[x][y];
        for (int row = 0; row < x; row++) {
            for (int column = 0; column < y; column++) {
                maze[row][column] = true;
            }
        }
        maze[1][0] = false;
        return maze;
    }
    /**
     * Generates and set to <code>false</code> a path to solve any given <code>boolean[][]</code> maze
     * @Param maze The maze to generate the solution from
     **/
    public static boolean[][] genInitialSolution(boolean[][] maze){
        int curx = 1;
        int cury = 0;
        int newSpotx;
        int newSpoty;

        while (true) {
            Random r = new Random();
            int result = r.nextInt(4);
            switch (result) {
                case 0:
                    newSpotx = curx;
                    newSpoty = cury+1;
                    break;
                case 1:
                    newSpotx = curx;
                    newSpoty = cury-1;
                    break;
                case 2:
                    newSpotx = curx+1;
                    newSpoty = cury;
                    break;
                case 3:
                    newSpotx = curx-1;
                    newSpoty = cury;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value");
            }
            if (newSpotx == maze.length-2 && newSpoty == maze[0].length-1) {
                return maze;
            }
            if(validSpot(maze, newSpotx, newSpoty)) {
                maze[newSpotx][newSpoty] = false;
                curx = newSpotx;
                cury = newSpoty;
            }
        }
    }
    /**
     * Checks if a point is valid to solve until the end of the maze
     * @Param maze The maze to check for valid point in
     * @Param x The x value of the initial point
     * @Param x The y value of the initial point
     **/
    public static boolean validSpot(boolean[][] maze, int x, int y) {
        Object[][] tried = new Object[0][0];
        Object[][] toTry = new Object[][]{{x, y, maze.clone()}};
        int curx;
        int cury;
        boolean[][] curMaze;
        boolean[][] newMaze;
        whileLoop:
        while (true) {
            //Check if no points left to try
            if (toTry.length==0) {
                return false;
            }
            //Assign current position and maze
            curx = (Integer)toTry[toTry.length-1][0];
            cury = (Integer)toTry[toTry.length-1][1];
            curMaze = (boolean[][])toTry[toTry.length-1][2];
            //Check if current points have already been
            for (Object[] point : tried) {
                if (Arrays.equals(point, new Object[]{curx, cury, curMaze})) {
                    continue whileLoop;
                }
            }
            //Append current point to array of tried points
            tried = Arrays.copyOf(tried, tried.length+1);
            tried[tried.length-1] = new Object[]{curx, cury, curMaze};
            //Check if there is too long a backlog of points
            if (tried.length> maze.length*maze[0].length/5) {
                tried = new Object[0][0];
                toTry = new Object[][]{{x, y, maze.clone()}};
            }
            //Check if the current position is the final point
            if (curx == maze.length-2 && cury == maze[0].length-1) {
                System.out.printf("got: %s,%s tried:%s\n", x, y,tried.length);
                return true;
            }
            //Remove current points from array of positions try
            Object[][] copy = new Object[toTry.length - 1][];
            for (int i = 0, j = 0; i < toTry.length-1; i++) {
                copy[j++] = toTry[i];
            }
            toTry = copy;

            //Check if the current spot does not intersect with other paths
            if (!validSingleSpot(curx,cury,curMaze)) {
                continue;
            }
            //Create new maze for new points
            newMaze = new boolean[curMaze.length][];
            for ( int i = 0; i < newMaze.length; i++ ) {
                newMaze[i] = curMaze[i].clone();
            }
            //Remove wall from current point
            newMaze[curx][cury] = false;
            //Add new points to array of points to try
            toTry = Arrays.copyOf(toTry,toTry.length+4);
            toTry[toTry.length-3] = new Object[]{curx-1, cury, newMaze};
            toTry[toTry.length-4] = new Object[]{curx, cury-1, newMaze};
            toTry[toTry.length-2] = new Object[]{curx+1, cury, newMaze};
            toTry[toTry.length-1] = new Object[]{curx, cury+1, newMaze};
        }
    }
    /**
     * Checks if a point has only only one <code>false</code> directly next to it
     * @Param maze The maze to check a valid point in
     * @Param x The x value of the initial point
     * @Param x The y value of the initial point
     **/
    public static boolean validSingleSpot(int x, int y, boolean[][] maze) {
        int emptyAdjacent = 0;
        try {
            if (!maze[x][y]) {
                return false;
            }
            if (!maze[x + 1][y]) {
                emptyAdjacent++;
            }
            if (!maze[x][y + 1]) {
                emptyAdjacent++;
            }
            if (!maze[x][y - 1]) {
                emptyAdjacent++;
            }
            if (!maze[x - 1][y]) {
                emptyAdjacent++;
            }

        } catch (ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
        if (emptyAdjacent!=1) {
            return false;
        } else return true;
    }
    /**
     * Takes a <code>boolean[][]</code> maze and outputs a <code>String[]</code> representation
     * @Param maze The maze to make printable
     * @return The <code>String[]</code> representation
     **/
    public static String[] genPrintableMaze(boolean[][] maze) {
        String[] printableMaze = new String[maze.length];
        char[] rowChars;
        //Make sure the final exit of the maze is not a wall
        maze[maze.length-2][maze[0].length-1]= maze[maze.length-2][maze[0].length-2];
        for (int x = 0; x < maze.length; x++) {
            rowChars = new char[maze[x].length];
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y])     {
                    rowChars[y] = 'X';
                } else {
                    rowChars[y] = '0';
                }
            }
            printableMaze[x] = new String(rowChars);
        }
        return printableMaze;
    }
    private static String[] printableMaze;
    public static void setPrintableMaze(String[] PrintableMaze) {
        backend.printableMaze = PrintableMaze;
    }
    public static String[] getPrintableMaze() {
        return printableMaze;
    }
}
