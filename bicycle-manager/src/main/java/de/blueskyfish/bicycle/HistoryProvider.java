/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

import de.blueskyfish.bicycle.battery.BatteryDetailFragment;
import de.blueskyfish.bicycle.battery.BatteryEditFragment;
import de.blueskyfish.bicycle.battery.BatteryListFragment;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.setting.SettingFragment;

/**
 * Manages the history of the fragment views.
 *
 *
 */
public class HistoryProvider {

    private static final Stack<ViewKey> GLOBAL_HISTORY_VIEW = new Stack<>();

    private static int GLOBAL_VIEW_TAG_ID = 0;

    private final Activity mActivity;
    private final SettingRepository mSetting;

    public HistoryProvider(Activity mActivity) {
        this.mActivity = mActivity;
        BicycleApplication app = (BicycleApplication) mActivity.getApplication();
        this.mSetting = app.getSetting();
        if (GLOBAL_VIEW_TAG_ID == 0) {
            GLOBAL_VIEW_TAG_ID = mSetting.getCurrentView();
        }
    }

    /**
     * Get the last view by start the application.
     *
     * @return the view
     */
    public BaseFragment getLastView() {
        return createView(getLastViewTagId());
    }

    public int getLastViewTagId() {
        return mSetting.getCurrentView();
    }

    /**
     * If change the orientation, then restore the view
     */
    public void restoreView() {
        showOrHideHomeButton();
    }

    /**
     * Starts with this fragment view. Per definition: there are no bundle arguments. It is
     * always {@link BaseFragment#EMPTY_BUNDLE}
     *
     * @param fragment the start view
     * @param tagId the tag id of the view
     * @param args always {@link BaseFragment#EMPTY_BUNDLE}
     */
    public void startView(BaseFragment fragment, int tagId, Bundle args) {
        String tagName = mActivity.getString(tagId);
        FragmentManager fm = mActivity.getFragmentManager();
        int entryId = fm.beginTransaction()
            .add(R.id.content, fragment, tagName)
            .addToBackStack(tagName)
            .commit();
        addEntry(tagId, entryId, tagName, args);
    }

    /**
     * Open a new view.
     *
     * @param fragment the new view
     * @param tagId the tag of the view
     * @param args the arguments for the view
     */
    public void openView(BaseFragment fragment, int tagId, Bundle args) {
        if (args != null && !args.isEmpty()) {
            fragment.setArguments(args);
        }
        String tagName = mActivity.getString(tagId);
        FragmentManager fm = mActivity.getFragmentManager();
        int entryId = fm.beginTransaction()
            .replace(R.id.content, fragment, tagName)
            .addToBackStack(tagName)
            .commit();
        mSetting.storeCurrentView(tagId);

        addEntry(tagId, entryId, tagName, args);
        Logger.debug("History: add new View (%s - %s)", entryId, tagName);
    }

    /**
     * Go to the previous view. If a previous view is not exist, then returns the tag id of the
     * previous view.
     *
     * @return returns 0 if the previous view is show, otherwise it contains the tag id.
     */
    public int backward() {
        // get the previous fragment from the history
        ViewKey key = getPrevEntry();
        if (key == null) {
            return getPrevViewTagId();
        }
        String tagName = key.tagName;
        FragmentManager fm = mActivity.getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tagName);
        if (fragment != null) {
            int entryId = fm.beginTransaction()
                .replace(R.id.content, fragment, tagName)
                .addToBackStack(tagName)
                .commit();
            addEntry(key.tagId, entryId, tagName, key.args);
            Logger.debug("History: found former View (%s - %s)", entryId, tagName);
            return 0;
        }
        return getPrevViewTagId();
    }

    public BaseFragment createView(int tagId) {
        switch (tagId) {
            case R.string.fragment_overview:
                return new OverviewFragment();
            case R.string.fragment_setting:
                return new SettingFragment();
            case R.string.fragment_battery_list:
                return new BatteryListFragment();
            case R.string.fragment_battery_edit:
                return new BatteryEditFragment();
            case R.string.fragment_battery_detail:
                return new BatteryDetailFragment();
            default:
                Logger.warn("Unknown View id: %s", tagId);
                return null;
        }
    }

    private int getPrevViewTagId() {
        switch (GLOBAL_VIEW_TAG_ID) {
            case R.string.fragment_battery_list:
            case R.string.fragment_setting:
                return R.string.fragment_overview;
            case R.string.fragment_battery_edit:
            case R.string.fragment_battery_detail:
                return R.string.fragment_battery_list;
            default:
                return 0;
        }
    }


    private void addEntry(int currentTagId, int entryId, String tagName, Bundle args) {
        GLOBAL_HISTORY_VIEW.push(new ViewKey(entryId, currentTagId, tagName, args));
        GLOBAL_VIEW_TAG_ID = currentTagId;

        showOrHideHomeButton();
    }

    private void showOrHideHomeButton() {
        boolean visibility = true;
        switch (GLOBAL_VIEW_TAG_ID) {
            case R.string.fragment_overview:
                visibility = false;
                break;
        }
        Logger.debug("Home Button is %s", visibility);

        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(visibility);
            actionBar.setHomeButtonEnabled(visibility);
        }
    }

    private ViewKey getPrevEntry() {
        if (GLOBAL_HISTORY_VIEW.size() < 2) {
            Logger.debug("History: there is no View for going backward...");
            return null;
        }
        // Remove the last fragment from the history
        Logger.debug("History: remove current view (%s)", GLOBAL_HISTORY_VIEW.pop());
        // get the previous fragment from the history
        return GLOBAL_HISTORY_VIEW.pop();
    }



    static final class ViewKey {

        private final int entryId;

        private final int tagId;

        private final String tagName;

        private final Bundle args;

        ViewKey(int entryId, int tagId, String tagName, Bundle args) {
            this.entryId = entryId;
            this.tagId = tagId;
            this.tagName = tagName;
            this.args = args;
        }

        @Override
        public String toString() {
            return String.format("View [entry=%s, %s, bundle=%s]", entryId, tagName, args);
        }
    }
}
