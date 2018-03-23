package parkingsites.android.com.parkingsites.presenter;

/**
 * Created by Nora on 21-Mar-18.
 */

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parkingsites.android.com.parkingsites.R;
import parkingsites.android.com.parkingsites.engine.ParkingSitesEngine;
import parkingsites.android.com.parkingsites.engine.RouteInformationEngine;
import parkingsites.android.com.parkingsites.model.Location;
import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.Polyline;
import parkingsites.android.com.parkingsites.model.Route;
import parkingsites.android.com.parkingsites.view.MapsView;

import static com.google.maps.android.PolyUtil.decode;

public class MapsPresenter {
    private final int CAMERA_ZOOM = 15, ROUTE_WIDTH = 10, PARK_CORIDOR = 100;
    private MapsView mMapsView;
    private GoogleMap mMap;
    private List<ParkingSite> mParkingSites;
    private List<ParkingSite> nearParkSites;
    private List<Route> mRouteInfo;
    private Map<Route, String> mRouteValues;

    public MapsPresenter() {
        Log.d("TAG", "Constructor Presenter");
    }

    public void getParkingSites(MapsView view, GoogleMap mMap) {
        this.mMapsView = view;
        this.mMap = mMap;
        mMapsView.showPorgressBar();
        ParkingSitesEngine.getEngineInstance().loadParkingSites(new ParkingSitesEngine.OnParkRequestResponse() {
            @Override
            public void onSuccessRequest() {
                loadParkingSites();
                mMapsView.hideProgressBar();
                mMapsView.showSearchBox();
            }

            @Override
            public void onErrorRequest() {
                mMapsView.hideProgressBar();
                mMapsView.OnParksErrorMessage();
            }
        });
    }

    public void loadParkingSites() {
        mParkingSites = ParkingSitesEngine.getEngineInstance().getParkingSite();

        if (mParkingSites.size() > 0 && mParkingSites != null) {
            showParkingsSites(mParkingSites);
        }
    }

    public void getRouteInfo(String from, String to, String key, Boolean alternatives) {
        mMapsView.showPorgressBar();
        RouteInformationEngine.getRouteEngineInstance().loadRouteInformation(new RouteInformationEngine.onRouteRequestResponse() {
            @Override
            public void RouteInfoSuccess() {
                mMap.clear();
                List<Route> routes = RouteInformationEngine.getRouteEngineInstance().getRouteInfo();
                if (showRouteInfo(routes)) {
                    nearParkSites = showNearCoridorParkingSites(routes);
                    showParkingsSites(nearParkSites);
                }
                mMapsView.hideProgressBar();
            }

            @Override
            public void RouteInfoError() {
                mMapsView.hideProgressBar();
                mMapsView.OnRouteFetchErrorMessage();
            }
        }, from, to, key, true);
    }

    private List<ParkingSite> showNearCoridorParkingSites(List<Route> routes) {
        List<ParkingSite> nearParkingSites = new ArrayList<>();
        for (ParkingSite park : mParkingSites) {
            LatLng latlng = new LatLng(park.getLocation().getLatitude(), park.getLocation().getLongitude());
            for (Route route : routes) {
                String polyline = route.getOverviewPolyline().getPoints();
                List<LatLng> listLatLng = decode(polyline);
                if (PolyUtil.isLocationOnPath(latlng, listLatLng, true, PARK_CORIDOR)) {
                    nearParkingSites.add(park);
                }
            }
        }

        return nearParkingSites;

    }

    private boolean showRouteInfo(List<Route> routes) {
        List<LatLng> latlngs = new ArrayList<>();
        if (!routes.isEmpty() && routes.size() > 0) {
            for (Route route : routes) {
                String polyLine = route.getOverviewPolyline().getPoints();
                latlngs = decode(polyLine);

                mMap.addPolyline(new PolylineOptions()
                        .addAll(latlngs)
                        .width(ROUTE_WIDTH)
                        .color(Color.GREEN)
                        .geodesic(true));
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlngs.get(0))
                    .zoom(CAMERA_ZOOM)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            return true;

        } else {
            mMapsView.OnRouteFetchErrorMessage();
            return false;
        }
    }

    private void showParkingsSites(List<ParkingSite> mParkingSites) {
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
