package com.example.cookingapp.ui.cookbook

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.example.cookingapp.RecipeActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.row.view.*
import kotlin.concurrent.thread

class CookbookAdapter(private val context: Context, private val data: ArrayList<Recipe>) :
    BaseAdapter(), Filterable {
    private var mOriginalValues: ArrayList<Recipe>? = data
    private var mDisplayedValues: ArrayList<Recipe>? = data

    override fun getCount(): Int {
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

            //prendiamo le view definite nel file di layout e le assegniamo a delle variabili
            val name = newView.findViewById<TextView>(R.id.textViewName)
            val difficoltà = newView.findViewById<TextView>(R.id.textViewDifficoltà)
            val durata = newView.findViewById<TextView>(R.id.textViewDurata)
            val portata = newView.findViewById<TextView>(R.id.textViewPortata)

            //assegno i dati alle view
            name.text = mDisplayedValues?.get(position)?.name
            difficoltà.text = mDisplayedValues?.get(position)?.difficoltà
            durata.text = mDisplayedValues?.get(position)?.durata.toString()
            portata.text = mDisplayedValues?.get(position)?.portata

            val id=mDisplayedValues?.get(position)?.ident
            Log.v("id ricetta", id.toString())

            //CATTURA IMMAGINE RICETTA DAL DB

            val mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
            val idImgRef = mStorageReference.child("images/${id}.jpg")
            thread {
                idImgRef.getBytes(1024 * 1024).addOnSuccessListener {
                    newView.imageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }.addOnFailureListener {
                    Log.e("IMAGE DOWNLOAD", "Error")
                }
            }


            //creo il istener per quando si clicca l'intera riga
            newView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //Toast.makeText(context as MainActivity, "hai cliccato la riga $position", Toast.LENGTH_SHORT).show()
                    //apertura activity ricetta singola

                    val contesto = context as MainActivity
                    val intent = Intent(contesto, RecipeActivity::class.java)
                    intent.putExtra("recipe_data", id.toString())
                    intent.putExtra("chiamante", "ricettario")
                    contesto.startActivity(intent)
                }
            })

        }

        return newView

    }

    //metodo che serve per filtrare i vari elementi
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                lateinit var  data: String
                val results = FilterResults()

                var cons = constraint
                val FilteredArrList = ArrayList<Recipe>()

                if (mOriginalValues == null)
                    mOriginalValues = ArrayList<Recipe>(mDisplayedValues)
                if (cons == null || cons.isEmpty()) {

                    // set the Original result to return
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                } else {
                    cons = cons.toString()

                    //in base al bottone premuto si faranno le query adeguate
                    when(cons){
                        /*
                        "MieRicette" -> //metto in filteredArrList le ricette create dall'utente
                        "Preferite" -> //metto in filteredArrList le ricette preferite dall'utente
                        "Tutte" -> //metto in filteredArrList tutte le ricette dell'utente
                        */
                    }

                    // set the Filtered result to return
                    results.count = FilteredArrList.size
                    results.values = FilteredArrList
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                mDisplayedValues = results?.values as ArrayList<Recipe>
                notifyDataSetChanged()
            }

        }
    }



}