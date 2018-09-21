package com.magnitudestudios.sriharivishnu.statscentral;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by sriharivishnu on 2018-08-15.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);
        Preference preference = (Preference) findPreference("logout");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
                return false;
            }
        });
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View v = inflater.inflate(R.layout.fragment_your_teams, container, false);
//
//        return v;
//    }
    /*if (!toBeRemoved.isEmpty()) {
            ArrayList<String> temp= new ArrayList<>();
            int i = 0;
            for (int x = 0; x < a.size(); x+=5) {

                if (!toBeRemoved.get(i).equals(Integer.toString(x/5))) {
                    for (int y = 0; y< 5; y++) {
                        temp.add(a.get(x+y));
                    }
                } else {
                    if (i+1 < toBeRemoved.size()) {
                        i++;
                    }
                }
            }
            a = new ArrayList<>(temp);
            temp.clear();
            i = 0;
            for (int x = 0; x < b.size(); x++) {
                if (!toBeRemoved.get(i).equals(Integer.toString(x))) {
                    temp.add(b.get(x));
                } else {
                    if (i+1 < toBeRemoved.size()) {
                        i++;
                    }
                }
            }
            b = new ArrayList<>(temp);
            temp.clear();
        }*/
}