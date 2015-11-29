/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xyc.proj.global.ApplicationStartup;
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
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
