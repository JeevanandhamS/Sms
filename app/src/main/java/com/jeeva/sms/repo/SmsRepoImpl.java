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

    @Inject
    SmsDao mNoteDao;

    public SmsRepoImpl(SmsDao noteDao) {
        this.mNoteDao = noteDao;
    }

    @Override
    public Observable<List<Sms>> getSmsList(long lastFetchDate) {
        return mNoteDao.getSmsList(lastFetchDate);
    }
}