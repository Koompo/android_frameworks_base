/*
 * Copyright (C) 2013-2014 The CyanogenMod Project
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

package com.android.systemui.quicksettings;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import com.android.internal.view.RotationPolicy;
import com.android.systemui.R; 
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class AutoRotateTile extends QuickSettingsTile {
    public AutoRotateTile(Context context, QuickSettingsController qsc) {
        super(context, qsc);

        mOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotationPolicy.setRotationLock(mContext, isAutoRotateEnabled());
            }
        };

        mOnLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(Settings.ACTION_DISPLAY_SETTINGS);
                return true;
            }
        };
        qsc.registerObservedContent(
                Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), this);
    }

    @Override
    public void updateResources() {
        updateTile();
        super.updateResources();
    }

    @Override
    void onPostCreate() {
        updateTile();
        super.onPostCreate();
    }

    @Override
    public void onChangeUri(ContentResolver resolver, Uri uri) {
        updateResources();
    }

    private void updateTile() {
        if (isAutoRotateEnabled()) {
            mDrawable = R.drawable.ic_qs_auto_rotate;
            mLabel = mContext.getString(R.string.quick_settings_rotation_unlocked_label);
        } else {
            mDrawable = R.drawable.ic_qs_rotation_locked;
            mLabel = mContext.getString(R.string.quick_settings_rotation_locked_label);
        }
    }

    private boolean isAutoRotateEnabled() {
        return !RotationPolicy.isRotationLocked(mContext);
    }
}
