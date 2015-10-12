package de.kirchnerei.bicycle.battery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.FloatingButtonKind;
import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.helper.Unit;
import de.kirchnerei.bicycle.http.DiagnoseManager;
import de.kirchnerei.bicycle.http.HttpManager;
import de.kirchnerei.bicycle.http.StatusCheck;
import kirchnerei.httpclient.HttpRequest;
import kirchnerei.httpclient.HttpResponse;
import kirchnerei.httpclient.Method;
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
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_battery_list, container, false);

        mBatteryList = (RecyclerView) view.findViewById(R.id.battery_list);
        mBatteryList.setHasFixedSize(true);
        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBatteryList.setLayoutManager(linearManager);

        Formatter formatter = getBicycleApplication().getFormatter();
        mAdapter = new BatteryListAdapter(formatter);
        mBatteryList.setAdapter(mAdapter);

        mHttpManager = getBicycleApplication().getHttpManager();
        mMapper = getBicycleApplication().getMapper();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMiddlewareHandler().changeFloatingButton(FloatingButtonKind.BATTERY, addBatteryListener);
        GetBatteryListRequest request = new GetBatteryListRequest();
        request.execute("battery");
    }

    private final View.OnClickListener addBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.debug("add battery...");
        }
    };

    private static class BatteryListAdapter extends RecyclerView.Adapter<BatteryItemHolder> {

        private final List<BatteryItem> items;
        private final Formatter formatter;

        public BatteryListAdapter(Formatter formatter) {
            this.items = new ArrayList<>(64);
            this.formatter = formatter;
        }

        public void changeData(List<BatteryItem> items) {
            if (items != null && !items.isEmpty()) {
                this.items.clear();
                this.items.addAll(items);
                this.notifyDataSetChanged();
            }
        }

        @Override
        public BatteryItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.cardview_battery, viewGroup, false);
            return new BatteryItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BatteryItemHolder holder, int position) {
            BatteryItem item = items.get(position);
            holder.mDate.setText(formatter.from(item.getDate()));
            holder.mDistance.setText(formatter.from(item.getDistance(), Unit.DISTANCE));
            holder.mMileage.setText(formatter.from(item.getMileage(), Unit.DISTANCE));
            holder.mAverageSpeed.setText(formatter.from(item.getAverageSpeed(), Unit.SPEED));
            holder.mLeftover.setText(formatter.from(item.getLeftover(), Unit.DISTANCE));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class BatteryItemHolder extends RecyclerView.ViewHolder {

        protected final TextView mDate;
        protected final TextView mDistance;
        protected final TextView mMileage;
        protected final TextView mAverageSpeed;
        protected final TextView mLeftover;

        public BatteryItemHolder(View itemView) {
            super(itemView);

            mDate         = (TextView) itemView.findViewById(R.id.battery_date);
            mDistance     = (TextView) itemView.findViewById(R.id.battery_distance);
            mMileage      = (TextView) itemView.findViewById(R.id.battery_mileage);
            mAverageSpeed = (TextView) itemView.findViewById(R.id.battery_average_speed);
            mLeftover     = (TextView) itemView.findViewById(R.id.battery_leftover);
        }
    }

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
                Snackbar
                    .make(mBatteryList, R.string.battery_list_mapper_error, Snackbar.LENGTH_LONG)
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
            Snackbar
                .make(mBatteryList, R.string.battery_list_request_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.battery_list_request_error_action, diagnoseListener)
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
