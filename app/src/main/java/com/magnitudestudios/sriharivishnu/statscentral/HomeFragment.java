package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-26.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    private SeekBar seekBar;
    private Spinner sportSelector;
    private ImageButton startGame, settingsButton;
    private TextView showLength, sportTitle, timeLengthTitle, vsText;
    private ImageView mainLogo;
    private EditText yourTeamEdit, opposingTeamEdit;
    private ImageButton livestream_button;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        showLength = (TextView) view.findViewById(R.id.timeLength);
        mainLogo = (ImageView) view.findViewById(R.id.mainLogo);
        sportTitle = (TextView) view.findViewById(R.id.sportTitle);
        timeLengthTitle = (TextView) view.findViewById(R.id.lengthTitle);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        yourTeamEdit = (EditText) view.findViewById(R.id.yourTeamEdit);
        opposingTeamEdit = (EditText) view.findViewById(R.id.opposingTeamEdit);
        startGame = (ImageButton) view.findViewById(R.id.startNewGame);
        vsText = (TextView) view.findViewById(R.id.vsText);
        settingsButton = (ImageButton) view.findViewById(R.id.settingsButton);
        livestream_button = (ImageButton) view.findViewById(R.id.start_livestream);

        sportSelector = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSelector.setAdapter(adapter);

        seekBar.setProgress(90);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() != 150) {
                    showLength.setText(Integer.toString(seekBar.getProgress()));
                } else {
                    showLength.setText("∞");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        buttonAnimation(startGame);
        buttonAnimation(settingsButton);
        buttonAnimation(livestream_button);
        return view;

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
    public void buttonAction(ImageButton b) {
        switch (b.getId()) {
            case R.id.startNewGame:
                startNewGame(false);
                break;
            case R.id.settingsButton:
                goToSettings();
                break;
            case R.id.start_livestream:
                startLiveStream();
                break;
        }
    }
    private void startNewGame(boolean b){
        Intent intent = new Intent(getActivity(), Game.class);
        if (yourTeamEdit.getText().toString().equals("")) {
            intent.putExtra("YourTeamName", "Team1");
        } else {
            intent.putExtra("YourTeamName", yourTeamEdit.getText().toString());
        }
        if (opposingTeamEdit.getText().toString().equals("")) {
            intent.putExtra("OtherTeamName", "Team2");
        } else {
            intent.putExtra("OtherTeamName", opposingTeamEdit.getText().toString());
        }
        intent.putExtra("TimeLength", seekBar.getProgress());
        if (b) {
            intent.putExtra("LiveStream",true);
        } else {
            intent.putExtra("LiveStream", false);
        }
        startActivity(intent);

    }
    private void startLiveStream() {
        if (isNetworkConnected()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            String key = reference.child("livestreams").push().getKey();
            final CustomDialog showCode = new CustomDialog(getActivity(), key);
            showCode.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (showCode.getUserSelection()) {
                        startNewGame(true);
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "No Wifi Connection", Toast.LENGTH_LONG).show();
        }
    }
    private void goToSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
