/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.blueskyfish.bicycle.BicycleApplication;
import de.blueskyfish.bicycle.R;

public class DiagnoseManager {

    public static final String PARAM_FROM = "de.kirchnerei.bicycle.diagnose.from";

    public static void showDialog(Activity activity) {

        BicycleApplication app = (BicycleApplication) activity.getApplication();

        View view = View.inflate(activity, R.layout.fragment_diagnose, null);
        ImageView signalImage  = (ImageView) view.findViewById(R.id.diagnose_signal_image);
        TextView signalText    = (TextView) view.findViewById(R.id.diagnose_signal_text);

        if (app.isNetworkOnline()) {
            signalImage.setImageDrawable(activity.getDrawable(R.drawable.ic_signal_on_accent_24dp));
            signalText.setText(R.string.diagnose_signal_on);
        } else {
            signalImage.setImageDrawable(activity.getDrawable(R.drawable.ic_signal_off_accent_24dp));
            signalText.setText(R.string.diagnose_signal_off);
        }

        new AlertDialog.Builder(activity)
            .setTitle(R.string.diagnose_title)
            .setView(view)
            .show();
    }
}
