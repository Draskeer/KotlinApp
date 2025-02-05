package com.amonteiro.a2025_sdv_parisa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a2025_sdv_parisa.model.PictureBean
import com.amonteiro.a2025_sdv_parisa.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Nice")

    while (viewModel.runInProgress.value) {
        println("Tâche toujours en cours : " + viewModel.runInProgress.value)
        Thread.sleep(500)
    }

    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("runInProgress : ${viewModel.runInProgress.value}")
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}" )
}

class MainViewModel : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadWeathers(cityName: String) {

        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
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
            catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            }
            runInProgress.value = false

        }

    }
}