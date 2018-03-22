package parkingsites.android.com.parkingsites.service;

import parkingsites.android.com.parkingsites.model.ParkingSiteSerializable;
import parkingsites.android.com.parkingsites.model.RouteInformationSerializable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nora on 22-Mar-18.
 */

public interface GoogleMapsApiService {
    @GET("maps/api/directions/json")
    Call<RouteInformationSerializable> getMapRouteDirection(@Query("origin") String from, @Query("destination") String to,
                                                            @Query("key") String mapKey, @Query("alternatives") Boolean alternatives);
}
