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

    var includeSpoiler: Boolean = false
    var userId: String
    var username: String

    init {
        this.includeSpoiler = publicReviewInfo.includeSpoiler
        this.username = publicReviewInfo.username
        this.userId = publicReviewInfo.userId
    }

    override fun isPublic() = true


}