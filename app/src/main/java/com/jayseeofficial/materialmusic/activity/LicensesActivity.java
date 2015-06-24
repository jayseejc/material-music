package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;

import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by jon on 24/06/15.
 */
public class LicensesActivity extends BaseActivity {
    public static final String ACTION_OPEN_SOURCE_LICENSES = "com.jayseeofficial.intent.OPEN_SOURCE_LICENSES";
    public static final String ACTION_GOOGLE_LICENSES = "com.jayseeofficial.intent.GOOGLE_LICENSES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction().equalsIgnoreCase(ACTION_OPEN_SOURCE_LICENSES)) {
            LicensesDialog dialog = new LicensesDialog.Builder(this)
                    .setNotices(R.raw.notices)
                    .setShowFullLicenseText(true)
                    .setIncludeOwnLicense(true)
                    .build();
            dialog.setOnDismissListener(dialog1 -> finish());
            dialog.show();
        } else if (getIntent().getAction().equalsIgnoreCase(ACTION_GOOGLE_LICENSES)) {
            // Setup dialog contents
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ScrollView scrollView = new ScrollView(this);
            scrollView.setLayoutParams(params);

            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            // TODO uncomment this once play services are enabled
            //textView.setText(GoogleApiAvalability.getOpenSourceSoftwareLicenseInfo(this));
            String s = "COMING SOON!\n";
            for (int i = 0; i < 10; i++) s += s;
            textView.setText(s);
            scrollView.addView(textView);

            new AlertDialog.Builder(this)
                    .setView(scrollView)
                    .setNegativeButton("Close", (dialog, which) -> finish())
                    .setOnDismissListener(dialog1 -> finish())
                    .show();
        }
    }
}
