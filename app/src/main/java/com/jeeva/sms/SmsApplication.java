package com.jeeva.sms;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jeeva.sms.di.DaggerSmsComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by jeevanandham on 19/07/18
 */
public class SmsApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private SmsBroadcastReceiver mSmsBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        DaggerSmsComponent.builder().application(this)
                .build().inject(this);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AndroidInjection.inject(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        mSmsBroadcastReceiver = new SmsBroadcastReceiver(this);
        registerReceiver(mSmsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(mSmsBroadcastReceiver);
        super.onTerminate();
    }
}