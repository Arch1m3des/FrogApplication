package at.ac.univie.SplitDAO;

import java.io.Serializable;

/**
 * Created by Markus on 27.05.16.
 */
public class MapMarker implements Serializable {

    double lat;
    double lang;
    String description;

    public MapMarker(double lat, double lang, String description){
        this.lat = lat;
        this.lang = lang;
        this.description = description;
    }


    public double getLat(){
        return this.lat;
    }

    public double getLang(){
        return this.lang;
    }

    public String getDescription(){
        return this.description;
    }

}

