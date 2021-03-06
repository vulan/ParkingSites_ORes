package parkingsites.android.com.parkingsites.engine;

import java.util.ArrayList;
import java.util.List;

import parkingsites.android.com.parkingsites.dagger.AppModule;
import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.ParkingSiteSerializable;
import parkingsites.android.com.parkingsites.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nora on 20-Mar-18.
 */

public class ParkingSitesEngine {

    private ApiService mApiService;
    private List<ParkingSite> mParkingSites = new ArrayList<>();
    private static ParkingSitesEngine mEngineInstance;

    public interface OnParkRequestResponse{
        void onSuccessRequest();
        void onErrorRequest();
    }

    public ParkingSitesEngine(){

    }

    public static ParkingSitesEngine getEngineInstance(){
        if ( mEngineInstance == null ){
            mEngineInstance = new ParkingSitesEngine();
        }
        return mEngineInstance;
    }

    public void loadParkingSites(final OnParkRequestResponse requestResponse){
        mApiService = AppModule.provideApiService();
        Call<ParkingSiteSerializable> siteParkingsCaller = mApiService.getParkingLocation();
        siteParkingsCaller.enqueue(new Callback<ParkingSiteSerializable>() {
            @Override
            public void onResponse(Call<ParkingSiteSerializable> call, Response<ParkingSiteSerializable> response) {
                mParkingSites = response.body().getParkingSites();
                requestResponse.onSuccessRequest();
            }

            @Override
            public void onFailure(Call<ParkingSiteSerializable> call, Throwable t) {
                requestResponse.onErrorRequest();
            }
        });
    }

    public List<ParkingSite> getParkingSite(){
        return mParkingSites;
    }

}
