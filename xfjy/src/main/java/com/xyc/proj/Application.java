/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xyc.proj.utility.Properties;

/**
 * 幸福家缘服务启动类
 * @author xieyunchao
 *
 */
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.xyc.proj.repository")
@EnableConfigurationProperties({Properties.class}) 
@EnableScheduling
public class Application extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		//app.addListeners(new ApplicationStartup());
		app.run(args);
		//SpringApplication.run(Application.class, args);
	}
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
        org.springframework.boot.context.embedded.MultipartConfigFactory factory = new MultipartConfigFactory();  
        factory.setMaxFileSize("10000KB");  
        factory.setMaxRequestSize("10000KB");  
        return factory.createMultipartConfig();  
    }  
	
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
