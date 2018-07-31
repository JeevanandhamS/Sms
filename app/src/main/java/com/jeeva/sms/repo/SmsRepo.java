package com.jeeva.sms.repo;

import com.jeeva.sms.data.dto.Sms;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jeevanandham on 19/07/18
 */
public interface SmsRepo {

    Observable<List<Sms>> getSmsList(long lastFetchDate);
}