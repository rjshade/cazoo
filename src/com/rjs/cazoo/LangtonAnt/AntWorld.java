package com.rjs.cazoo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.Log;

import java.util.Random;

public class AntWorld implements World
{
    private static final String TAG = "AntWorld";
    private int width,height;
    private AntCell[][] cells;

    private Ant ant;

    private int mCellSize = 1;

    private Random rnd = new Random();

    Paint mLinePaint = new Paint();
    Paint mCellPaint = new Paint();

    public AntWorld()
    {
        this.width = 20;
        this.height = 20;

        init();
    }

    public AntWorld( int width, int height )
    {
        this.width = width;
        this.height = height;

        init();
     }

    private void init()
    {
        mSteps = 0;
        ant = new Ant( this, 10, 10 );
        cells = new AntCell[width][height];

        for( int x = 0; x < width; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                boolean initVal = false;
                cells[x][y] = new AntCell(initVal);
            }
        }

        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(64,255,255,255);

        mCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCellPaint.setAntiAlias(true);
        mCellPaint.setARGB(255,255,255,255);
    }

    public void reset()
    {
        width = 20;
        height = 20;
        init();
    }

    public void reset(int w, int h)
    {
        width = w;
        height = h;
        init();
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    int mSteps = 0;
    public int iterate()
    {
        //while(steps < 10000)
        //{
            //ant.move(this);
            //steps += 1;
        //}
        
        ant.move(this);

        mSteps += 1;

        return mSteps;
    }

    public void draw(Canvas canvas, int zoom)
    {
        canvas.drawRGB( 0,0,0 );
        //if( steps > 10000 )
        {
            int ch = canvas.getHeight();
            int cw = canvas.getWidth();

            int wh = getHeight();
            int ww = getWidth();

            int cellRad = mCellSize * zoom;
            int cellPad = 0;
            int cellWidth = (cellRad + cellPad) * 2 + 1;
            int gridWidth = cellWidth + 1;

            //always center view in canvas
            int worldHW = (ww / 2) * gridWidth;
            int worldHH = (wh / 2) * gridWidth;
            int canvasMidW = cw / 2;
            int canvasMidH = ch / 2;
            int wOffset = canvasMidW - worldHW;
            int hOffset = canvasMidH - worldHH;

            drawGrid( canvas, wOffset, hOffset, ww, wh, gridWidth );

            drawCells( canvas, ww, wh, wOffset, hOffset, gridWidth, cellRad, cellPad );

            drawAnt(   canvas, ww, wh, wOffset, hOffset, gridWidth, cellRad, cellPad, ant );
        }
        //else
        {
        }
    }

    private void drawAnt( Canvas canvas, int ww, int wh, int wOffset, int hOffset, int gridWidth, int cellRad, int cellPad, Ant ant )
    {
        mCellPaint.setARGB(255,0,255,0);

        int antXCenter = wOffset + ant.getX() * gridWidth + cellRad + cellPad + 1 ; 
        int antYCenter = hOffset + ant.getY() * gridWidth + cellRad + cellPad + 1 ; 
        canvas.drawCircle( antXCenter, antYCenter, cellRad, mCellPaint);
        
        mCellPaint.setARGB(255,255,255,255);
    }

    public boolean isAlive(int x, int y)
    {
        //Log.d( TAG, " x = "+x+ "  y = "+y+"  dims = "+width+ " "+height);
        return cells[x][y].getState();
    }

    public void setCell( int x, int y, boolean state )
    {
        cells[x][y].setState( state );
    }

    private void drawCells( Canvas canvas, int ww, int wh, int wOffset, int hOffset, int gridWidth, int cellRad, int cellPad )
    {
        for( int x = 0; x < ww; x++ )
        {
            for( int y = 0; y < wh; y++ )
            {
                if(isAlive(x,y))
                {
                    int cellXCenter = wOffset + x * gridWidth + cellRad + cellPad + 1 ; 
                    int cellYCenter = hOffset + y * gridWidth + cellRad + cellPad + 1 ; 
                    //Log.d(TAG,"    drawing cell at x,y: " +cellXCenter + " " + cellYCenter);
                    canvas.drawCircle( cellXCenter, cellYCenter, cellRad, mCellPaint);
                }
            }
        }
    }

    private void drawGrid( Canvas canvas, int wOffset, int hOffset, int ww, int wh, int gridWidth )
    {
        int line_x = wOffset;
        for( int x = 0; x < ww; x++ )
        {
            //Log.d(TAG," drawing grid x at x = "+line_x);
            canvas.drawLine( line_x, hOffset, line_x, hOffset + wh * gridWidth, mLinePaint );
            line_x += gridWidth;
        }

        canvas.drawLine( line_x, hOffset, line_x, hOffset + wh * gridWidth + 1, mLinePaint );

        int line_y = hOffset;
        for( int y = 0; y < wh; y++ )
        {
            //Log.d(TAG," drawing grid y at y = "+line_y);
            canvas.drawLine( wOffset, line_y, wOffset + ww * gridWidth, line_y, mLinePaint );
            line_y += gridWidth;
        }
        canvas.drawLine( wOffset, line_y, wOffset + ww * gridWidth + 1, line_y, mLinePaint );
    }
}

