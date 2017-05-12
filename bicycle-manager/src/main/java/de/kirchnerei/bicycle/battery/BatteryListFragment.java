/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.battery;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.bottomsheet.BottomSheet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.FloatingButtonKind;
import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Delay;
import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.http.DiagnoseManager;
import de.kirchnerei.bicycle.http.HttpManager;
import de.kirchnerei.bicycle.http.StatusCheck;
import de.blueskyfish.httpclient.HttpRequest;
import de.blueskyfish.httpclient.HttpResponse;
import de.blueskyfish.httpclient.PathBuilder;


/**
 * A simple {@link BaseFragment} subclass.
 */
public class BatteryListFragment extends BaseFragment {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mBatteryList;
    private BatteryListAdapter mAdapter;

    private HttpManager mHttpManager;
    private ObjectMapper mMapper;
    private Formatter mFormatter;

    public BatteryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHttpManager = getBicycleApplication().getHttpManager();
        mMapper = getBicycleApplication().getMapper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battery_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBatteryList = (RecyclerView) view.findViewById(R.id.battery_list);
        mBatteryList.setHasFixedSize(true);

        // update the layout manager
        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBatteryList.setLayoutManager(linearManager);

        mFormatter = getBicycleApplication().getFormatter();
        mAdapter = new BatteryListAdapter(batteryItemClick, mFormatter);
        mBatteryList.setAdapter(mAdapter);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefreshBatteryList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getMiddlewareHandler().changeFloatingButton(FloatingButtonKind.BATTERY, addBatteryListener);
        getMiddlewareHandler().post(new Runnable() {
            @Override
            public void run() {
                GetBatteryListRequest request = new GetBatteryListRequest();
                request.execute("battery");
            }
        }, Delay.START_REQUEST);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mSwipeRefresh = null;
        mBatteryList = null;
        mAdapter = null;
        mHttpManager = null;
        mMapper = null;
        mFormatter = null;
    }

    private void doBatteryItemClick(final View view) {
        final int itemPosition = mBatteryList.getChildLayoutPosition(view);
        getMiddlewareHandler().post(new Runnable() {
            @Override
            public void run() {
                doShowButtonSheet(itemPosition);
            }
        }, Delay.START_BOTTOM_SHEET);
    }

    private void doShowButtonSheet(int itemPosition) {
        final BatteryItem item = mAdapter.getItem(itemPosition);
        new BottomSheet.Builder(getActivity(), R.style.AppTheme_Dialog_BottomSheet)
            .sheet(R.menu.menu_battery_list)
            .title(mFormatter.getBatteryTitle(item.getDate(), item.getDistance()))
            .grid()
            .listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doBatteryItemActionClick(which, item);
                }
            })
            .show();
    }

    private void doBatteryItemActionClick(int action, BatteryItem item) {
        Bundle args = new Bundle();
        args.putInt(BatteryDefine.PARAM_BATTERY_ID, item.getId());

        switch (action) {
            case R.id.action_detail:
                getMiddlewareHandler().onAction(R.string.fragment_battery_detail, args);
                break;
            case R.id.action_edit:
                getMiddlewareHandler().onAction(R.string.fragment_battery_edit, args);
                break;
            case R.id.action_delete:
                Logger.debug("Not implemented yet");
                break;
        }
    }

    private void doRefreshBatteryList() {
        mSwipeRefresh.setRefreshing(true);
        getMiddlewareHandler().post(new Runnable() {
            @Override
            public void run() {
                GetBatteryListRequest request = new GetBatteryListRequest();
                request.execute("battery");
            }
        }, Delay.START_REQUEST);
    }

    private final View.OnClickListener addBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt(BatteryDefine.PARAM_BATTERY_ID, 0);
            getMiddlewareHandler().onAction(R.string.fragment_battery_edit, args);
        }
    };

    private final View.OnClickListener batteryItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doBatteryItemClick(view);
        }
    };

    /**
     * Starts and executes a request to get the battery list
     */
    class GetBatteryListRequest extends AsyncTask<Object, Void, List<BatteryItem>> {

        @Override
        protected List<BatteryItem> doInBackground(Object... params) {
            String url = PathBuilder.toUrl(params);
            HttpRequest request = HttpRequest.buildGET(url);
            HttpResponse response = mHttpManager.execute(request);
            int statusCode = response.getStatusCode();
            if (response.hasError()) {
                Logger.debug("error result: %s", response.getContent());
                showRequestError(statusCode);
                return EMPTY_LIST;
            }
            String content = response.getContent();
            try {
                ResultBatteryList result = mMapper.readValue(content, ResultBatteryList.class);
                if (StatusCheck.isOkay(result)) {
                    return result.getBatteryList();
                }
                showRequestError(statusCode);
            }
            catch (IOException e) {
                getMiddlewareHandler()
                    .makeSnackbar(R.string.battery_list_mapper_error)
                    .show();
            }
            finally {
                Logger.debug("request duration %s ms", response.getDuration());
            }
            return EMPTY_LIST;
        }

        // The request for the battery elements has failed!
        private void showRequestError(int statusCode) {
            Logger.debug("The request for the battery elements has failed! (http status = %s)",
                statusCode);

            getMiddlewareHandler()
                .makeSnackbar(R.string.battery_list_request_error,
                    R.string.battery_list_request_error_action, diagnoseListener)
                .show();
        }

        @Override
        protected void onPostExecute(List<BatteryItem> items) {
            mAdapter.changeData(items);
            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(false);
            }
        }

        private final List<BatteryItem> EMPTY_LIST = new ArrayList<>();
    }

    private final View.OnClickListener diagnoseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt(DiagnoseManager.PARAM_FROM, 1);
            getMiddlewareHandler().onAction(R.string.fragment_diagnose, args);
        }
    };
}
