package parkingsites.android.com.parkingsites.dagger;

import android.app.Application;

import parkingsites.android.com.parkingsites.MapsActivity;


/**
 * Created by Nora on 21-Mar-18.
 */

public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void inject(MapsActivity activity){
        appComponent.inject(activity);
    }
}
