package com.amonteiro.a2025_sdv_parisa.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

object WeatherRepository {

    val client = OkHttpClient()
    val gson = Gson()

    const val URL_API_WEATHER = "https://api.openweathermap.org/data/2.5/find?cnt=5&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    fun loadWeathers(cityName: String): List<WeatherBean> {
        val json = sendGet(URL_API_WEATHER + cityName)
        val list = gson.fromJson(json, WeatherAPIResult::class.java).list
        list.forEach {
            it.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }

        return list
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

data class WeatherAPIResult(var list: List<WeatherBean>)
data class WeatherBean(var name: String, var main: TempBean, var wind: WindBean, var weather: List<DescriptionBean>, var id: Int)
data class TempBean(var temp: Double)
data class WindBean(var speed: Double)
data class DescriptionBean(var description: String, var icon: String)


//Utilisation
fun main() {
    //Lance la requête et met le corps de la réponse dans html
    val res = WeatherRepository.loadWeathers("Nice")

    for (city in res) {
        println(
            """
        Il fait ${city.main.temp}° à ${city.name} (id=${city.id}) avec un vent de ${city.wind.speed} m/s
        -Description : ${city.weather.firstOrNull()?.description ?: "-"}
        -Icône : ${city.weather.firstOrNull()?.icon ?: "-"}
        
    """.trimIndent()
        )
    }

}