package com.example.cookingapp

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : AppCompatActivity() {
    var editable = false
    var prefer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val chiamante = intent.getStringExtra("chiamante")
        if(chiamante == "home"){
            Log.v("chiamante","homepage")
            //fab_edit.hide()
        }

        //remove black underline
        et_recipe_name.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

        //set editText not editable first
        et_recipe_name.isEnabled = false
        et_recipe_name.setTextColor(Color.BLACK)
        et_cottura.isEnabled = false
        et_difficolta.isEnabled = false
        et_dosi.isEnabled = false
        et_portata.isEnabled = false
        et_preparazione.isEnabled = false

        val nome = intent.getStringExtra("recipe_data")
        et_recipe_name.setText(nome)
    }

    override fun onStart() {
        super.onStart()
        fab_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(!editable)
                    editRecipe()
                else
                    saveRecipe()
            }
        })
    }

    fun editRecipe() {
        //add black underline
        et_recipe_name.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        //set editText editable
        et_recipe_name.isEnabled = true
        et_preparazione.isEnabled = true
        et_portata.isEnabled = true
        et_dosi.isEnabled = true
        et_difficolta.isEnabled = true
        et_cottura.isEnabled = true

        Log.v("editRecipe","editRecipe")
        editable = true
        fab_edit.setImageResource(R.mipmap.ic_save_foreground)
    }
    fun saveRecipe(){
        //remove black underline
        et_recipe_name.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

        //set editText not editable
        et_recipe_name.isEnabled = false
        et_preparazione.isEnabled = false
        et_portata.isEnabled = false
        et_dosi.isEnabled = false
        et_difficolta.isEnabled = false
        et_cottura.isEnabled = false

        et_recipe_name.setTextColor(Color.BLACK)
        et_preparazione.setTextColor(resources.getColor(R.color.scritte))
        et_portata.setTextColor(resources.getColor(R.color.scritte))
        et_dosi.setTextColor(resources.getColor(R.color.scritte))
        et_difficolta.setTextColor(resources.getColor(R.color.scritte))
        et_cottura.setTextColor(resources.getColor(R.color.scritte))

        Log.v("saveRecipe","SaveRecipe")
        editable = false
        fab_edit.setImageResource(R.mipmap.ic_pencil_foreground)
    }
    fun setPrefer(view: View)
    {
        if(!prefer) {
            img_heart.setImageResource(R.drawable.heart_red)
            prefer = true
        }
        else {
            img_heart.setImageResource(R.drawable.heart)
            prefer = false
        }
    }
}