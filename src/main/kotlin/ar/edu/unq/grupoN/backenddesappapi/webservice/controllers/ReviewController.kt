package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
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
                "\"rating\":\"FIVE\",\n" +
                "\"date\":\"2016-05-23T14:43:39.354\",\n" +
                "\"reviewType\":\"MOVIE\",\n" +
                "\"includeSpoiler\":true,\n" +
                "\"userId\":\"chesteroide1\",\n" +
                "\"username\":\"chester.oide\",\n" +
                "\"geographicLocation\":\"Argentina\"} }"
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Review created succesfully"),
            ApiResponse(code = 400, message = "Bad request in fields")]
    )
    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addReview(@RequestBody createReviewRequest: CreateReviewRequest): ResponseEntity<*>? {
        val myReview = createReviewRequest.reviewToCreate.toModel()

        try {
            reviewService.saveReview(createReviewRequest.titleId, myReview)
        } catch (e: RuntimeException) {

            return createExceptionResponse(e)
        }

        return ResponseEntity.ok().body(ReviewDTO.fromModel(myReview))
    }

    @ApiOperation( value = "Rate a review. If the same user from the same platform rate a review more than one time," +
            "his valoration was the last who he/she sent.")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Review rated succesfully"),
            ApiResponse(code = 400, message = "Bad request in fields")]
    )
    @RequestMapping(value = ["/rate/{reviewId}"], method = [RequestMethod.POST])
    fun rate(
        @ApiParam(value = "reviewId", example = "1", required = true)
        @PathVariable reviewId: Long,
        @RequestBody valorationDTO: ValorationDTO): ResponseEntity<*>? {
        val review = try {
            reviewService.findById(reviewId).get()
        } catch (e: NoSuchElementException) {

            return createExceptionResponse(e)
        }
        review.rate(valorationDTO.userId, valorationDTO.platform, valorationDTO.valoration)
        reviewService.update(review = review)

        return ResponseEntity.ok().body(ReviewDTO.fromModel(review))

    }

    @ApiOperation( value = "Search reviews on a specific titleId content.")
    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun getReviewsFrom(
        @ApiParam(value = "titleId", example = "GladiatorID", required = true)
        @RequestParam
        titleId: String
    ): ResponseEntity<*>? {
        val reviewsResult = reviewService.findReviewsBy(titleId).map { ReviewDTO.fromModel(it) }

        return ResponseEntity.ok().body(reviewsResult)
    }

    @ApiOperation(value = "Endpoint used for api develop. to show generated fake reviews", hidden = true)
    @RequestMapping(value = ["","/"], method = [RequestMethod.GET])
    fun reviews(): ResponseEntity<*>? {
        val reviews = reviewService.findAll().map { review -> ReviewDTO.fromModel(review) }

        return ResponseEntity.ok().body(reviews)
    }

    private fun createExceptionResponse(e: Exception): ResponseEntity<MutableMap<String, String>> {
        val exceptionResponse = mutableMapOf<String, String>()
        exceptionResponse["type"] = e::class.simpleName.toString()
        exceptionResponse["message"] = e.message.toString()

        return ResponseEntity.badRequest().body(exceptionResponse)
    }

}

data class CreateReviewRequest(
    @ApiModelProperty(value = "titleId", example = "GladiatorID", required = true)
    val titleId: String, val reviewToCreate: ReviewDTO)