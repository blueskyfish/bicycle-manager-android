package de.kirchnerei.bicycle;

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
}
