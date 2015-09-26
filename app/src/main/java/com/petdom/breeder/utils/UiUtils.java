package com.petdom.breeder.utils;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.petdom.breeder.BreederApplication;
import com.petdom.breeder.R;

import java.util.regex.Pattern;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class UiUtils {

    public static Snackbar showInternetConnectionError(View rootLayout, View.OnClickListener listener) {
        Resources resources = BreederApplication.getInstance().getResources();
        Snackbar snackbar = Snackbar.make(rootLayout, resources.getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(resources.getString(R.string.retry), listener);
        snackbar.setActionTextColor(resources.getColor(R.color.primaryColor));
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showWentWrongError(View rootLayout, View.OnClickListener listener) {
        Resources resources = BreederApplication.getInstance().getResources();
        Snackbar snackbar = Snackbar.make(rootLayout, resources.getString(R.string.oops_went_wrong), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(resources.getString(R.string.retry), listener);
        snackbar.setActionTextColor(resources.getColor(R.color.primaryColor));
        snackbar.show();
        return snackbar;
    }

    final static Pattern rfc2822 = Pattern
            .compile ("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    public static boolean isValidEmail(String email){
        return rfc2822.matcher(email).matches ();
    }
}
