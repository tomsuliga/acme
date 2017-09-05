package org.suliga.acme.model.earthquake;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EarthquakeEvent {
	private List<EarthquakeFeature> features;

	public List<EarthquakeFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<EarthquakeFeature> features) {
		this.features = features;
	}
}
