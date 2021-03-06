package com.example.daniel.livewallpaper2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/21/2015.
 * Handles Maze stuff
 */
public class Maze {
    boolean[][] map;
    private ArrayList<Cell> trace= new ArrayList<>();
    int xMax, yMax;
    boolean finished;
    int x, y; // mark the current cell we're working on

    /* Create a random maze of the given size. */
    Maze(int xx, int yy){
        Log.v("tag1", "just entered Maze constructor");

        xMax = xx;
        yMax = yy;
        if(xMax%2 == 0) xMax--;
        if(yMax%2 == 0) yMax--;

        map = new boolean[2* xMax +1][2* yMax +1];


        Cell first = (new Cell(xMax/2, yMax/2));
        trace.add(first);
        map[first.x][first.y] = true;

        finished = false;
    }

    /* Another step in the animation. Returns a list of all the cells that
       were updated.
     */
    public void step(){
        if(finished) flood();
        else  make();
    }

    private void flood() {
        map = new boolean[2* xMax +1][2* yMax +1];
        Cell first = (new Cell(xMax/2, yMax/2));
        trace.add(first);
        map[first.x][first.y] = true;
        finished = false;
    }

    /* Another step in making the maze, returns whether it's done.
    (if it returns false, call it again) */
    public boolean make(){
        if(trace.size() == 0){
            Log.v("tag1", "Setting finished A");
            finished = true;
            return true;
        }
        //Not sure if the next three lines are needed.
        Cell current = trace.get(trace.size() - 1);
        x = current.x;
        y = current.y;

        // Choose a direction to move from the last Cell, or pop the last one off

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
            finished = trace.size() == 0;
            if(finished){
                Log.v("tag1", "Setting finished B");
                return true;
            }

            current = trace.get(trace.size() - 1);
            x = current.x;
            y = current.y;
            return false;
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
                next = new Cell(x, y-2);
                map[x][y-1] = true;
                break;
            default:
                throw new AssertionError("Choice was set to "+choice);
        }
        map[next.x][next.y] = true;
        trace.add(next);

        current = trace.get(trace.size() - 1);
        x = current.x;
        y = current.y;
        return false;
    }


    public class Cell{
        int x;
        int y;

        Cell(int xx, int yy){
            x=xx;
            y=yy;
        }
    }
}
