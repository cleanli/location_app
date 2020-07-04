package com.justice.clean.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.util.Log;
import android.location.LocationListener;
import android.content.pm.PackageManager;


import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v7.appcompat.R.attr.height;
import static android.support.v7.appcompat.R.attr.switchMinWidth;

public class location extends AppCompatActivity {
    private static final String TAG = "CleanLOC";
    private LocationManager lm;
    private Context mContext;
    private List<String> prodiverlist;
    private TextView tw;
    //private PermissionsChecker mPermissionsChecker;
    private String[] perms = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE
    };
    private String provider = null;
    private int loc_listen_ct = 0;
    private final LocationListener LocListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            // log it when the location changes
            if (location != null) {
                //checkDistance(location);
            }
            tw.setText(Integer.toString(loc_listen_ct++)+":loc changed\nLongitude:");
            tw.append(String.valueOf(location.getLongitude()));
            tw.append("\nLatitude：");
            tw.append(String.valueOf(location.getLatitude()));
            tw.append("\nAltitude:");
            tw.append(String.valueOf(location.getAltitude()));
            tw.append("\nTime:");
            tw.append(""+getGpsLoaalTime(location.getTime()));
        }


        public void onProviderDisabled(String provider) {
            tw.setText(Integer.toString(loc_listen_ct++)+":prov disabled:"+provider);
        }

        public void onProviderEnabled(String provider) {
            tw.setText(Integer.toString(loc_listen_ct++)+":prov enabled"+provider);
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {
            tw.setText(Integer.toString(loc_listen_ct++)+":status changed");
        }
    };

    private static String getGpsLoaalTime(long gpsTime){
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(gpsTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String datestring = df.format(calendar.getTime());

        return datestring;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Snackbar.make(view, prodiverlist.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tw = (TextView) findViewById(R.id.text);

        String serviceString = Context.LOCATION_SERVICE;
        lm = (LocationManager) getSystemService(serviceString);
        /*
        lm = (LocationManager) mContext.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        */

        prodiverlist = lm.getProviders(true);
        tw.setText(prodiverlist.toString());
        if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            if(checkPermission("android.permission.ACCESS_FINE_LOCATION", Binder.getCallingPid(),
                    Binder.getCallingUid()) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(provider, 1000, 1, LocListener);
            }
            else {
                tw.setText("No permission!!!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkpm(){
        /*
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermissionsGranted(perms)) {
                mylog("no permission, quit");
                //set_cam_hint("no permission, open camera failed");
                return false;
            } else {
                mylog("permision granted, continue");
                return true;
            }
        }
        */
        return true;

    }

    private void mylog(String l){
        Log.i(TAG, "|||||||||||||||||||||||||||||||||||||||||||");
        Log.i(TAG, l);
    }

    @Override
    protected void onDestroy(){
        lm.removeUpdates(LocListener);
        super.onDestroy();
    }

}
