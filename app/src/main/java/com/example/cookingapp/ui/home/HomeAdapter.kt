package com.example.cookingapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.row.view.*

class HomeAdapter(private val context: Context, private val data: ArrayList<Recipe>) :
    BaseAdapter(), Filterable {
    private var mOriginalValues: ArrayList<Recipe>? = data
    private var mDisplayedValues: ArrayList<Recipe>? = data
    private var filteredValues: ArrayList<Recipe>? = data

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
                    val id=mDisplayedValues?.get(position)?.ident
                    intent.putExtra("recipe_data", id.toString())
                    intent.putExtra("chiamante", "home")
                    contesto.startActivity(intent)
                }
            })

        }

        return newView

    }

    //TODO: risolvere problema che quando si ricerca qualcosa mentre si sta filtrando torna alla lista di partenza non filtrata
    // (provare ricevendo una stringa contenente i filtri selezionati, separati l'uno dall'altro da uno spazio)
    //metodo che serve per la searchBar per filtrare i vari elementi
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                var FilteredArrList=ArrayList<Recipe>()

                //controllo se è stato selezionato il filtro "tutte"
                if(constraint.toString().equals("Tutte"))
                {
                    filteredValues= mOriginalValues!!
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                    return results
                }

                //altrimenti controllo se è stato chiamato per la ricerca o per il filtro (capendo il numero di stringhe passate alla funzione)
                val stringhe=constraint?.split(";")
                val num=stringhe?.count()

                if (mOriginalValues == null)
                    mOriginalValues = ArrayList<Recipe>(mDisplayedValues)
                if (constraint == null || constraint.isEmpty()) {

                    // set the Original result to return
                    results.count = mOriginalValues!!.size
                    results.values = mOriginalValues
                } else {

                    if(num==1)
                        FilteredArrList=ricerca(constraint)
                    else
                        FilteredArrList=filtra(constraint)

                    // set the Filtered result to return
                    /*if(FilteredArrList.size>0)
                        filteredValues=FilteredArrList*/
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

    private fun ricerca(constraint:CharSequence?): ArrayList<Recipe>
    {
        val FilteredArrList=ArrayList<Recipe>()
        lateinit var  data: String
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

    private fun filtra(constraint:CharSequence?): ArrayList<Recipe>
    {
        //Toast.makeText(context as MainActivity, "sono in filtra", Toast.LENGTH_SHORT).show()
        val FilteredArrList=ArrayList<Recipe>()
        lateinit var  data: String
        val stringhe:ArrayList<String> = constraint?.split(";") as ArrayList<String>
        stringhe.removeAt(stringhe.lastIndex)
        var tipo=""
        var durata=""
        var cont=0
        for(e in filteredValues!!.indices)
        {
            Log.v("prima", filteredValues!![e].name)
        }
        for(ele in stringhe.indices) {
            FilteredArrList.clear()
            Log.v("elemento", stringhe[ele])
            Log.v("contatore", cont.toString())

            if ((stringhe[ele].equals("Facile")) || (stringhe[ele].equals("Media")) || (stringhe[ele].equals("Difficile")))
                tipo = "difficoltà"
            if ((stringhe[ele].equals("Veloce")) || (stringhe[ele].equals("Media_durata")) || (stringhe[ele].equals("Lunga"))) {
                tipo = "durata"
            }
            if ((stringhe[ele].equals("Antipasto")) || (stringhe[ele].equals("Primo")) || (stringhe[ele].equals("Secondo")) || (stringhe[ele].equals(
                    "Dessert"
                ))
            )
                tipo = "portata"

            for (i in mOriginalValues!!.indices) {
                when(tipo){
                    "difficoltà" -> data = mOriginalValues!![i].difficoltà
                    "durata" -> data = mOriginalValues!![i].durata
                    "portata" -> data = mOriginalValues!![i].portata
                }
                //se si filtra per durata bisogna dare una significato in minuti a "Veloce", "Media_durata" e "Lunga"
                if(tipo.equals("durata")){
                    val numero=data.split(" ")
                    val num:Int=numero[0].toInt()
                    when(stringhe[ele]){
                        "Veloce" -> {
                            if (num<=20) {
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
                        "Media_durata" -> {
                            if ((num>20)&&(num<=40)) {
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
                        "Lunga" -> {
                            if (num>40) {
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
                    }
                } else{
                        if (data.startsWith(stringhe[ele])) {
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

            }
            if(FilteredArrList.size>0)
                filteredValues=FilteredArrList

            cont++

            for(e in filteredValues!!.indices)
            {
                Log.v("dopo", filteredValues!![e].name)
            }
        }


        return FilteredArrList
    }



}