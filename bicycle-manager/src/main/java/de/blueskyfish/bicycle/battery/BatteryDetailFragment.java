/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.battery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

import de.blueskyfish.bicycle.BaseFragment;
import de.blueskyfish.bicycle.FloatingButtonKind;
import de.blueskyfish.bicycle.R;
import de.blueskyfish.bicycle.helper.Delay;
import de.blueskyfish.bicycle.helper.Formatter;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.helper.Unit;
import de.blueskyfish.bicycle.http.HttpManager;
import de.blueskyfish.httpclient.HttpRequest;
import de.blueskyfish.httpclient.HttpResponse;
import de.blueskyfish.httpclient.PathBuilder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class BatteryDetailFragment extends BaseFragment {


    private int mId;

    private HttpManager mHttpManager;
    private ObjectMapper mMapper;
    private Formatter mFormatter;

    private TextView mDate;
    private TextView mDistance;
    private TextView mAverageSpeed;
    private TextView mMileage;
    private TextView mLeftover;

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

        mHttpManager = getBicycleApplication().getHttpManager();
        mMapper = getBicycleApplication().getMapper();
        mFormatter = getBicycleApplication().getFormatter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battery_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDate =         (TextView) view.findViewById(R.id.battery_date);
        mDistance =     (TextView) view.findViewById(R.id.battery_distance);
        mAverageSpeed = (TextView) view.findViewById(R.id.battery_average_speed);
        mMileage =      (TextView) view.findViewById(R.id.battery_mileage);
        mLeftover =     (TextView) view.findViewById(R.id.battery_leftover);
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

    @Override
    public void onDetach() {
        super.onDetach();

        mDate = null;
        mDistance = null;
        mAverageSpeed = null;
        mMileage = null;
        mLeftover = null;
        mHttpManager = null;
        mMapper = null;
        mFormatter = null;
    }

    private void doEditBatteryClick() {
        Bundle args = new Bundle();
        args.putInt(BatteryDefine.PARAM_BATTERY_ID, mId);
        getMiddlewareHandler().onAction(R.string.fragment_battery_edit, args);
    }

    private void updateDate(Date date) {
        mDate.setText(mFormatter.from(date));
    }

    private void updateDistance(int distance) {
        mDistance.setText(mFormatter.from(distance, Unit.DISTANCE));
    }

    private void updateAverageSpeed(int averageSpeed) {
        mAverageSpeed.setText(mFormatter.from(averageSpeed, Unit.SPEED));
    }

    private void updateMileage(int mileage) {
        mMileage.setText(mFormatter.from(mileage, Unit.DISTANCE));
    }

    private void updateLeftover(int leftover) {
        mLeftover.setText(mFormatter.from(leftover, Unit.DISTANCE));
    }

    private void doGetRequestBatteryDetail() {
        GetRequestBatteryDetail request = new GetRequestBatteryDetail();
        request.execute("battery", "detail", mId);
    }

    private final View.OnClickListener editBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doEditBatteryClick();
        }
    };

    private class GetRequestBatteryDetail extends AsyncTask<Object, Void, BatteryItem> {


        @Override
        protected BatteryItem doInBackground(Object... params) {
            String url = PathBuilder.toUrl(params);
            HttpRequest request = HttpRequest.buildGET(url);
            HttpResponse response = mHttpManager.execute(request);
            if (response.hasError()) {
                return null;
            }
            try {
                ResultBatteryDetail result = mMapper.readValue(response.getContent(),
                    ResultBatteryDetail.class);
                return result.getBattery();
            } catch (IOException e) {
                Logger.warn("Couldn't read the result and transform into BatteryItem (%s)", url);
            }
            return null;
        }

        @Override
        protected void onPostExecute(BatteryItem item) {
            if (item == null) {
                getMiddlewareHandler().makeSnackbar(R.string.battery_detail_request_error);
                getMiddlewareHandler().onAction(R.string.fragment_battery_list,
                    BaseFragment.EMPTY_BUNDLE);
                return;
            }
            updateDate(item.getDate());
            updateDistance(item.getDistance());
            updateAverageSpeed(item.getAverageSpeed());
            updateMileage(item.getMileage());
            updateLeftover(item.getLeftover());
        }
    }
}
