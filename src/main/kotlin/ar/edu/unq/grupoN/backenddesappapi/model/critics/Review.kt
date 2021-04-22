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
    open var cinematographicContent: CinematographicContent,
    open var resumeText: String,
    open var text: String,
    open var rating: Rating,
    open var date: LocalDateTime,
    open var platform: String,
    open var isAChapterReview: IsAChapterReview,
    open var seasonNumber: Int?,
    open var episodeNumber: Int?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
}

