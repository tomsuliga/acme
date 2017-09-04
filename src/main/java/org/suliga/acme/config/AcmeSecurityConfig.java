package org.suliga.acme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AcmeSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity hs) throws Exception {
		hs.authorizeRequests()
			.antMatchers("/", "/index", "/home", "/resources/**").permitAll();
		
		//hs.csrf().disable();
		
		// add this line to use H2 web console
	    hs.headers().frameOptions().disable();
	   	hs.csrf().ignoringAntMatchers("/h2-console/**", "/console/**");
	}

}
