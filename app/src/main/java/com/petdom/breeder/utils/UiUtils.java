package com.petdom.breeder.utils;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.petdom.breeder.BreederApplication;
import com.petdom.breeder.R;

import java.io.File;
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
            .compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    public static boolean isValidEmail(String email) {
        return rfc2822.matcher(email).matches();
    }

    public static Bitmap scaleImage(String filePath, int dw, int dh) {
        // Get the dimensions of the View
        int targetW = dw;
        int targetH = dh;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        return bitmap;
    }
}
