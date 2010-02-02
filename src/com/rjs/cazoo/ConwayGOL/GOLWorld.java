package com.rjs.cazoo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.Log;

import java.util.Random;

public class GOLWorld implements World
{
    private static final String TAG = "GOLWorld";
    private int width,height;
    private GOLCell[][] cells;

    private int mCellSize = 1;

    private int mSteps = 0;

    private Random rnd = new Random();

    Paint mLinePaint = new Paint();
    Paint mCellPaint = new Paint();

    public GOLWorld()
    {
        this.width = 10;
        this.height = 10;

        init();
    }

    public GOLWorld( int width, int height )
    {
        this.width = width;
        this.height = height;

        init();
     }

    private void init()
    {
        mSteps = 0;
        
        cells = new GOLCell[width][height];

        for( int x = 0; x < width; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                boolean initVal = rnd.nextBoolean();
                //boolean initVal = false;
                cells[x][y] = new GOLCell(initVal);
            }
        }

        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(64,255,255,255);

        mCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCellPaint.setAntiAlias(true);
        mCellPaint.setARGB(255,255,255,255);
    }

    public void glider()
    {
       cells = new GOLCell[width][height];
        for( int x = 0; x < width; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                cells[x][y] = new GOLCell(false);
            }
        }

        cells[3][1].setNewState(true); cells[3][1].updateState();
        cells[4][1].setNewState(true); cells[4][1].updateState();
        cells[5][1].setNewState(true); cells[5][1].updateState();
        cells[5][2].setNewState(true); cells[5][2].updateState();
        cells[4][3].setNewState(true); cells[4][3].updateState();
    }
    
    public void reset()
    {
        width = 10;
        height = 10;
        init();
    }

    public void reset(int w, int h)
    {
        width = w;
        height = h;
        init();
    }

    public boolean isAlive(int x, int y)
    {
        //Log.d( TAG, " x = "+x+ "  y = "+y+"  dims = "+width+ " "+height);
        return cells[x][y].getState();
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public int iterate()
    {
        for( int x = 0; x < width; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                int numNeigh = countNeighbours( x, y );

                if( numNeigh < 2 || numNeigh > 3 )
                {
                    // dies of starvation or overcrowding
                    cells[x][y].setNewState( false );
                }
				else
                {
					if(numNeigh == 3)
                    {
                        // new born
						cells[x][y].setNewState(true);
                    }
                }
            }
        }

        for( int x = 0; x < width; x++ )
        {
            for( int y = 0; y < height; y++ )
            {
                cells[x][y].updateState();
            }
        }

        mSteps += 1;

        return mSteps;
    }

    private int countNeighbours( int x, int y )
    {
        int count = 0;

        if(cellAlive(x-1, y-1)){ count++; }
        if(cellAlive(x-1, y  )){ count++; }
        if(cellAlive(x-1, y+1)){ count++; }

        if(cellAlive(x  , y-1)){ count++; }
        if(cellAlive(x  , y+1)){ count++; }
        
        if(cellAlive(x+1, y-1)){ count++; }
        if(cellAlive(x+1, y  )){ count++; }
        if(cellAlive(x+1, y+1)){ count++; }

		return count;
    }
    
    private boolean cellAlive(int x, int y)
	{
        if ( x < 0 )
            x = width + x;
        if( y < 0 )
            y = height + y;
        if( x > width - 1 )
            x = x - width;
        if( y > height - 1 )
            y = y - height;

        return cells[x][y].getState();
	}

    public void draw(Canvas canvas, int zoom)
    {
        canvas.drawRGB( 0,0,0 );

        int ch = canvas.getHeight();
        int cw = canvas.getWidth();

        int wh = getHeight();
        int ww = getWidth();

        int cellRad = mCellSize * zoom;
        int cellPad = 0;
        int cellWidth = (cellRad + cellPad) * 2 + 1;
        int gridWidth = cellWidth + 1;

        //always center world in canvas
        int worldHW = (ww / 2) * gridWidth;
        int worldHH = (wh / 2) * gridWidth;
        int canvasMidW = cw / 2;
        int canvasMidH = ch / 2;
        int wOffset = canvasMidW - worldHW;
        int hOffset = canvasMidH - worldHH;

        drawGrid( canvas, wOffset, hOffset, ww, wh, gridWidth );

        drawCells( canvas, ww, wh, wOffset, hOffset, gridWidth, cellRad, cellPad );
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

