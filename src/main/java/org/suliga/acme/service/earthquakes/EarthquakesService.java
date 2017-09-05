package org.suliga.acme.service.earthquakes;

import java.util.List;

import org.suliga.acme.model.earthquake.EarthquakeFeature;

public interface EarthquakesService {
	String getRawJson();
	String getFormattedJson();
	List<EarthquakeFeature> getEarthquakeFeatures();
}
