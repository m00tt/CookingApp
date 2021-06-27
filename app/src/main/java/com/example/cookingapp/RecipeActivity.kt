package com.example.cookingapp

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.res.Configuration
import android.text.Editable
import android.view.View
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.row_ingredient.*
import kotlinx.android.synthetic.main.row_ingredient.view.*

class RecipeActivity : AppCompatActivity() {
    var editable = false
    var prefer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val chiamante = intent.getStringExtra("chiamante")
        if (chiamante == "home") {
            Log.v("chiamante", "homepage")
            //fab_edit.hide()
        }

        //remove black underline
        et_recipe_name.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)


        if (et_recipe_ingredient != null && et_ingredient_qty != null && et_preparazione_descrizione != null && et_conservazione != null) {
            et_recipe_ingredient.background.setColorFilter(
                Color.TRANSPARENT,
                PorterDuff.Mode.SRC_IN
            )
            et_ingredient_qty.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

            et_preparazione_descrizione.background.setColorFilter(
                Color.TRANSPARENT,
                PorterDuff.Mode.SRC_IN
            )
            et_conservazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        }
        //set editText not editable first
        et_recipe_name.isEnabled = false
        et_recipe_name.setTextColor(Color.BLACK)
        et_cottura.isEnabled = false
        et_difficolta.isEnabled = false
        et_dosi.isEnabled = false
        et_portata.isEnabled = false
        et_preparazione.isEnabled = false

        et_preparazione_descrizione.isEnabled = false
        et_conservazione.isEnabled = false

        //btn add ingredient not visible first
        btn_ingredient_add.visibility = View.INVISIBLE

        val nome = intent.getStringExtra("recipe_data")
        et_recipe_name.setText(nome)
    }

    override fun onStart() {
        super.onStart()
        fab_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!editable)
                    editRecipe()
                else
                    saveRecipe()
            }

        })
        btn_ingredient_add.setOnClickListener {
            onAddIngredient()
        }
    }

    fun editRecipe() {
        //add black underline
        et_recipe_name.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        if (et_recipe_ingredient != null && et_ingredient_qty != null && et_preparazione_descrizione != null && et_conservazione != null) {
            et_recipe_ingredient.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            et_ingredient_qty.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

            et_preparazione_descrizione.background.setColorFilter(
                Color.BLACK,
                PorterDuff.Mode.SRC_IN
            )
            et_conservazione.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

        }
        //set editText editable
        et_recipe_name.isEnabled = true
        et_preparazione.isEnabled = true
        et_portata.isEnabled = true
        et_dosi.isEnabled = true
        et_difficolta.isEnabled = true
        et_cottura.isEnabled = true


        et_preparazione_descrizione.isEnabled = true
        et_conservazione.isEnabled = true

        Log.v("editRecipe", "editRecipe")
        editable = true

        //set btn add ingredient visible
        btn_ingredient_add.visibility = View.VISIBLE

        var childCountParent = linear_ingredienti.childCount
        for ((index) in (1 until childCountParent).withIndex()) {
            if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && linear_ingredienti[index].spin_ingredient_units != null && linear_ingredienti[index].iv_ingredient_delete != null) {
                linear_ingredienti[index].et_recipe_ingredient.isEnabled = true
                linear_ingredienti[index].et_ingredient_qty.isEnabled = true
                linear_ingredienti[index].spin_ingredient_units.isEnabled = true
                linear_ingredienti[index].iv_ingredient_delete.isEnabled = true
            }
        }

        fab_edit.setImageResource(R.mipmap.ic_save_foreground)
    }

    fun saveRecipe() {
        //remove black underline
        et_recipe_name.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

        if (et_recipe_ingredient != null && et_ingredient_qty != null && et_preparazione_descrizione != null && et_conservazione != null) {
            et_recipe_ingredient.background.setColorFilter(
                Color.TRANSPARENT,
                PorterDuff.Mode.SRC_IN
            )
            et_ingredient_qty.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

            et_preparazione_descrizione.background.setColorFilter(
                Color.TRANSPARENT,
                PorterDuff.Mode.SRC_IN
            )
            et_conservazione.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)

        }
        //set editText not editable
        et_recipe_name.isEnabled = false
        et_preparazione.isEnabled = false
        et_portata.isEnabled = false
        et_dosi.isEnabled = false
        et_difficolta.isEnabled = false
        et_cottura.isEnabled = false


        et_preparazione_descrizione.isEnabled = false
        et_conservazione.isEnabled = false

        et_recipe_name.setTextColor(Color.BLACK)
        et_preparazione.setTextColor(resources.getColor(R.color.scritte))
        et_portata.setTextColor(resources.getColor(R.color.scritte))
        et_dosi.setTextColor(resources.getColor(R.color.scritte))
        et_difficolta.setTextColor(resources.getColor(R.color.scritte))
        et_cottura.setTextColor(resources.getColor(R.color.scritte))

        Log.v("saveRecipe", "SaveRecipe")
        editable = false

        //set btn add ingredient not visible
        btn_ingredient_add.visibility = View.INVISIBLE

        var childCountParent = linear_ingredienti.childCount
        for ((index) in (1 until childCountParent).withIndex()) {
            if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && linear_ingredienti[index].spin_ingredient_units != null && linear_ingredienti[index].iv_ingredient_delete != null) {
                linear_ingredienti[index].et_recipe_ingredient.isEnabled = false
                linear_ingredienti[index].et_ingredient_qty.isEnabled = false
                linear_ingredienti[index].spin_ingredient_units.isEnabled = false
                linear_ingredienti[index].iv_ingredient_delete.isEnabled = false
            }

            /*
            et_shoplist_product.isEnabled = false
            et_shoplist_qty.isEnabled = false
            spin_shoplist_units.isEnabled = false
            iv_shoplist_delete.isEnabled = false*/
        }

        fab_edit.setImageResource(R.mipmap.ic_pencil_foreground)
    }

    fun setPrefer(view: View) {
        if (!prefer) {
            img_heart.setImageResource(R.drawable.heart_red)
            prefer = true
        } else {
            img_heart.setImageResource(R.drawable.heart)
            prefer = false
        }
    }

    fun onAddIngredient() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toInflate: View = inflater.inflate(R.layout.row_ingredient, linear_ingredienti, false)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linear_ingredienti.addView(toInflate, linear_ingredienti.childCount)
        } else {
            linear_ingredienti.addView(toInflate, linear_ingredienti.childCount - 1)
        }
    }
    fun onDeleteIngredient(v: View){
        linear_ingredienti.removeView(v.parent as View)
    }
}