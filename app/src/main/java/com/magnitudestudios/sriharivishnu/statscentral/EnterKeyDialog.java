package com.magnitudestudios.sriharivishnu.statscentral;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

/**
 * Created by sriharivishnu on 2018-08-23.
 */

public class EnterKeyDialog extends Dialog implements View.OnClickListener {
    Button cancelButton, pasteButton, enterButton;
    private EditText codeToShare;
    private Activity activity;
    private String liveStreamKey;
    private boolean userSelection;
    private ProgressBar pbar;
    public EnterKeyDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enter_code_dialog);

        userSelection = false;

        codeToShare = (EditText) findViewById(R.id.enter_key);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        pasteButton = (Button) findViewById(R.id.copy_button);
        enterButton = (Button) findViewById(R.id.enter_button);
        pbar = (ProgressBar) findViewById(R.id.enterKeyPbar);
        pbar.setVisibility(View.GONE);

        cancelButton.setOnClickListener(this);
        pasteButton.setOnClickListener(this);
        enterButton.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                userSelection = false;
                dismiss();
                break;
            case R.id.copy_button:
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = "";

                // If it does contain data, decide if you can handle the data.
                if (!(clipboard.hasPrimaryClip())) {

                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                    // since the clipboard has data but it is not plain text

                } else {

                    //since the clipboard contains plain text.
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    // Gets the clipboard as text.
                    pasteData = item.getText().toString();
                }
                codeToShare.setText(pasteData);
                break;
            case R.id.enter_button:
                pbar.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child("livestreams").child(getLiveStreamKey());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pbar.setVisibility(View.GONE);
                        if (dataSnapshot.exists()) {
                            userSelection = true;
                            liveStreamKey = codeToShare.getText().toString();
                            dismiss();
                        }
                        else {
                            Toast.makeText(getContext(), "Key Not Found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
        }
    }
    public boolean getUserSelection() {
        return userSelection;
    }
    public String getLiveStreamKey() {
        liveStreamKey = codeToShare.getText().toString();
        return liveStreamKey;
    }
}
