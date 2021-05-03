package ar.edu.unq.grupoN.backenddesappapi.model

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import java.lang.RuntimeException


enum class Rating {
    ONE, TWO, THREE, FOUR, FIVE;
}

enum class ReviewType {
    CHAPTER {
        override fun validate(content: CinematographicContent, review: Review) {
            throwIfIsNotASerie(content)

            if (review.episodeNumber == null || review.seasonNumber == null)
                throw  InvalidSeasonOrEpisodeNumberException(
                    "Invalid season or episode number, in a chapter review, both must be a number"
                )

            if (!content.haveEpisode(review.seasonNumber, review.episodeNumber))
                throw  DoesNotExistChapterException("This chapter doesn't exist in '${content.title}' yet")
        }
    },
    SERIE {
        override fun validate(content: CinematographicContent, review: Review)  = throwIfIsNotASerie(content)
    },
    MOVIE {
        override fun validate(content: CinematographicContent, review: Review) {
            if (content.isSerie()) {
                throw InvalidReviewTypeException("Invalid review type, '${content.title}' is a Serie")
            }
        }
    };

    abstract fun validate(content: CinematographicContent, review: Review)

    protected fun throwIfIsNotASerie(content: CinematographicContent) {
        if (!content.isSerie())
            throw InvalidReviewTypeException("Invalid review type, '${content.title}' is a Movie")
    }
}

enum class Valoration {
    LIKE {
        override fun toInt() = 1
    },
    DISLIKE {
        override fun toInt() = -1
    };

    abstract fun toInt(): Int
}

enum class Employment {
    ACTOR, WRITER, DIRECTOR
}
