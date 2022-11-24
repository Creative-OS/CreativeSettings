/*
 * Copyright (C) 2016-2020 CreativeDroid Android Project
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.R;

import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class About extends SettingsPreferenceFragment {

    public static final String TAG = "About";

    private String KEY_CREATIVE_DONATE = "creative_donate";
    private String KEY_CREATIVE_SOURCE = "creative_source";
    private String KEY_CREATIVE_TELEGRAM = "creative_telegram";
    private String KEY_CREATIVE_SHARE = "creative_share";
    private String KEY_CREATIVE_TRANSLATE = "creative_translate";
    private String KEY_CREATIVE_TELEGRAM_CHANNEL = "creative_telegram_channel";

    private Preference mDonate;
    private Preference mSourceUrl;
    private Preference mTelegramUrl;
    private Preference mShare;
    private Preference mTranslate;
    private Preference mTelegramChannelUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.creative_settings_about);

        mDonate = findPreference(KEY_CREATIVE_DONATE);
        mSourceUrl = findPreference(KEY_CREATIVE_SOURCE);
        mTelegramUrl = findPreference(KEY_CREATIVE_TELEGRAM);
        mShare = findPreference(KEY_CREATIVE_SHARE);
        mTranslate = findPreference(KEY_CREATIVE_TRANSLATE);
        mTelegramChannelUrl = findPreference(KEY_CREATIVE_TELEGRAM_CHANNEL);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mDonate) {
            launchUrl("https://www.youtube.com/watch?v=kJQP7kiw5Fk");
        } else if (preference == mSourceUrl) {
            launchUrl("https://github.com/Creative-OS");
        } else if (preference == mTelegramUrl) {
            launchUrl("https://t.me/CreativeRaviole");
        } else if (preference == mShare) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, String.format(
                    getActivity().getString(R.string.share_message), Build.MODEL));
            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
        } else if (preference == mTranslate) {
            launchUrl("https://crdroid.net/translations.php");
        } else if (preference == mTelegramChannelUrl) {
            launchUrl("https://t.me/CreativeRaviole");
        }

        return super.onPreferenceTreeClick(preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(intent);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CREATIVE_SETTINGS;
    }

    /**
     * For search
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.creative_settings_about);
}
