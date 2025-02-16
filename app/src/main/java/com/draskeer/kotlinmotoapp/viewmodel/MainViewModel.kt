package com.draskeer.kotlinmotoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draskeer.kotlinmotoapp.model.MotoBeansItem
import com.draskeer.kotlinmotoapp.model.MotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun main() {
    val viewModel = MainViewModel()
    viewModel.loadMoto("Honda")

    while (viewModel.runInProgress.value) {
        println("Tâche toujours en cours : " + viewModel.runInProgress.value)
        Thread.sleep(500)
    }

    println("runInProgress : ${viewModel.runInProgress.value}")
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}")
}

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<MotoBeansItem>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            MotoBeansItem(make = "Honda", type = "Sport", clutch = "a", model = "CBR600RR", icon = "https://example.com/moto1.jpg", engine = "599cc Inline-4", power = "120 HP", torque = "66 Nm", top_speed = "250 km/h", total_weight = "194 kg", year = "2023", fuel_system = "Fuel Injection", emission = "Euro 5"),
            MotoBeansItem(make = "Yamaha", type = "Sport", clutch = "a", model = "R1", icon = "https://example.com/moto2.jpg", engine = "998cc Inline-4", power = "200 HP", torque = "112 Nm", top_speed = "299 km/h", total_weight = "201 kg", year = "2022", fuel_system = "Fuel Injection", emission = "Euro 5"),
            MotoBeansItem(make = "Suzuki", type = "Sport", clutch = "a", model = "GSX-R750", icon = "https://example.com/moto3.jpg", engine = "750cc Inline-4", power = "150 HP", torque = "85 Nm", top_speed = "270 km/h", total_weight = "190 kg", year = "2023", fuel_system = "Fuel Injection", emission = "Euro 5")
        ).shuffled()
    }

    fun loadMoto(brandName: String) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MotoRepository.loadMoto(brandName)
                dataList.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            } finally {
                runInProgress.value = false
            }
        }
    }
}
