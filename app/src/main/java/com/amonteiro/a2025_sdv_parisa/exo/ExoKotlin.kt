package com.amonteiro.a2025_sdv_parisa.exo

import com.amonteiro.a2025_sdv_parisa.PRICE_BAGUETTE
import com.amonteiro.a2025_sdv_parisa.PRICE_CROISSANT
import com.amonteiro.a2025_sdv_parisa.PRICE_SANDWITCH

fun main() {

    var res= boulangerie(nbB = 12)
    println("res=$res")

    println(scanNumber("Donnez un texte : "))
}

fun myPrint(text : String) = println("#$text#")

fun boulangerie(nbC:Int = 0, nbB : Int = 0 , nbS : Int = 0) :Double
   =  PRICE_BAGUETTE * nbB + PRICE_CROISSANT * nbC + PRICE_SANDWITCH * nbS

fun scanText(question:String): String {
    print(question)
    return readlnOrNull() ?: "-"
}

fun scanNumber(question:String): Int {
    return scanText(question).toIntOrNull() ?: 0
}
