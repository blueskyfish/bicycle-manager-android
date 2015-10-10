package de.kirchnerei.bicycle.battery;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BatteryEditFragment extends BaseFragment {

    public BatteryEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battery_edit, container, false);
    }

}
