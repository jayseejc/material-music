package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.jayseeofficial.materialmusic.R;

import java.util.List;

/**
 * Created by jon on 12/06/15.
 */
public class SettingsActivity extends BasePreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers,target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return super.isValidFragment(fragmentName);
    }

    public static class MainSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences_general, false);

            addPreferencesFromResource(R.xml.preferences_general);
        }
    }

    public static class LicensesFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences_licenses, false);

            addPreferencesFromResource(R.xml.preferences_licenses);
        }
    }
}
