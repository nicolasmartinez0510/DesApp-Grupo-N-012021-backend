package ar.edu.unq.grupoN.backenddesappapi.model


enum class Rating {
    ONE, TWO, THREE, FOUR, FIVE;
}

enum class ReviewType {
    CHAPTER, SERIE, MOVIE
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
