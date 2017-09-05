package org.suliga.acme.model.earthquake;

import java.util.Map;

public class EarthquakeFeature {
	private String type;
	private Map<String, Object> properties;
	private Map<String, Object> geometry;
	private String id;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	public Map<String, Object> getGeometry() {
		return geometry;
	}
	public void setGeometry(Map<String, Object> geometry) {
		this.geometry = geometry;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

