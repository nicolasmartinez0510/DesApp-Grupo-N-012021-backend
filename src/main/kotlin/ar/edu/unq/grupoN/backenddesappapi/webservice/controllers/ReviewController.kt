package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
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

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addReview(@RequestBody createReviewRequest: CreateReviewRequest): ResponseEntity<*>? {
        val myReview = createReviewRequest.reviewToCreate.toModel()

        try{
            reviewService.saveReview(createReviewRequest.titleId, myReview)
        } catch (e:RuntimeException){

           return createExceptionResponse(e)
        }

        return ResponseEntity.ok().body(ReviewDTO.fromModel(myReview))
    }

    @RequestMapping(value = ["/rate/{reviewId}"], method = [RequestMethod.POST])
    fun rate(@PathVariable reviewId: Long, @RequestBody valorationDTO: ValorationDTO): ResponseEntity<*>?{
        val review = try{
            reviewService.findById(reviewId).get()
        } catch (e:NoSuchElementException){

            return createExceptionResponse(e)
        }
        review.rate(valorationDTO.userId,valorationDTO.platform,valorationDTO.valoration)
        reviewService.update(review = review)

        return ResponseEntity.ok().body(ReviewDTO.fromModel(review))

    }

    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun getReviewsFrom(@RequestParam titleId:String):ResponseEntity<*>? {
        val reviewsResult = reviewService.findReviewsBy(titleId).map { ReviewDTO.fromModel(it) }

        return ResponseEntity.ok().body(reviewsResult)
    }

    @GetMapping
    fun reviews(): ResponseEntity<*>? {
        val reviews = reviewService.findAll().map { review -> ReviewDTO.fromModel(review) }

        return ResponseEntity.ok().body(reviews)
    }

    private fun createExceptionResponse(e: Exception): ResponseEntity<MutableMap<String, String>> {
        val exceptionResponse = mutableMapOf<String, String>()
        exceptionResponse["type"]      = e::class.simpleName.toString()
        exceptionResponse["message"] = e.message.toString()

        return ResponseEntity.badRequest().body(exceptionResponse)
    }

}

data class CreateReviewRequest(val titleId: String, val reviewToCreate: ReviewDTO)