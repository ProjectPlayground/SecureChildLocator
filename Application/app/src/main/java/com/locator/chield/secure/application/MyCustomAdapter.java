package com.locator.chield.secure.application;

import android.app.Activity;
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

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;



    public MyCustomAdapter(List<String> list, Context context) {
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
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle buttons and add onClickListeners
        final Button elementBtn = (Button)view.findViewById(R.id.buttonListElement);
        final Button deleteBtn = (Button)view.findViewById(R.id.buttonListRemove);

        elementBtn.setText(list.get(position));

        elementBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LocalMemory m = LocalMemory.getInstance();
                List<Parent> parents = m.getParents();
                List<Kid> kids = m.getKids();

                if(context.getClass().getSimpleName().equals("MainKidsActivity")){
                    Intent myIntent = new Intent(context, ViewParentActivity.class);
                    for (int i=0;i<parents.size();i++){
                        if(parents.get(i).getMail().equals(elementBtn.getText())){
                            myIntent.putExtra("MAIL", parents.get(i).getMail());
                            myIntent.putExtra("NAME", parents.get(i).getName());
                            break;
                        }
                    }
                    context.startActivity(myIntent);
                }
                else{
                    Intent myIntent = new Intent(context, ViewKidActivity.class);
                    for (int i=0;i<kids.size();i++){
                        if(kids.get(i).getName().equals(elementBtn.getText())){
                            myIntent.putExtra("NAME", kids.get(i).getName());
                            myIntent.putExtra("PASS", kids.get(i).getPass());
                            break;
                        }
                    }
                    context.startActivity(myIntent);
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LocalMemory m = LocalMemory.getInstance();

                if(context.getClass().getSimpleName().equals("MainKidsActivity")) {
                    m.removeParent(list.get(position));
                    Intent myIntent = new Intent(context, MainKidsActivity.class);
                    context.startActivity(myIntent);
                    Activity a = (Activity) context;
                    a.finish();
                }
                else{
                    m.removeKid(list.get(position));
                    Intent myIntent = new Intent(context, MainParentActivity.class);
                    context.startActivity(myIntent);
                    Activity a = (Activity) context;
                    a.finish();
                }
            }
        });

        return view;
    }
}
