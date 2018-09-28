package com.magnitudestudios.sriharivishnu.statscentral;

/**
 * Created by sriharivishnu on 2018-07-26.
 */

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YourTeamsFragment extends Fragment {
    private ListView showTeams;
    private MyAdapter2 myAdapter;
    private DatabaseReference mReference;
    private FirebaseDatabase database;
    private String teams;
    private ArrayList<String> team1Stats, team2Stats;
    private ArrayList<MatchClass> games;
    private ProgressBar progressBar;
    private SearchView searchView;
    private TextView titleYourTeams;
    public static YourTeamsFragment newInstance() {
        YourTeamsFragment fragment = new YourTeamsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_your_teams, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();

        titleYourTeams = (TextView) v.findViewById(R.id.past_games_title);

        showTeams = (ListView) v.findViewById(R.id.showTeams);
        showTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowStats.class);
                MatchClass game = games.get(i);
                intent.putStringArrayListExtra("Master", game.getMaster());
                intent.putStringArrayListExtra("YourTeam", new ArrayList<String>(game.getTeam1Stats().subList(0, 6)));
                intent.putStringArrayListExtra("OtherTeam",new ArrayList<String>(game.getTeam2Stats().subList(0,6)));
                intent.putExtra("YourTeamName", game.getTeam1());
                intent.putExtra("OtherTeamName", game.getTeam2());
                startActivity(intent);
            }
        });
        games = new ArrayList<MatchClass>();

        mReference = database.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Games");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("YE","Ondatachange");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    games.add(child.getValue(MatchClass.class));
                    Log.d("SDF",games.toString());
                }
                onComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchView = (SearchView) v.findViewById(R.id.search_bar);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                //titleYourTeams.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //titleYourTeams.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //titleYourTeams.setVisibility(View.INVISIBLE);
                if (myAdapter != null) {
                    myAdapter.getFilter().filter(s);
                }
                return false;
            }
        });

        return v;
    }

    public void onComplete() {
        if (getContext() != null) {
            Collections.sort(games);
            myAdapter = new MyAdapter2(getContext(), games);
            showTeams.setAdapter(myAdapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}