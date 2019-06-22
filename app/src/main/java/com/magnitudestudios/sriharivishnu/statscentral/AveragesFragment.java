package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-26.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
        ageSelector.setMaxValue(5);
        ageSelector.setWrapSelectorWheel(true);
        String arr[] = new String[99];
        String arr2[] = {"Passes","Shots","Goals","Fouls", "Turnovers"};
//        for (int i =1; i<100;i++){
//            arr[i-1] = Integer.toString(i);
//        }
        ageSelector.setDisplayedValues(arr2);
        ageSelector.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            }
        });
        ArrayList<String> data = new ArrayList<>(Arrays.asList(arr2));
        MyAdapter list_adapter = new MyAdapter(getContext(), data);
        showAverages.setAdapter(list_adapter);
        buttonAnimation(submitGame);
        return v;
    }
    public void buttonAnimation(final ImageButton button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.9f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f);
                        scaleDownX.setDuration(0);
                        scaleDownY.setDuration(0);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY);

                        scaleDown.start();

                        return true;
                    case MotionEvent.ACTION_UP:
                        buttonAction(button);
                        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(button, "scaleX", 1f);
                        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(button, "scaleY", 1f);
                        scaleDownX2.setDuration(0);
                        scaleDownY2.setDuration(0);

                        AnimatorSet scaleDown2 = new AnimatorSet();
                        scaleDown2.play(scaleDownX2).with(scaleDownY2);

                        scaleDown2.start();

                        return true;
                }
                return true;
            }
        });
    }
    private void buttonAction(ImageButton b) {
        switch (b.getId()) {
            case R.id.submit_button:
                submit();
        }
    }
    private void submit() {
        //TODO
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext()).setTitle("BETA").setMessage("Sorry, this feature isn't available yet!").setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();



    }
}