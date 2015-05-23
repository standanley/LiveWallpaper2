package com.example.daniel.livewallpaper2;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/21/2015.
 * Handles Maze stuff
 */
public class Maze {
    boolean[][] map;

    /* Create a random maze of the given size. xMax and yMax must be odd numbers */
    Maze(int xMax, int yMax){
        if(xMax%2 == 0) xMax--;
        if(yMax%2 == 0) yMax--;

        map = new boolean[2* xMax +1][2* yMax +1];

        ArrayList<Cell> trace= new ArrayList<>();
        int x, y;
        Cell first = (new Cell(xMax/2, yMax/2));
        trace.add(first);
        map[first.x][first.y] = true;
        while(trace.size()!=0){
            // Choose a direction to move from the last Cell, or pop the last one off
            Cell current = trace.get(trace.size() - 1);
            x = current.x;
            y = current.y;

            boolean[] options = new boolean[4]; // right up left down
            int num = 0;
            if(x + 2 < xMax && !map[x + 2][y]){
                options[0] = true;
                num++;
            }
            if(y + 2 < yMax && !map[x][y + 2]){
                options[1] = true;
                num++;
            }
            if(x - 2 >= 0 && !map[x - 2][y]){
                options[2] = true;
                num++;
            }
            if(y - 2 >= 0 && !map[x][y - 2]){
                options[3] = true;
                num++;
            }

            if(num == 0){
                // backed into a corner
                trace.remove(trace.size()-1);
                continue;
            }
            int choiceNum = (int) (Math.random()*num);
            int choice = -1; // should always be changed
            num = 0;
            for(int i=0;i<4;i++){
                if(options[i]){
                    if(num == choiceNum){
                        //go this direction
                        choice = i;
                        break;
                    }
                    num++;
                }
            }

            // choice now holds the direction we will go next
            Cell next;
            switch(choice){
                case 0:
                    next = new Cell(x+2, y);
                    map[x+1][y] = true;
                    break;
                case 1:
                    next = new Cell(x, y+2);
                    map[x][y+1] = true;
                    break;
                case 2:
                    next = new Cell(x-2, y);
                    map[x-1][y] = true;
                    break;
                case 3:
                    next = new Cell(x+2, y-2);
                    map[x][y-1] = true;
                    break;
                default:
                    throw new AssertionError("Choice was set to "+choice);
            }
            map[next.x][next.y] = true;
            trace.add(next);

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
