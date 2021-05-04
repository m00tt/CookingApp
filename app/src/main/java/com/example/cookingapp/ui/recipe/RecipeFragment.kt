package com.example.cookingapp.ui.recipe

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_recipe.*

class RecipeFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recipeViewModel =
                ViewModelProvider(this).get(RecipeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipe, container, false)
        val textView: TextView = root.findViewById(R.id.recipe_name)
        recipeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        fab_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                editRecipe()
            }
        })
        return root
    }

    fun editRecipe() {
        et_recipe_name.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        et_recipe_name.isEnabled = true
        Log.v("editRecipe","editRecipe")
    }
    fun saveRecipe(){
        et_recipe_name.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_recipe_name.isEnabled = false
        Log.v("saveRecipe","SaveRecipe")
    }
}