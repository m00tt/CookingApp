package com.example.cookingapp.ui.cookbook

import android.graphics.Color
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.example.cookingapp.RecipeActivity
import com.example.cookingapp.ui.home.HomeAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cookbook.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.list_view


class CookbookFragment : Fragment() {
    //diciamo che vogliamo il riferimento al nodo users all'interno del quale vogliamo mettere le informazioni
    var mRecipeReference: DatabaseReference? = FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app").getReference("Recipes")
    //creiamo il listener
    private var mRecipesChildListener: ChildEventListener = getRecipesChildEventListner()

    private val mRecipeArrayList = ArrayList<Recipe>()
    private var adapter1: CookbookAdapter? = null
    private lateinit var cookbookViewModel: CookbookViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        /*cookbookViewModel =
                ViewModelProvider(this).get(CookbookViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cookbook, container, false)
        val textView: TextView = root.findViewById(R.id.text_cookbook)
        cookbookViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //apertura activty ricetta

        return root*/
        return inflater.inflate(R.layout.fragment_cookbook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Ricettario"

    }
    override fun onStart() {
        super.onStart()
        //aggiungiamo il listener appena creato
        mRecipeReference!!.addChildEventListener(mRecipesChildListener)

        /*
        //aggiungo le ricette
        if(mRecipeArrayList.isEmpty()) {
            mRecipeArrayList.add(Recipe("Ricetta 1", "Facile", "15 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 2", "Media", "20 minuti", "Primo"))
            mRecipeArrayList.add(Recipe("Ricetta 3", "Facile", "10 minuti", "Secondo"))
            mRecipeArrayList.add(Recipe("Ricetta 4", "Difficile", "30 minuti", "Dessert"))
            mRecipeArrayList.add(Recipe("Ricetta 5", "Facile", "7 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 6", "Media", "25 minuti", "Primo"))
            mRecipeArrayList.add(Recipe("Ricetta 7", "Facile", "15 minuti", "Dessert"))
            mRecipeArrayList.add(Recipe("Ricetta 8", "Difficile", "60 minuti", "Antipasto"))
            mRecipeArrayList.add(Recipe("Ricetta 9", "Facile", "7 minuti", "Secondo"))
            mRecipeArrayList.add(Recipe("Ricetta 10", "Difficile", "40 minuti", "Antipasto"))
        }*/

        //carico le ricette nella listView
        adapter1 = CookbookAdapter(context as MainActivity, mRecipeArrayList)
        list_view.adapter = adapter1

        //aggiungo i listener per i 3 bottoni (per filtrare)
        btn_mieRicette.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano solo le ricette create dall'utente
                //adapter1?.filter?.filter("MieRicette")
            }
        })

        btn_preferite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano solo le ricette preferite dall'utente
                //adapter1?.filter?.filter("Preferite")
            }
        })

        btn_tutte.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si filtrano tutte le ricette dell'utente
                //adapter1?.filter?.filter("Tutte")
            }
        })

        //aggiungo il listener per il fab dell'aggiunta ricetta
        fab_add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //si apre l'activity di creazione ricetta
                val contesto = context as MainActivity
                val intent = Intent(contesto, RecipeActivity::class.java)
                intent.putExtra("chiamante", "addRicetta")
                startActivity(intent)

            }
        })

    }

    //metodo che restituisce il listener
    private fun getRecipesChildEventListner(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            //dobbiamo fare la gestione dell'operazione di lettura (ovvero quando e dove ci interessa leggere)

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildAdded: ${snapshot.key!!}")

                //prendiamo i valori dentro lo snapshot e i mettiamo in una variabile
                val newRecipe = snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo Recipe
                //prendiamo la lista dei dati che è collegata all'adapter e gli aggiungiamo il newRecipe
                mRecipeArrayList.add(Recipe(newRecipe!!.ident, newRecipe.name, newRecipe.difficoltà, newRecipe.durata, newRecipe.portata))
                //rigeneriamo l'adapter così va a rilggere i dati e si ridisegna
                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i cambiamenti dei nodi avvenuti direttamente dal DB
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildChanged: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente che arriva come snapshot e cambiare le relative informazioni
                val newRecipe = snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo User
                val recipeKey=snapshot.key
                mRecipeArrayList.find { e -> e.toString().equals(recipeKey) }?.set(newRecipe!!)

                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i nodi rimossi
            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TAG", "onChildRemoved: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente da eliminare anche in locale
                val newRecipe = snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo User
                val recipeKey=snapshot.key
                var elimineted_recipe=mRecipeArrayList.find { e -> e.toString().equals(recipeKey) }
                mRecipeArrayList.remove(elimineted_recipe)

                adapter1?.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildMoved: ${snapshot.key!!}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.toException()}")
            }

        }
        return childEventListener
    }

    override fun onStop() {
        super.onStop()

        //rimuoviamo il listener (se non è null)
        if (mRecipesChildListener != null)
            mRecipeReference!!.removeEventListener(mRecipesChildListener)
    }

    override fun onPause() {
        super.onPause()
        //svuoto l'arrayList di ricette per evitare che si duplichino quando si rientra
        mRecipeArrayList.clear()
    }

}