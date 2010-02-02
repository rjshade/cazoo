package com.rjs.cazoo;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

class CazooThread extends Thread
{
    private SurfaceHolder _surfaceHolder;
    private CazooView _cv;
    private boolean _run = false;

    public CazooThread(SurfaceHolder surfaceHolder, CazooView cv) {
        _surfaceHolder = surfaceHolder;
        _cv = cv;
    }

    public void setRunning(boolean run) {
        _run = run;
    }

    public SurfaceHolder getSurfaceHolder() {
        return _surfaceHolder;
    }

    @Override public void run()
    {
        Canvas c;
        while (_run)
        {
            c = null;
            //try
            //{
                //Thread.sleep(100);
            //}
            //catch(InterruptedException e){}
            try {

                _cv.iterate();

                c = _surfaceHolder.lockCanvas(null);
                synchronized (_surfaceHolder)
                {
                    _cv.onDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}

