package com.magnitudestudios.sriharivishnu.statscentral;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by sriharivishnu on 2018-08-15.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new SettingsFragment())
                .commit();
        Toolbar toolbar = findViewById(R.id.dialog_toolbar);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setTint(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
}
