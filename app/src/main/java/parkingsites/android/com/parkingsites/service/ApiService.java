package parkingsites.android.com.parkingsites.service;

import parkingsites.android.com.parkingsites.model.ParkingSiteSerializable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nora on 21-Mar-18.
 */

public interface ApiService {
    @GET("parking_sites.json")
    Call<ParkingSiteSerializable> getParkingLocation();
}
