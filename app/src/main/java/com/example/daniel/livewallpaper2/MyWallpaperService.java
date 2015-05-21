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
import java.util.List;

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

        private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;

        public MyWallpaperEngine() {
            Log.v("tag1", "Setting up preferences");
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(MyWallpaperService.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "4"));
            touchEnabled = prefs.getBoolean("touch", true);
            Log.v("tag1", "Setting touchenabled to "+touchEnabled);
            circles = new ArrayList<MyPoint>();

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
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
            for(int i=0;i<maxNumber;i++){
                int x = (int) (width * Math.random());
                int y = (int) (height * Math.random());
                circles.add(new MyPoint(x, y));
            }
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
                        canvas.drawColor(Color.BLACK);
                        circles.clear();
                        circles.add(new MyPoint(x, y));
                        drawCircles(canvas, circles);

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
                    moveCircles(circles);
                    drawCircles(canvas, circles);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 30);
            }
        }

        private void moveCircles(List<MyPoint> circles) {
            for(MyPoint circle: circles){
                circle.ddx+=(2*Math.random()-1)*.1;
                circle.ddy+=(2*Math.random()-1)*.1;
                circle.ddx*=.9;
                circle.ddy*=.9;

                circle.dx+=circle.ddx;
                circle.dy+=circle.ddy;
                circle.dx*=.99;
                circle.dy*=.99;

                circle.x+=circle.dx+width;
                circle.y+=circle.dy+height;
                circle.x%=width;
                circle.y%=height;
            }
        }

        // Surface view requires that all elements are drawn completely
        private void drawCircles(Canvas canvas, List<MyPoint> circles) {
            canvas.drawColor(Color.BLACK);
            for (MyPoint point : circles) {
                canvas.drawCircle(point.x, point.y, 20.0f, paint);
            }
        }



        private class MyPoint {
            private float x;
            private float y;
            private float dx = 0;
            private float dy = 0;
            private float ddx = 0;
            private float ddy = 0;

            public MyPoint(float x, float y) {
                this.x = x;
                this.y = y;
            }
        }
    }



}

