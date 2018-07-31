package com.jeeva.sms.di;

import com.jeeva.sms.SmsApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by jeevanandham on 19/07/18
 */
@Singleton
@Component(modules = {SmsModule.class, AppBinder.class})
public interface SmsComponent {

    void inject(SmsApplication application);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(SmsApplication application);

        SmsComponent build();
    }
}