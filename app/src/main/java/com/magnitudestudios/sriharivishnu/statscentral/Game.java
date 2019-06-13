package com.magnitudestudios.sriharivishnu.statscentral;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class Game extends AppCompatActivity {
    private ImageButton passYourTeam, shotYourTeam, goalYourTeam, foulYourTeam, turnoverYourTeam;
    private ImageButton passOtherTeam, shotOtherTeam, goalOtherTeam, foulOtherTeam, turnoverOtherTeam;
    private ImageButton endgame, pauseButton;
    private int ypass,yshot,ygoal,yfoul,yturnover;
    private int opass,oshot,ogoal,ofoul,oturnover;
    private ArrayList<String> yourTeam, otherTeam, master;
    private TextView yourTeamText, otherTeamText;
    private Chronometer chronometer;
    private TextView scoreTeam1, scoreTeam2;
    private boolean activated;
    private boolean chronoState;
    private long timewhenstopped;
    private Button team1Button, team2Button;
    private boolean possession;
    private Drawable pause,play,ball,transparent;
    private int myPos, otherPos;
    private String team1Name, team2Name;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    ArrayList<String> team1Contents,team2Contents;
    private boolean livestream_state;
    private String livestreamKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Database Instances
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        //Red, Blue, White, Black, Yellow, Green
        //States
        chronoState = true;
        possession = true;
        myPos = otherPos = 0;

        ypass = yshot = ygoal = yfoul = yturnover = 0;
        opass = oshot = ogoal = ofoul = oturnover = 0;
        yourTeam = new ArrayList<String>();
        otherTeam = new ArrayList<String>();
        master = new ArrayList<String>();

        yourTeamText = (TextView) findViewById(R.id.team1Name);
        otherTeamText = (TextView) findViewById(R.id.team2Name);

        team1Name = getIntent().getStringExtra("YourTeamName");
        team2Name = getIntent().getStringExtra("OtherTeamName");

        livestream_state = getIntent().getBooleanExtra("LiveStream", false);

        livestreamKey = getIntent().getStringExtra("LiveStreamKey");

        if (livestream_state) {
            init_livestream();
        }

        yourTeamText.setText(team1Name);
        otherTeamText.setText(team2Name);

        //Your Team
        passYourTeam = (ImageButton) findViewById(R.id.pass_your_team);
        shotYourTeam = (ImageButton) findViewById(R.id.shot_your_team);
        goalYourTeam = (ImageButton) findViewById(R.id.goal_your_team);
        foulYourTeam = (ImageButton) findViewById(R.id.foul_your_team);
        turnoverYourTeam = (ImageButton) findViewById(R.id.turnover_your_team);

        buttonAnimation(passYourTeam);
        buttonAnimation(shotYourTeam);
        buttonAnimation(goalYourTeam);
        buttonAnimation(foulYourTeam);
        buttonAnimation(turnoverYourTeam);

        //Other Team
        passOtherTeam = (ImageButton) findViewById(R.id.pass_other_team);
        shotOtherTeam = (ImageButton) findViewById(R.id.shot_other_team);
        goalOtherTeam = (ImageButton) findViewById(R.id.goal_other_team);
        foulOtherTeam = (ImageButton) findViewById(R.id.foul_other_team);
        turnoverOtherTeam = (ImageButton) findViewById(R.id.turnover_other_team);

        buttonAnimation(passOtherTeam);
        buttonAnimation(shotOtherTeam);
        buttonAnimation(goalOtherTeam);
        buttonAnimation(foulOtherTeam);
        buttonAnimation(turnoverOtherTeam);

        chronometer = (Chronometer) findViewById(R.id.chronometer2);
        final int timeLength = getIntent().getIntExtra("TimeLength", 0);
        final long maxTime = 1000*60*timeLength;
        activated = false;
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (possession) {
                    myPos += 1;
                } else {
                    otherPos += 1;
                }
                if (timeLength != 150) {
                    int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                    if (elapsedMillis > maxTime) {
                        if (!activated) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(chronometer.getContext());
                            builder.setTitle("Time Run Out");
                            builder.setMessage("Would you like to end the game and view the stats?");
                            builder.setPositiveButton("End Game", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    endGame(true);
                                }
                            });
                            builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            activated = true;
                        }
                    }
                }

            }
        });
        chronometer.start();

        scoreTeam1 = (TextView) findViewById(R.id.scoreTeam1);
        scoreTeam2 = (TextView) findViewById(R.id.scoreTeam2);

        endgame = (ImageButton) findViewById(R.id.end_game);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        buttonAnimation(endgame);
        buttonAnimation(pauseButton);

        team1Button = (Button) findViewById(R.id.buttonTeam1);
        team1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMyTeam();
            }
        });
        team2Button = (Button) findViewById(R.id.buttonTeam2);
        team2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToOtherTeam();
            }
        });

        //Load Drawables
        play = getResources().getDrawable(R.drawable.play_button);
        pause = getResources().getDrawable(R.drawable.pause_button);
        ball = getResources().getDrawable(R.drawable.soccer_ball);
        ball.setAlpha(45);
        transparent = new ColorDrawable(Color.parseColor("#00000000"));

        switchToMyTeam();

    }
    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Game?");
        builder.setMessage("Would you like to end the game and view the stats?");
        builder.setPositiveButton("End Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Game.this, ShowStats.class);
                intent.putExtra("YourPasses", ypass);
                intent.putExtra("YourShots", yshot);
                intent.putExtra("YourGoals", ygoal);
                intent.putExtra("YourFouls", yfoul);
                intent.putExtra("YourTurnovers", yturnover);

                intent.putExtra("OtherPasses", opass);
                intent.putExtra("OtherShots", oshot);
                intent.putExtra("OtherGoals", ogoal);
                intent.putExtra("OtherFouls", ofoul);
                intent.putExtra("OtherTurnovers", oturnover);

                intent.putStringArrayListExtra("YourTeamStats", yourTeam);
                intent.putStringArrayListExtra("OtherTeamStats", otherTeam);
                intent.putStringArrayListExtra("Master", master);

                intent.putExtra("YourTeamName", getIntent().getStringExtra("YourTeamName"));
                intent.putExtra("OtherTeamName", getIntent().getStringExtra("OtherTeamName"));

                String arr[] = {"Passes: "+ypass,"Shots: "+yshot, "Goals: "+ygoal, "Fouls: " + yfoul, "Turnovers: " + yturnover, "Possession: " + (int) (((float) myPos/(float) (otherPos+myPos))*100) +"%"};
                String arr2[] = {"Passes: "+opass,"Shots: "+oshot, "Goals: "+ogoal, "Fouls: " + ofoul, "Turnovers: " + oturnover, "Possession: " + (int) (((float) otherPos/(float) (otherPos+myPos))*100)+"%"};
                team1Contents = new ArrayList<>();
                team2Contents = new ArrayList<>();
                for (int x = 0; x <6; x++) {
                    team1Contents.add(arr[x]);
                    team2Contents.add(arr2[x]);
                }
                intent.putStringArrayListExtra("YourTeam",team1Contents);
                intent.putStringArrayListExtra("OtherTeam",team2Contents);

                saveToDatabase();
                endLiveStream();
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void endGame(boolean bool) {
        Intent intent = new Intent(Game.this, ShowStats.class);
        intent.putExtra("YourPasses", ypass);
        intent.putExtra("YourShots", yshot);
        intent.putExtra("YourGoals", ygoal);
        intent.putExtra("YourFouls", yfoul);
        intent.putExtra("YourTurnovers", yturnover);

        intent.putExtra("OtherPasses", opass);
        intent.putExtra("OtherShots", oshot);
        intent.putExtra("OtherGoals", ogoal);
        intent.putExtra("OtherFouls", ofoul);
        intent.putExtra("OtherTurnovers", oturnover);

        intent.putStringArrayListExtra("YourTeamStats", yourTeam);
        intent.putStringArrayListExtra("OtherTeamStats", otherTeam);
        intent.putStringArrayListExtra("Master", master);

        intent.putExtra("YourTeamName", getIntent().getStringExtra("YourTeamName"));
        intent.putExtra("OtherTeamName", getIntent().getStringExtra("OtherTeamName"));

        String arr[] = {"Passes: "+ypass,"Shots: "+yshot, "Goals: "+ygoal, "Fouls: " + yfoul, "Turnovers: " + yturnover, "Possession: " + (int) (((float) myPos/(float) (otherPos+myPos))*100) +"%"};
        String arr2[] = {"Passes: "+opass,"Shots: "+oshot, "Goals: "+ogoal, "Fouls: " + ofoul, "Turnovers: " + oturnover, "Possession: " + (int) (((float) otherPos/(float) (otherPos+myPos))*100)+"%"};
        team1Contents = new ArrayList<>();
        team2Contents = new ArrayList<>();
        for (int x = 0; x <6; x++) {
            team1Contents.add(arr[x]);
            team2Contents.add(arr2[x]);
        }
        intent.putStringArrayListExtra("YourTeam",team1Contents);
        intent.putStringArrayListExtra("OtherTeam",team2Contents);
        endLiveStream();
        saveToDatabase();

        startActivity(intent);
        finish();
    }
    private void saveToDatabase() {
        myRef = database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("Games");
        String key = myRef.push().getKey();
        MatchClass match = new MatchClass(getIntent().getStringExtra("YourTeamName") + " vs " + getIntent().getStringExtra("OtherTeamName"),
                team1Contents,team2Contents,master);
        myRef.child(key).setValue(match);
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
    public void buttonAction(ImageButton v) {
        switch (v.getId()) {
            case R.id.pass_your_team:
                ypass += 1;
                yourTeam.add(chronometer.getText().toString() + " Pass");
                master.add(chronometer.getText().toString() + " " + team1Name + " Pass");
                switchToMyTeam();
                addToLiveStream();
                break;
            case R.id.shot_your_team:
                yshot += 1;
                yourTeam.add(chronometer.getText().toString() + " Shot");
                master.add(chronometer.getText().toString() + " " + team1Name + " Shot");
                switchToMyTeam();
                addToLiveStream();
                break;
            case R.id.goal_your_team:
                ygoal += 1;
                yourTeam.add(chronometer.getText().toString() + " Goal");
                master.add(chronometer.getText().toString() + " " + team1Name + " Goal");
                scoreTeam1.setText(Integer.toString(ygoal));
                switchToMyTeam();
                addToLiveStream();
                team1Goal();
                break;
            case R.id.foul_your_team:
                yfoul += 1;
                yourTeam.add(chronometer.getText().toString() + " Foul");
                master.add(chronometer.getText().toString() + " " + team1Name + " Foul");
                switchToMyTeam();
                addToLiveStream();
                break;
            case R.id.turnover_your_team:
                yturnover += 1;
                yourTeam.add(chronometer.getText().toString() + " Turnover");
                master.add(chronometer.getText().toString() + " " + team1Name + " Turnover");
                switchToOtherTeam();
                addToLiveStream();
                break;

            case R.id.pass_other_team:
                opass += 1;
                otherTeam.add(chronometer.getText().toString() + " Pass");
                master.add(chronometer.getText().toString() + " " + team2Name + " Pass");
                switchToOtherTeam();
                addToLiveStream();
                break;
            case R.id.shot_other_team:
                oshot += 1;
                otherTeam.add(chronometer.getText().toString() + " Shot");
                master.add(chronometer.getText().toString() + " " + team2Name + " Shot");
                switchToOtherTeam();
                addToLiveStream();
                break;
            case R.id.goal_other_team:
                ogoal += 1;
                otherTeam.add(chronometer.getText().toString() + " Goal");
                master.add(chronometer.getText().toString() + " " + team2Name + " Goal");
                scoreTeam2.setText(Integer.toString(ogoal));
                switchToOtherTeam();
                addToLiveStream();
                team2Goal();
                break;
            case R.id.foul_other_team:
                ofoul += 1;
                otherTeam.add(chronometer.getText().toString() + " Foul");
                master.add(chronometer.getText().toString() + " " + team2Name + " Foul");
                switchToOtherTeam();
                addToLiveStream();
                break;
            case R.id.turnover_other_team:
                oturnover += 1;
                otherTeam.add(chronometer.getText().toString() + " Turnover");
                master.add(chronometer.getText().toString() + " " + team2Name + " Turnover");
                switchToMyTeam();
                addToLiveStream();
                break;
            case R.id.end_game:
                endGame();
            case R.id.pauseButton:
                if (chronoState) {
                    //Chrono is running
                    timewhenstopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    pauseButton.setImageDrawable(play);
                    chronometer.stop();
                    chronoState = false;
                } else {
                    //Chrono is stopped
                    chronometer.setBase(SystemClock.elapsedRealtime() + timewhenstopped);
                    chronometer.start();
                    pauseButton.setImageDrawable(pause);
                    chronoState = true;
                }
        }
    }
    private void switchToMyTeam() {
        if (!possession) {
            team1Button.setBackground(ball);
            team2Button.setBackground(transparent);
            possession = true;
        }
    }
    private void switchToOtherTeam() {
        if (possession) {
            team1Button.setBackground(transparent);
            team2Button.setBackground(ball);
            possession = false;

        }
    }
    private void init_livestream() {
        DatabaseReference reference = database.getReference();
        reference.child("livestreams").child(livestreamKey).child("Score").child("Team1").setValue(0);
        reference.child("livestreams").child(livestreamKey).child("Score").child("Team2").setValue(0);
        reference.child("livestreams").child(livestreamKey).child("Team1").setValue(team1Name);
        reference.child("livestreams").child(livestreamKey).child("Team2").setValue(team2Name);
    }
    private void addToLiveStream() {
        if (livestream_state) {
            DatabaseReference reference = database.getReference();
            reference.child("livestreams").child(livestreamKey).child("Contents").setValue(master);
        }
    }
    private void team1Goal() {
        if (livestream_state) {
            DatabaseReference reference = database.getReference();
            reference.child("livestreams").child(livestreamKey).child("Score").child("Team1").setValue(ygoal);
        }
    }
    private void team2Goal() {
        if (livestream_state) {
            DatabaseReference reference = database.getReference();
            reference.child("livestreams").child(livestreamKey).child("Score").child("Team2").setValue(ogoal);
        }
    }
    private void endLiveStream() {
        //End Live Stream
        if (livestream_state) {
            DatabaseReference reference = database.getReference();
            reference.child("livestreams").child(livestreamKey).setValue(null);
        }
    }
    @Override
    public void onBackPressed() {
        endGame();
    }
}
