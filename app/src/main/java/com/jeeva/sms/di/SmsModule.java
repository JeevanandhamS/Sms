package com.jeeva.sms.di;

import com.jeeva.sms.SmsApplication;
import com.jeeva.sms.data.dao.SmsDao;
import com.jeeva.sms.data.db.MyDbHandler;
import com.jeeva.sms.repo.SmsRepo;
import com.jeeva.sms.repo.SmsRepoImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;

/**
 * Created by jeevanandham on 19/07/18
 */
@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class SmsModule {

    @Provides
    SmsRepo providesSmsRepo(SmsDao smsDao) {
        return new SmsRepoImpl(smsDao);
    }

    @Provides
    @Singleton
    SmsDao providesSmsDao(MyDbHandler dbHandler) {
        return dbHandler.getSmsDao();
    }

    @Provides
    @Singleton
    MyDbHandler providesMyDbHandler(SmsApplication context) {
        return new MyDbHandler(context);
    }
}