package com.amonteiro.a2025_sdv_parisa.exo

import com.amonteiro.a2025_sdv_parisa.model.CarBean

class MyLiveData(value : String? = null) {

    var value = value
        set(newValue) {
            field = newValue
            action.invoke(newValue)
        }

    var action : (String?)->Unit = {}
        set(newAction) {
            field = newAction
            action.invoke(value)
        }
}

fun main() {
    //exo3()

    var toto = MyLiveData("Coucou")

    toto.action = {
        println(it)
    }

    toto.value = "Hello"
    toto.value = null
}

data class PersonBean(var name:String, var note:Int)

fun exo3(){
    val list = arrayListOf(PersonBean ("toto", 16),
        PersonBean ("Tata", 20),
        PersonBean ("Toto", 8),
        PersonBean ("Charles", 14))

    println("Afficher la sous liste de personne ayant 10 et +")
    //println(list.filter { it.note >=10 })
    //Pour un affichage de notre choix
    println(list.filter { it.note >=10 }.joinToString("\n") { "-${it.name} : ${it.note}"})

    var isToto : (PersonBean) -> Boolean = {it.name.equals("toto", true) }

    //TODO
    println("\n\nAfficher combien il y a de Toto dans la classe ?")
    println("Nombre de toto : ${list.count { it.name.equals("toto", true) }}")

    println("\n\nAfficher combien de Toto ayant la moyenne (10 et +)")
    println("Nombre de toto : ${list.count {        isToto(it) && it.note >= 10 }}")

    println("\n\nAfficher combien de Toto ont plus que la moyenne de la classe")
    var average = list.map { it.note }.average()
    println("Nombre de toto : ${list.count { it.name.equals("toto", true) && it.note >= average }}")

    println("\n\nAfficher la list triée par nom sans doublon")
    list.distinctBy { it.name.lowercase() }.sortedBy { it.name }.joinToString()

    println("\n\nAjouter un point a ceux n’ayant pas la moyenne (<10)")
    list.filter { it.note < 10 }.forEach { it.note++ }

    println("\n\nAjouter un point à tous les Toto")
    list.filter { it.name.equals("toto", true) }.forEach { it.note++ }

    println("\n\nRetirer de la liste ceux ayant la note la plus petite. (Il peut y en avoir plusieurs)")
    var minNote = list.minOf { it.note }
    list.removeIf { it.note == minNote }

    println("\n\nAfficher les noms de ceux ayant la moyenne(10et+) par ordre alphabétique")

    //TODO Créer une variable isToto contenant une lambda qui teste si un PersonBean s'appelle Toto

    println("\n\nDupliquer la liste ainsi que tous les utilisateurs (nouvelle instance) qu'elle contient")

    var newList = list.map { it.copy() }


    println("\n\nAfficher par notes croissantes les élèves ayant eu cette note comme sur l'exemple")
}

fun exo1() {
    //Déclaration
    val lower: (String) -> Unit = { it: String -> println(it.lowercase()) }
    val lower2 = { it: String -> println(it.lowercase()) }
    val lower3: (String) -> Unit = { it -> println(it.lowercase()) }
    val lower4: (String) -> Unit = { println(it.lowercase()) }

    //Appel
    lower("Coucou")

//    hour : prenant un nombre de minutes et retournant le nombre d’heures équivalentes
    val hour : (Int)->Int = { it/60}
    var res = hour(123)
    println("res=$res")

//    max : prenant 2 entiers et retournant le plus grand (Math.max())
    val max = {a:Int, b:Int -> Math.max(a,b) }
    res = max(123, 124)
    println("res=$res")

//    reverse : retourne le texte à l’envers toto -> otot (.reversed())
    val reverse : (String)-> String = {it.reversed()}
    println("reverse=${reverse("coucou")}")

    println(arrayListOf(CarBean("Seat", "Leon"), CarBean("Audi", "A3")).joinToString(separator = "\n", limit = 1))
}