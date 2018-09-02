package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-30.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public MyAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("TAG",data.toString());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        vi = inflater.inflate(R.layout.row_stats, null);
        TextView text = vi.findViewById(R.id.stat);
        text.setText(data.get(position));

        return vi;
    }
}
