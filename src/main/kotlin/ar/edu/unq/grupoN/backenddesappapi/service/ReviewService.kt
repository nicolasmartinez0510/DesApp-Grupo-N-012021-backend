package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReviewService{

    @Autowired
    private lateinit var repository: ReviewRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Transactional
    fun saveReview(titleId: String, review: Review): Review {
        val content = contentRepository.findById(titleId).get()
        review.cinematographicContent = content

        validateReview(review)

        return repository.save(review)
    }

    @Transactional
    fun update(review: Review){
        repository.save(review)
    }

    @Transactional
    fun findReviewsBy(titleId:String): List<Review>{
        return repository.findByCinematographicContentTitleIdOrderByValorationSumDesc(titleId)
    }


    @Transactional
    fun findById(id: Long): Optional<Review> {
        return repository.findById(id)
    }


    @Transactional
    fun addFakeReview(review: Review): Review {

        return repository.save(review)
    }

    @Transactional
    fun findAll(): MutableIterable<Review> = repository.findAll()

    private fun validateReview(review: Review) = review.validate()
}
