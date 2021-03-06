package com.axlav;

public class Main {
    public static void main(String[] args) {
        int mazex = 50;
        int mazey = 100;
        if (args.length==2) {
            try {
                mazex = Integer.parseInt(args[0]);
                mazey = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                System.err.println("Failed trying to parse a non-numeric argument");
            }
        }
        Maze maze = new Maze();
        maze.generate(mazex, mazey);
        maze.genInitialSolution();
        GUI GUI = new GUI();
        GUI.start(maze);
    }
}
