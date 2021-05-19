package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@ServiceREST
@RequestMapping("/api/review")
@EnableAutoConfiguration
class ReviewController {

    @Autowired
    private lateinit var reviewService: ReviewService

    @ApiOperation(
        value = "Create a new review about a specific content. Check Models section to know all kinds of reviews types and structures. " +
                "Aditionally, you must insert property '@type' on the review object, with value 'PUBLIC' or 'PREMIUM'." +
                " Try this example: {\n" +
                "\n" +
                "  \"titleId\": \"GladiatorID\",\n" +
                "  \"reviewToCreate\": {\n" +
                "\"@type\":\"PUBLIC\",\n" +
                "\"platform\":\"PLEX\",\n" +
                "\"language\":\"ENGLISH\",\n" +
                "\"resumeText\":\"I love this movie.\",\n" +
                "\"text\":\"Is the best in the world, the true masterpiece!!\",\n" +
                "\"rating\": 5.0,\n" +
                "\"date\":\"2016-05-23T14:43:39.354\",\n" +
                "\"reviewType\":\"MOVIE\",\n" +
                "\"includeSpoiler\":true,\n" +
                "\"userId\":\"chesteroide1\",\n" +
                "\"username\":\"chester.oide\",\n" +
                "\"geographicLocation\":\"ARGENTINA\"} }"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Review created succesfully"),
            ApiResponse(code = 400, message = "Bad request in fields")]
    )
    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addReview(@RequestBody createReviewRequest: CreateReviewRequest): ResponseEntity<*>? {
        val review: ReviewDTO = try {
            reviewService.saveReview(createReviewRequest)
        } catch (e: ReviewTypeException) {
            return ResponseEntity.badRequest().body(createExceptionResponse(e))
        }

        return ResponseEntity.ok(review)
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
    fun rate(
        @ApiParam(value = "id of review who you want to report", example = "1", required = true)
        @PathVariable reviewId: Long,
        @RequestBody valorationDTO: ValorationDTO
    ): ResponseEntity<*>? {
        val review: ReviewDTO = try {
            reviewService.rate(reviewId, valorationDTO)
        } catch (e: NoSuchElementException) {

            return ResponseEntity.badRequest().body(createExceptionResponse(e))
        }

        return ResponseEntity.ok(review)

    }

    @RequestMapping(value = ["/report/{reviewId}"], method = [RequestMethod.POST])
    @ApiOperation(
        value = "Report a review. If the same user from the same platform rate a review more than one time," +
                "his/him report was the last who he/she sent."
    )
    fun report(
        @ApiParam(value = "id of review who you want to report", example = "1", required = true)
        @PathVariable reviewId: Long,
        @RequestBody reportDTO: ReportDTO
    ): ResponseEntity<*>? {
        val review: ReviewDTO = try {
            reviewService.report(reviewId, reportDTO)
        } catch (e: Exception) {

            return ResponseEntity.badRequest().body(createExceptionResponse(e))
        }

        return ResponseEntity.ok(review)

    }

    @ApiOperation(value = "Search reviews on a specific titleId content.")
    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
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
        val reviewsResult = reviewService.search(titleId, applicableFilters)

        return ResponseEntity.ok(reviewsResult)
    }

    @ApiOperation(value = "Endpoint used for api develop. to show generated fake reviews", hidden = true)
    @RequestMapping(value = ["", "/"], method = [RequestMethod.GET])
    fun reviews(): ResponseEntity<*>? {
        return ResponseEntity.ok().body(reviewService.findAll())
    }

    @ApiOperation(value = "reverse search")
    @RequestMapping(value = ["/searchContent"], method = [RequestMethod.GET])
    fun searchContent(
        @RequestParam
        @ApiParam(value = "Select a rating required in a review")
        reviewRating: Double?,
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
            reviewRating, wellValued, genre, decade, isAdultContent, searchCastMember, jobInContent
        )
        return ResponseEntity.ok().body(reviewService.findContentBy(reverseSearchFilter))
    }
}

fun createExceptionResponse(e: Exception): MutableMap<String, String> {
    val exceptionResponse = mutableMapOf<String, String>()
    exceptionResponse["error"] = e::class.simpleName.toString()
    exceptionResponse["message"] = e.message.toString()

    return exceptionResponse
}

data class CreateReviewRequest(
    @ApiModelProperty(value = "titleId", example = "GladiatorID", required = true)
    val titleId: String, val reviewToCreate: ReviewDTO
)