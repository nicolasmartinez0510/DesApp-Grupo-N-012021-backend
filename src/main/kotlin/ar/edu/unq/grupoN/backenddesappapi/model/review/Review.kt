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
    @ManyToOne(fetch = FetchType.LAZY)
    open lateinit var cinematographicContent: CinematographicContent
    open lateinit var platform: String
    open lateinit var isAChapterReview: IsAChapterReview
    open var seasonNumber: Int? = null
    open var episodeNumber: Int? = null
    open lateinit var resumeText: String
    open lateinit var text: String
    open lateinit var rating: Rating
    open lateinit var date: LocalDateTime
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    open var valorations: MutableList<ValorationData> = mutableListOf()
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

    fun rate(userId:String, platform:String, valoration:Valoration){
        val existingValoration: ValorationData? = valorations.firstOrNull {
                aValoration -> aValoration.isFromUser(userId, platform)
        }
        if (existingValoration == null) {
            val newValoration = createValoration(userId, platform, valoration)
            valorations.add(newValoration)
        } else {
            existingValoration.valoration = valoration
        }
    }

    fun amountOf(valoration: Valoration):Int{
        return counter(valoration)
    }

    fun valoration():Int{
        return amountOf(Valoration.LIKE) - amountOf(Valoration.DISLIKE)
    }

    private fun createValoration(userId: String, platform: String, valoration: Valoration) =
        ValorationData(this, userId, platform, valoration)

    private fun counter(valoration_to_count:Valoration):Int{
        return valorations.count { it.valoration == valoration_to_count }
    }
}


