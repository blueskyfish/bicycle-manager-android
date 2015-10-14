package de.kirchnerei.bicycle.battery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import kirchnerei.httpclient.HttpRequest;
import kirchnerei.httpclient.HttpResponse;
import kirchnerei.httpclient.PathBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class BatteryListFragment extends BaseFragment {

    private RecyclerView mBatteryList;
    private BatteryListAdapter mAdapter;

    private HttpManager mHttpManager;
    private ObjectMapper mMapper;

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

        Formatter formatter = getBicycleApplication().getFormatter();
        mAdapter = new BatteryListAdapter(formatter);
        mBatteryList.setAdapter(mAdapter);
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

    private final View.OnClickListener addBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt(BatteryEditFragment.PARAM_BATTERY_ID, 0);
            getMiddlewareHandler().onAction(R.string.fragment_battery_edit, args);
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
