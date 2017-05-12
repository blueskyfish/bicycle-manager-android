/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Floating button changer switch or replace the current Floating Action Button.
 *
 * It changes the Image and the click event.
 */
public interface MiddlewareHandler {

    void onAction(int action, Bundle arguments);

    /**
     * Change the floating action button. There are only few floating buttons possible
     *
     * @param kind the kind of the floating action button
     * @param onClickListener a listener for call on click the floating action button.
     */
    void changeFloatingButton(FloatingButtonKind kind, View.OnClickListener onClickListener);

    void showFloatingButton();

    void hideFloatingButton();

    Snackbar makeSnackbar(int resourceId);

    Snackbar makeSnackbar(int resourceId, int actionId, View.OnClickListener listener);

    void post(Runnable runnable, long delay);

}
