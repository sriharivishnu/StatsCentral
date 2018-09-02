package com.magnitudestudios.sriharivishnu.statscentral;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShowStats extends AppCompatActivity {
    private ArrayList<String> yourTeamStats, otherTeamStats, master;
    private int ypass,yshot,ygoal,yfoul,yturnover;
    private int opass,oshot,ogoal,ofoul,oturnover;
    private TextView team1Name, team2Name;
    private MyAdapter adapter1, adapter2;
    private ListView showTeam1Stats, showTeam2Stats;
    private ImageButton rawStatsButton, homeButton;
    ArrayList<String> team1Contents,team2Contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stats);

        yourTeamStats = getIntent().getStringArrayListExtra("YourTeamStats");
        otherTeamStats = getIntent().getStringArrayListExtra("OtherTeamStats");
        master = getIntent().getStringArrayListExtra("Master");

        ypass = getIntent().getIntExtra("YourPasses",0);
        yshot = getIntent().getIntExtra("YourShots",0);
        ygoal = getIntent().getIntExtra("YourGoals",0);
        yfoul = getIntent().getIntExtra("YourFouls",0);
        yturnover = getIntent().getIntExtra("YourTurnovers",0);

        opass = getIntent().getIntExtra("OtherPasses",0);
        oshot = getIntent().getIntExtra("OtherShots",0);
        ogoal = getIntent().getIntExtra("OtherGoals",0);
        ofoul = getIntent().getIntExtra("OtherFouls",0);
        oturnover = getIntent().getIntExtra("OtherTurnovers",0);

        team1Name = (TextView) findViewById(R.id.Team1StatsText);
        team2Name = (TextView) findViewById(R.id.Team2StatsText);
        team1Name.setText(getIntent().getStringExtra("YourTeamName"));
        team2Name.setText(getIntent().getStringExtra("OtherTeamName"));

        showTeam1Stats = (ListView) findViewById(R.id.team1Stats);
        showTeam2Stats = (ListView) findViewById(R.id.team2Stats);

        team1Contents = getIntent().getStringArrayListExtra("YourTeam");
        team1Contents.addAll(analyze(1, getIntent().getStringExtra("YourTeamName") + " Pass"));
        team2Contents = getIntent().getStringArrayListExtra("OtherTeam");
        team2Contents.addAll(analyze(2, getIntent().getStringExtra("OtherTeamName") + " Pass"));

        adapter1 = new MyAdapter(this, team1Contents);
        adapter2 = new MyAdapter(this, team2Contents);
        showTeam1Stats.setAdapter(adapter1);
        showTeam2Stats.setAdapter(adapter2);

        rawStatsButton = (ImageButton) findViewById(R.id.rawStatsButton);
        buttonAnimation(rawStatsButton);

        homeButton = (ImageButton) findViewById(R.id.homeButton);
        buttonAnimation(homeButton);


    }
    private void goToRawStats() {
        Intent intent = new Intent(ShowStats.this, RawStats.class);
        intent.putStringArrayListExtra("Master", master);
        startActivity(intent);
    }
    private ArrayList<String> analyze(Integer s, String search) {
        ArrayList<String> toBeAdded = new ArrayList<>();
        HashMap<Integer, Integer> sequences = new HashMap<>();
        String element;
        int c = 0;
        //Team 1
        for (int i = 0; i < master.size(); i++) {
            element = master.get(i);
            if (element.substring(6, element.length()).equals(search)) {
                c += 1;
            } else {
                if (c > 1) {
                    if (sequences.containsKey(c)) {
                        sequences.put(c, sequences.get(c) + 1);
                    } else {
                        sequences.put(c, 1);
                    }
                }
                c = 0;
            }
        }
        if (c > 1) {
            if (sequences.containsKey(c)) {
                sequences.put(c, sequences.get(c) + 1);
            } else {
                sequences.put(c, 1);
            }
        }
        Iterator it = sequences.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            toBeAdded.add(pair.getKey().toString() + " Consec. Passes: " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return toBeAdded;
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
            case R.id.rawStatsButton:
                goToRawStats();
                break;
            case R.id.homeButton:
                finish();
                break;
        }
    }
}
