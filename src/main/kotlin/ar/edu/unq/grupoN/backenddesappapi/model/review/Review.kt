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
    open lateinit var rating: Rating
    open lateinit var date: LocalDateTime
    open lateinit var language : String
    open var valorationSum: Int = 0
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    open var usersWhoValued: MutableSet<ValorationData> = mutableSetOf()
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null

    init {
        this.seasonNumber = contentInfo.seasonNumber
        this.episodeNumber = contentInfo.episodeNumber

        this.resumeText = reviewInfo.resumeText
        this.text = reviewInfo.text
        this.rating = reviewInfo.rating
        this.date = reviewInfo.date
        this.language = reviewInfo.language
        this.reviewType = reviewInfo.reviewType
    }

    fun rate(userId:String, platform:String, valoration: Valoration){
        val existingValoration: ValorationData? = usersWhoValued.firstOrNull {
                aValoration -> aValoration.isFromUser(userId, platform)
        }
        if (existingValoration == null) {
            val newValoration = createValoration(userId, platform, valoration)
            usersWhoValued.add(newValoration)
            operationOverValoration(valoration)
        } else {
            checkOperation(existingValoration, valoration)
            existingValoration.valoration = valoration
        }
    }

    fun amountOf(valoration: Valoration):Int{
        return counter(valoration)
    }

    open fun isPublic() = false

    private fun createValoration(userId: String, platform: String, valoration: Valoration) =
        ValorationData(this, userId, platform, valoration)

    private fun counter(valorationToFind: Valoration):Int{
        return usersWhoValued.count { it.valoration == valorationToFind }
    }

    private fun operationOverValoration(valoration: Valoration) {
        this.valorationSum += valoration.toInt()
    }

    private fun checkOperation(existingValoration: ValorationData, newValoration: Valoration) {
        if (existingValoration.valoration != newValoration){
            operationOverValoration(newValoration)
        }
    }

}


