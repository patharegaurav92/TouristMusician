/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;

import static android.R.attr.x;

public class TourMap extends View {
    private static final String TAG="TourMap";
    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(12f);
        Point previousPt = null;
        Point startPt = null;
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        for (Point p : list) {
            Log.v(TAG,"for loop");

            if(previousPt == null) {
                Log.v(TAG,"First Point");
                canvas.drawCircle(p.x, p.y, 20, pointPaint);
                previousPt = p;
                startPt = p;
            }
            else {
                canvas.drawLine(previousPt.x, previousPt.y, p.x, p.y, linePaint);
                canvas.drawCircle(p.x, p.y, 20, pointPaint);
                previousPt = p;
            }
        }
        if(startPt!=null && previousPt!=startPt)
        canvas.drawLine(startPt.x, startPt.y, previousPt.x, previousPt.y, linePaint);


        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                Log.v(TAG,"The point is ("+p.x+","+p.y+")");
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
