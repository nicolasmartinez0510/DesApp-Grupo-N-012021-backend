package ar.edu.unq.grupoN.backenddesappapi.model

abstract class ReviewTypeException(message: String) : RuntimeException(message)

class InvalidReviewTypeException(message: String) : ReviewTypeException(message)

class DoesNotExistChapterException(title: String) :
    ReviewTypeException("This chapter doesn't exist in '$title' yet")

class InvalidSeasonOrEpisodeNumberException :
    ReviewTypeException("Invalid season or episode number, in a chapter review, both must be a number")

class InvalidReviewRatingException : RuntimeException("Invalid review rating")