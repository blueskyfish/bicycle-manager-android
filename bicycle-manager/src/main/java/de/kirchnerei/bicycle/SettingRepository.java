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
}
