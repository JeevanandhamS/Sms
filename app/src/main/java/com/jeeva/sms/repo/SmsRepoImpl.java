package com.jeeva.sms.repo;

import com.jeeva.sms.data.dao.SmsDao;
import com.jeeva.sms.data.dto.Sms;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by jeevanandham on 19/07/18
 */
public class SmsRepoImpl implements SmsRepo {

    private static int PAGINATION_LIMIT = 10;

    @Inject
    SmsDao mSmsDao;

    public SmsRepoImpl(SmsDao smsDao) {
        this.mSmsDao = smsDao;
    }

    @Override
    public Observable<List<Sms>> getSmsList(long lastFetchDate) {
        return mSmsDao.getSmsList(lastFetchDate, PAGINATION_LIMIT);
    }
}