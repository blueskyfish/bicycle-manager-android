/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public abstract class BaseFragment extends Fragment {

    public static final Bundle EMPTY_BUNDLE = new Bundle();

    private MiddlewareHandler mMiddlewareHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMiddlewareHandler = (MiddlewareHandler) activity;
        } catch (ClassCastException e) {
            throw new RuntimeException(getActivity().getLocalClassName() +
                " should implemented the MiddlewareHandler");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMiddlewareHandler = null;
    }

    protected MiddlewareHandler getMiddlewareHandler() {
        return mMiddlewareHandler;
    }

    protected BicycleApplication getBicycleApplication() {
        return (BicycleApplication) getActivity().getApplication();
    }

    protected SettingRepository getSetting() {
        return getBicycleApplication().getSetting();
    }
}
