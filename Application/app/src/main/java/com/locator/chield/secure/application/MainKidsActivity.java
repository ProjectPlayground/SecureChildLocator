package com.locator.chield.secure.application;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainKidsActivity extends AppCompatActivity {

    LocalMemory m = LocalMemory.getInstance();
    List<Parent> parents = m.getParents();

    private List<String> mails = new ArrayList <>();
    private List<String> names = new ArrayList <>();
    private List<String> passes = new ArrayList <>();

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kids);

        context=this;

        for (int i=0;i<parents.size();i++){
            mails.add(parents.get(i).getMail());
            names.add(parents.get(i).getName());
            passes.add(parents.get(i).getPass());
        }

        populateListView();

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(getBaseContext());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

        if (m.getRunnableGPS()==null && m.getParents().size()>0){
             Runnable runnable = new Runnable(){
                public void run() {

                    int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    double lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    double longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                    m.getManager().addLocation(context,lat,longitude);

                    Handler handler = new Handler();
                    m.setHandlerGPS(handler);
                    handler.postDelayed(this, 30000);
                }
            };
            m.setRunnableGPS(runnable);
            runnable.run();
        }
        if (m.getRunnableGPS()!=null && m.getParents().size()==0){
            Handler handler = m.getHandlerGPS();
            handler.removeCallbacks(m.getRunnableGPS());
            m.setRunnableGPS(null);
            m.setHandlerGPS(null);
        }

        final Button button = (Button) findViewById(R.id.buttonGoAddParent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainKidsActivity.this, AddParentActivity.class);
                MainKidsActivity.this.startActivity(myIntent);
                finish();
            }
        });
    }


    private void populateListView() {
        MyKidsAdapter adapter = new MyKidsAdapter(mails,this);
        ListView list = (ListView) findViewById(R.id.listViewParents);
        list.setAdapter(adapter);
    }
}
