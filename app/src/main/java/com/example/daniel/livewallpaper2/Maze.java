package com.example.daniel.livewallpaper2;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/21/2015.
 * Handles Maze stuff
 */
public class Maze {
    boolean[][] maze;

    Maze(int xPaths, int yPaths){
        int x = 2*xPaths+1;
        int y = 2*yPaths+1;
        maze = new boolean[2*x+1][2*y+1];

        ArrayList<Cell> trace= new ArrayList<>();
        trace.add(new Cell(x/2, y/2));
        while(trace.size()!=0){
            // Choose a direction to move from the last Cell, or pop the last one off
            x++;
        }
    }

    private class Cell{
        int x;
        int y;
        Cell(int xx, int yy){
            x=xx;
            y=yy;
        }
    }
}
