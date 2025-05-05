package com.example.reservili.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_CLIENT_ID    = "clientId";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME  = "lastName";
    private static final String KEY_EMAIL      = "email";
    private static final String KEY_PHONE      = "phone";

    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private String FirstName;
    private String LastName;
    private String Email;
    private String Phone;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private SessionManager(Context ctx) {
        prefs = ctx.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();

        FirstName = prefs.getString(KEY_FIRST_NAME, "");
        LastName  = prefs.getString(KEY_LAST_NAME,  "");
        Email     = prefs.getString(KEY_EMAIL,      "");
        Phone     = prefs.getString(KEY_PHONE,      "");
    }
    public static synchronized SessionManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new SessionManager(ctx);
        }
        return instance;
    }
    public void login(int clientId, String firstName, String lastName,
                      String email, String phone) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_CLIENT_ID, clientId);
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME,  lastName);
        editor.putString(KEY_EMAIL,      email);
        editor.putString(KEY_PHONE,      phone);
        editor.apply();

        this.FirstName = firstName;
        this.LastName  = lastName;
        this.Email     = email;
        this.Phone     = phone;



    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getClientId() {
        return prefs.getInt(KEY_CLIENT_ID, -1);
    }


}
