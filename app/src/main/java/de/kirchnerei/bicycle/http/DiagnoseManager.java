package de.kirchnerei.bicycle.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.kirchnerei.bicycle.BicycleApplication;
import de.kirchnerei.bicycle.R;

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
