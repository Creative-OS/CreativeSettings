/*
 * Copyright (C) 2022 CreativeOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.creative.settings.fragments;

import com.creative.settings.fragments.misc.SmartCharging;

import static android.os.UserHandle.USER_CURRENT;
import static android.os.UserHandle.USER_SYSTEM;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Handler;
import android.content.om.IOverlayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.text.TextUtils;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.creative.settings.fragments.statusbar.Clock;
import com.creative.settings.fragments.lockscreen.UdfpsSettings;
import com.creative.settings.preferences.CustomSeekBarPreference;
import com.creative.settings.preferences.SystemSettingListPreference;
import com.creative.settings.preferences.SystemSettingSwitchPreference;
import com.creative.settings.utils.DeviceUtils;
import com.creative.settings.utils.TelephonyUtils;

import lineageos.preference.LineageSystemSettingListPreference;
import lineageos.preference.LineageSecureSettingListPreference;
import lineageos.preference.LineageSecureSettingSwitchPreference;
import lineageos.providers.LineageSettings;

import com.creative.settings.fragments.ui.SmartPixels;
import com.android.internal.util.creative.CreativeUtils;

import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class Misc extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    public static final String TAG = "Misc";
    
    private static final String MISC_CATEGORY = "misc_category";
    private static final String SMART_CHARGING = "smart_charging";
    private static final String POCKET_JUDGE = "pocket_judge";
    private static final String SMART_PIXELS = "smart_pixels";

    private Preference mSmartCharging;
    private SwitchPreference mPocketJudge;
    private Preference mSmartPixels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.creative_misc);

	Context mContext = getActivity().getApplicationContext();
	ContentResolver resolver = mContext.getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        PreferenceCategory miscCategory = (PreferenceCategory) findPreference(MISC_CATEGORY);
 
        boolean mSmartChargingSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_smartChargingAvailable);
	mSmartCharging = findPreference(SMART_CHARGING);
	if (!mSmartChargingSupported)
	miscCategory.removePreference(mSmartCharging);

        boolean mPocketJudgeSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_pocketModeSupported);
	mPocketJudge= findPreference(POCKET_JUDGE);
	if (!mPocketJudgeSupported)
	miscCategory.removePreference(mPocketJudge);
	
        mSmartPixels = (Preference) prefScreen.findPreference(SMART_PIXELS);
        boolean mSmartPixelsSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_supportSmartPixels);
        if (!mSmartPixelsSupported)
            prefScreen.removePreference(mSmartPixels);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CREATIVE_SETTINGS;
    }

    /**
     * For search
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.creative_misc) {

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);

                    boolean mPocketJudgeSupported = context.getResources().getBoolean(
                            com.android.internal.R.bool.config_pocketModeSupported);
                    if (!mPocketJudgeSupported)
                        keys.add(POCKET_JUDGE);

                    boolean mSmartPixelsSupported = context.getResources().getBoolean(
                            com.android.internal.R.bool.config_supportSmartPixels);
                    if (!mSmartPixelsSupported)
                        keys.add(SMART_PIXELS);

                    boolean mSmartChargingSupported = context.getResources().getBoolean(
                            com.android.internal.R.bool.config_smartChargingAvailable);
                    if (!mSmartChargingSupported)
                        keys.add(SMART_CHARGING);

                    return keys;

                }
            };
}
