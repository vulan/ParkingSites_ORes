package parkingsites.android.com.parkingsites.engine;

import java.util.ArrayList;
import java.util.List;

import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.ParkingSiteSerializable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Nora on 20-Mar-18.
 */

public class ParkingSitesEngine {

    private ApiService mApiService;

    public interface ApiService {
        @GET("parking_sites.json")
        Call<ParkingSiteSerializable> getParkingLocation();
    }

}
