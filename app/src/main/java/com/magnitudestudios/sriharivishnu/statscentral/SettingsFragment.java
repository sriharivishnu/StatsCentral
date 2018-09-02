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
}