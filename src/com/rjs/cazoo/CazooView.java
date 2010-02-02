package com.rjs.cazoo;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.AttributeSet;
import android.util.Log;

import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import java.util.Random;
import java.util.ArrayList;
 
public class CazooView extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG="CazooView";

    private CazooThread _thread;

    private final Random rnd = new Random();

    private int mWorldHeight   = 20;
    private int mWorldWidth    = 20;
    private int zoomLevel      = 1;
    private int zoomInc        = 1;
    private int mSteps         = 0;
    private int mSizeInc       = 8;

    private Paint mTextPaint = new Paint();

    private World world;
    //private World world = new AntWorld(20,20);
    //private World world = new GOLWorld(mWorldHeight,mWorldWidth);

    public CazooView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CazooView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public CazooView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        gol();

        mTextPaint.setTextSize( 10 );
        mTextPaint.setARGB( 128, 255, 255, 255 );

        // so can grab keyevents etc.
        setFocusable(true);
        setFocusableInTouchMode(true);

        getHolder().addCallback(this);
        _thread = new CazooThread(getHolder(), this);
    }

    public void adjustBoardSize( int adj )
    {
        if( adj > 0 )
        {
            mWorldHeight += mSizeInc;
            mWorldWidth += mSizeInc;
        }
        else
        {
            mWorldHeight -= mSizeInc;
            mWorldWidth -= mSizeInc;
        }

        clampWorldDims();
        resetWorld();
    }

    private void clampWorldDims()
    {
        if(mWorldHeight < 10)
            mWorldHeight = 10;
        if(mWorldWidth < 10)
            mWorldWidth = 10;
        if(mWorldHeight > 100)
            mWorldHeight = 100;
        if(mWorldWidth > 100)
            mWorldWidth = 100;
    }

    public void adjustZoom( int adj )
    {
        zoomLevel += zoomInc * adj;
        if(zoomLevel < 1)
            zoomLevel = 1;
        if(zoomLevel > 100)
            zoomLevel = 100;
    }

    public void gol()
    {
        world = new GOLWorld( mWorldWidth, mWorldHeight );
    }
    
    public void ant()
    {
        world = new AntWorld( mWorldWidth, mWorldHeight );
    }

    public void setSize( int w, int h )
    {
        mWorldHeight = h;
        mWorldWidth = w;
        resetWorld();
    }

    public void resetWorld()
    {
        synchronized(world)
        {
            world.reset(mWorldWidth, mWorldHeight);
        }
    }

    // called from the thread
    public void iterate()
    {
        synchronized(world)
        {
            mSteps = world.iterate();
        }
    }

    @Override protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        synchronized(world)
        {
            world.draw(canvas, zoomLevel);
        }

        canvas.drawText( "iterations: "+String.valueOf( mSteps ), 10,10, mTextPaint);
    }


    @Override public void surfaceChanged(SurfaceHolder holder, int format, int
            width, int height)
    {
        // called whenever surface changed -including just after creation
    }

    @Override public void surfaceCreated(SurfaceHolder holder)
    {
        _thread.setRunning(true);
        _thread.start();
    }

    @Override public void surfaceDestroyed(SurfaceHolder holder)
    {
        // simply copied from sample application LunarLander:
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
}

