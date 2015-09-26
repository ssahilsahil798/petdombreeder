package com.petdom.breeder.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.petdom.breeder.R;
import com.petdom.breeder.adapter.DogsAdapter;
import com.petdom.breeder.events.*;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.operations.GetBreedersOperation;
import com.petdom.breeder.http.operations.GetDogsOperation;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.BreederList;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.Dog;
import com.petdom.breeder.utils.BackgroundExecutor;
import com.petdom.breeder.utils.UiUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class DogListActivity extends BreederBaseActivity implements DogsAdapter.OnItemClickListener {

    private static final String KEY_BREEDER_INDEX = "breeder_index";
    public static final int POSITION_NONE = -1;

    public static Intent createIntent(Context context, int breederIndex) {
        Intent intent = new Intent(context, DogListActivity.class);
        intent.putExtra(KEY_BREEDER_INDEX, breederIndex);
        return intent;
    }

    private static final int REQUEST_ADD_DOG = 0X1F;
    private static final int REQUEST_EDIT_DOG = 0X2F;
    private View rootLayout;
    private FloatingActionButton fab;
    private RecyclerView dogListView;
    private DogsAdapter dogsAdapter;
    private Breeder breeder;
    private int breederPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);

        breederPosition = getIntent().getIntExtra(KEY_BREEDER_INDEX, POSITION_NONE);
        breeder = DataController.getInstance().getBreeders().get(breederPosition);

        // Setup action bar
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.dogs_listing);
        actionBar.setDisplayHomeAsUpEnabled(true);

        rootLayout = findViewById(R.id.root_layout_dog_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddEditDogProfileActivity.createIntent(DogListActivity.this, breederPosition, POSITION_NONE), REQUEST_ADD_DOG);
            }
        });
        dogListView = (RecyclerView) findViewById(R.id.dog_recycler_view);
        dogListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dogListView.setLayoutManager(layoutManager);


        dogsAdapter = new DogsAdapter(this, breeder.getDogs(), this);
        dogListView.setAdapter(dogsAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventManager.getInstance().register(this);
        hideProgressDialog();

        BreederList breeders = DataController.getInstance().getBreeders();
        if (breeder.getDogs() == null || breeder.getDogs().isEmpty()) {
            showProgressDialog("Loading...");
            BackgroundExecutor.getInstance().run(new GetDogsOperation(breeder));
        } else if (dogsAdapter.getItemCount() == 1) {
            dogsAdapter.setDogs(breeder.getDogs());
        } else {
            dogsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.getInstance().remove(this);
    }

    private Snackbar snackbar;

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getType()) {
            case Event.EVENT_DOWNLOAD_DOGS:
                onDogsDownloaded(event);
                break;
        }

    }

    private void onDogsDownloaded(Event event) {
        hideProgressDialog();

        if (event.isSuccess()) {
            dogsAdapter.setDogs(breeder.getDogs());
        } else {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    snackbar = null;
                    showProgressDialog("Loading...");
                    BackgroundExecutor.getInstance().run(new GetDogsOperation(breeder));
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_DOG) {
            onAddDogActivityFinished(resultCode, data);
        } else if (requestCode == REQUEST_EDIT_DOG) {
            onEditDogActivityFinished(resultCode, data);
        }
    }

    private void onEditDogActivityFinished(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Dog profile edited!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.green));
            snackbar.show();
        } else {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Dog profile not edited!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.red));
            snackbar.show();
        }

    }

    private void onAddDogActivityFinished(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Dog added!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.green));
            snackbar.show();
            BackgroundExecutor.getInstance().run(new GetDogsOperation(breeder));

        } else {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Dog not added!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.red));
            snackbar.show();
        }
    }

    @Override
    public void onItemClicked(Dog dog, int dogPosition) {
//        startActivityForResult(AddEditDogProfileActivity.createIntent(this, breederPosition, dogPosition), REQUEST_EDIT_DOG);
    }
}
