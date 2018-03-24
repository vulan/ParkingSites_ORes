package parkingsites.android.com.parkingsites.presenter;

/**
 * Created by Nora on 21-Mar-18.
 */

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parkingsites.android.com.parkingsites.R;
import parkingsites.android.com.parkingsites.engine.ParkingSitesEngine;
import parkingsites.android.com.parkingsites.engine.RouteInformationEngine;
import parkingsites.android.com.parkingsites.model.Location;
import parkingsites.android.com.parkingsites.model.ParkingSite;
import parkingsites.android.com.parkingsites.model.Route;
import parkingsites.android.com.parkingsites.view.MapsView;

import static com.google.maps.android.PolyUtil.decode;

public class MapsPresenter {
    private final int MAP_CAMERA_ZOOM = 15, ROUTE_WIDTH = 10, PARK_CORRIDOR = 100;
    private MapsView mMapsView;
    private GoogleMap mMap;
    private List<ParkingSite> mParkingSites;
    private List<ParkingSite> nearParkSites;
    private List<Route> mRouteInfo;
    private Map<Route, String> mRouteValues;
    private List<String> routeDetails;

    public MapsPresenter() {

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
                mMapsView.showSearchBox();//show search destinations, only if parkings available
            }

            @Override
            public void onErrorRequest() {
                mMapsView.hideProgressBar();
                mMapsView.OnParksErrorMessage();
            }
        });
    }

    public void loadParkingSites() {
        mMap.clear();
        mParkingSites = ParkingSitesEngine.getEngineInstance().getParkingSite();

        if (mParkingSites.size() > 0 && mParkingSites != null) {
            showParkingsSites(mParkingSites);
        }
    }

    public void getRouteInfo(String from, String to, String key, Boolean alternatives) {
        mMapsView.showPorgressBar();
        mMap.clear();
        RouteInformationEngine.getRouteEngineInstance().loadRouteInformation(new RouteInformationEngine.onRouteRequestResponse() {
            @Override
            public void RouteInfoSuccess() {
                mRouteInfo = RouteInformationEngine.getRouteEngineInstance().getRouteInfo();
                if (showRouteInfo(mRouteInfo)) {//if info route found, get info for 100m corridor
                    nearParkSites = showOnCorridorParkingSites(mRouteInfo);
                    showParkingsSites(nearParkSites);//show near parking sites
                    mMapsView.showRouteDetails(routeDetails);
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

    private List<ParkingSite> showOnCorridorParkingSites(List<Route> routes) {
        List<ParkingSite> nearParkingSites = new ArrayList<>();
        for (ParkingSite park : mParkingSites) {
            LatLng latlng = new LatLng(park.getLocation().getLatitude(), park.getLocation().getLongitude());
            for (Route route : routes) {
                String polyline = route.getOverviewPolyline().getPoints();
                List<LatLng> listLatLng = decode(polyline);
                if (PolyUtil.isLocationOnPath(latlng, listLatLng, true, PARK_CORRIDOR)) {
                    nearParkingSites.add(park);
                }
            }
        }

        return nearParkingSites;

    }

    private boolean showRouteInfo(List<Route> routes) {
        List<LatLng> latlngs = new ArrayList<>();
        mRouteValues = new HashMap<>();
        routeDetails = new ArrayList<>();
        String routeDuration, routeDistance;
        if (!routes.isEmpty() && routes.size() > 0) {
            int count = 0;
            for (Route route : routes) {
                String polyLine = route.getOverviewPolyline().getPoints();
                latlngs = decode(polyLine);
                routeDistance = route.getLegs().get(0).getDistance().getText();
                routeDuration = getDurationInfo(route.getLegs().get(0).getDuration().getText());
                String infos = routeDuration + " " + routeDistance;
                routeDetails.add(infos);
                mRouteValues.put(route, infos);
                if (count == 0) {//fastest route, green color
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(latlngs)
                            .width(ROUTE_WIDTH)
                            .color(Color.GREEN)
                            .geodesic(true));
                } else {//alternative routes, gray colors
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(latlngs)
                            .width(ROUTE_WIDTH)
                            .color(Color.GRAY)
                            .geodesic(true));
                }
                count++;
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()//position camera on first route
                    .target(latlngs.get(0))
                    .zoom(MAP_CAMERA_ZOOM)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            return true;

        } else {
            mMapsView.OnRouteFetchErrorMessage();
            return false;
        }
    }

    private String getDurationInfo(String duration) { //convert format (ex:2 hours 30 minutes to 02:30h)
        String[] splitDuration = duration.split(" ");
        String stringFormat = "";
        if (splitDuration.length == 2) {
            if (splitDuration[0].length() == 1) {
                splitDuration[0] = "0" + splitDuration[0];
            }
            stringFormat = "00:" + splitDuration[0] + "h";
        } else {
            if (splitDuration[1].equals("day")) {
                stringFormat = splitDuration[0] + "d " + splitDuration[2] + "h";
            } else {
                if (splitDuration[2].length() == 1) {
                    splitDuration[2] = "0" + splitDuration[2];
                }
                stringFormat = splitDuration[0] + ":" + splitDuration[2] + "h";
            }
        }
        return stringFormat;
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
                    .zoom(MAP_CAMERA_ZOOM)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void highlightPickedRoute(int routeIndex) {
        String details = "";
        mMap.clear();
        List<Route> pickedRoute = new ArrayList<>();
        Boolean addRoute = false;

        switch (routeIndex) {
            case 1:
                //first route chosen
                if (routeDetails.size() > 0) {//avoid out of bound index exc
                    details = routeDetails.get(0);
                    addRoute = true;
                }
                break;
            case 2:
                //second route chosen
                if (routeDetails.size() > 1) {//avoid out of bound index exc
                    details = routeDetails.get(1);
                    addRoute = true;
                }
                break;
            case 3:
                //third route chosen
                if (routeDetails.size() > 2) {//avoid out of bound index exc
                    details = routeDetails.get(2);
                    addRoute = true;
                }
                break;
        }
        if (addRoute) {
            for (Map.Entry<Route, String> route : mRouteValues.entrySet()) {
                if (route.getValue().equals(details)) {
                    pickedRoute.add(route.getKey());
                }
            }

            redrawRoutes(pickedRoute);
        }
    }

    private void redrawRoutes(List<Route> pickedRoute) {

        for (Route route : mRouteInfo) {
            if (route.equals(pickedRoute.get(0))) {
                List<LatLng> listLatLngSelectedRoute = decode(pickedRoute.get(0).getOverviewPolyline().getPoints());
                mMap.addPolyline(new PolylineOptions()
                        .addAll(listLatLngSelectedRoute)
                        .width(ROUTE_WIDTH)
                        .color(Color.GREEN)
                        .geodesic(true));
            } else {
                List<LatLng> listLatLngSelectedRoute = decode(route.getOverviewPolyline().getPoints());
                mMap.addPolyline(new PolylineOptions()
                        .addAll(listLatLngSelectedRoute)
                        .width(ROUTE_WIDTH)
                        .color(Color.GRAY)
                        .geodesic(true));
            }
        }

        if (!mParkingSites.isEmpty() && mParkingSites != null) {
            List<ParkingSite> nearParkingSites = showOnCorridorParkingSites(pickedRoute);
            showParkingsSites(nearParkingSites);
        }

    }

    public boolean checkInputText(String txtFrom, String txtTo) {
        if (!txtFrom.isEmpty() && !txtTo.isEmpty())
            return true;
        else
            return false;
    }
}
