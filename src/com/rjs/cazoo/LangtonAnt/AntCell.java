package com.rjs.cazoo;

public class AntCell
{
    // false means dead, true means alive
    private boolean state;

    public AntCell( boolean state )
    {
        this.state = state;
    }

    public void setState( boolean state )
    {
        this.state = state;
    }

    public boolean getState()
    {
        return state;
    }
}

