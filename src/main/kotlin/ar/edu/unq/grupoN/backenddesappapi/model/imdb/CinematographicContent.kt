package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import ar.edu.unq.grupoN.backenddesappapi.model.RatingInfo
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class CinematographicContent(){
    @Id
    open var titleId: String = ""
    open var title: String = ""
    open var titleType: String = ""
    open var isAdultContent: Boolean = false
    open var startYear: Int? = null
    open var runtimeMinutes: Int? = null
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "party_id")
    open var cast: MutableList<CastMember> = mutableListOf()
    open var averageRating: Double = 0.0
    open var votesAmount: Int = 0
    @OneToMany(cascade = [CascadeType.ALL])
    open val subscribers: MutableList<PlatformAdministrator> = mutableListOf()

    constructor(basicInformation: BasicInformation, cast: MutableList<CastMember>, rating: RatingInfo): this(){
        this.cast = cast
        set_basic_information(basicInformation)
        set_rating(rating)

    }

    open fun isSerie() = false

    fun addToSubscribers(platformAdministrator: PlatformAdministrator) = subscribers.add(platformAdministrator)

    private fun set_basic_information(basicInformation: BasicInformation) {
        this.titleId = basicInformation.titleId
        this.title = basicInformation.title
        this.titleType = basicInformation.titleType
        this.isAdultContent = basicInformation.isAdultContent
        this.startYear = basicInformation.startYear
        this.runtimeMinutes = basicInformation.runtimeMinutes
    }

    private fun set_rating(rating: RatingInfo) {
        this.averageRating = rating.averageRating
        this.votesAmount = rating.votesAmount
    }

    open fun haveEpisode(seasonNumber: Int?, episodeNumber: Int?) = false
}
