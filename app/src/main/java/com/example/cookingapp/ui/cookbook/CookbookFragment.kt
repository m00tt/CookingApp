package com.example.cookingapp.ui.cookbook

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.example.cookingapp.RecipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cookbook.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.list_view
import kotlinx.android.synthetic.main.row.*


class CookbookFragment : Fragment() {
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
    private val mRecipeReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Recipes")
    private val mFavouriteReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Favourites").child(userID)
    private val mUserRecipesReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users_recipes").child(userID)

    //creiamo il listener
    private val mRecipesChildListener: ChildEventListener = getRecipesChildEventListner()

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

        //di default si mettono le ricette create dall'utente
        changeTextButton(btn_mieRicette,18F,true)
        changeTextButton(btn_preferite,14F,false)
        loadRecipes(mUserRecipesReference)


        //aggiungo i listener per i bottoni (per filtrare)
        btn_mieRicette.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //mostro il bottone selezionato
                changeTextButton(btn_mieRicette,18F,true)
                changeTextButton(btn_preferite,14F,false)
                //si filtrano solo le ricette create dall'utente
                loadRecipes(mUserRecipesReference)
            }
        })

        btn_preferite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //mostro il bottone selezionato
                changeTextButton(btn_preferite,18F,true)
                changeTextButton(btn_mieRicette,14F,false)
                //si filtrano solo le ricette preferite dall'utente
                loadRecipes(mFavouriteReference)
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
                val newRecipe =
                    snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo Ricetta
                //prendiamo la lista dei dati che è collegata all'adapter e gli aggiungiamo il newRecipe
                var diff = ""
                var port = ""
                var dur = newRecipe!!.durata
                when (newRecipe.difficoltà) {
                    "Facile" -> {
                        diff = getString(R.string.popup_facile)
                    }
                    "Media" -> {
                        diff = getString(R.string.popup_medio)
                    }
                    "Difficile" -> {
                        diff = getString(R.string.popup_difficile)
                    }
                }
                when (newRecipe.portata) {
                    "Antipasto" -> {
                        port = getString(R.string.popup_antipasto)
                    }
                    "Primo" -> {
                        port = getString(R.string.popup_primo)
                    }
                    "Secondo" -> {
                        port = getString(R.string.popup_secondo)
                    }
                    "Dessert" -> {
                        port = getString(R.string.popup_dessert)
                    }
                }
                val tmp = dur.split(" ")
                dur = tmp[0] + " " + getString(R.string.minutes)


                mRecipeArrayList.add(Recipe(newRecipe.ident, newRecipe.name, diff, dur, port))
                //rigeneriamo l'adapter così va a rilggere i dati e si ridisegna
                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i cambiamenti dei nodi avvenuti direttamente dal DB
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildChanged: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente che arriva come snapshot e cambiare le relative informazioni
                val newRecipe =
                    snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo Ricetta
                val recipeKey = snapshot.key
                mRecipeArrayList.find { e -> e.toString().equals(recipeKey) }?.set(newRecipe!!)

                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i nodi rimossi
            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TAG", "onChildRemoved: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente da eliminare anche in locale
                val newRecipe =
                    snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo Ricetta
                val recipeKey = snapshot.key
                val elimineted_recipe =
                    mRecipeArrayList.find { e -> e.toString().equals(recipeKey) }
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
        mRecipeReference.removeEventListener(mRecipesChildListener)
    }

    override fun onPause() {
        super.onPause()
        //svuoto l'arrayList di ricette per evitare che si duplichino quando si rientra e si refreshano
        loadRecipes(mUserRecipesReference)
    }

    private fun loadRecipes(riferimento: DatabaseReference)
    {
        //svuoto l'arrayList di ricette per evitare che si duplichino quando si rientra
        mRecipeArrayList.clear()
        //prendo tutte le ricette create dall'utente o tutte le ricette preferite dall'utente
        riferimento.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children)
                {
                    val r=ds.key
                    mRecipeReference.orderByChild("ident").equalTo(r).addChildEventListener(mRecipesChildListener)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //carico le ricette nella listView
        adapter1 = CookbookAdapter(context as MainActivity, mRecipeArrayList)
        list_view.adapter = adapter1
    }

    private fun changeTextButton(b: Button, size: Float, selected:Boolean )
    {
        b.textSize=size
        if(selected)
            b.paintFlags = b.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        else
            b.paintFlags=0
    }


}