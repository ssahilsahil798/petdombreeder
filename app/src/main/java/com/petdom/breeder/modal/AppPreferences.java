package com.petdom.breeder.modal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.petdom.breeder.BreederApplication;

/**
 * Created by diwakar.mishra on 9/25/2015.
 */
public class AppPreferences {
    private static final String TAG = AppPreferences.class.getSimpleName();
    private static final String NAME = "pref_breeder_app";
    private static AppPreferences _instance;

    private enum Keys {

        LATTITUDE("lat"), LONGITUDE("lng");
        private String label;

        private Keys(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private SharedPreferences preferences;

    private AppPreferences() {
        preferences = BreederApplication.getInstance().getSharedPreferences(
                NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance() {
        if (_instance == null) {
            _instance = new AppPreferences();
        }
        return _instance;
    }


    /**
     * This Method Clear shared preference.
     */
    protected void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    protected void commit() {
        preferences.edit().commit();
    }

    private void setString(String key, String value) {
        if (key != null && value != null) {
            try {
                if (preferences != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(key, value);
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e);
            }
        }
    }

    private void setLong(String key, long value) {
        if (key != null) {
            try {
                if (preferences != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong(key, value);
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e);
            }
        }
    }

    private void setInt(String key, int value) {
        if (key != null) {
            try {
                if (preferences != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(key, value);
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e);
            }
        }
    }

    private void setDouble(String key, double value) {
        if (key != null) {
            try {
                if (preferences != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong(key, Double.doubleToRawLongBits(value));
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e);
            }
        }
    }

    private void setBoolean(String key, boolean value) {
        if (key != null) {
            try {
                if (preferences != null) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(key, value);
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e);
            }
        }
    }

    private int getInt(String key, int defaultValue) {
        if (preferences != null && key != null && preferences.contains(key)) {
            return preferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    private long getLong(String key, long defaultValue) {
        if (preferences != null && key != null && preferences.contains(key)) {
            return preferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        if (preferences != null && key != null && preferences.contains(key)) {
            return preferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    private String getString(String key, String defaultValue) {
        if (preferences != null && key != null && preferences.contains(key)) {
            return preferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    private double getDouble(String key, double defaultValue) {
        if (preferences != null && key != null && preferences.contains(key)) {
            return preferences.getFloat(key, (float) defaultValue);
        }
        return defaultValue;
    }

    private void putBoolean(String key, boolean value) {
        try {
            if (preferences != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(key, value);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Boolean in Shared preference", e);
        }
    }

    public void setLocation(double latitude, double longitude) {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Keys.LATTITUDE.getLabel(),
                    String.valueOf(latitude));
            editor.putString(Keys.LONGITUDE.getLabel(),
                    String.valueOf(longitude));
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG,
                    "Unable to update lattitude N longitude in Shared preference",
                    e);
        }

    }
    public double getLatitude() {
        return Double.parseDouble(preferences.getString(Keys.LATTITUDE.getLabel(), "0.0"));
    }
    public double getLongitude() {
        return Double.parseDouble(preferences.getString(Keys.LONGITUDE.getLabel(), "0.0"));
    }
}
