package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PerformedContent(
    @Id
    val titleId: String,
    val averageRating: Double,
    val votesAmount: Int
) : Serializable {

    companion object {
        fun from(content: CinematographicContent): PerformedContent {
            return PerformedContent(content.titleId, content.averageRating, content.votesAmount)
        }
    }
}