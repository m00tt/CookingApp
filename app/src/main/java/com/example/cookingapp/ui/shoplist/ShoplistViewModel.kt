package com.example.cookingapp.ui.shoplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoplistViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Lista della Spesa Fragment"
    }
    val text: LiveData<String> = _text
}