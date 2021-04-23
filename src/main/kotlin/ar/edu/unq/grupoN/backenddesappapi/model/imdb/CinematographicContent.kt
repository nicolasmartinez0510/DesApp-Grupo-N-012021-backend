package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.model.Cast
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
    @ManyToMany()
    open var directors: List<CastMember> = listOf()
    @ManyToMany()
    open var writers: List<CastMember> = listOf()
    @ManyToMany()
    open var actors: List<CastMember> = listOf()
    open var averageRating: Double = 0.0
    open var votesAmount: Int = 0

    constructor(basicInformation: BasicInformation, cast: Cast, rating: RatingInfo): this(){
        set_basic_information(basicInformation)
        set_cast(cast)
        set_rating(rating)
    }

    open fun isSerie() = false

    private fun set_basic_information(basicInformation: BasicInformation) {
        this.titleId = basicInformation.titleId
        this.title = basicInformation.title
        this.titleType = basicInformation.titleType
        this.isAdultContent = basicInformation.isAdultContent
        this.startYear = basicInformation.startYear
        this.runtimeMinutes = basicInformation.runtimeMinutes
    }

    private fun set_cast(cast: Cast) {
        this.actors = cast.actors
        this.writers = cast.writers
        this.directors = cast.directors
    }

    private fun set_rating(rating: RatingInfo) {
        this.averageRating = rating.averageRating
        this.votesAmount = rating.votesAmount
    }
}
