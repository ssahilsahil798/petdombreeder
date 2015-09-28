package com.petdom.breeder.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.petdom.breeder.R;
import com.petdom.breeder.utils.UiUtils;

public class ImagePreviewActivity extends BreederBaseActivity {

    public static final String KEY_PIC_PATH = "pic_path";
    public static final String KEY_PIC_INDEX = "pic_index";

    public static Intent createIntent(Context context, String localPath, int index) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(KEY_PIC_PATH, localPath);
        intent.putExtra(KEY_PIC_INDEX, index);
        return intent;
    }

    private ZoomableImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("");

        iv = (ZoomableImageView) findViewById(R.id.iv_preview);
        String path = getIntent().getStringExtra(KEY_PIC_PATH);

        Bitmap bmp =null;
        try{
            bmp = UiUtils.scaleImage(path, 800, 800);
        }catch (Exception e){
            Toast.makeText(this, "Error in loading full image, try again!", Toast.LENGTH_LONG).show();
        }
        if (bmp!=null){
            iv.setImageBitmap(bmp);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            setResult(RESULT_OK,getIntent());
            finish();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
