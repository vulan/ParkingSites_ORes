package parkingsites.android.com.parkingsites.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nora on 20-Mar-18.
 */

public class ParkingSite {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
