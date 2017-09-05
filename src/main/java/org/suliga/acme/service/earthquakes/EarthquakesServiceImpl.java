package org.suliga.acme.service.earthquakes;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.suliga.acme.model.earthquake.EarthquakeEvent;
import org.suliga.acme.model.earthquake.EarthquakeFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EarthquakesServiceImpl implements EarthquakesService {
	private static final Logger logger = LoggerFactory.getLogger(EarthquakesServiceImpl.class);
	private static final String MONTHLY_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
	private String monthlyJson;
	private EarthquakeEvent earthquakeEvent;

	private void loadMonthlyJson() {
		RestTemplate restTemplate = new RestTemplate();
		monthlyJson = restTemplate.getForObject(MONTHLY_URL, String.class);
		ObjectMapper om = new ObjectMapper();
		try {
			earthquakeEvent = om.readValue(monthlyJson, EarthquakeEvent.class);
			logger.info("****** event features size = " + earthquakeEvent.getFeatures().size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<EarthquakeFeature> getEarthquakeFeatures() {
		if (monthlyJson == null) {
			loadMonthlyJson();
		}
		
		return earthquakeEvent.getFeatures();
	}

	@Override
	public String getRawJson() {
		if (monthlyJson == null) {
			loadMonthlyJson();
		}
		return monthlyJson;
	}

	@Override
	public String getFormattedJson() {
		if (monthlyJson == null) {
			loadMonthlyJson();
		}
		
		StringBuilder sb = new StringBuilder();
		int len = monthlyJson.length();
		int indent = 0;
		char ch;
		String BR = "<br>";
		boolean prevColon = false;
		boolean withinQuotes = false;
		
		for (int i=0;i<len;i++) {
			ch = monthlyJson.charAt(i);
			if (ch == '{') {
				if (!prevColon) {
					sb.append(BR);
				}
				sb.append(ch);
				sb.append(BR);
				indent++;
				insertSpaces(sb, indent);
			} else if (ch == ',') {
				sb.append(ch);
				if (!withinQuotes) {
					sb.append(BR);
					insertSpaces(sb, indent);
				}
			} else if (ch == '}') {
				sb.append(BR);
				indent--;
				insertSpaces(sb, indent);
				sb.append(ch);
			} else {
				sb.append(ch);
				if (ch == ':') {
					prevColon = true;
				} else {
					prevColon = false;
				}
				if (ch == '"') {
					withinQuotes = !withinQuotes;
				} 
			}
		}
		
		return sb.toString();
	}
	
	private void insertSpaces(StringBuilder sb, int indent) {
		while (indent > 0) {
			sb.append("&nbsp;");
			sb.append("&nbsp;");
			sb.append("&nbsp;");
			sb.append("&nbsp;");
			indent--;
		}
	}
}
