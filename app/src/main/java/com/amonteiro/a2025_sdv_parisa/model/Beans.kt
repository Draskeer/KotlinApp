package com.amonteiro.a2025_sdv_parisa.model

import java.util.Random

fun main() {

    var car = CarBean("","")
    var car2 = CarBean("","")

}

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)

class RandomName() {
    private val list = arrayListOf("Toto", "Bob", "Tata")
    private var oldValue = ""

    fun add(name: String) {
        if (name.isNotBlank() && name !in list) {
            list.add(name)
        }
    }

    fun next() = list.random()

    fun addAll(vararg names: String) {
        for (name in names) {
            add(name)
        }
    }

    fun nextDiff(): String {
        var newValue = next()
        while (oldValue == newValue) {
            newValue = next()
        }

        oldValue = newValue
        return oldValue
    }

    fun next2() = Pair(nextDiff(), nextDiff())


    fun nextDiff2(): String {
        oldValue = list.filter { it != oldValue }.random()
        return oldValue
    }

    fun nextDiff3() = list.filter { it != oldValue }.random().also { oldValue = it  }

}

class ThermometerBean(val min: Int, val max: Int, value: Int) {
    var value: Int = value.coerceIn(min, max)
        set(newValue) {
            field = newValue.coerceIn(min, max)
        }

    companion object {
        fun getCelsiusThermometer() = ThermometerBean(-30, 50, 0)
        fun getFahrenheitThermometer() = ThermometerBean(20, 120, 32)
    }

}

class PrintRandomIntBean(var max: Int) {
    private val random = Random()

    init {
        println(random.nextInt(max))
        println(random.nextInt(max))
        println(random.nextInt(max))
    }

}

class HouseBean(width: Int, length: Int, var color: String) {
    var area = width * length

    fun print() = println("La maison $color fait ${area}m²")
}

data class CarBean(var marque: String, var model: String?) {
    var color = ""
}