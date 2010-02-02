package com.rjs.cazoo;

class Ant
{
    private int x,y;

    private final int NORTH = 0;
    private final int EAST  = 1;
    private final int SOUTH = 2;
    private final int WEST  = 3;

    private int dir = NORTH;

    private AntWorld world;

    public Ant( AntWorld world, int x, int y )
    {
        this.x = x;
        this.y = y;

        this.world = world;
    }

    public void move( AntWorld world )
    {
        boolean liveCell = world.isAlive( x, y );

        if( liveCell )
            dir -= 1;
        else
            dir += 1;

        if( dir < 0 )
            dir = dir + 4;

        if( dir > 3 )
            dir = dir - 4;

        world.setCell( x, y, !liveCell );

        switch(dir)
        {
            case NORTH:
                y += 1;
                break;
            case EAST:
                x += 1;
                break;
            case SOUTH:
                y -= 1;
                break;
            case WEST:
                x -= 1;
                break;
            default:
                //should never get here
                break;
        }

        wrapPos();
    }

    private void wrapPos()
    {
        int ww = world.getWidth();
        int wh = world.getHeight();

        if( x < 0 )
            x += ww;
        if( y < 0 )
            y += wh;

        x = x % ww;
        y = y % wh;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
    
