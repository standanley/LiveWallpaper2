package com.example.daniel.livewallpaper2;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/21/2015.
 */
public class MyWallpaperService extends WallpaperService {


    @Override
    public Engine onCreateEngine() {
        Log.v("tag1", "Creating a MyWallpaperEngine");
        Engine myEngine = new MyWallpaperEngine();
        Log.v("tag1", "About to return it");
        return myEngine;
    }

    private class MyWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };

        private final int PIXELS_PER_SQUARE = 63;

        private int maxNumber;
        private boolean visible;
        private Maze maze;
        private Paint paint = new Paint();
        private int width;
        private int height;
        private boolean touchEnabled;

        public MyWallpaperEngine() {
            Log.v("tag1", "Setting up preferences");
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(MyWallpaperService.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "4"));
            touchEnabled = prefs.getBoolean("touch", true);
            Log.v("tag1", "Setting touchEnabled to " + touchEnabled);
            maze = null;

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.v("tag1", "In onVisibilityChanged");
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.v("tag1", "In onSurfaceDestroyed");
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            Log.v("tag1", "In onSurfaceChanged");
            maze = new Maze(width / PIXELS_PER_SQUARE, height / PIXELS_PER_SQUARE);
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (touchEnabled) {

                float x = event.getX();
                float y = event.getY();
                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        Log.v("tag1", "In onTouchEvent 2");
                        // Do your thing here
                    }
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
                super.onTouchEvent(event);
            }
        }

        private void draw() {
            //Log.v("tag1", "In Draw");
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    maze.step();
                    drawMaze(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            handler.postDelayed(drawRunner, 30);
        }

        private void drawMaze2(Canvas canvas, ArrayList<Maze.Cell> updates) {
            if (maze.finished) {
                canvas.drawColor(Color.BLACK);
                return;
            }
            paint.setColor(Color.WHITE);
            int count = 0;
            for (Maze.Cell c : updates) {
                count++;
                Log.v("tag1", "Repainting update number " + count);
                canvas.drawRect(c.x * PIXELS_PER_SQUARE, c.y * PIXELS_PER_SQUARE,
                        (c.x + 1) * PIXELS_PER_SQUARE, (c.y + 1) * PIXELS_PER_SQUARE,
                        paint);
            }
            paint.setColor(Color.GREEN);
            canvas.drawRect(maze.x * PIXELS_PER_SQUARE, maze.y * PIXELS_PER_SQUARE,
                    (maze.x + 1) * PIXELS_PER_SQUARE, (maze.y + 1) * PIXELS_PER_SQUARE,
                    paint);
        }

        private void drawMaze(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            for (int xx = 0; xx < maze.map.length; xx++) {
                for (int yy = 0; yy < maze.map[0].length; yy++) {
                    //drawRect(float left, float top, float right, float bottom, Paint paint)
                    if (xx == maze.x && yy == maze.y) paint.setColor(Color.GREEN);
                    else if (maze.map[xx][yy]) paint.setColor(Color.WHITE);
                    else continue;
                    canvas.drawRect(xx * PIXELS_PER_SQUARE, yy * PIXELS_PER_SQUARE, (xx + 1) * PIXELS_PER_SQUARE, (yy + 1) * PIXELS_PER_SQUARE, paint);
                }
            }
        }
    }
}

