package de.kirchnerei.bicycle;

import android.os.Bundle;
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
}
