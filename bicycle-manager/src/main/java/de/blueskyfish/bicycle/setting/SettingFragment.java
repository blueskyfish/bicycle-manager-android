/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.setting;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import de.blueskyfish.bicycle.BaseFragment;
import de.blueskyfish.bicycle.FloatingButtonKind;
import de.blueskyfish.bicycle.R;
import de.blueskyfish.bicycle.SettingRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment {

    private EditText mUserEmail;

    private EditText mPassword;

    private TextView mToken;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mUserEmail = (EditText) view.findViewById(R.id.user_email);
        mPassword = (EditText) view.findViewById(R.id.password);
        mToken = (TextView) view.findViewById(R.id.lbl_token);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMiddlewareHandler().changeFloatingButton(
            FloatingButtonKind.SETTING, settingClickListener);

        SettingRepository setting = getSetting();

        mUserEmail.setText(setting.getUserEmail());
        mPassword.setText(setting.getPassword());
        if (setting.hasToken()) {
            mToken.setText(setting.getToken());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUserEmail = null;
        mPassword = null;
        mToken = null;
    }

    private void doSaveSettings() {
        String userEmail = mUserEmail.getText().toString();
        String password = mPassword.getText().toString();

        SettingRepository setting = getSetting();
        try {
            setting.change(userEmail, password);
            getMiddlewareHandler().onAction(R.string.fragment_overview, new Bundle());
        } catch (RuntimeException e) {
            getMiddlewareHandler()
                .makeSnackbar(R.string.setting_change_email_error)
                .show();
        }
    }

    private View.OnClickListener settingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSaveSettings();
        }
    };
}
