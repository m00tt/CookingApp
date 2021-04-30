package com.example.cookingapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _ricette = MutableLiveData<Array<String>>().apply {
        value = arrayOf(
                "Ricetta1 Facile 15 antipasto",
                "Ricetta2 Media 60 primo",
                "Ricetta3 Difficile 120 secondo",
                "Ricetta4 Facile 20 dessert",
                "Ricetta5 Media 120 secondo",
                "Ricetta6 Facile 15 primo",
                "Ricetta7 Media 20 secondo",
                "Ricetta8 Facile 3 dessert",
                "Ricetta9 Difficile 100 primo",
                "Ricetta10 Facile 10 secondo",
                "Ricetta11 Media 18 primo",
                "Ricetta12 Facile 8 antipasto",
                "Ricetta13 Media 20 primo",
                "Ricetta14 Difficile 40 secondo",
                "Ricetta15 Facile 9 primo",
                "Ricetta16 Media 25 secondo",
        )
    }
    val ricette: LiveData<Array<String>> = _ricette
}