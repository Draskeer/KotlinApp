package com.amonteiro.a2025_sdv_parisa.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

object UserRepository {

    val client = OkHttpClient()
    val gson = Gson()

    const val URL_API_USER = "https://www.amonteiro.fr/api/randomuser"

    fun loadRandomUser(): UserBean {
        val json = sendGet(URL_API_USER)
        println(json)

        return gson.fromJson(json, UserBean::class.java)
    }

    fun loadRandomUsers(): List<UserBean> {
        val json = sendGet(URL_API_USER + "s")
        println(json)

        return gson.fromJson(json, Array<UserBean>::class.java).toList()
    }

    //Méthode qui prend en entrée une url, execute la requête
    //Retourne le code HTML/JSON reçu
    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        return client.newCall(request).execute().use { //it:Response
            //use permet de fermer la réponse qu'il y ait ou non une exception
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Réponse du serveur incorrect :${it.code}\n${it.body.string()}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }
}

data class UserBean(var name: String, var age: Int, var coord: CoordBean?)
data class CoordBean(var phone: String?, var mail: String?)

//Utilisation
fun main() {
    //Lance la requête et met le corps de la réponse dans html
    val users = UserRepository.loadRandomUsers()

    for (user in users) {
        println("""
        Il s'appelle ${user.name} pour le contacter :
        Phone : ${user.coord?.phone ?: "-"}
        Mail : ${user.coord?.mail ?: "-"}
        
    """.trimIndent())
    }


}