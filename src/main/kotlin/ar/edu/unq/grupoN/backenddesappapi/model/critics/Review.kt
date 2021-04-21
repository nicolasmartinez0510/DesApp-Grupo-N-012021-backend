package ar.edu.unq.grupoN.backenddesappapi.model.critics

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class Review(
    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    open var cinematographicContent: CinematographicContent,
    open var resumeText: String,
    open var text: String,
    open var rating: Rating,
    open var date: LocalDateTime,
    open var platform: String,
    open var isAChapterReview: IsAChapterReview,
    open var seasonNumber: Int?,
    open var episodeNumber: Int?,
    open var valorations:Int = 0
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
    //    fun rate(rate : varorations){
//        if(rate.equals(valorations.LIKE)) {
//            like()
//        } else { dislike() }
//    }
//
//    private fun like() {
//        this.open varorations += 1
//    }
//
//    private fun dislike(){
//        if (this.valorations != 0) {
//            this.open varorations -= 1
//        }
//    }
//
}

