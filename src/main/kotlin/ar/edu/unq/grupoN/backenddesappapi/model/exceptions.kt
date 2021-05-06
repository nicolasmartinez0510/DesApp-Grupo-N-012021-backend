package ar.edu.unq.grupoN.backenddesappapi.model

import java.lang.RuntimeException

abstract class ReviewTypeException(message: String): RuntimeException(message)

class InvalidReviewTypeException(message: String): ReviewTypeException(message)

class DoesNotExistChapterException(message: String): ReviewTypeException(message)

class InvalidSeasonOrEpisodeNumberException(message: String): ReviewTypeException(message)
