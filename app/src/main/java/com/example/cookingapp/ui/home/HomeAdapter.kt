package com.example.cookingapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.cookingapp.R

class HomeAdapter(private val context: Context, private val data: Array<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
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

            //prendiamo i dati dalla fonte di informazioni
            val recipes=data[position].split(" ")

            //assegniamo i dati alle view appena recuperate
            name.text=recipes[0]
            difficoltà.text=recipes[1]
            durata.text=recipes[2]+" minuti"
            portata.text=recipes[3]

        }

        return newView

    }

}