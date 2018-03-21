package parkingsites.android.com.parkingsites.dagger;

import javax.inject.Singleton;

import dagger.Component;
import parkingsites.android.com.parkingsites.MapsActivity;

/**
 * Created by Nora on 21-Mar-18.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MapsActivity mainActivity);
}
