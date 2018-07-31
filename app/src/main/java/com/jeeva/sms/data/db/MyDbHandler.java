package com.jeeva.sms.data.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.jeeva.sms.data.dao.SmsDao;
import com.jeeva.sms.data.db.DbDictionary.InbuiltSmsDictionary;
import com.jeeva.sms.data.dto.Sms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeeva on 16/12/17.
 */
public class MyDbHandler {

    private SmsDao mSmsDao;

    private ContentResolver mContentResolver;

    public MyDbHandler(Context context) {
        mContentResolver = context.getContentResolver();
        mSmsDao = new SmsDao(this);
    }

    public List<Sms> fetchSmsFromAppInbox(Long lastFetchDateInMs, int paginationLimit) {
        List<Sms> smsList = new ArrayList<>();

        String[] projection = {
                InbuiltSmsDictionary.ADDRESS,
                InbuiltSmsDictionary.BODY,
                InbuiltSmsDictionary.DATE
        };

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(
                    DbDictionary.INBUILT_INBOX_URI,
                    projection,
                    DbDictionary.FilterQuery.FETCH_SMS_BEFORE_LAST_DATE,
                    new String[] {String.valueOf(lastFetchDateInMs)},
                    InbuiltSmsDictionary.DESC_SORT_BY_DATE + " LIMIT " + paginationLimit
            );

            if (cursor.moveToFirst()) {

                do {
                    smsList.add(createSms(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getLong(2)
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return smsList;
    }

    public Sms createSms(String senderId, String messageBody, long receivedDate) {
        return new Sms(
                0,
                senderId,
                messageBody,
                receivedDate
        );
    }

    public void createSms(Sms sms) {
        createSms(sms.getSenderId(), sms.getMessageBody(), sms.getReceivedDate());
    }

    public SmsDao getSmsDao() {
        return mSmsDao;
    }
}