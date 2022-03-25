package com.dca.dcaapi;

import com.dca.dcaapi.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DcaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcaApiApplication.class, args);
	}

	/*@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.addUrlPatterns("/api/features/*");
		return registrationBean;
	}*/
}
