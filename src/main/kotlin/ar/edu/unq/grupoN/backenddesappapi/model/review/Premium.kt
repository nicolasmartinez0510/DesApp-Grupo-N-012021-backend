package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Premium(
    contentInfo: ContentInfo,
    reviewInfo: ReviewInfo,
    val reviewerId: String,
): Review(contentInfo, reviewInfo)