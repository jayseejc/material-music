package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;

import com.jayseeofficial.materialmusic.R;

import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by jon on 24/06/15.
 */
public class LicensesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LicensesDialog dialog = new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setShowFullLicenseText(true)
                .setIncludeOwnLicense(true)
                .build();
        dialog.setOnDismissListener(dialog1 -> finish());
        dialog.show();
    }
}
