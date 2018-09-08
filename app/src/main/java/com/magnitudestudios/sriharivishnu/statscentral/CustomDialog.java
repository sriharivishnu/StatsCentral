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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sriharivishnu on 2018-08-23.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {
    Button cancelButton, copyButton, continueButton;
    private TextView codeToShare;
    private Activity activity;
    private String liveStreamKey;
    public CustomDialog(Activity activity, String liveStreamKey) {
        super(activity);
        this.activity = activity;
        this.liveStreamKey = liveStreamKey;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        codeToShare = (TextView) findViewById(R.id.code_text);
        codeToShare.setText(liveStreamKey);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        copyButton = (Button) findViewById(R.id.copy_button);
        continueButton = (Button) findViewById(R.id.enter_button);

        cancelButton.setOnClickListener(this);
        copyButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.copy_button:
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("key", liveStreamKey);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Key Copied", Toast.LENGTH_LONG).show();
                break;
            case R.id.enter_button:
                dismiss();
                break;

        }
    }
}
