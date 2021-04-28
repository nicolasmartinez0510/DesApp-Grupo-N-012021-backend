package ar.edu.unq.grupoN.backenddesappapi.service


import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

class ReviewService(val repository: ReviewRepository, val contentRepository: CinematographicContentRepository) {

    @Transactional
    fun saveReview(titleId: String, review: Review): Review {
        //TODO: verificaciones
        val content = contentRepository.findById(titleId).get()
        review.cinematographicContent = content

        return repository.save(review)
    }

    @Transactional
    fun update(review: Review){
        repository.save(review)
    }


    // Rename
    @Transactional
    fun getReviewsWithContentId(contentId:String): List<Review>{
        return repository.findAll().filter {
            it.cinematographicContent!!.titleId.equals(contentId)
        }
    }


    @Transactional
    fun findById(id: Long): Optional<Review> {
        return repository.findById(id)
    }

    @Transactional
    fun addFakeReview(review: Review): Review {

        return repository.save(review)
    }

    fun findAll() = repository.findAll()
}