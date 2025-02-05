package com.amonteiro.a2025_sdv_parisa.viewmodel

import androidx.lifecycle.ViewModel
import com.amonteiro.a2025_sdv_parisa.model.PictureBean
import com.amonteiro.a2025_sdv_parisa.model.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Nice")

    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")
}

class MainViewModel : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())

    fun loadWeathers(cityName: String) {
        //Lance la requête et met le corps de la réponse dans html
        dataList.value = WeatherRepository.loadWeathers(cityName)
            .map {
                PictureBean(
                    it.id,
                    it.weather.firstOrNull()?.icon ?: "",
                    it.name,
                    """
        Il fait ${it.main.temp}° à ${it.name} (id=${it.id}) avec un vent de ${it.wind.speed} m/s
        -Description : ${it.weather.firstOrNull()?.description ?: "-"}
        -Icône : ${it.weather.firstOrNull()?.icon ?: "-"}
    """.trimIndent()
                )
            }
    }
}