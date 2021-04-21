package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class CinematographicContent(
    @Id
     open val titleId: String,
     open val title: String,
     open val titleType: String,
     open val isAdult: Boolean,
    @ElementCollection
    @JoinColumn(name="titleId")
    @Column(name="types")
     open val types: List<String>,
     open val startYear: Int,
     open val runtimeMinutes: Int,
    @ElementCollection
    @JoinColumn(name="titleId")
    @Column(name="genres")
     open val genres: List<String>,

    @ManyToMany()
     open val directors: List<CastMember>,

    @ManyToMany()
     open val writers: List<CastMember>,

    @ManyToMany()
     open val actors: List<CastMember>,

     open val averageRating: Double,
     open val numVotes: Int
){
    open fun isSerie() = false
}