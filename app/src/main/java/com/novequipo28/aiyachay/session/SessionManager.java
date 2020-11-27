package com.novequipo28.aiyachay.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.novequipo28.aiyachay.Login;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Pref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USER = "user";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_NAME = "name";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }


    public void createLoginSession(String user, String email) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER, user);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER, pref.getString(KEY_USER, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        return user;
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public String getUsuario() {
        return pref.getString(KEY_USER, "admin");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "ADMINISTRADOR");
    }

    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

}