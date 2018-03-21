package parkingsites.android.com.parkingsites.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nora on 20-Mar-18.
 */

public class ParkingSiteSerializable {

    @SerializedName("parking_sites")
    @Expose
    private List<ParkingSite> parkingSites = null;

    public List<ParkingSite> getParkingSites() {
        return parkingSites;
    }

    public void setParkingSites(List<ParkingSite> parkingSites) {
        this.parkingSites = parkingSites;
    }
}
