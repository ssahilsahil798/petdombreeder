package com.petdom.breeder.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.petdom.breeder.R;
import com.petdom.breeder.adapter.BreederAdapter;
import com.petdom.breeder.events.*;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.modal.BreederList;
import com.petdom.breeder.utils.BackgroundExecutor;
import com.petdom.breeder.http.operations.GetBreedersOperation;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.utils.LocationTracker;
import com.petdom.breeder.utils.UiUtils;
import com.squareup.otto.Subscribe;

public class BreederListActivity extends BreederBaseActivity implements BreederAdapter.OnItemClickListener {

    private static final int REQUEST_ADD_BREEDER = 0XFF;
    private View rootLayout;
    private FloatingActionButton fab;
    private RecyclerView breederListView;
    private BreederAdapter breederAdapter;

    private LocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeder_list);
        rootLayout = findViewById(R.id.root_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddBreederActivity.createIntent(BreederListActivity.this, false), REQUEST_ADD_BREEDER);
            }
        });
        breederListView = (RecyclerView) findViewById(R.id.breeder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        breederListView.setLayoutManager(layoutManager);

        breederAdapter = new BreederAdapter(this, this);
        breederListView.setAdapter(breederAdapter);

        //init location tracker
        locationTracker = new LocationTracker(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventManager.getInstance().register(this);
        hideProgressDialog();

        BreederList breeders = DataController.getInstance().getBreeders();
        if (breeders == null || breeders.isEmpty()) {
            showProgressDialog("Loading...");
            BackgroundExecutor.getInstance().run(new GetBreedersOperation());
        } else if (breederAdapter.getItemCount() == 1) {
            breederAdapter.setBreeders(DataController.getInstance().getBreeders());
        } else {
            breederAdapter.notifyDataSetChanged();
        }
        if (!locationTracker.isGPSEnabled()) {
            locationTracker.showSettingsAlert();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.getInstance().remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_BREEDER) {
            onAddBreederActivityFinished(resultCode, data);
        }
    }

    private void onAddBreederActivityFinished(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Breeder added!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.green));
            snackbar.show();
            BackgroundExecutor.getInstance().run(new GetBreedersOperation());

        } else {
            final Snackbar snackbar = Snackbar.make(rootLayout, "Breeder not added!", Snackbar.LENGTH_LONG);
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
    public void onItemClicked(Breeder breeder, int position) {
        startActivity(DogListActivity.createIntent(this, position));
    }

    private Snackbar snackbar;

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getType()) {
            case Event.EVENT_DOWNLOAD_BREEDERS:
                onBreedersDownloaded(event);
                break;
        }
    }

    private void onBreedersDownloaded(Event event) {
        hideProgressDialog();
        if (event.isSuccess()) {
            breederAdapter.setBreeders((BreederList) event.getData());
        } else {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    snackbar = null;
                    showProgressDialog("Loading...");
                    BackgroundExecutor.getInstance().run(new GetBreedersOperation());
                }
            };
            Error error = event.getError();
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
}
