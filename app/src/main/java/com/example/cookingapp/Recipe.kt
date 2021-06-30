package com.example.cookingapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime

class Recipe
{
    var name:String=""
    var difficoltà: String=""
    var durata:String=""
    var portata:String=""
    var preparazione=""
    var cottura=""
    var dosi=""
    var descrizione=""
    var conservazione=""
    var ingredienti=ArrayList<String>()
    var preferiti:Boolean=false


    constructor(name: String, difficoltà: String, durata: String, portata: String)
    {
        this.name=name
        this.difficoltà=difficoltà
        this.durata=durata
        this.portata=portata

    }
    constructor(name: String, difficoltà: String, preparazione: String, cottura: String, dosi:String, portata: String, ingredienti:ArrayList<String>, descrizione:String, conservazione:String, preferiti:Boolean ) {
        this.name=name
        this.difficoltà=difficoltà
        this.preparazione=preparazione
        this.cottura=cottura
        this.dosi=dosi
        this.portata=portata
        this.ingredienti=ingredienti
        this.descrizione=descrizione
        this.conservazione=conservazione
        this.preferiti=preferiti
    }

    fun getId():String
    {
        val id=name+LocalDate.now().toString()
        return id
    }

}