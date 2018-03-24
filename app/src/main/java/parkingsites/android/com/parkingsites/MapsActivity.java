package parkingsites.android.com.parkingsites;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

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
    private Button btnPath1, btnPath2, btnPath3;

    @Inject
    MapsPresenter mMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ((App) getApplication()).getAppComponent().inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initialize();
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

    @Override
    public void showRouteDetails(List<String> details) {
        switch (details.size()) {
            case 1:
                btnPath1.setVisibility(View.VISIBLE);
                btnPath1.setText(details.get(0).toString());
                btnPath2.setVisibility(View.INVISIBLE);
                btnPath3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                btnPath1.setVisibility(View.VISIBLE);
                btnPath1.setText(details.get(0).toString());
                btnPath2.setVisibility(View.VISIBLE);
                btnPath2.setText(details.get(1).toString());
                btnPath3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                btnPath1.setVisibility(View.VISIBLE);
                btnPath1.setText(details.get(0).toString());
                btnPath2.setVisibility(View.VISIBLE);
                btnPath2.setText(details.get(1).toString());
                btnPath3.setVisibility(View.VISIBLE);
                btnPath3.setText(details.get(2).toString());
                break;
            default:
                break;
        }
    }

    private void initialize() {
        txtFrom = (EditText) findViewById(R.id.txtFrom);
        txtTo = (EditText) findViewById(R.id.txtTo);
        btnRouteDirection = (Button) findViewById(R.id.btnRouteInfo);
        srchRouteBox = findViewById(R.id.searchRouteBox);
        prgBarSearch = findViewById(R.id.prgBarSearch);
        btnPath1 = findViewById(R.id.btnPath1);
        btnPath2 = findViewById(R.id.btnPath2);
        btnPath3 = findViewById(R.id.btnPath3);
    }

    public void getRouteInfo(View view) {
        if (mMapPresenter.checkInputText(txtFrom.getText().toString(), txtTo.getText().toString())) {
            mMapPresenter.getRouteInfo(txtFrom.getText().toString(), txtTo.getText().toString(), getResources().getString(R.string.google_maps_key), true);
            refreshControls();
        }
        else
            Toast.makeText(this, getResources().getString(R.string.check_text), Toast.LENGTH_LONG).show();
    }

    private void refreshControls(){
        btnPath1.setBackgroundColor(getResources().getColor(R.color.dark_green));
        btnPath2.setBackgroundColor(getResources().getColor(R.color.gray));
        btnPath3.setBackgroundColor(getResources().getColor(R.color.gray));

        btnPath1.setText("");
        btnPath2.setText("");
        btnPath3.setText("");
    }

    public void clearSearchHistory(View view) {
        txtFrom.setText("");
        txtTo.setText("");
        mMapPresenter.loadParkingSites();
        btnPath1.setVisibility(View.INVISIBLE);
        btnPath2.setVisibility(View.INVISIBLE);
        btnPath3.setVisibility(View.INVISIBLE);
        btnPath1.setText("");
        btnPath2.setText("");
        btnPath3.setText("");
    }

    public void path1(View view) {
        if (!btnPath1.getText().toString().isEmpty()) {
            btnPath1.setBackgroundColor(getResources().getColor(R.color.dark_green));
            btnPath2.setBackgroundColor(getResources().getColor(R.color.gray));
            btnPath3.setBackgroundColor(getResources().getColor(R.color.gray));
            mMapPresenter.highlightPickedRoute(1);
        }
    }

    public void path2(View view) {
        if (!btnPath2.getText().toString().isEmpty()) {
            btnPath1.setBackgroundColor(getResources().getColor(R.color.gray));
            btnPath2.setBackgroundColor(getResources().getColor(R.color.dark_green));
            btnPath3.setBackgroundColor(getResources().getColor(R.color.gray));
            mMapPresenter.highlightPickedRoute(2);
        }
    }

    public void path3(View view) {
        if (!btnPath1.getText().toString().isEmpty()) {
            btnPath1.setBackgroundColor(getResources().getColor(R.color.gray));
            btnPath2.setBackgroundColor(getResources().getColor(R.color.gray));
            btnPath3.setBackgroundColor(getResources().getColor(R.color.dark_green));
            mMapPresenter.highlightPickedRoute(3);
        }
    }


}
