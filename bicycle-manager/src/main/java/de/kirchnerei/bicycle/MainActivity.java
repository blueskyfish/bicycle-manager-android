/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.kirchnerei.bicycle.battery.BatteryDetailFragment;
import de.kirchnerei.bicycle.battery.BatteryEditFragment;
import de.kirchnerei.bicycle.battery.BatteryListFragment;
import de.kirchnerei.bicycle.helper.Check;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.http.DiagnoseManager;
import de.kirchnerei.bicycle.setting.SettingFragment;

public class MainActivity extends AppCompatActivity
    implements MiddlewareHandler
{
    public static final int DELAY_FAB_BUTTON = 800;

    private Handler mMsgHandler = new Handler();

    private int mCurrentFragmentId = 0;
    private FloatingButtonKind mFABKind = null;
    private FloatingActionButton mFAButton;

    private SettingRepository mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFAButton = (FloatingActionButton) findViewById(R.id.action_add_distance);
        mSetting = ((BicycleApplication) getApplication()).getSetting();

        if (savedInstanceState != null) {
            return;
        }

        getFragmentManager().beginTransaction()
            .add(R.id.content, new OverviewFragment(), getString(R.string.fragment_overview))
            .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                doClickOnBackward();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onAction(int action, Bundle arguments) {
        switch (action) {
            case R.string.fragment_setting:
                openFragment(new SettingFragment(), arguments, R.string.fragment_setting);
                setHomeAction(true);
                break;
            case R.string.fragment_overview:
                openFragment(new OverviewFragment(), arguments, R.string.fragment_overview);
                setHomeAction(false);
                break;
            case R.string.fragment_battery_list:
                openFragment(new BatteryListFragment(), arguments, R.string.fragment_battery_list);
                setHomeAction(true);
                break;
            case R.string.fragment_diagnose:
                DiagnoseManager.showDialog(this);
                break;
            case R.string.fragment_battery_edit:
                openFragment(
                    new BatteryEditFragment(), arguments, R.string.fragment_battery_edit);
                setHomeAction(true);
                break;
            case R.string.fragment_battery_detail:
                openFragment(
                    new BatteryDetailFragment(), arguments, R.string.fragment_battery_detail);
                setHomeAction(true);
                break;
        }
    }

    /**
     * @see MiddlewareHandler#changeFloatingButton(FloatingButtonKind, View.OnClickListener)
     */
    @Override
    public void changeFloatingButton(FloatingButtonKind kind, View.OnClickListener onClickListener) {
        Check.notNull(kind, "changeFloatingButton() parameter kind couldn't be null");
        if (mFABKind != kind) {
            mFABKind = kind;
            mFAButton.setImageDrawable(getDrawable(kind.getImageId()));
            mFAButton.setOnClickListener(onClickListener);
            Logger.debug("change the FAB Button to %s", kind);
        }
    }

    @Override
    public void showFloatingButton() {
        mMsgHandler.postDelayed(showFAButton, DELAY_FAB_BUTTON);
    }

    @Override
    public void hideFloatingButton() {
        mMsgHandler.postDelayed(hideFAButton, DELAY_FAB_BUTTON);
    }

    @Override
    public Snackbar makeSnackbar(int resourceId) {
        View view = findViewById(R.id.main_layout);
        return Snackbar.make(view, resourceId, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar makeSnackbar(int resourceId, int actionId, View.OnClickListener listener) {
        return makeSnackbar(resourceId)
            .setAction(actionId, listener);
    }

    @Override
    public void post(Runnable runnable, long delay) {
        if (delay > 0) {
            mMsgHandler.postDelayed(runnable, delay);
        } else {
            mMsgHandler.post(runnable);
        }
    }

    private void openFragment(Fragment fragment, Bundle args, int tagId) {
        if (args != null && !args.isEmpty()) {
            fragment.setArguments(args);
        }
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
            .replace(R.id.content, fragment, getString(tagId))
            .addToBackStack(null)
            .commit();
        mCurrentFragmentId = tagId;
    }

    private void openDialog(DialogFragment fragment, Bundle arguments, int tagId) {
        FragmentManager fm = getFragmentManager();
        if (arguments != null && !arguments.isEmpty()) {
            fragment.setArguments(arguments);
        }
        fragment.show(fm, getString(tagId));
    }

    private void doClickOnBackward() {
        switch (mCurrentFragmentId) {
            case R.string.fragment_battery_list:
            case R.string.fragment_setting:
                openFragment(new OverviewFragment(), BaseFragment.EMPTY_BUNDLE,
                    R.string.fragment_overview);
                setHomeAction(false);
                break;
            case R.string.fragment_battery_edit:
                openFragment(new BatteryListFragment(), BaseFragment.EMPTY_BUNDLE,
                    R.string.fragment_battery_list);
                setHomeAction(true);
                break;
            case R.string.fragment_battery_detail:
                openFragment(new BatteryListFragment(), BaseFragment.EMPTY_BUNDLE,
                    R.string.fragment_battery_list);
                setHomeAction(true);
                break;
            default:
                Logger.debug("Backward: unknown fragment '%s'", mCurrentFragmentId);
                break;
        }
    }

    private void setHomeAction(boolean visibility) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(visibility);
            actionBar.setHomeButtonEnabled(visibility);
            Logger.debug("home button is %s", visibility);
        }
    }

    private final Runnable hideFAButton = new Runnable() {
        @Override
        public void run() {
            mFAButton.hide();
        }
    };

    private final Runnable showFAButton = new Runnable() {
        @Override
        public void run() {
            mFAButton.show();
        }
    };
}
