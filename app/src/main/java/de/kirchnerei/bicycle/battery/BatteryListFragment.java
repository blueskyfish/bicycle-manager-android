package de.kirchnerei.bicycle.battery;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.FloatingButtonKind;
import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.helper.Unit;


/**
 * A simple {@link Fragment} subclass.
 */
public class BatteryListFragment extends BaseFragment {

    private RecyclerView mBatteryList;
    private BatteryListAdapter mAdapter;

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
        List<BatteryItem> items = Arrays.asList(
            new BatteryItem(99, formatter.toDate("10.10.2015"), 184, 23002, 120, 840),
            new BatteryItem(98, formatter.toDate("30.09.2015"), 191, 22108, 50, 901),
            new BatteryItem(97, formatter.toDate("23.09.2015"), 174, 21703, 10, 872),
            new BatteryItem(96, formatter.toDate("18.09.2015"), 195, 22108, 70, 765),
            new BatteryItem(99, formatter.toDate("10.10.2015"), 184, 23002, 120, 840),
            new BatteryItem(98, formatter.toDate("30.09.2015"), 191, 22108, 50, 901),
            new BatteryItem(97, formatter.toDate("23.09.2015"), 174, 21703, 10, 872),
            new BatteryItem(96, formatter.toDate("18.09.2015"), 195, 22108, 70, 765)

        );
        mAdapter = new BatteryListAdapter(items, formatter);
        mBatteryList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMiddlewareHandler().changeFloatingButton(FloatingButtonKind.BATTERY, addBatteryListener);
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

        public BatteryListAdapter(List<BatteryItem> items, Formatter formatter) {
            this.items = items;
            this.formatter = formatter;
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

        public BatteryItemHolder(View itemView) {
            super(itemView);

            mDate         = (TextView) itemView.findViewById(R.id.battery_date);
            mDistance     = (TextView) itemView.findViewById(R.id.battery_distance);
            mMileage      = (TextView) itemView.findViewById(R.id.battery_mileage);
            mAverageSpeed = (TextView) itemView.findViewById(R.id.battery_average_speed);
        }
    }
}
