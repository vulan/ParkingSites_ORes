package parkingsites.android.com.parkingsites.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import parkingsites.android.com.parkingsites.presenter.MapsPresenter;
import parkingsites.android.com.parkingsites.service.ApiService;
import parkingsites.android.com.parkingsites.service.GoogleMapsApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    //127.0.0.1 is the emulated system's own loopback interface, not the one running on
    //your host development machine. within the Android system, one should use 10.0.2.2 which is an
    // alias specifically setup to contact your host 127.0.0.1
    @Provides
    @Singleton
    public static ApiService provideApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://138.68.16.98/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    @Provides
    @Singleton
    public static GoogleMapsApiService provideGoogleMapsApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GoogleMapsApiService mapsApiService = retrofit.create(GoogleMapsApiService.class);
        return mapsApiService;
    }

}
