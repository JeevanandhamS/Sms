package com.jeeva.sms.data.db;

import android.net.Uri;

/**
 * Created by jeeva on 16/12/17.
 */
public interface DbDictionary {

    String INBUILT_SMS_AUTHORITY = "sms";

    Uri INBUILT_INBOX_URI = Uri.parse("content://" + INBUILT_SMS_AUTHORITY + "/" + InbuiltSmsDictionary.TABLE_NAME);

    interface InbuiltSmsDictionary {
        String TABLE_NAME = "inbox";
        String ADDRESS = "address";
        String DATE = "date";
        String BODY = "body";
        String DESC_SORT_BY_DATE = DATE + " DESC";
    }

    interface FilterQuery {
        String FETCH_SMS_BEFORE_LAST_DATE = InbuiltSmsDictionary.DATE + " < ?";
    }
}