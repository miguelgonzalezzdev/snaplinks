package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.UrlAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlAccessLogRepository extends JpaRepository<UrlAccessLog, Long>  {

    @Query("SELECT COUNT(u) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId")
    long countByShortUrlId(@Param("shortUrlId") Long shortUrlId);

    @Query("SELECT MIN(u.accessedAt) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId")
    java.time.LocalDateTime findFirstAccess(@Param("shortUrlId") Long shortUrlId);

    @Query("SELECT MAX(u.accessedAt) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId")
    java.time.LocalDateTime findLastAccess(@Param("shortUrlId") Long shortUrlId);

    @Query("SELECT u.countryName, COUNT(u) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId GROUP BY u.countryName")
    List<Object[]> countByCountry(@Param("shortUrlId") Long shortUrlId);

    @Query("SELECT u.browser, COUNT(u) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId GROUP BY u.browser")
    List<Object[]> countByBrowser(@Param("shortUrlId") Long shortUrlId);

    @Query("SELECT u.deviceType, COUNT(u) FROM UrlAccessLog u WHERE u.shortUrl.id = :shortUrlId GROUP BY u.deviceType")
    List<Object[]> countByDeviceType(@Param("shortUrlId") Long shortUrlId);

}
