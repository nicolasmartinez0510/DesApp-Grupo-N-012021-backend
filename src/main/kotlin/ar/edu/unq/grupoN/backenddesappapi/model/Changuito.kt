package ar.edu.unq.grupoN.backenddesappapi.model

class Changuito {
    private val products = mutableListOf<String>()

    fun add(productName: String) {
        this.products.add(productName)
    }

    fun ammountOf(productName: String) = products.filter { it.equals(productName) }.size

}