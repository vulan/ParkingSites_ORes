package parkingsites.android.com.parkingsites.presenter;

/**
 * Created by Nora on 21-Mar-18.
 */


import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import okhttp3.Route;
import parkingsites.android.com.parkingsites.OnParkingSitesRequestResponse;
import parkingsites.android.com.parkingsites.engine.ParkingSitesEngine;
import parkingsites.android.com.parkingsites.view.MapsView;

public class MapsPresenter implements OnParkingSitesRequestResponse {
    private MapsView mView;
    private GoogleMap mMap;
    private List<Route> routes;
    public MapsPresenter() {
        Log.d("TAG", "Constructor Presenter");
    }

    public void loadParkingSites(MapsView view, GoogleMap mMap) {
        this.mView = view;
        this.mMap = mMap;
        new ParkingSitesEngine();
    }

    public void getParkingSites(){
        new ParkingSitesEngine().getParkingSite();
    }


    @Override
    public void onParkingsRequestSucces() {

    }

    @Override
    public void onParkingsRequestError() {

    }
}
