package parkingsites.android.com.parkingsites.view;

/**
 * Created by Nora on 21-Mar-18.
 */

public interface MapsView {
    void OnParksErrorMessage();
    void OnRouteFetchErrorMessage();
    void showPorgressBar();
    void hideProgressBar();
    void showSearchBox();
}

