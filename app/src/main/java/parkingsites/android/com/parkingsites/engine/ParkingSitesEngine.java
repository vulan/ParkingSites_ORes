package parkingsites.android.com.parkingsites.engine;

import java.util.ArrayList;
import java.util.List;

import parkingsites.android.com.parkingsites.OnParkingSitesRequestResponse;
import parkingsites.android.com.parkingsites.dagger.AppModule;
import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.ParkingSiteSerializable;
import parkingsites.android.com.parkingsites.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Nora on 20-Mar-18.
 */

public class ParkingSitesEngine {

    private ApiService mApiService;
    private List<ParkingSite> mParkingSites = new ArrayList<>();
    private OnParkingSitesRequestResponse responseRequest;
    private static ParkingSitesEngine mEngineInstance;

    public ParkingSitesEngine(ApiService apiService){
        this.mApiService = apiService;
    }

    public static ParkingSitesEngine getEngineInstance(){
        if ( mEngineInstance == null ){
            mEngineInstance = new ParkingSitesEngine();
        }
        return mEngineInstance;
    }

    public ParkingSitesEngine(){
        mApiService = AppModule.provideApiService();
        Call<ParkingSiteSerializable> siteParkingsCaller = mApiService.getParkingLocation();
        siteParkingsCaller.enqueue(new Callback<ParkingSiteSerializable>() {
            @Override
            public void onResponse(Call<ParkingSiteSerializable> call, Response<ParkingSiteSerializable> response) {
                mParkingSites = response.body().getParkingSites();
                responseRequest.onParkingsRequestSucces();
            }

            @Override
            public void onFailure(Call<ParkingSiteSerializable> call, Throwable t) {
                responseRequest.onParkingsRequestError();
            }
        });
    }

    public List<ParkingSite> getParkingSite(){
        return mParkingSites;
    }

}
