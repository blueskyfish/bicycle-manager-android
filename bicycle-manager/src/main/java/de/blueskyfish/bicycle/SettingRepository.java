/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle;

public interface SettingRepository {

    String getToken();

    boolean hasToken();

    String getUserEmail();

    String getPassword();

    /**
     * Update the user email and password settings
     *
     * @param userEmail the user mail
     * @param password the password
     */
    void change(String userEmail, String password);

    /**
     * Returns the base url from the server
     * @return the base url
     */
    String getBaseUrl();

    /**
     * Verify whether the token is valid.
     *
     * @param token the token
     */
    void verifyToken(String token);

    /**
     * Stores the current view.
     *
     * @param tagId the tag id of the current view.
     */
    void storeCurrentView(int tagId);

    /**
     * Returns the current view.
     *
     * @return the tag id of the current view
     */
    int getCurrentView();
}
