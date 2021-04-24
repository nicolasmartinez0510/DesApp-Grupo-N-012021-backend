package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.model.RatingInfo
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="titleId")
class Movie(basicInformation: BasicInformation, cast: MutableList<CastMember>, rating: RatingInfo)
    : CinematographicContent(basicInformation, cast, rating)
