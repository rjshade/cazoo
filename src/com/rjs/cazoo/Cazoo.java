package com.rjs.cazoo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Canvas;
import android.graphics.Bitmap;

import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;

import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.KeyEvent;

public class Cazoo extends Activity implements OnKeyListener
{
    private final String TAG="CazooActivity";
    private CazooView cazooView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        initComponents();
    }

    private void initComponents()
    {
        this.cazooView = (CazooView) this.findViewById( R.id.cazooView );
        cazooView.setOnKeyListener(this);
    }

    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if( event.getAction() == event.ACTION_DOWN )
        {
        //Log.d(TAG, "Key pressed!!! "+keyCode);
        boolean handled = true;
        switch (keyCode) {
            //case KeyEvent.KEYCODE_DPAD_UP:
                //cazooView.adjustBoardSize(1);
                //break;
            default:
                handled = false;
                break;
        }
		return handled;// super.onKeyDown(keyCode, event);
        }
        
        return false;
    }


    private final static int MENU_RESET    = 1;
    private final static int MENU_QUIT     = 2;
    private final static int MENU_ZOOM_IN  = 3;
    private final static int MENU_ZOOM_OUT = 4;
    private final static int MENU_GOL      = 5;
    private final static int MENU_ANT      = 6;

    // Creates the menu items
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, MENU_ZOOM_IN, 0, "Zoom In").setIcon(
                android.R.drawable.ic_menu_zoom);
        menu.add(0, MENU_ZOOM_OUT, 0, "Zoom Out").setIcon(
                android.R.drawable.ic_menu_zoom);
        menu.add(0, MENU_GOL, 0, "Conway's Game of Life").setIcon(
                android.R.drawable.ic_menu_add);
        menu.add(0, MENU_ANT, 0, "Langton's Ant").setIcon(
                android.R.drawable.ic_menu_add);
        menu.add(0, MENU_RESET, 0, "Reset").setIcon(
                android.R.drawable.ic_menu_compass);
        menu.add(0, MENU_QUIT, 0, "Quit").setIcon(
                android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    // Handles item selections
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case MENU_ZOOM_IN:
                cazooView.adjustZoom( +1 );
                return true;
            case MENU_ZOOM_OUT:
                cazooView.adjustZoom( -1 );
                return true;
            case MENU_GOL:
                cazooView.gol();
                return true;
            case MENU_ANT:
                cazooView.ant();
                return true;
            case MENU_RESET:
                cazooView.resetWorld();
                return true;
            case MENU_QUIT:
                this.finish();
                return true;
        }
        return false;
    }
}
