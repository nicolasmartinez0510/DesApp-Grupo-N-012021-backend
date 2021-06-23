package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.service.ReviewCacheService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableScheduling
class CacheManagerScheduling {

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var reviewCacheService: ReviewCacheService

    //scheduler works after 5 seconds app starts, and redo work every hour
    @Scheduled(initialDelay = 5000, fixedDelay = 3600000)
    fun schedulingReviews() {
        reviewService
            .allContentsBasicInfo()
            .forEach {
                reviewCacheService.saveContentInCache(it)
            }
    }
}