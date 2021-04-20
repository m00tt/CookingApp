package com.example.cookingapp.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Ricetta Singola Fragment"
    }
    val text: LiveData<String> = _text
}