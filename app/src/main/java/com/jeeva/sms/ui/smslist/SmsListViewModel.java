package com.jeeva.sms.ui.smslist;

import android.arch.lifecycle.ViewModel;

import com.jeeva.sms.data.dto.Sms;
import com.jeeva.sms.repo.SmsRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by jeevanandham on 19/07/18
 */
public class SmsListViewModel extends ViewModel {

    @Inject
    SmsRepo mNotesRepo;

    @Inject
    public SmsListViewModel() {
    }

    public Observable<List<Sms>> getSmsList(long lastFetchDate) {
        return mNotesRepo.getSmsList(lastFetchDate);
    }
}