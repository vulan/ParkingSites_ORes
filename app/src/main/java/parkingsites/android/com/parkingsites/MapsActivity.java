package parkingsites.android.com.parkingsites;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import javax.inject.Inject;
import parkingsites.android.com.parkingsites.dagger.App;
import parkingsites.android.com.parkingsites.presenter.MapsPresenter;
import parkingsites.android.com.parkingsites.view.MapsView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {

    private GoogleMap mMap;
    private Button btnRouteDirection;
    private EditText txtFrom, txtTo;
    private ConstraintLayout srchRouteBox;
    private ProgressBar prgBarSearch;

    @Inject
    MapsPresenter mMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ((App)getApplication()).getAppComponent().inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtFrom = (EditText) findViewById(R.id.txtFrom);
        txtTo = (EditText) findViewById(R.id.txtTo);
        btnRouteDirection = (Button)findViewById(R.id.btnRouteInfo);
        srchRouteBox = findViewById(R.id.searchRouteBox);
        prgBarSearch = findViewById(R.id.prgBarSearch);
    }

    public void getRouteInfo(View view){
        mMapPresenter.getRouteInfo(txtFrom.getText().toString(), txtTo.getText().toString(), getResources().getString(R.string.google_maps_key), true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapPresenter.getParkingSites(this, mMap);
    }


    @Override
    public void OnParksErrorMessage() {
        Toast.makeText(this, getResources().getString(R.string.parking_request_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnRouteFetchErrorMessage() {
        Toast.makeText(this, getResources().getString(R.string.route_request_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPorgressBar() {
        prgBarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        prgBarSearch.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSearchBox() {
        srchRouteBox.setVisibility(View.VISIBLE);
    }


}
