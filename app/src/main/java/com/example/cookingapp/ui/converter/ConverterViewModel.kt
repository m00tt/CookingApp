package com.example.cookingapp.ui.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Convertitore Fragment"
    }
    val text: LiveData<String> = _text
}