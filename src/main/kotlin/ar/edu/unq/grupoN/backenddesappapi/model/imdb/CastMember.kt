package ar.edu.unq.grupoN.backenddesappapi.model.imdb

data class CastMember(val name: String, val category: String, val job: String?, val character: String,
                      val birthYear: Int, val deathYear: Int?, val primaryProfession: List<String>)