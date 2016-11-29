package com.locator.child.secure.application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyLocationsAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;



    public MyLocationsAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_locs, null);
        }

        //Handle buttons and add onClickListeners
        final Button elementBtn = (Button)view.findViewById(R.id.buttonListElement1);

        elementBtn.setText(list.get(position));

        elementBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LocalMemory m = LocalMemory.getInstance();
                List<Parent> parents = m.getParents();

                Intent myIntent = new Intent(context, MapsActivity.class);
                String[] splited = elementBtn.getText().toString().split(";");
                String lat="0";
                String lon="0";
                if(splited.length==3){
                    lat=splited[0];
                    lon=splited[1];
                }
                myIntent.putExtra("LAT", lat);
                myIntent.putExtra("LON", lon);

                context.startActivity(myIntent);

            }
        });

        return view;
    }
}
