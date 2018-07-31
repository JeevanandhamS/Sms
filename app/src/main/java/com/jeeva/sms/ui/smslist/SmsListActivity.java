package com.jeeva.sms.ui.smslist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jeeva.sms.R;
import com.jeeva.sms.data.dto.Sms;
import com.jeeva.sms.di.SmsViewModelFactory;
import com.jeeva.sms.permission.PermissionHandler;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jeevanandham on 19/07/18
 */
public class SmsListActivity extends AppCompatActivity implements PermissionHandler.OnPermissionCallback, OnBottomReachedListener {

    private static final String NEW_SMS_DATA = "newSmsData";

    private static final int APP_SETTINGS_REQUEST_CODE = 201;

    @Inject
    SmsViewModelFactory mNotesViewModelFactory;

    private SmsListAdapter mNotesListAdapter;

    private SmsListViewModel mNotesListViewModel;

    private PermissionHandler mPermissionHandler;

    private long mLastFetchDate = System.currentTimeMillis();

    private boolean mLoadingSms = false;

    private boolean mAllSmsFetched = false;

    public static Intent getNewSmsIntent(Context context, Sms smsData) {
        Intent intent = new Intent(context, SmsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NEW_SMS_DATA, smsData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        mPermissionHandler = new PermissionHandler(Manifest.permission.READ_SMS, this);

        makeSureSmsPermission();
    }

    private void makeSureSmsPermission() {
        mPermissionHandler.checkPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.handleRequestPermissionResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (APP_SETTINGS_REQUEST_CODE == requestCode) {
            if (mPermissionHandler.hasPermission(this)) {
                onPermissionGranted();
            } else {
                onPermissionDenied(false);
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        setupRecyclerView();

        mNotesListViewModel = ViewModelProviders.of(this, mNotesViewModelFactory)
                .get(SmsListViewModel.class);

        fetchNextSmsList();
    }

    @Override
    public void onPermissionDenied(boolean neverShowChecked) {
        if (neverShowChecked) {
            showToastMessage(R.string.provide_sms_permission);
            openAppSettings();
        } else {
            showToastMessage(R.string.sms_permission_denied);
        }
    }

    private void setupRecyclerView() {
        RecyclerView rcvNotesList = findViewById(R.id.notes_list_rcv);
        rcvNotesList.setLayoutManager(new StickyHeaderLayoutManager());

        mNotesListAdapter = new SmsListAdapter(this, getNewSmsIfAvailable());
        rcvNotesList.setAdapter(mNotesListAdapter);
    }

    private Sms getNewSmsIfAvailable() {
        Bundle bundle = getIntent().getExtras();
        if(null != bundle && bundle.containsKey(NEW_SMS_DATA)) {
            return (Sms) bundle.getSerializable(NEW_SMS_DATA);
        }

        return null;
    }

    private void showToastMessage(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, APP_SETTINGS_REQUEST_CODE);
    }

    @Override
    public void onBottomReached() {
        fetchNextSmsList();
    }

    @SuppressLint("CheckResult")
    private void fetchNextSmsList() {
        if(!mAllSmsFetched && !mLoadingSms) {
            mLoadingSms = true;

            mNotesListViewModel.getSmsList(mLastFetchDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(smsList -> {
                        if(smsList.size() > 0) {
                            mLastFetchDate = smsList.get(smsList.size() - 1).getReceivedDate();

                            mNotesListAdapter.updateSmsList(smsList);

                        } else {
                            mAllSmsFetched = true;
                        }

                        mLoadingSms = false;
                    });
        }
    }
}