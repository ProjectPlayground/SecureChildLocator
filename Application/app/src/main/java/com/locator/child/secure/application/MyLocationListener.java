package com.locator.child.secure.application;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener {
    Manager m = LocalMemory.getInstance().getManager();
    Context context;

    public MyLocationListener(Context c){
       context=c;
    }
    @Override
    public void onLocationChanged(Location loc) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context,  " You cannot disable your GPS while running your App." , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}

