package ar.edu.unq.grupoN.backenddesappapi.model

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review

enum class ReviewType {
    CHAPTER {
        override fun validate(content: CinematographicContent, review: Review) {
            throwIfIsNotASerie(content)

            if (review.episodeNumber == null || review.seasonNumber == null)
                throw  InvalidSeasonOrEpisodeNumberException()

            if (!content.haveEpisode(review.seasonNumber, review.episodeNumber))
                throw  DoesNotExistChapterException(content.title)
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
    LIKE, DISLIKE;
}

enum class Employment {
    ACTOR, WRITER, DIRECTOR
}

enum class Platform {
    NETFLIX, AMAZON, PLEX, DISNEY
}

enum class Language {
    ENGLISH, FRENCH, SPANISH, PORTUGUESE, LATIN, ARAMIC, RUSSIAN
}

enum class WantedReview{
    PUBLIC, PREMIUM
}

enum class Country {
    ARGENTINA, EEUU, BRAZIL, FRANCE, ITALY, SPAIN, CHILE, URUGUAY
}

enum class Sort {
    ASC, DESC
}

enum class ReportType {
    OFFENSIVE, SPAM, UGLYWORDS, RACISM, XENOPHOBIA, SPOILER
}