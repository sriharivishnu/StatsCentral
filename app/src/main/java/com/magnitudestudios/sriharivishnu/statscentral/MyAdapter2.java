package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-30.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter2 extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<MatchClass> data;
    ArrayList<MatchClass> originalData;
    private static LayoutInflater inflater = null;

    public MyAdapter2(Context context, ArrayList<MatchClass> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.originalData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        vi = inflater.inflate(R.layout.row_past_games, null);
        TextView teamNames = vi.findViewById(R.id.teamNames);
        TextView date = vi.findViewById(R.id.date);
        TextView score = vi.findViewById(R.id.score);
        teamNames.setText(data.get(position).getTeams());
        date.setText(new Date(data.get(position).getTimestampCreatedLong()).toString());
        score.setText(data.get(position).getScore());
        return vi;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint,FilterResults results) {

                data = (ArrayList<MatchClass>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<MatchClass> filteredArrList = new ArrayList<MatchClass>();
                if (data == null) data = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = originalData.size();
                    results.values = originalData;
                } else {
                    data = originalData;
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getTeam1().toLowerCase().startsWith(constraint.toString())) {
                            filteredArrList.add(data.get(i));
                        }
                        else if (data.get(i).getTeam2().toLowerCase().startsWith(constraint.toString())) {
                            filteredArrList.add(data.get(i));
                        }
                        else if (new Date(data.get(i).getTimestampCreatedLong()).toString().toLowerCase().contains(constraint)) {
                            filteredArrList.add(data.get(i));
                        }
                        else if (data.get(i).getScore().startsWith(constraint.toString())) {
                            filteredArrList.add(data.get(i));
                        }
                    }
                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
