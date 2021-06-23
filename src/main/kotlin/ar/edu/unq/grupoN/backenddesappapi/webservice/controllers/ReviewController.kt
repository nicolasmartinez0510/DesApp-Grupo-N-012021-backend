package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.aspect.Authorize
import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewCacheService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@ServiceREST
@RequestMapping("/api/review")
@EnableAutoConfiguration
class ReviewController {

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var reviewCacheService: ReviewCacheService

    @ApiOperation(
        value = "Create a new review about a specific content. Check Models section to know all kinds of reviews types and structures. " +
                "Aditionally, you must insert property '@type' on the review object, with value 'PUBLIC' or 'PREMIUM'." +
                " Try this example: " +
                "{ \n" +
                "\"@type\":\"PUBLIC\", \n" +
                "\"platform\":\"PLEX\", \n" +
                "\"language\":\"ENGLISH\", \n" +
                "\"resumeText\":\"I love this movie.\", \n" +
                "\"text\":\"Is the best in the world, the true masterpiece!!\",\n" +
                "\"rating\": 5.0,\n" +
                "\"date\":\"2020-05-23T14:43:39.354\",\n" +
                "\"reviewType\":\"MOVIE\",\n" +
                "\"includeSpoiler\":true,\n" +
                "\"userId\":\"nicodyj001\",\n" +
                "\"username\":\"chester.oide\",\n" +
                "\"geographicLocation\":\"ARGENTINA\"\n" +
                "}"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Review created succesfully"),
            ApiResponse(code = 400, message = "Bad request in fields")]
    )
    @Authorize
    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addReview(
        @ApiParam(value = "id of content who you want to review", example = "GladiatorID", required = true)
        @RequestParam
        titleId: String,
        @Valid @RequestBody review: ReviewDTO
    ): ResponseEntity<*>? {
        return ResponseEntity.ok(reviewService.saveReview(titleId, review))
    }

    @ApiOperation(
        value = "Rate a review. If the same user from the same platform rate a review more than one time," +
                "his/him valoration was the last who he/she sent."
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Review rated succesfully"),
            ApiResponse(code = 400, message = "Bad request in fields")]
    )
    @RequestMapping(value = ["/rate/{reviewId}"], method = [RequestMethod.POST])
    @Authorize
    fun rate(
        @ApiParam(value = "id of review who you want to report", example = "1", required = true)
        @PathVariable reviewId: Long,
        @Valid @RequestBody valorationDTO: ValorationDTO
    ): ResponseEntity<*>? {
        return ResponseEntity.ok(reviewService.rate(reviewId, valorationDTO))

    }

    @RequestMapping(value = ["/report/{reviewId}"], method = [RequestMethod.POST])
    @ApiOperation(
        value = "Report a review. If the same user from the same platform rate a review more than one time," +
                "his/him report was the last who he/she sent."
    )
    @Authorize
    fun report(
        @ApiParam(value = "id of review who you want to report", example = "1", required = true)
        @PathVariable reviewId: Long,
        @Valid @RequestBody reportDTO: ReportDTO
    ): ResponseEntity<*>? {
        return ResponseEntity.ok(reviewService.report(reviewId, reportDTO))
    }

    @ApiOperation(value = "Search reviews on a specific titleId content with filters.")
    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    @Authorize
    fun getReviewsFrom(
        @ApiParam(value = "wanted content's titleId", example = "GladiatorID", required = true)
        @RequestParam
        titleId: String,
        @ApiParam(value = "review platform", example = "NETFLIX")
        @RequestParam
        platform: Platform? = null,
        @ApiParam(value = "review has or not a spoiler", example = "false")
        @RequestParam
        includeSpoiler: Boolean? = null,
        @ApiParam(value = "if you want publics, premiums or both review types", example = "PUBLIC")
        @RequestParam
        type: WantedReview?,
        @ApiParam(value = "review language", example = "ENGLISH")
        @RequestParam
        language: Language?,
        @ApiParam(value = "from where u want review from", example = "ARGENTINA")
        @RequestParam
        geographicLocation: Country?,
        @ApiParam(value = "content review specific type", example = "MOVIE")
        @RequestParam
        contentType: ReviewType?,
        @ApiParam(value = "only use if you want a specific chapter review")
        @RequestParam
        seasonNumber: Int?,
        @RequestParam
        @ApiParam(value = "only use if you want a specific chapter review")
        episodeNumber: Int?,
        @RequestParam
        @ApiParam(value = "if you want who reviews ordered by rating, by default is true")
        orderByDate: Boolean = true,
        @RequestParam
        @ApiParam(value = "if you want who reviews ordered by date, by default is true")
        orderByRating: Boolean = true,
        @RequestParam
        @ApiParam(value = "the order type, by default is DESC")
        order: Sort?,
        @RequestParam
        @ApiParam(value = "the required page, by default is the first page", example = "0")
        page: Int
    ): ResponseEntity<*>? {
        val applicableFilters =
            ApplicableFilters(
                platform = platform?.toString(),
                includeSpoiler, type = type?.toString(),
                language = language?.toString(), geographicLocation = geographicLocation?.toString(),
                contentType, seasonNumber, episodeNumber, orderByDate = orderByDate, orderByRating,
                order = order?.toString(), page
            )

        return ResponseEntity.ok(reviewService.search(titleId, applicableFilters))
    }

    @ApiOperation(value = "Search a content by filters")
    @RequestMapping(value = ["/searchContent"], method = [RequestMethod.GET])
    @Authorize
    fun searchContent(
        @RequestParam
        @ApiParam(value = "Select a content average rating")
        contentRating: Double?,
        @RequestParam
        @ApiParam(value = "If you want well or badly valued reviews")
        wellValued: Boolean?,
        @RequestParam
        @ApiParam(value = "A desired genre content")
        genre: String?,
        @RequestParam
        @ApiParam(value = "A wanted decade. It must be equal or greather than 1900")
        decade: Int?,
        @RequestParam
        @ApiParam(value = "If the desired content if for adults")
        isAdultContent: Boolean?,
        @RequestParam
        @ApiParam(value = "A desired cast member in a content")
        searchCastMember: String?,
        @RequestParam
        @ApiParam(value = "A desired job in a content, by default, we search only the cast member. If you don't insert a cast member, this filter is obsolete")
        jobInContent: Employment?,
    ): ResponseEntity<*>? {
        val reverseSearchFilter = ReverseSearchFilter(
            contentRating, wellValued, genre, decade, isAdultContent, searchCastMember, jobInContent
        )
        return ResponseEntity.ok(reviewService.findContentBy(reverseSearchFilter))
    }

    @ApiOperation(value = "Find basic information from a content with high performance.")
    @RequestMapping(value = ["/performed-search-content"], method = [RequestMethod.GET])
    @Authorize
    fun performantContentSearch(
        @ApiParam(example = "GladiatorID")
        @RequestParam titleId: String
    ): ResponseEntity<*>? {

        return ResponseEntity.ok(reviewCacheService.obtain(titleId))
    }
}