package no.ntnu.hmsproject.network;

import android.app.Application;
import android.content.Context;

public class HMSApp extends Application {
    private static HMSApp Instance;
    private static Context HMSAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        this.setHMSAppContext(getApplicationContext());
    }

    public static HMSApp instance() {
        return Instance;
    }

    public static Context getHMSAppContext() {
        return HMSAppContext;
    }

    public void setHMSAppContext(Context hmsAppContext) {
        HMSAppContext = hmsAppContext;
    }
}
