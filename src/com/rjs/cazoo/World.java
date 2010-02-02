package com.rjs.cazoo;

import android.graphics.Canvas;

public interface World
{
    public void draw(Canvas canvas, int zoom);

    public int iterate();

    public void reset();
    public void reset( int w, int h );
}

