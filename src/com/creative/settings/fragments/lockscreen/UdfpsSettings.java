/*
 * Copyright (C) 2022 CreativeOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.creative.settings.fragments.lockscreen;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.creative.CreativeUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.io.FileDescriptor;
import java.util.Arrays;

public class UdfpsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String UDFPS_ANIM_PREVIEW = "udfps_recognizing_animation_preview";
    private static final String UDFPS_ICON_PICKER = "udfps_icon_picker";
    private static final String SCREEN_OFF_UDFPS = "screen_off_udfps";

    private Preference mUdfpsAnimPreview;
    private Preference mUdfpsIconPicker;
    private Preference mScreenOffUdfps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.udfps_settings);

        final PreferenceScreen prefSet = getPreferenceScreen();
        Resources resources = getResources();

        final boolean udfpsResPkgInstalled = CreativeUtils.isPackageInstalled(getContext(),
                "com.creative.udfps.animations");
        final boolean udfpsResIconsPkgInstalled = CreativeUtils.isPackageInstalled(getContext(),
                "com.creative.udfps.icons");
        mUdfpsAnimPreview = findPreference(UDFPS_ANIM_PREVIEW);
        mUdfpsIconPicker = findPreference(UDFPS_ICON_PICKER);
        if (!udfpsResPkgInstalled) {
            prefSet.removePreference(mUdfpsAnimPreview);
        } else if (!udfpsResIconsPkgInstalled) {
            prefSet.removePreference(mUdfpsIconPicker);
        }

        mScreenOffUdfps = (Preference) findPreference(SCREEN_OFF_UDFPS);
        boolean mScreenOffUdfpsAvailable = resources.getBoolean(
                com.android.internal.R.bool.config_supportsScreenOffUdfps);
        if (!mScreenOffUdfpsAvailable)
            prefSet.removePreference(mScreenOffUdfps);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CREATIVE_SETTINGS;
    }
}
