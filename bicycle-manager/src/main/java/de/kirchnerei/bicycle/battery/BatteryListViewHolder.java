/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.battery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.kirchnerei.bicycle.R;

public class BatteryListViewHolder extends RecyclerView.ViewHolder {

    public final TextView mDate;
    public final TextView mDistance;
    public final TextView mMileage;
    public final TextView mAverageSpeed;
    public final TextView mLeftover;

    public BatteryListViewHolder(View itemView) {
        super(itemView);

        mDate = (TextView) itemView.findViewById(R.id.battery_date);
        mDistance = (TextView) itemView.findViewById(R.id.battery_distance);
        mMileage = (TextView) itemView.findViewById(R.id.battery_mileage);
        mAverageSpeed = (TextView) itemView.findViewById(R.id.battery_average_speed);
        mLeftover = (TextView) itemView.findViewById(R.id.battery_leftover);
    }
}
