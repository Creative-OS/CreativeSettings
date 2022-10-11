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

import com.android.internal.util.creative.CreativeUtils;

import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class Lockscreen extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    public static final String TAG = "Lockscreen";
    
    private static final String LOCKSCREEN_CATEGORY = "lockscreen_category";
    private static final String KEY_UDFPS_SETTINGS = "udfps_settings";
    private static final String KEY_RIPPLE_EFFECT = "enable_ripple_effect";
    private static final String KEY_FP_AUTH = "fp_success_vibrate";
    private static final String KEY_FP_ERROR = "fp_error_vibrate";
    private static final String UDFPS_HAPTIC_FEEDBACK = "udfps_haptic_feedback";

    private Preference mUdfpsSettings;
    private SwitchPreference mRippleEffect;
    private SwitchPreference mFPVibAuth;
    private SwitchPreference mFPVibError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.creative_lockscreen);

	Context mContext = getActivity().getApplicationContext();
	ContentResolver resolver = mContext.getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        PreferenceCategory lockscreenCategory = (PreferenceCategory) findPreference(LOCKSCREEN_CATEGORY);

        FingerprintManager mFingerprintManager = (FingerprintManager)
                getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        mUdfpsSettings = findPreference(KEY_UDFPS_SETTINGS);
        mRippleEffect = findPreference(KEY_RIPPLE_EFFECT);
        mFPVibAuth = findPreference(KEY_FP_AUTH);
        mFPVibError = findPreference(KEY_FP_ERROR);
        
        if (mFingerprintManager == null || !mFingerprintManager.isHardwareDetected()) {
            lockscreenCategory.removePreference(mUdfpsSettings);
            lockscreenCategory.removePreference(mRippleEffect);
            lockscreenCategory.removePreference(mFPVibAuth);
            lockscreenCategory.removePreference(mFPVibError);
        } else {
            if (!CreativeUtils.isPackageInstalled(getContext(), "com.creative.udfps.icons")) {
                lockscreenCategory.removePreference(mUdfpsSettings);
            }
        }
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
            new BaseSearchIndexProvider(R.xml.creative_lockscreen) {

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);

                    return keys;

                }
            };
}
