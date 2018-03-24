package parkingsites.android.com.parkingsites.engine;

import java.util.ArrayList;
import java.util.List;

import parkingsites.android.com.parkingsites.dagger.AppModule;
import parkingsites.android.com.parkingsites.model.Route;
import parkingsites.android.com.parkingsites.model.RouteInformationSerializable;
import parkingsites.android.com.parkingsites.service.GoogleMapsApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nora on 22-Mar-18.
 */

public class RouteInformationEngine {
    private GoogleMapsApiService mMapApiService;

    private static RouteInformationEngine mMapEngineInstance;
    private List<Route> mRouteInfo = new ArrayList<>();

    public interface onRouteRequestResponse {
        void RouteInfoSuccess();
        void RouteInfoError();
    }

    public static RouteInformationEngine getRouteEngineInstance() {
        if (mMapEngineInstance == null) {
            mMapEngineInstance = new RouteInformationEngine();
        }
        return mMapEngineInstance;
    }

    public void loadRouteInformation(final onRouteRequestResponse requestResponse, String from, String to, String key, Boolean alternatives) {
        mMapApiService = AppModule.provideGoogleMapsApiService();
        Call<RouteInformationSerializable> routeInfoCaller = mMapApiService.getMapRouteDirection(from, to, key, alternatives);
        routeInfoCaller.enqueue(new Callback<RouteInformationSerializable>() {
            @Override
            public void onResponse(Call<RouteInformationSerializable> call, Response<RouteInformationSerializable> response) {
                if (response.body() != null)
                    mRouteInfo = response.body().getRoutes();
                requestResponse.RouteInfoSuccess();
            }

            @Override
            public void onFailure(Call<RouteInformationSerializable> call, Throwable t) {
                requestResponse.RouteInfoError();
            }
        });
    }

    public List<Route> getRouteInfo() {
        return mRouteInfo;
    }
}
