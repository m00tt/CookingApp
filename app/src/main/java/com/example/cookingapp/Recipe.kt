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
    var ident=""

    constructor ()

    constructor(id:String,name: String, difficoltà: String, durata: String, portata: String)
    {
        this.ident=id
        this.name=name
        this.difficoltà=difficoltà
        this.durata=durata
        this.portata=portata

    }

    constructor(id:String, name: String, difficoltà: String, preparazione: String, cottura: String,durata: String, dosi:String, portata: String, ingredienti:ArrayList<String>, descrizione:String, conservazione:String) {
        this.ident=id
        this.name=name
        this.difficoltà=difficoltà
        this.preparazione=preparazione
        this.cottura=cottura
        this.durata=durata
        this.dosi=dosi
        this.portata=portata
        this.ingredienti=ingredienti
        this.descrizione=descrizione
        this.conservazione=conservazione
    }



    //metodo per aggiornare la ricetta con i valori contenuto nell'istanza passategli
    fun set(newRecipe: Recipe){
        this.ident=newRecipe.ident
        this.name=newRecipe.name
        this.difficoltà=newRecipe.difficoltà
        this.preparazione=newRecipe.preparazione
        this.cottura=newRecipe.cottura
        this.dosi=newRecipe.dosi
        this.portata=newRecipe.portata
        this.ingredienti=newRecipe.ingredienti
        this.descrizione=newRecipe.descrizione
        this.conservazione=newRecipe.conservazione
    }

    //restituisce la stringa che sarà usata per identificare un oggetto di tipo Recipe
    override fun toString(): String {
        return ident
    }

}