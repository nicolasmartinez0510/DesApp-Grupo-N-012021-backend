package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.service.dto.SubscribedUrl
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
    @OneToMany(cascade = [CascadeType.ALL])
    open var subscribers: MutableList<SubscribedUrl> = mutableListOf()

    constructor(basicInformation: BasicInformation, cast: MutableList<CastMember>): this(){
        this.cast = cast
        setBasicInformation(basicInformation)
    }

    open fun isSerie() = false

    fun addToSubscribers(subscribedSubscribedUrl: SubscribedUrl) = subscribers.add(subscribedSubscribedUrl)

    private fun setBasicInformation(basicInformation: BasicInformation) {
        this.titleId = basicInformation.titleId
        this.title = basicInformation.title
        this.titleType = basicInformation.titleType
        this.isAdultContent = basicInformation.isAdultContent
        this.startYear = basicInformation.startYear
        this.runtimeMinutes = basicInformation.runtimeMinutes
    }

    open fun haveEpisode(seasonNumber: Int?, episodeNumber: Int?) = false
}