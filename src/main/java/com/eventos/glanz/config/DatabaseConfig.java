package com.eventos.glanz.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseConfig {

	@Bean
	@Profile("local")
	@Primary
	public DataSource localDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/glanz");
		config.setUsername("root");
		config.setPassword("root");
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return new HikariDataSource(config);
	}
	
	@Bean
	@Profile("prod")
	@Primary
	public DataSource azureDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlserver://glanzdatabase.database.windows.net:1433;database=Glanz;user=glanzADM@glanzdatabase;password=GLAnz@123;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
		config.setUsername("glanzADM");
		config.setPassword("GLAnz@123");
		config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return new HikariDataSource(config);
	}
}


