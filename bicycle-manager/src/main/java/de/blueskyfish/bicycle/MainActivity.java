/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import de.blueskyfish.bicycle.helper.Check;
import de.blueskyfish.bicycle.helper.Delay;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.http.DiagnoseManager;

public class MainActivity extends AppCompatActivity
    implements MiddlewareHandler
{
    private Handler mMsgHandler = new Handler();

    private FloatingButtonKind mFABKind = null;
    private FloatingActionButton mFAButton;

    private HistoryProvider mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFAButton = (FloatingActionButton) findViewById(R.id.action_add_distance);
        mHistory = new HistoryProvider(this);

        if (savedInstanceState != null) {
            mHistory.restoreView();
            Logger.debug("Restore... (tag: %s)", getString(mHistory.getLastViewTagId()));
            return;
        }

        BaseFragment fragment = mHistory.getLastView();
        mHistory.startView(fragment, mHistory.getLastViewTagId(), BaseFragment.EMPTY_BUNDLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Logger.debug("Back pressed...");
                doClickOnBackward();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMsgHandler = null;
        mFABKind = null;
        mHistory = null;
    }

    @Override
    public void onAction(int action, Bundle arguments) {
        switch (action) {
            case R.string.fragment_setting:
            case R.string.fragment_overview:
            case R.string.fragment_battery_list:
            case R.string.fragment_battery_edit:
            case R.string.fragment_battery_detail:
                mHistory.openView(mHistory.createView(action), action, arguments);
                break;
            case R.string.fragment_diagnose:
                DiagnoseManager.showDialog(this);
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
        mMsgHandler.postDelayed(showFAButton, Delay.START_FAB_BUTTON);
    }

    @Override
    public void hideFloatingButton() {
        mMsgHandler.postDelayed(hideFAButton, Delay.START_FAB_BUTTON);
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

    private void doClickOnBackward() {
        int tagId = mHistory.backward();
        if (tagId != 0) {
            BaseFragment fragment = mHistory.createView(tagId);
            mHistory.openView(fragment, tagId, BaseFragment.EMPTY_BUNDLE);
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
