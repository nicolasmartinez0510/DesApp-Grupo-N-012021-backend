package ar.edu.unq.grupoN.backenddesappapi.model

import java.lang.RuntimeException

class InvalidReviewTypeException(message: String): RuntimeException(message)

class DoesNotExistChapterException(message: String): RuntimeException(message)

class InvalidSeasonOrEpisodeNumberException(message: String): RuntimeException(message)
