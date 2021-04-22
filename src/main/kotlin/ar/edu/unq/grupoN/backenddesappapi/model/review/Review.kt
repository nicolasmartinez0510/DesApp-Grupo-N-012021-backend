package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class Review(contentInfo: ContentInfo, reviewInfo: ReviewInfo) {
    @ManyToOne(fetch = FetchType.EAGER)
    open lateinit var cinematographicContent: CinematographicContent
    open lateinit var platform: String
    open lateinit var isAChapterReview: IsAChapterReview
    open var seasonNumber: Int? = null
    open var episodeNumber: Int? = null
    open lateinit var resumeText: String
    open lateinit var text: String
    open lateinit var rating: Rating
    open lateinit var date: LocalDateTime
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    init {
        this.cinematographicContent = contentInfo.cinematographicContent
        this.platform = contentInfo.platform
        this.seasonNumber = contentInfo.seasonNumber
        this.episodeNumber = contentInfo.episodeNumber

        this.resumeText = reviewInfo.resumeText
        this.text = reviewInfo.text
        this.rating = reviewInfo.rating
        this.date = reviewInfo.date
        this.isAChapterReview = reviewInfo.isAChapterReview
    }
}

