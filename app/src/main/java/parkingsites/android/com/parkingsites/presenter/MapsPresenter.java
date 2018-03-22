package parkingsites.android.com.parkingsites.presenter;

/**
 * Created by Nora on 21-Mar-18.
 */


import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import parkingsites.android.com.parkingsites.R;
import parkingsites.android.com.parkingsites.engine.ParkingSitesEngine;
import parkingsites.android.com.parkingsites.engine.RouteInformationEngine;
import parkingsites.android.com.parkingsites.model.Location;
import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.Route;
import parkingsites.android.com.parkingsites.view.MapsView;

public class MapsPresenter {
    private final int CAMERA_ZOOM = 15;
    private MapsView mMapsView;
    private GoogleMap mMap;
    private List<ParkingSite> mParkingSites;
    private List<Route> mRouteInfo;

    public MapsPresenter() {
        Log.d("TAG", "Constructor Presenter");
    }

    public void getParkingSites(MapsView view, GoogleMap mMap) {
        this.mMapsView = view;
        this.mMap = mMap;
        ParkingSitesEngine.getEngineInstance().loadParkingSites(new ParkingSitesEngine.OnParkRequestResponse() {
            @Override
            public void onSuccessRequest() {
                loadParkingSites();
            }

            @Override
            public void onErrorRequest() {
                mMapsView.OnParkingErrorMessage();
            }
        });
    }

    private void loadParkingSites(){
        mParkingSites = ParkingSitesEngine.getEngineInstance().getParkingSite();

        if (mParkingSites.size() > 0 && mParkingSites != null){
            showFreeParkingsSites();
        }
    }

    public void getRouteInfo(String from, String to, String key, Boolean alternatives){
        RouteInformationEngine.getRouteEngineInstance().loadRouteInformation(new RouteInformationEngine.onRouteRequestResponse() {
            @Override
            public void RouteInfoSuccess() {
                showRouteInfo();
            }

            @Override
            public void RouteInfoError() {
                mMapsView.OnRouteFetchError();
            }
        }, from, to, key, false);
    }

    private void showRouteInfo(){ //to be implemented

    }

    private void showFreeParkingsSites(){
        for (ParkingSite parking : mParkingSites) {
            Location location = parking.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking_sign))
                    .position(latLng).title(parking.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(CAMERA_ZOOM)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
