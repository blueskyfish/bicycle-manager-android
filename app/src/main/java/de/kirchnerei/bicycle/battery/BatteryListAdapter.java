package de.kirchnerei.bicycle.battery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.helper.Unit;

public class BatteryListAdapter extends RecyclerView.Adapter<BatteryListViewHolder> {

    private final List<BatteryItem> items;
    private final Formatter mFormatter;

    public BatteryListAdapter(Formatter formatter) {
        this.items = new ArrayList<>(64);
        this.mFormatter = formatter;
    }

    public void changeData(List<BatteryItem> items) {
        if (items != null && !items.isEmpty()) {
            this.items.clear();
            this.items.addAll(items);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public BatteryListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater
            .from(viewGroup.getContext())
            .inflate(R.layout.cardview_battery, viewGroup, false);
        return new BatteryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BatteryListViewHolder holder, int position) {
        BatteryItem item = items.get(position);
        holder.mDate.setText(mFormatter.from(item.getDate()));
        holder.mDistance.setText(mFormatter.from(item.getDistance(), Unit.DISTANCE));
        holder.mMileage.setText(mFormatter.from(item.getMileage(), Unit.DISTANCE));
        holder.mAverageSpeed.setText(mFormatter.from(item.getAverageSpeed(), Unit.SPEED));
        holder.mLeftover.setText(mFormatter.from(item.getLeftover(), Unit.DISTANCE));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
