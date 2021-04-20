package com.example.cookingapp.ui.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CookbookViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Ricettario Fragment"
    }
    val text: LiveData<String> = _text
}