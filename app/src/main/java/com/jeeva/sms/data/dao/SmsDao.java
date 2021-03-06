package com.jeeva.sms.data.dao;

import com.jeeva.sms.data.db.MyDbHandler;
import com.jeeva.sms.data.dto.Sms;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jeevanandham on 19/07/18
 */
public class SmsDao {

    private MyDbHandler mDbHandler;

    public SmsDao(MyDbHandler dbHandler) {
        this.mDbHandler = dbHandler;
    }

    public Observable<List<Sms>> getSmsList(long lastFetchDate, int paginationLimit) {
        return Observable
                .just(lastFetchDate)
                .flatMap(dateInMs -> Observable.just(mDbHandler.fetchSmsFromAppInbox(dateInMs, paginationLimit)));
    }
}