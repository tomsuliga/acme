package org.suliga.acme.service.rss;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.suliga.acme.model.rss.Rss;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RssServiceImpl implements RssService {
	private static final Logger logger = LoggerFactory.getLogger(Rss.class);
	
	@Autowired
	private Jaxb2Marshaller marshaller;
	
	public <T> String marshallXml(final T obj) throws JAXBException {
		StringWriter sw = new StringWriter();
		Result result = new StreamResult(sw);
		marshaller.marshal(obj, result);
		return sw.toString();
	}

	// (tries to) unmarshall(s) an InputStream to the desired object.
	@SuppressWarnings("unchecked")
	public <T> T unmarshallXml(final InputStream xml) throws JAXBException {
		return (T) marshaller.unmarshal(new StreamSource(xml));
	}
	
	@Override
	public String getArticleSummaries() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			RestTemplate restTemplate = new RestTemplate();
			String summaryXml = restTemplate.getForObject("https://www.theguardian.com/world/rss", String.class);
			//InputStream is = new ByteArrayInputStream(summaryXml.getBytes(StandardCharsets.UTF_8.name()));
	        //Rss rss = (Rss) jaxbUnmarshaller.unmarshal(is);
			javax.xml.transform.Source source = new StreamSource(new StringReader(summaryXml));
			Rss rss = (Rss) marshaller.unmarshal(source);
	        logger.info(rss.toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		/*
		RestTemplate restTemplate = new RestTemplate();
		String summaryXml = restTemplate.getForObject("https://www.theguardian.com/world/rss", String.class);
		ObjectMapper om = new ObjectMapper();
		try {
			Rss rss = om.readValue(summaryXml, Rss.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return "TODO 2";
	}
}

