package parkingsites.android.com.parkingsites.dagger;

import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import parkingsites.android.com.parkingsites.presenter.MapsPresenter;

/**
 * Created by Nora on 21-Mar-18.
 */

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    MapsPresenter provideMapPresenter() {
        return new MapsPresenter();
    }

}
