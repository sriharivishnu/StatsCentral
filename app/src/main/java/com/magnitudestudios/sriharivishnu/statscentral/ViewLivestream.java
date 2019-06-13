package com.magnitudestudios.sriharivishnu.statscentral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewLivestream extends AppCompatActivity {
    private TextView scoreteam1, scoreteam2, team1name, team2name;
    private String name1, name2;
    private ListView showContents;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ArrayList<String> contents;
    private MyAdapter adapter;
    private Integer score1, score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_livestream);

        init_database();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                contents = dataSnapshot.child("Contents").getValue(genericTypeIndicator);
                name1 = dataSnapshot.child("Team1").getValue(String.class);
                name2 = dataSnapshot.child("Team2").getValue(String.class);
                score1 = dataSnapshot.child("Score").child("Team1").getValue(Integer.class);
                score2 = dataSnapshot.child("Score").child("Team2").getValue(Integer.class);
                onComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scoreteam1 = (TextView) findViewById(R.id.scoreTeam1Livestream);
        scoreteam2 = (TextView) findViewById(R.id.scoreTeam2Livestream);
        team1name = (TextView) findViewById(R.id.team1NameViewLivestream);
        team2name = (TextView) findViewById(R.id.team2NameViewLivestream);

        showContents = (ListView) findViewById(R.id.livestream_contents);

    }
    private void init_database() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("livestreams").child(getIntent().getStringExtra("LiveStreamViewKey"));
    }
    private void onComplete() {
        if (adapter == null) {
            if (contents == null) {
                contents = new ArrayList<>();
            }
            adapter = new MyAdapter(ViewLivestream.this, contents);
            showContents.setAdapter(adapter);
            team1name.setText(name1);
            team2name.setText(name2);
            scoreteam1.setText(Integer.toString(score1));
            scoreteam2.setText(Integer.toString(score2));
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
