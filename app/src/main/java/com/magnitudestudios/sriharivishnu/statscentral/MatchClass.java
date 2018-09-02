package com.magnitudestudios.sriharivishnu.statscentral;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sriharivishnu on 2018-08-01.
 */
@IgnoreExtraProperties
public class MatchClass implements Comparable<MatchClass> {
    private String Teams;
    private HashMap<String, Object> timestampCreated;
    private ArrayList<String> Team1Stats, Team2Stats, Master;


    public MatchClass() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public MatchClass(String Teams, ArrayList<String> Team1Stats, ArrayList<String> Team2Stats, ArrayList<String> Master) {
        this.Teams = Teams;
        this.Team1Stats = Team1Stats;
        this.Team2Stats = Team2Stats;
        this.Master = Master;
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow; }

    public String getTeams() {
        return Teams;
    }

    public void setTeams(String teams) {
        this.Teams = teams;
    }

    /*public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }*/
    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    public ArrayList<String> getTeam1Stats() {
        return Team1Stats;
    }

    public void setTeam1Stats(ArrayList<String> team1Stats) {
        this.Team1Stats = team1Stats;
    }

    public ArrayList<String> getTeam2Stats() {
        return Team2Stats;
    }

    public void setTeam2Stats(ArrayList<String> team2Stats) {
        this.Team2Stats = team2Stats;
    }

    public ArrayList<String> getMaster() {
        return Master;
    }

    public void setMaster(ArrayList<String> master) {
        this.Master = master;
    }

    @Override
    public int compareTo(MatchClass match1) {
        return new Date(match1.getTimestampCreatedLong()).compareTo(new Date(getTimestampCreatedLong()));
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }

    @Exclude
    public String getScore() {
        return Team1Stats.get(2).substring(Team1Stats.get(2).indexOf(": ")+2) + " - " + Team2Stats.get(2).substring(Team2Stats.get(2).indexOf(": ")+2);
    }

    @Exclude
    public String getTeam1() {
        return this.getTeams().substring(0,this.getTeams().indexOf(" vs "));
    }
    @Exclude
    public String getTeam2() {
        return this.getTeams().substring(this.getTeams().indexOf(" vs ")+4,this.getTeams().length());
    }



}
