package de.kirchnerei.bicycle;

import android.app.Activity;
import android.app.Fragment;

public abstract class BaseFragment extends Fragment {

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
