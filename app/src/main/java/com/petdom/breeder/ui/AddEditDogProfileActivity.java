package com.petdom.breeder.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.petdom.breeder.AppConfig;
import com.petdom.breeder.R;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.events.EventManager;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.http.operations.CreateDogOperation;
import com.petdom.breeder.http.operations.GetDogBreedsOperation;
import com.petdom.breeder.http.operations.GetDogColorPatternsOperation;
import com.petdom.breeder.http.operations.GetDogColorsOperation;
import com.petdom.breeder.modal.AppPreferences;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.ColorList;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.Dog;
import com.petdom.breeder.modal.DogBreedList;
import com.petdom.breeder.modal.PatternList;
import com.petdom.breeder.modal.Photo;
import com.petdom.breeder.utils.BackgroundExecutor;
import com.petdom.breeder.utils.UiUtils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddEditDogProfileActivity extends BreederBaseActivity implements View.OnClickListener {


    public static final int POSITION_NONE = -1;
    public static final int TAKE_PHOTO_CODE = 0X1;
    private static final String KEY_DOG_POSITION = "dog_position";
    private static final String KEY_BREEDER_POSITION = "breeder_position";
    private Breeder breeder;
    private ArrayList<Photo> photos = new ArrayList<>();

    public static Intent createIntent(Context context, int breederPosition, int dogPosition) {
        Intent intent = new Intent(context, AddEditDogProfileActivity.class);
        intent.putExtra(KEY_BREEDER_POSITION, breederPosition);
        intent.putExtra(KEY_DOG_POSITION, dogPosition);
        return intent;
    }

    private int breederPosition;
    private int dogPosition;

    private boolean isEditMode() {
        return dogPosition != POSITION_NONE;
    }

    private EditText etName;
    private Spinner spGender;
    private EditText etDOBDay;
    private EditText etDOBMonth;
    private EditText etDOBYear;
    private EditText etAgeMonth;
    private EditText etAgeYear;
    private Spinner spBreedType;
    private Spinner spBreed;
    private EditText etWeight;
    private EditText etHeight;
    private AutoCompleteTextView etColor;
    private AutoCompleteTextView etColorPattern;
    private ArrayAdapter colorAdapter;
    private ArrayAdapter patternAdapter;
    private ArrayAdapter breedTypeAdapter;

    private EditText etDescription;
    private EditText etPrice;
    private Spinner spListingType;
    private EditText etMicrochipNumber;
    private ImageView ivVaccination;
    private ImageView ivBirth;
    private ImageView ivHealth;

    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_dog_profile);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        breederPosition = getIntent().getIntExtra(KEY_BREEDER_POSITION, POSITION_NONE);
        dogPosition = getIntent().getIntExtra(KEY_DOG_POSITION, POSITION_NONE);

        if (!DataController.getInstance().getBreeders().isEmpty()) {
            breeder = DataController.getInstance().getBreeders().get(breederPosition);
        }
        // Setup action bar
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(isEditMode() ? R.string.edit_dog_profile : R.string.add_dog_profile);
        actionBar.setDisplayHomeAsUpEnabled(true);

        rootLayout = findViewById(R.id.root_layout_add_dog);
        etName = (EditText) rootLayout.findViewById(R.id.et_dogs_name);
        spGender = (Spinner) rootLayout.findViewById(R.id.sp_gender);
        etDOBDay = (EditText) rootLayout.findViewById(R.id.et_dd);
        etDOBMonth = (EditText) rootLayout.findViewById(R.id.et_mm);
        etDOBYear = (EditText) rootLayout.findViewById(R.id.et_yy);
        etAgeMonth = (EditText) rootLayout.findViewById(R.id.et_age_mm);
        etAgeYear = (EditText) rootLayout.findViewById(R.id.et_age_yy);
        spBreedType = (Spinner) rootLayout.findViewById(R.id.sp_breed_type);
        spBreed = (Spinner) rootLayout.findViewById(R.id.sp_breed);
        etWeight = (EditText) rootLayout.findViewById(R.id.et_weight);
        etHeight = (EditText) rootLayout.findViewById(R.id.et_height);
        etColor = (AutoCompleteTextView) rootLayout.findViewById(R.id.et_color);
        etColorPattern = (AutoCompleteTextView) rootLayout.findViewById(R.id.et_coat_pattern);
        etDescription = (EditText) rootLayout.findViewById(R.id.et_description);
        etPrice = (EditText) rootLayout.findViewById(R.id.et_price);
        spListingType = (Spinner) rootLayout.findViewById(R.id.sp_listing_types);
        etMicrochipNumber = (EditText) rootLayout.findViewById(R.id.et_microchip_number);
        ivVaccination = (ImageView) rootLayout.findViewById(R.id.iv_vaccination_certificate);
        ivBirth = (ImageView) rootLayout.findViewById(R.id.iv_birth_certificate);
        ivHealth = (ImageView) rootLayout.findViewById(R.id.iv_health_history);
        ivBirth.setOnClickListener(this);
        ivHealth.setOnClickListener(this);
        ivVaccination.setOnClickListener(this);


        //set colors
        if (DataController.getInstance().getColorList() != null) {
            setupColorList(DataController.getInstance().getColorList());
        }
        //set patterns
        if (DataController.getInstance().getPatternList() != null) {
            setupPatternList(DataController.getInstance().getPatternList());
        }

        //set breed types
        //set breed type
        if (DataController.getInstance().getDogBreedsList() != null) {
            DogBreedList list = DataController.getInstance().getDogBreedsList();
            setupBreedTypeList(list);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventManager.getInstance().register(this);
        if (DataController.getInstance().getColorList() == null) {
            BackgroundExecutor.getInstance().run(new GetDogColorsOperation());
        }
        if (DataController.getInstance().getPatternList() == null) {
            BackgroundExecutor.getInstance().run(new GetDogColorPatternsOperation());
        }
        if (DataController.getInstance().getDogBreedsList() == null) {
            BackgroundExecutor.getInstance().run(new GetDogBreedsOperation());
        }
        inputMethodManager.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);
        hideProgressDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.getInstance().remove(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Event.EVENT_DOWNLOAD_DOG_COLORS:
                onColorEvent(event);
                break;
            case Event.EVENT_DOWNLOAD_DOG_COLOR_PATTERNS:
                onPatternEvent(event);
                break;
            case Event.EVENT_CREATE_NEW_DOG:
                onCreateNewDogEvent(event);
                break;
            case Event.EVENT_DOWNLOAD_DOG_BREEDS:
                onBreedTypeEvent(event);
                break;

        }
    }

    private void onBreedTypeEvent(Event event) {
        if (event.isSuccess()) {
            DogBreedList list = (DogBreedList) event.getData();
            setupBreedTypeList(list);

        }
    }

    private Snackbar snackbar;

    private void onCreateNewDogEvent(Event event) {
        hideProgressDialog();
        if (event.isSuccess()) {
            setResult(RESULT_OK);
            finish();
        } else {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    snackbar = null;
                    onSaveClicked();
                }
            };
            com.petdom.breeder.events.Error error = event.getError();
            if (error.hasInternetError()) {
                snackbar = UiUtils.showInternetConnectionError(rootLayout, listener);
            } else if (error.hasUnknownError()) {
                snackbar = UiUtils.showWentWrongError(rootLayout, listener);
            } else if (error.hasInvalidResponseError()) {
                int code = ((InvalidResponseCodeException) error.getException()).getCode();
                Toast.makeText(this, getResources().getString(R.string.invalid_response_code) + " : " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onPatternEvent(Event event) {
        if (event.isSuccess()) {
            PatternList list = DataController.getInstance().getPatternList();
            setupPatternList(list);
        }
    }

    private void onColorEvent(Event event) {
        if (event.isSuccess()) {
            ColorList list = DataController.getInstance().getColorList();
            setupColorList(list);
        }
    }

    private void setupColorList(ColorList list) {
        String[] values = list.getAllName();
        if (values != null && values.length > 0) {
            colorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, values);
            colorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            etColor.setAdapter(colorAdapter);
        }
    }

    private void setupPatternList(PatternList list) {
        String[] values = list.getAllName();
        if (values != null && values.length > 0) {
            patternAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, values);
            patternAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            etColorPattern.setAdapter(patternAdapter);
        }
    }

    private void setupBreedTypeList(DogBreedList list) {
        String[] values = list.getAllName();
        if (values != null && values.length > 0) {
            breedTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, values);
            breedTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spBreed.setAdapter(breedTypeAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_breeder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            onSaveClicked();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        String message = "Wanna exit?";
        String btn1Text = "Yes";
        String btn2Text = "No";

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        AddEditDogProfileActivity.super.onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(btn1Text, dialogClickListener)
                .setNegativeButton(btn2Text, dialogClickListener).show();


    }

    private InputMethodManager inputMethodManager;

    private void onSaveClicked() {
        inputMethodManager.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);

        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Dog Name", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }
        if (spGender.getSelectedItem() == null) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            spGender.requestFocus();
            return;
        }
        if (spGender.getSelectedItem() == null) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            spGender.requestFocus();
            return;
        }
        int ageYY = 0;
        int ageMM = 0;
        try {
            ageYY = Integer.parseInt(etAgeYear.getText().toString());
        } catch (Exception e) {

        }
        try {
            ageMM = Integer.parseInt(etAgeMonth.getText().toString());
        } catch (Exception e) {

        }
        if (ageYY == 0 && ageMM == 0) {
            Toast.makeText(this, "Enter age", Toast.LENGTH_SHORT).show();
            etAgeMonth.requestFocus();
            return;
        }
        if (spBreedType.getSelectedItem() == null) {
            Toast.makeText(this, "Select breed type", Toast.LENGTH_SHORT).show();
            spBreedType.requestFocus();
            return;
        }
        if (spBreed.getSelectedItem() == null) {
            Toast.makeText(this, "Select breed", Toast.LENGTH_SHORT).show();
            spBreed.requestFocus();
            return;
        }
        float weight = 0;
        try {
            weight = Float.parseFloat(etWeight.getText().toString());
        } catch (Exception e) {

        }
        if (weight == 0) {
            Toast.makeText(this, "Enter weight", Toast.LENGTH_SHORT).show();
            etWeight.requestFocus();
            return;
        }
        float height = 0;
        try {
            height = Float.parseFloat(etHeight.getText().toString());
        } catch (Exception e) {

        }
        if (height == 0) {
            Toast.makeText(this, "Enter height", Toast.LENGTH_SHORT).show();
            etHeight.requestFocus();
            return;
        }
        String color = etColor.getText().toString().trim();
        if (TextUtils.isEmpty(color)) {
            Toast.makeText(this, "Enter coat color", Toast.LENGTH_SHORT).show();
            etColor.requestFocus();
            return;
        }
        String coatPattern = etColorPattern.getText().toString().trim();
        if (TextUtils.isEmpty(coatPattern)) {
            Toast.makeText(this, "Enter coat pattern", Toast.LENGTH_SHORT).show();
            etColorPattern.requestFocus();
            return;
        }
        if (spListingType.getSelectedItem() == null) {
            Toast.makeText(this, "Select Listing type", Toast.LENGTH_SHORT).show();
            spListingType.requestFocus();
            return;
        }
        int indexListing = spListingType.getSelectedItemPosition();
        float price = 0;
        try {
            price = Float.parseFloat(etPrice.getText().toString());
        } catch (Exception e) {

        }
        if (indexListing == 0 && price == 0) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
            etPrice.requestFocus();
            return;
        }
        String microchip = etMicrochipNumber.getText().toString().trim();
        if (TextUtils.isEmpty(microchip)) {
            Toast.makeText(this, "Enter microchip number", Toast.LENGTH_SHORT).show();
            etMicrochipNumber.requestFocus();
            return;
        }

        Dog d = new Dog(breeder);
        d.setName(name);
        d.setGender(spGender.getSelectedItemPosition() == 0 ? "M" : "F");
        d.setDob(getDOB());
        d.setAge(getAge(ageYY, ageMM));
        d.setBreedType(spBreedType.getSelectedItemPosition() == 0 ? "P" : "H");
        d.setBreed("/api/" + URLConstants.RESOURCE_DOGBREED + DataController.getInstance().getDogBreedsList().get(spBreed.getSelectedItemPosition()).getId() + "/");
        d.setWeight(weight);
        d.setHeight(height);
        d.setColor(color);
        d.setPattern(coatPattern);
        String desc = etDescription.getText().toString().trim();
        d.setDescription(TextUtils.isEmpty(desc) ? null : desc);
        d.setListing_type(spListingType.getSelectedItemPosition() == 0 ? "S" : "A");
        d.setPrice(price);
        d.setLat(AppPreferences.getInstance().getLatitude());
        d.setLng(AppPreferences.getInstance().getLongitude());
        d.setMicrochip_number(microchip);

        showProgressDialog("Please wait...");
        BackgroundExecutor.getInstance().run(new CreateDogOperation(d, photos));
    }

    private int getAge(int ageYY, int ageMM) {
        return ((ageYY * 12) + ageMM);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBirth || v == ivHealth || v == ivVaccination) {
            onCheckBoxClicked(v.getId());
        }
    }

    private void onCheckBoxClicked(int checkboxId) {
        //write photo upload code here
        String key = null;
        switch (checkboxId) {
            case R.id.iv_birth_certificate:
                key = Photo.KEY_BIRTH_CERTIFICATE;
                break;
            case R.id.iv_health_history:
                key = Photo.KEY_HEALTH_HISTORY;
                break;
            case R.id.iv_vaccination_certificate:
                key = Photo.KEY_VACCINATION_CERTIFICATE;
                break;
            default:
                throw new IllegalArgumentException("Invalid checkbox choice!");
        }
        launchCamera(checkboxId, key);
    }

    public String getDOB() {
        StringBuilder sb = new StringBuilder();
        String dd = etDOBDay.getText().toString().trim();
        String mm = etDOBMonth.getText().toString().trim();
        String yy = etDOBYear.getText().toString().trim();
        if (TextUtils.isEmpty(dd) || dd.equals("0") || dd.equals("00") || TextUtils.isEmpty(mm) || mm.equals("0") || mm.equals("00") || TextUtils.isEmpty(yy) || yy.length() < 4 || yy.equals("0000")) {
            return null;
        }
        if (mm.length() == 1) {
            sb.append('0');
        }
        sb.append(mm);
        sb.append('-');
        if (dd.length() == 1) {
            sb.append('0');
        }
        sb.append(dd);
        sb.append('-');
        sb.append(yy);
        return sb.toString();
    }

    private void launchCamera(int checkboxId, String key) {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.NAME_APP_FOLDER + "/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String file = dir + AppConfig.NAME_PICS + "_" + System.currentTimeMillis() + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(new Photo(checkboxId, key, file));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && !photos.isEmpty()) {
            if (resultCode != RESULT_OK) {

                int index = photos.size() - 1;
                Photo p = photos.get(index);
                photos.remove(index);
                ImageView iv = (ImageView) rootLayout.findViewById(p.getId());
                iv.setImageResource(R.drawable.ic_place_holder_image);
            } else {


                Photo p = photos.get(photos.size() - 1);

                //start media scan to new pic into the pic gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(p.getLocalPath());
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                ImageView iv = (ImageView) rootLayout.findViewById(p.getId());

                Bitmap bmp =null;
                try{
                    bmp = UiUtils.scaleImage(p.getLocalPath(),iv.getWidth(),iv.getHeight());
                }catch (Exception e){
                    Toast.makeText(this,"Error in creating thumbnail, try again!",Toast.LENGTH_LONG).show();
                }
                if (bmp!=null){
                    iv.setImageBitmap(bmp);
                }
            }
        }


    }
}
