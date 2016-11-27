package com.locator.child.secure.application;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MyLocationListener implements LocationListener {
    Manager m = LocalMemory.getInstance().getManager();
    Context context;

    public MyLocationListener(Context c){
       context=c;
    }
    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_LONG).show();
        Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + loc.getLatitude() + ", lon=" + loc.getLongitude());


        /*
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
        editLocation.setText(s);*/
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

