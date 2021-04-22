package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
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
    @OneToMany(cascade = [CascadeType.ALL])
    private var valorations: MutableList<ValorationData> = mutableListOf()
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

    fun rate(nick:String,platform:String,email:String,valoration:Valorations){
        valorations.add(ValorationData(id,nick,platform,email,valoration))
    }

    fun count_likes():Int{
        return counter(Valorations.LIKE)
    }

    fun count_dislikes():Int{
        return counter(Valorations.DISLIKE)
    }

    fun valorations():Int{
        return count_likes() - count_dislikes()
    }

    private fun counter(valoration_to_count:Valorations):Int{
        return valorations.count { it.valoration == valoration_to_count }
    }

}


