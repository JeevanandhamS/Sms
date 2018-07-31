package com.jeeva.sms.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.jeeva.sms.R;

/**
 * Created by jeeva on 16/12/17.
 */
public class PermissionHandler {

    private static final int SPECIFIC_PERMISSION_REQUEST_CODE = 200;

    private String mRequestingPermission;

    private OnPermissionCallback mPermissionCallback;

    public PermissionHandler(String requestingPermission, OnPermissionCallback permissionCallback) {
        this.mRequestingPermission = requestingPermission;
        this.mPermissionCallback = permissionCallback;
    }

    public interface OnPermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied(boolean neverShowChecked);
    }

    public boolean hasPermission(Activity activity) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(activity, mRequestingPermission);
    }

    public void checkPermission(Activity activity) {
        if (hasPermission(activity)) {
            mPermissionCallback.onPermissionGranted();
        } else {
            requestPermissionToUser(activity, false);
        }
    }

    public void handleRequestPermissionResult(Activity activity, int requestCode, int[] grantResults) {
        if (SPECIFIC_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                mPermissionCallback.onPermissionGranted();
            } else {
                requestPermissionToUser(activity, true);
            }
        }
    }

    private void requestPermissionToUser(Activity activity, boolean afterResult) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, mRequestingPermission)) {
            showReasonToRequestPermission(activity);
        } else if (afterResult) {
            mPermissionCallback.onPermissionDenied(true);
        } else {
            showDefaultPermissionDialog(activity);
        }
    }

    private void showDefaultPermissionDialog(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{mRequestingPermission},
                SPECIFIC_PERMISSION_REQUEST_CODE
        );
    }

    private void showReasonToRequestPermission(final Activity activity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.sms_permission_title);
        alertBuilder.setMessage(R.string.sms_permission_rationale);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                showDefaultPermissionDialog(activity);
            }
        });

        alertBuilder.create().show();
    }
}