package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.GeoIpInfo;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.InetAddress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class GeoIpServiceTest {

    @Mock
    private DatabaseReader databaseReader;

    @Mock
    private CityResponse cityResponse;

    @Mock
    private Country country;

    @Mock
    private City city;

    @Mock
    private Subdivision subdivision;

    @InjectMocks
    private GeoIpService geoIpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGeoIpInfo_returnsEmpty_whenIpIsNullOrBlank() {
        assertThat(geoIpService.getGeoIpInfo(null)).isEqualTo(GeoIpInfo.empty());
        assertThat(geoIpService.getGeoIpInfo("")).isEqualTo(GeoIpInfo.empty());
        verifyNoInteractions(databaseReader);
    }

    @Test
    void getGeoIpInfo_returnsEmpty_whenIpIsPrivate() {
        GeoIpInfo result = geoIpService.getGeoIpInfo("127.0.0.1");
        assertThat(result).isEqualTo(GeoIpInfo.empty());
        verifyNoInteractions(databaseReader);
    }

    @Test
    void getGeoIpInfo_returnsGeoInfo_whenIpIsValid() throws Exception {
        InetAddress inetAddress = InetAddress.getByName("8.8.8.8");

        when(databaseReader.city(inetAddress)).thenReturn(cityResponse);
        when(cityResponse.getCountry()).thenReturn(country);
        when(country.getIsoCode()).thenReturn("US");
        when(country.getName()).thenReturn("United States");
        when(cityResponse.getMostSpecificSubdivision()).thenReturn(subdivision);
        when(subdivision.getName()).thenReturn("California");
        when(cityResponse.getCity()).thenReturn(city);
        when(city.getName()).thenReturn("Mountain View");

        GeoIpInfo result = geoIpService.getGeoIpInfo("8.8.8.8");

        assertThat(result.countryIso()).isEqualTo("US");
        assertThat(result.countryName()).isEqualTo("United States");
        assertThat(result.region()).isEqualTo("California");
        assertThat(result.city()).isEqualTo("Mountain View");

        verify(databaseReader).city(inetAddress);
    }

    @Test
    void getGeoIpInfo_returnsEmpty_whenDatabaseReaderThrowsException() throws Exception {
        InetAddress inetAddress = InetAddress.getByName("8.8.8.8");
        when(databaseReader.city(inetAddress)).thenThrow(new IOException("Database error"));

        GeoIpInfo result = geoIpService.getGeoIpInfo("8.8.8.8");

        assertThat(result).isEqualTo(GeoIpInfo.empty());
        verify(databaseReader).city(inetAddress);
    }

    @Test
    void getGeoIpInfo_returnsEmpty_whenIpIsIPv6Local() throws Exception {
        GeoIpInfo result = geoIpService.getGeoIpInfo("fd12:3456:789a:1::1");
        assertThat(result).isEqualTo(GeoIpInfo.empty());
        verifyNoInteractions(databaseReader);
    }
}
