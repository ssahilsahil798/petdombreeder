package com.petdom.breeder.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.petdom.breeder.R;
import com.petdom.breeder.events.*;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.operations.CreateBreederOperation;
import com.petdom.breeder.http.operations.GetDogBreedsOperation;
import com.petdom.breeder.http.operations.GetBreedersOperation;
import com.petdom.breeder.http.operations.GetPetTypesOperation;
import com.petdom.breeder.modal.AppPreferences;
import com.petdom.breeder.modal.DogBreedList;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.PetTypeList;
import com.petdom.breeder.utils.BackgroundExecutor;
import com.petdom.breeder.utils.UiUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class AddBreederActivity extends BreederBaseActivity {

    private static final String KEY_MODE_EDIT = "mode_edit";

    public static Intent createIntent(Context context, boolean edit) {
        Intent intent = new Intent(context, AddBreederActivity.class);
        intent.putExtra(KEY_MODE_EDIT, edit);
        return intent;
    }

    private boolean isEditMode;

    private EditText etKennelName;
    private EditText etCity;
    private EditText etLocality;
    private EditText etPincode;
    private EditText etAddress;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etWebsite;
    private EditText etPetdomContact;
    private EditText etOwnerName;
    private EditText etYearInOperation;
    private EditText etStartedOn;

    private CheckBox cbVaccination;
    private CheckBox cbSurgery;
    private CheckBox cbDentistry;
    private CheckBox cbBoarding;
    private CheckBox cbPedigreed;
    private CheckBox cbTraining;
    private CheckBox cbGrooming;
    private CheckBox cbAccessories;

    private MultiSpinner spBreedType;
    private Spinner spPetType;
    private Spinner spBreederScale;
    private ArrayAdapter petTypeAdapter;

    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_breeder);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        isEditMode = getIntent().getBooleanExtra(KEY_MODE_EDIT, false);
        // Setup action bar
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(isEditMode ? R.string.edit_breeder : R.string.add_breeder);
        actionBar.setDisplayHomeAsUpEnabled(true);

        rootLayout = findViewById(R.id.root_layout_add_breeder);

        etKennelName = (EditText) rootLayout.findViewById(R.id.et_kennel_name);
        etCity = (EditText) rootLayout.findViewById(R.id.et_city);
        etLocality = (EditText) rootLayout.findViewById(R.id.et_locality);
        etPincode = (EditText) rootLayout.findViewById(R.id.et_pin);
        etAddress = (EditText) rootLayout.findViewById(R.id.et_address);
        etPhone = (EditText) rootLayout.findViewById(R.id.et_phone);
        etEmail = (EditText) rootLayout.findViewById(R.id.et_email);
        etWebsite = (EditText) rootLayout.findViewById(R.id.et_website);
        etPetdomContact = (EditText) rootLayout.findViewById(R.id.et_petdom_contact);
        etOwnerName = (EditText) rootLayout.findViewById(R.id.et_owner_name);
        etYearInOperation = (EditText) rootLayout.findViewById(R.id.et_year_operation);
        etStartedOn = (EditText) rootLayout.findViewById(R.id.et_started_on);

        cbVaccination = (CheckBox) rootLayout.findViewById(R.id.cb_vaccination);
        cbSurgery = (CheckBox) rootLayout.findViewById(R.id.cb_surgery);
        cbDentistry = (CheckBox) rootLayout.findViewById(R.id.cb_dentistry);
        cbBoarding = (CheckBox) rootLayout.findViewById(R.id.cb_boarding);
        cbPedigreed = (CheckBox) rootLayout.findViewById(R.id.cb_pedigreed);
        cbTraining = (CheckBox) rootLayout.findViewById(R.id.cb_training);
        cbGrooming = (CheckBox) rootLayout.findViewById(R.id.cb_grooming);
        cbAccessories = (CheckBox) rootLayout.findViewById(R.id.cb_accessories);

        spBreedType = (MultiSpinner) rootLayout.findViewById(R.id.sp_breed_types);
        spPetType = (Spinner) rootLayout.findViewById(R.id.sp_pet_types);
        spBreederScale = (Spinner) rootLayout.findViewById(R.id.sp_breeder_scale);

        //set breed type
        if (DataController.getInstance().getDogBreedsList() != null) {
            DogBreedList list = DataController.getInstance().getDogBreedsList();
            setupBreedTypeList(list);
        }
        //set pet type
        if (DataController.getInstance().getPetTypeList() != null) {
            PetTypeList list = DataController.getInstance().getPetTypeList();
            setupPetTypeList(list);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventManager.getInstance().register(this);

        if (DataController.getInstance().getDogBreedsList() == null) {
            BackgroundExecutor.getInstance().run(new GetDogBreedsOperation());
        }
        if (DataController.getInstance().getPetTypeList() == null) {
            BackgroundExecutor.getInstance().run(new GetPetTypesOperation());
        }

        inputMethodManager.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);

        hideProgressDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.getInstance().remove(this);
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
                        AddBreederActivity.super.onBackPressed();
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

        String kennel = etKennelName.getText().toString().trim();
        if (TextUtils.isEmpty(kennel)) {
            Toast.makeText(this, "Enter Kennel Name", Toast.LENGTH_SHORT).show();
            etKennelName.requestFocus();
            return;
        }
        String city = etCity.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Enter City Name", Toast.LENGTH_SHORT).show();
            etCity.requestFocus();
            return;
        }
        String locality = etLocality.getText().toString().trim();
        if (TextUtils.isEmpty(locality)) {
            Toast.makeText(this, "Enter Locality Name", Toast.LENGTH_SHORT).show();
            etLocality.requestFocus();
            return;
        }
        String pincode = etPincode.getText().toString().trim();
        if (pincode.length() != 6) {
            Toast.makeText(this, "Enter 6 digit Pin code", Toast.LENGTH_SHORT).show();
            etPincode.requestFocus();
            return;
        }
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Enter full address", Toast.LENGTH_SHORT).show();
            etAddress.requestFocus();
            return;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 7 || phone.length() > 12) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }
        String email = etEmail.getText().toString().trim();
        if (!UiUtils.isValidEmail(email)) {
            Toast.makeText(this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }
        String ownerName = etOwnerName.getText().toString().trim();
        if (TextUtils.isEmpty(ownerName)) {
            Toast.makeText(this, "Enter Owner name", Toast.LENGTH_SHORT).show();
            etOwnerName.requestFocus();
            return;
        }
        if (spPetType.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Select pet type", Toast.LENGTH_SHORT).show();
            spPetType.requestFocus();
            return;
        }
        ArrayList<Integer> breeds = spBreedType.getAllSelectedIndex();
        if (breeds.isEmpty()) {
            Toast.makeText(this, "Select at least 1 breed type", Toast.LENGTH_SHORT).show();
            spBreedType.requestFocus();
            return;
        }
        if (spBreederScale.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Select breeder scale", Toast.LENGTH_SHORT).show();
            spBreederScale.requestFocus();
            return;
        }


        String website = etWebsite.getText().toString().trim();
        String yearInOperartion = etYearInOperation.getText().toString().trim();
        String petdomContact = etPetdomContact.getText().toString().trim();
        String startedOn = etStartedOn.getText().toString().trim();

        int indexPetType = spPetType.getSelectedItemPosition();
        int indexBreederScale = spBreederScale.getSelectedItemPosition();

        Breeder b = new Breeder();
        b.setName(kennel);
        b.setOwner_name(ownerName);
        try{
            b.setSince(Integer.parseInt(startedOn));
        }catch (Exception e){

        }
        b.setCity(city);
        b.setCountry("India");
        b.setScale_type(getBreederScaleType(indexBreederScale));
        b.setWebsite(website);
        b.setEmail(email);
        b.setVaccination(cbVaccination.isChecked());
        b.setSurgery(cbSurgery.isChecked());
        b.setDentistry(cbDentistry.isChecked());
        b.setTraining(cbTraining.isChecked());
        b.setGrooming_centre(cbGrooming.isChecked());
        b.setBoarding(cbBoarding.isChecked());
        b.setPedigreed_pup(cbPedigreed.isChecked());
        b.setAccessories(cbAccessories.isChecked());
        b.setLocality(locality);
        try{
            b.setPincode(Integer.parseInt(pincode));
        }catch (Exception e){
        }
        b.setAddress(address);
        b.setPhone(phone);
        b.setBreed_types(getBreedCSV(breeds));
        b.setPet_types(DataController.getInstance().getPetTypeList().get(indexPetType).getId());
        try{
            b.setYears_in_operation(Integer.parseInt(yearInOperartion));
        }catch (Exception e){
        }
        b.setPetdom_contact(petdomContact);
        b.setLatitude(AppPreferences.getInstance().getLatitude());
        b.setLongitude(AppPreferences.getInstance().getLongitude());

        showProgressDialog("Please wait...");
        BackgroundExecutor.getInstance().run(new CreateBreederOperation(b));

    }

    private String getBreedCSV(ArrayList<Integer> breeds) {
        StringBuilder sb = new StringBuilder();
        int size=breeds.size();
        for (int i=0;i<size;i++){
            sb.append(DataController.getInstance().getDogBreedsList().get(breeds.get(i)).getId());
            if (i!=size-1){
                sb.append(',');
            }
        }
        return sb.toString();
    }


    private String getBreederScaleType(int position) {
        switch (position) {
            case 0:
                return "S";
            case 1:
                return "M";
            case 2:
                return "L";
        }
        return "";
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Event.EVENT_DOWNLOAD_DOG_BREEDS:
                onBreedTypeEvent(event);
                break;
            case Event.EVENT_DOWNLOAD_PET_TYPES:
                onPetTypeEvent(event);
                break;
            case Event.EVENT_CREATE_NEW_BREEDER:
                onCreateNewBreederEvent(event);
                break;

        }
    }

    private Snackbar snackbar;

    private void onCreateNewBreederEvent(Event event) {
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

    private void onPetTypeEvent(Event event) {

        if (event.isSuccess()) {
            PetTypeList list = (PetTypeList) event.getData();
            setupPetTypeList(list);
        }
    }

    private void onBreedTypeEvent(Event event) {
        if (event.isSuccess()) {
            DogBreedList list = (DogBreedList) event.getData();
            setupBreedTypeList(list);

        }
    }

    private void setupBreedTypeList(DogBreedList list) {
        String[] values = list.getAllName();
        if (values != null && values.length > 0) {
            spBreedType.setItems(Arrays.asList(values),"Choose", new MultiSpinner.MultiSpinnerListener() {
                @Override
                public void onItemsSelected(boolean[] selected) {

                }
            });
        }
    }

    private void setupPetTypeList(PetTypeList list) {
        String[] values = list.getAllName();
        if (values != null && values.length > 0) {
            petTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, values);
            petTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spPetType.setAdapter(petTypeAdapter);
        }

    }
}
