package de.kirchnerei.bicycle.battery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.FloatingButtonKind;
import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Delay;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class BatteryDetailFragment extends BaseFragment {


    private int mId;

    public BatteryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mId = args.getInt(BatteryDefine.PARAM_BATTERY_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battery_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getMiddlewareHandler().changeFloatingButton(FloatingButtonKind.BATTERY_DETAIL,
            editBatteryListener);

        getMiddlewareHandler().post(new Runnable() {
            @Override
            public void run() {
                doGetRequestBatteryDetail();
            }
        }, Delay.START_REQUEST);
    }


    private void doEditBatteryClick() {

    }

    private void doGetRequestBatteryDetail() {

    }

    private final View.OnClickListener editBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doEditBatteryClick();
        }
    };
}
