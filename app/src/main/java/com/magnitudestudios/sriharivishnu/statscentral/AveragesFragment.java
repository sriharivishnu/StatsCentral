package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-26.
 */

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.List;

public class AveragesFragment extends Fragment{
    private Spinner sportSelector;
    private ListView showAverages;
    private NumberPicker ageSelector;
    private ImageButton submitGame;

    public static AveragesFragment newInstance() {
        AveragesFragment fragment = new AveragesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_averages, container, false);
        showAverages = (ListView) v.findViewById(R.id.averages_listview);
        sportSelector = (Spinner) v.findViewById(R.id.sport_selector_averages);
        ageSelector = (NumberPicker) v.findViewById(R.id.age_selector);
        submitGame = (ImageButton) v.findViewById(R.id.submit_button);

        submitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSelector.setAdapter(adapter);

        //ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.sports_array, android.R.layout.simple_spinner_item);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ageSelector.setMinValue(1);
        ageSelector.setMaxValue(99);
        ageSelector.setWrapSelectorWheel(true);
        String arr[] = new String[99];
        for (int i =1; i<100;i++){
            arr[i-1] = Integer.toString(i);
        }
        ageSelector.setDisplayedValues(arr);
        ageSelector.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            }
        });

        return v;
    }
}