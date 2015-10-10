package de.kirchnerei.bicycle;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends BaseFragment {

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        CardView batteryCard = (CardView) view.findViewById(R.id.cardview_battery);
        batteryCard.setOnClickListener(batteryClickListener);

        CardView settingCard = (CardView) view.findViewById(R.id.cardview_setting);
        settingCard.setOnClickListener(settingClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MiddlewareHandler mh = getMiddlewareHandler();
        if (getSetting().hasToken()) {
            mh.changeFloatingButton(FloatingButtonKind.OVERVIEW_BATTERY, batteryClickListener);
        } else {
            mh.changeFloatingButton(FloatingButtonKind.OVERVIEW_SETTING, settingClickListener);
        }
    }

    private void doBatteryCardClick() {
        getMiddlewareHandler().onAction(R.string.fragment_battery_list, new Bundle());
    }

    private void doSettingCardClick() {
        getMiddlewareHandler().onAction(R.string.fragment_setting, new Bundle());
    }


    private final View.OnClickListener settingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSettingCardClick();
        }
    };

    private final View.OnClickListener batteryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doBatteryCardClick();
        }
    };
}
