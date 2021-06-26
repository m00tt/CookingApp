package com.example.cookingapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.row.view.*

class HomeAdapter(private val context: Context, private val data: ArrayList<Recipe>) :
    BaseAdapter(), Filterable {
    private var mOriginalValues: ArrayList<Recipe>? = data
    private var mDisplayedValues: ArrayList<Recipe>? = data
    private lateinit var filteredItems: ArrayList<Recipe>

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

            //creo il istener per quando si clicca l'intera riga
            newView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //Toast.makeText(context as MainActivity, "hai cliccato la riga $position", Toast.LENGTH_SHORT).show()
                    //apertura activity ricetta singola
                    val contesto = context as MainActivity
                    val intent = Intent(contesto, RecipeActivity::class.java)
                    intent.putExtra("recipe_data", name.text.toString())
                    intent.putExtra("chiamante", "home")
                    contesto.startActivity(intent)
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
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var tipo = "ricerca"
                lateinit var  data: String
                val results = FilterResults()

                //controllo se è stato selezionato il filtro "tutte"
                if(constraint.toString().equals("Tutte"))
                {
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                    return results
                }

                //TODO: risolvere problema che quando si ricerca qualcosa mentre si sta filtrando torna alla lista di partenza non filtrata
                // (provare utilizando un arrayList che tiene salvati gli elementi salvati e viene svuotata quando si preme su "tutte le ricette")
                //controllo per vedere se la chiamata avviene dalla searchBar o dal filtro
                if ((constraint.toString().equals("Facile")) || (constraint.toString().equals("Media")) || (constraint.toString().equals("Difficile")))
                    tipo = "difficoltà"
                if ((constraint.toString().equals("Veloce")) || (constraint.toString().equals("Media_durata")) || (constraint.toString().equals("Lunga"))) {
                    tipo = "durata"
                    var durata=constraint.toString()
                }
                if ((constraint.toString().equals("Antipasto")) || (constraint.toString().equals("Primo")) || (constraint.toString().equals("Secondo")) || (constraint.toString().equals("Dessert")))
                    tipo="portata"

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
                    for (i in mOriginalValues!!.indices) {
                        //in base da dove arriva la chiamata si filtra per nome o per difficoltà o per durata o per portata
                        when(tipo){
                            "ricerca" -> data = mOriginalValues!![i].name
                            "difficoltà" -> data = mOriginalValues!![i].difficoltà
                            "durata" -> data = mOriginalValues!![i].durata
                            "portata" -> data = mOriginalValues!![i].portata
                        }

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
                        //se si filtra per durata bisogna dare una significato in minuti a "Veloce", "Media_durata" e "Lunga"
                        else if(tipo.equals("durata")){
                            val numero=data.split(" ")
                            val num:Int=numero[0].toInt()
                            when(cons){
                                "Veloce" -> {
                                    if (num<=20) {
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
                                "Media_durata" -> {
                                    if ((num>20)&&(num<=40)) {
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
                                "Lunga" -> {
                                    if (num>40) {
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
                            }
                        }
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