package com.jeeva.sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.jeeva.sms.data.dao.SmsDao;
import com.jeeva.sms.data.db.MyDbHandler;
import com.jeeva.sms.data.dto.Sms;
import com.jeeva.sms.ui.smslist.SmsListActivity;
import com.jeeva.sms.utils.NotificationUtils;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private SmsDao mSmsDao;

    public SmsBroadcastReceiver(Context context) {
        mSmsDao = new SmsDao(new MyDbHandler(context));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage smsMessage = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                if(smsMessages.length > 0) {
                    smsMessage = smsMessages[0];
                }
            } else {
                Bundle smsBundle = intent.getExtras();

                if (null != smsBundle) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (null == pdus) {
                        return;
                    }

                    if(pdus.length > 0) {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    }
                }
            }

            if(null == smsMessage) {
                return;
            }

            /*Sms smsData = new Sms();
            smsData.setSenderId(smsMessage.getDisplayOriginatingAddress());
            smsData.setMessageBody(smsMessage.getMessageBody());
            smsData.setReceivedDate(smsMessage.getTimestampMillis());

            showNotification(context, smsData);*/

            mSmsDao.getSmsList(System.currentTimeMillis(), 1)
                    .subscribe(smsList -> {
                        if (smsList.size() > 0) {
                            showNotification(context, smsList.get(0));
                        }
                    });
        }
    }

    private void showNotification(Context context, Sms smsData) {
        Intent targetIntent = SmsListActivity.getNewSmsIntent(context, smsData);
        PendingIntent pIntent = PendingIntent.getActivity(
                context, (int) System.currentTimeMillis(), targetIntent, 0);

        NotificationUtils.presentNotification(
                context,
                (int) smsData.getSmsId(),
                smsData.getSenderId(),
                smsData.getMessageBody(),
                pIntent,
                0,
                null,
                null
        );
    }
}