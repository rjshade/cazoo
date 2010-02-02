package com.rjs.cazoo;

public class GOLCell
{
    // false means dead, true means alive
    private boolean state, newState;
    private int age = 0;

    public GOLCell( boolean state )
    {
        this.state = state;
        newState = state;
    }

    public void setNewState(boolean newState)
    {
        this.newState = newState;
    }

    public void updateState()
    {
        this.state = newState;
    }

    public boolean getState()
    {
        return state;
    }

    //public void draw(Canvas canvas)
    //{
        //if (!isLiving())
        //{
			//return;
		//}
		//cellPaint.setColor(color);
		//canvas.drawCircle(cX, cY, radius, cellPaint);
    //}
}

