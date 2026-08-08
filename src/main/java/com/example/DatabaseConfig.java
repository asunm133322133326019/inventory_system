package com.example;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public HikariDataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        
        System.out.println("==========================================================");
        System.out.println("[DatabaseConfig] Initializing custom DataSource bean...");
        System.out.println("[DatabaseConfig] Original properties URL: " + url);
        System.out.println("[DatabaseConfig] Original properties Username: " + properties.getUsername());
        
        // If the URL is empty or null, check if DATABASE_URL env var is set
        if (url == null || url.trim().isEmpty()) {
            url = System.getenv("DATABASE_URL");
            System.out.println("[DatabaseConfig] URL was empty, fetched from DATABASE_URL env: " + url);
        }
        
        String username = properties.getUsername();
        String password = properties.getPassword();

        if (url != null && !url.trim().isEmpty()) {
            try {
                String cleanUrl = url.trim();
                // Handle standard postgres:// or postgresql:// URIs
                if (cleanUrl.startsWith("postgres://") || cleanUrl.startsWith("postgresql://")) {
                    System.out.println("[DatabaseConfig] Parsing postgres:// or postgresql:// URI format...");
                    URI uri = new URI(cleanUrl);
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":", 2);
                        username = parts[0];
                        password = parts[1];
                    }
                    
                    String host = uri.getHost();
                    int port = uri.getPort();
                    String path = uri.getPath();
                    String query = uri.getQuery();
                    
                    StringBuilder sb = new StringBuilder("jdbc:postgresql://");
                    sb.append(host);
                    if (port != -1) {
                        sb.append(":").append(port);
                    } else {
                        sb.append(":5432");
                    }
                    sb.append(path);
                    if (query != null) {
                        sb.append("?").append(query);
                    } else {
                        sb.append("?sslmode=require");
                    }
                    url = sb.toString();
                } 
                // Handle jdbc:postgresql:// with inline credentials
                else if (cleanUrl.startsWith("jdbc:postgresql://") && cleanUrl.contains("@")) {
                    System.out.println("[DatabaseConfig] Parsing jdbc:postgresql:// with inline credentials...");
                    String standardUriStr = cleanUrl.substring(5); // strip "jdbc:"
                    URI uri = new URI(standardUriStr);
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":", 2);
                        username = parts[0];
                        password = parts[1];
                    }
                    
                    String host = uri.getHost();
                    int port = uri.getPort();
                    String path = uri.getPath();
                    String query = uri.getQuery();
                    
                    StringBuilder sb = new StringBuilder("jdbc:postgresql://");
                    sb.append(host);
                    if (port != -1) {
                        sb.append(":").append(port);
                    } else {
                        sb.append(":5432");
                    }
                    sb.append(path);
                    if (query != null) {
                        sb.append("?").append(query);
                    } else {
                        sb.append("?sslmode=require");
                    }
                    url = sb.toString();
                }
            } catch (Exception e) {
                System.err.println("[DatabaseConfig] Error parsing connection URL: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("[DatabaseConfig] Final configured JDBC URL: " + url);
        System.out.println("[DatabaseConfig] Final configured Username: " + username);
        System.out.println("==========================================================");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        if (properties.getDriverClassName() != null) {
            dataSource.setDriverClassName(properties.getDriverClassName());
        } else {
            dataSource.setDriverClassName("org.postgresql.Driver");
        }
        return dataSource;
    }
}
