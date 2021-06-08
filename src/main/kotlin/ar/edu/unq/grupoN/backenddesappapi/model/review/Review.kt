package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class Review(contentInfo: ContentInfo, reviewInfo: ReviewInfo) {
    @ManyToOne(fetch = FetchType.EAGER)
    open var cinematographicContent: CinematographicContent? = contentInfo.cinematographicContent

    open var platform: String = contentInfo.platform
    open lateinit var reviewType: ReviewType
    open var seasonNumber: Int? = null
    open var episodeNumber: Int? = null
    open lateinit var resumeText: String
    open lateinit var text: String
    open lateinit var date: LocalDateTime
    open lateinit var language : String
    open var valoration: Int = 0
    open lateinit var geographicLocation: String
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var usersWhoValued: MutableSet<ValorationData> = mutableSetOf()
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open val reports: MutableList<Report> = mutableListOf()
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null
    open val isPublic = false
    open val includeSpoiler = false
    open val rating: Double

    init {
        this.seasonNumber = contentInfo.seasonNumber
        this.episodeNumber = contentInfo.episodeNumber

        this.resumeText = reviewInfo.resumeText
        this.text = reviewInfo.text
        this.rating = checkRatingValue(reviewInfo.rating)
        this.date = reviewInfo.date
        this.language = reviewInfo.language
        this.reviewType = reviewInfo.reviewType
        this.geographicLocation = reviewInfo.geographicLocation
    }

    fun rate(userId:String, platform:String, valoration: Valoration){
        val existingValoration = usersWhoValued.firstOrNull { it.isFromUser(userId, platform) }

        if (existingValoration == null) {
            val newValoration = createValoration(userId, platform, valoration)
            usersWhoValued.add(newValoration)
        } else {
            existingValoration.valoration = valoration
        }

        updateValoration()
    }

    fun report(userId: String, userPlatform: String, reportType: ReportType) {
        val existingReport = reports.firstOrNull { it.isFromUser(userId, userPlatform) }
            if ( existingReport == null ) {
                val newReport = Report(userId, userPlatform, reportType)
                reports.add(newReport)
            } else {
                existingReport.reportType = reportType
            }
    }

    fun validate() {
        if (reviewType == ReviewType.SERIE || reviewType == ReviewType.MOVIE){
            episodeNumber = null
            seasonNumber  = null
        }
        this.cinematographicContent?.let { reviewType.validate(it, this) }
    }

    private fun createValoration(userId: String, platform: String, valoration: Valoration) =
        ValorationData(this, userId, platform, valoration)

    private fun updateValoration() {
        valoration = amountOf(Valoration.LIKE) - amountOf(Valoration.DISLIKE)
    }

    private fun amountOf(valoration: Valoration):Int{
        return counter(valoration)
    }

    private fun counter(valorationToFind: Valoration):Int{
        return usersWhoValued.count { it.valoration == valorationToFind }
    }


    private fun checkRatingValue(rating: Double): Double {
        if (rating<0 || rating > 5) throw InvalidReviewRatingException()

        return rating
    }
}


