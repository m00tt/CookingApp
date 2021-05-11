package com.example.cookingapp.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import kotlinx.android.synthetic.main.row.view.*

class HomeAdapter(private val context: Context, private val data: ArrayList<Recipe>) : BaseAdapter(), Filterable {
    private var mOriginalValues : ArrayList<Recipe>? = data
    private var mDisplayedValues : ArrayList<Recipe>? = data

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
            val portata= newView.findViewById<TextView>(R.id.textViewPortata)

            //assegno i dati alle view
            name.text=mDisplayedValues?.get(position)?.name
            difficoltà.text=mDisplayedValues?.get(position)?.difficoltà
            durata.text= mDisplayedValues?.get(position)?.durata.toString()
            portata.text=mDisplayedValues?.get(position)?.portata

            //creo il istener per quando si clicca l'intera riga
            newView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //Toast.makeText(context as MainActivity, "hai cliccato la riga $position", Toast.LENGTH_SHORT).show()

                }
            })

            //creo il listener per quando si clicca il cuore
            newView.heart.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //Toast.makeText(context as MainActivity, "hai cliccato il cuore della ricetta ${mDisplayedValues?.get(position)?.name}", Toast.LENGTH_SHORT).show()

                }
            })
        }

        return newView

    }

    //metodo che serve per la searchBar per filtrare i vari elementi
    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                var cons=constraint
                var FilteredArrList=ArrayList<Recipe>()
                var results= FilterResults()

                if(mOriginalValues==null)
                    mOriginalValues=ArrayList<Recipe>(mDisplayedValues)
                if (cons == null || cons.isEmpty()) {

                    // set the Original result to return
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                } else {
                    cons = cons.toString()
                    for (i in mOriginalValues!!.indices) {
                        val data = mOriginalValues!![i].name
                        if (data.startsWith(cons.toString())) {
                            FilteredArrList.add(
                                Recipe(
                                    mOriginalValues!![i].name,
                                    mOriginalValues!![i].difficoltà,
                                    mOriginalValues!![i].durata,
                                    mOriginalValues!![i].portata
                                )
                            )
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size
                    results.values = FilteredArrList
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                mDisplayedValues=results?.values as ArrayList<Recipe>
                notifyDataSetChanged()
            }

        }
    }

}