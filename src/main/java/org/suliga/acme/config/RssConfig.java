package org.suliga.acme.config;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.suliga.acme.model.rss.Channel;
import org.suliga.acme.model.rss.Item;
import org.suliga.acme.model.rss.Rss;

@Configuration
public class RssConfig {
    @SuppressWarnings("serial")
	@Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[]{
           //all the classes the context needs to know about
           Rss.class,
           Channel.class,
           Item.class
        }); //"alternatively" setContextPath(<jaxb.context>), 

        marshaller.setMarshallerProperties(new HashMap<String, Object>() {{
          put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }});

        return marshaller;
    }
}

