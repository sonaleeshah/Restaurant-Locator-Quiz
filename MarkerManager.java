package ca.ubc.cs.cpsc210.quiz.activity;

import android.view.ViewDebug;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * Manager for markers plotted on map
 */
public class MarkerManager {
    private GoogleMap map;
    private List<Marker> allMarkers;

    /**
     * Constructor initializes manager with map for which markers are to be managed.
     * @param map  the map for which markers are to be managed
     */
    public MarkerManager(GoogleMap map) {
        this.map = map;
        allMarkers = new ArrayList<Marker>();
    }

    /**
     * Get last marker added to map
     * @return  last marker added
     */
    public Marker getLastMarker() {
        return allMarkers.get(allMarkers.size());
    }

    /**
     * Add green marker to show position of restaurant
     * @param point   the point at which to add the marker
     * @param title   the marker's title
     */
    public void addRestaurantMarker(LatLng point, String title) {
       removeMarkers();

       Marker m = map.addMarker(
               new MarkerOptions()
                       .position(point)
                       .title(title)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                       .alpha(0.7f));

    }

    /**
     * Add a marker to mark latest guess from user.  Only the most recent two positions selected
     * by the user are marked.  The distance from the restaurant is used to create the marker's title
     * of the form "xxxx m away" where xxxx is the distance from the restaurant in metres (truncated to
     * an integer).
     *
     * The colour of the marker is based on the distance from the restaurant:
     * - red, if the distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param latLng
     * @param distance
     */
    public void addMarker(LatLng latLng, double distance) {
        Marker m = map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(Integer.toString((int)(distance)) + "m away")
                        .icon(BitmapDescriptorFactory.defaultMarker(getColour(distance)))
                        .alpha(0.7f)
        );

        allMarkers.add(m);


        while (allMarkers.size() > 2) {
            allMarkers.get(0).remove();
            allMarkers.remove(allMarkers.get(0));
        }
    }

    /**
     * Remove markers that mark user guesses from the map
     */
    public void removeMarkers() {
        for (Marker m : allMarkers){
            m.remove();
        }
    }

    /**
     * Produce a colour on a linear scale between red and green based on distance:
     *
     * - red, if distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     * @param distance  distance from restaurant
     * @return  colour of marker
     */
    private float getColour(double distance) {
        if (distance>2000){
            return BitmapDescriptorFactory.HUE_RED;
        } else if (distance>1000){
            return 15.0f;
        } else if (distance>500){
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (distance>50){
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }
}