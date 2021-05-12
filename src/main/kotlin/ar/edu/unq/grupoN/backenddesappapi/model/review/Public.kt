package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.PublicReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn


@Entity
@PrimaryKeyJoinColumn(name="id")
class Public(contentInfo: ContentInfo, reviewInfo: ReviewInfo, publicReviewInfo: PublicReviewInfo)
    : Review(contentInfo, reviewInfo) {

    override var includeSpoiler = publicReviewInfo.includeSpoiler
    var userId = publicReviewInfo.userId
    var username = publicReviewInfo.username
    override val isPublic = true
}