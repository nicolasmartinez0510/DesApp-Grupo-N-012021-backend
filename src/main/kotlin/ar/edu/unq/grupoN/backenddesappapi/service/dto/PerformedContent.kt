package ar.edu.unq.grupoN.backenddesappapi.service.dto

import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import javax.persistence.Id

@RedisHash("fast-content")
class PerformedContent(
    @Id
    val id: String,
    val averageRating: Double,
    val votesAmount: Long
) : Serializable