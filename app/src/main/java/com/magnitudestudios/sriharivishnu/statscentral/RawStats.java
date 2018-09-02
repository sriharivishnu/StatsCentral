package com.magnitudestudios.sriharivishnu.statscentral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ServerValue;

public class RawStats extends AppCompatActivity {
    ListView rawStats;
    MyAdapter adapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_stats);

        rawStats = (ListView) findViewById(R.id.rawStats);
        adapter = new MyAdapter(this, getIntent().getStringArrayListExtra("Master"));
        rawStats.setAdapter(adapter);

        backButton = findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
