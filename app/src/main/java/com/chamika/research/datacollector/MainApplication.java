package com.chamika.research.datacollector;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import me.everything.providers.stetho.ProvidersStetho;

/**
 * Created by chamika on 3/17/17.
 */

public class MainApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ProvidersStetho providersStetho = new ProvidersStetho(this);
        providersStetho.enableDefaults();

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(providersStetho.defaultInspectorModulesProvider())
                .build());
    }
}
