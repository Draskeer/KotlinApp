package com.draskeer.kotlinmotoapp.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.UUID

object MotoRepository {

    private val client = OkHttpClient()
    private val gson = Gson()

    private const val URL_API_MOTO = "https://motorcycles-by-api-ninjas.p.rapidapi.com/v1/motorcycles?make="
    private val API_KEY = "fa50523f89msh10552313b0ef00fp12c1a6jsn02839ffb832e"

    // Fonction qui charge les motos d'une marque et assigne une image aléatoire
    fun loadMoto(brandName: String): List<MotoBeansItem> {
        return try {
            val json = sendGet("$URL_API_MOTO$brandName")

            // Correction du parsing en supposant que l'API renvoie directement une liste
            val type = object : TypeToken<List<MotoBeansItem>>() {}.type
            val list: List<MotoBeansItem> = gson.fromJson(json, type)

            // Assigner une image aléatoire avec un identifiant unique pour chaque image
            list.map { it.copy(icon = "https://picsum.photos/200/300?random=${UUID.randomUUID()}") }
        } catch (e: Exception) {
            println("Erreur lors du chargement des motos: ${e.message}")
            emptyList()
        }
    }

    // Méthode pour envoyer la requête GET à l'API
    private fun sendGet(url: String): String {
        println("URL : $url")  // Debug
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("x-rapidapi-key", API_KEY)
            .addHeader("x-rapidapi-host", "motorcycles-by-api-ninjas.p.rapidapi.com")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Erreur de réponse du serveur: ${response.code}")
                }
                response.body?.string() ?: ""
            }
        } catch (e: IOException) {
            println("Erreur réseau: ${e.message}")
            ""
        }
    }
}

// Exemple d'utilisation
fun main() {
    val res = MotoRepository.loadMoto("Honda")
    println("Résultat de la requête:")
    println(res)
    if (res.isEmpty()) {
        println("Aucune moto trouvée.")
    } else {
        res.forEach { moto ->
            println("Modèle: ${moto.model}, Image: ${moto.icon}")
        }
    }
}
