package com.example.cookingapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.example.cookingapp.RecipeActivity
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.row.view.*
import kotlin.concurrent.thread

class HomeAdapter(private val context: Context, private val data: ArrayList<Recipe>) :
    BaseAdapter(), Filterable {
    private var mOriginalValues: ArrayList<Recipe>? = data
    private var mDisplayedValues: ArrayList<Recipe>? = data


    override fun getCount(): Int {
        if (mDisplayedValues == null)
            return 0
        else
            return mDisplayedValues!!.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var newView = convertView

        if (newView == null)
            newView = LayoutInflater.from(context).inflate(R.layout.row, parent, false)

        if (newView != null) {

            //prendiamo le view definite nel file di layout e le assegnamo a delle variabili
            val name = newView.findViewById<TextView>(R.id.textViewName)
            val difficoltà = newView.findViewById<TextView>(R.id.textViewDifficoltà)
            val durata = newView.findViewById<TextView>(R.id.textViewDurata)
            val portata = newView.findViewById<TextView>(R.id.textViewPortata)

            val id = mDisplayedValues?.get(position)?.ident
            //CATTURA IMMAGINE RICETTA DAL DB

            val mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
            val idImgRef = mStorageReference.child("images/${id}.jpg")
            thread {
                idImgRef.getBytes(1024 * 1024).addOnSuccessListener {
                    newView.imageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }.addOnFailureListener {
                    //Log.e("IMAGE DOWNLOAD", "Error")
                    newView.imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }

            //assegno i dati alle view
            name.text = mDisplayedValues?.get(position)?.name
            difficoltà.text = mDisplayedValues?.get(position)?.difficoltà
            durata.text = mDisplayedValues?.get(position)?.durata.toString()
            portata.text = mDisplayedValues?.get(position)?.portata

            //creo il istener per quando si clicca l'intera riga
            newView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //Toast.makeText(context as MainActivity, "hai cliccato la riga $position", Toast.LENGTH_SHORT).show()
                    //apertura activity ricetta singola
                    val contesto = context as MainActivity
                    val intent = Intent(contesto, RecipeActivity::class.java)


                    intent.putExtra("recipe_data", id.toString())
                    intent.putExtra("chiamante", "home")
                    contesto.startActivity(intent)
                }
            })

        }

        return newView

    }

    //metodo che serve per la searchBar per filtrare i vari elementi
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                var FilteredArrList = ArrayList<Recipe>()


                if (mOriginalValues == null)
                    mOriginalValues = ArrayList<Recipe>(mDisplayedValues)
                if (constraint == null || constraint.isEmpty()) {

                    // set the Original result to return
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                } else {

                    FilteredArrList = ricerca(constraint)

                    // set the Filtered result to return
                    /*if(FilteredArrList.size>0)
                        filteredValues=FilteredArrList*/
                    results.count = FilteredArrList.size
                    results.values = FilteredArrList
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                mDisplayedValues = results?.values as ArrayList<Recipe>?
                notifyDataSetChanged()
            }

        }
    }

    private fun ricerca(constraint: CharSequence?): ArrayList<Recipe> {
        val FilteredArrList = ArrayList<Recipe>()
        lateinit var data: String
        val cons = constraint.toString()
        for (i in mOriginalValues!!.indices) {
            data = mOriginalValues!![i].name
            if (data.startsWith(cons.toString())) {
                FilteredArrList.add(
                    Recipe(
                        mOriginalValues!![i].ident,
                        mOriginalValues!![i].name,
                        mOriginalValues!![i].difficoltà,
                        mOriginalValues!![i].durata,
                        mOriginalValues!![i].portata
                    )
                )
            }
        }
        return FilteredArrList
    }



}