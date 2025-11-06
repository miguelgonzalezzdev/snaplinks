package com.github.miguelgonzalezzdev.snaplinks.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoIpConfig {

    @Bean
    public DatabaseReader databaseReader() throws IOException {
        try (InputStream dbStream = getClass().getResourceAsStream("/geoip/GeoLite2-City.mmdb")) {
            return new DatabaseReader.Builder(dbStream).build();
        }
    }

}
