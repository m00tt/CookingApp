package com.example.cookingapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import com.example.cookingapp.Recipe
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    //diciamo che vogliamo il riferimento al nodo users all'interno del quale vogliamo mettere le informazioni
    private var mRecipeReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Recipes")

    //creiamo il listener
    private var mRecipesChildListener: ChildEventListener = getRecipesChildEventListner()

    //private lateinit var homeViewModel: HomeViewModel
    private val mRecipeArrayList = ArrayList<Recipe>()
    private var adapter1: HomeAdapter? = null
    lateinit var menu: Menu
    lateinit var checkati: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listView: ListView = root.findViewById(R.id.list_view)
        homeViewModel.ricette.observe(viewLifecycleOwner, Observer {
            listView.adapter=HomeAdapter(context as MainActivity, it)
        })
        return root*/

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onStart() {
        super.onStart()
        //aggiungiamo il listener appena creato
        mRecipeReference.addChildEventListener(mRecipesChildListener)

        //carico le ricette nella listView
        adapter1 = HomeAdapter(context as MainActivity, mRecipeArrayList)
        list_view.adapter = adapter1

        //aggiungo il listener alla edit_text che fa da searchBar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Call back the Adapter with current character to Filter
                adapter1?.filter?.filter(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        val popup = PopupMenu(context as MainActivity, img_filter)
        popup.setOnMenuItemClickListener(this@HomeFragment)
        popup.inflate(R.menu.popup_menu)
        menu = popup.menu

        //aggiungo il listener all'imageButton per il filtro
        img_filter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                popup.show()
            }
        })
    }


    //metodo che gestisce l'evento click degli elementi del popup_menu
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //gestione difficoltà
            R.id.Facile -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Facile", Toast.LENGTH_SHORT).show()
                setFilter(item,R.id.Media, R.id.Difficile)
                return true
            }
            R.id.Media -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Media", Toast.LENGTH_SHORT).show()
                setFilter(item,R.id.Facile, R.id.Difficile)
                return true
            }
            R.id.Difficile -> {
                //Toast.makeText(context as MainActivity, "hai selezionato difficolta' Difficile", Toast.LENGTH_SHORT).show()
                setFilter(item,R.id.Media, R.id.Facile)
                return true
            }

            //gestione durata
            R.id.Veloce -> {
                setFilter(item,R.id.Media_durata, R.id.Lunga)
                return true
            }
            R.id.Media_durata -> {
                setFilter(item,R.id.Veloce, R.id.Lunga)
                return true
            }
            R.id.Lunga -> {
                setFilter(item,R.id.Veloce, R.id.Media_durata)
                return true
            }

            //gestione portata
            R.id.Antipasto -> {
                setFilter_por(item,R.id.Primo,R.id.Secondo,R.id.Dessert)
                return true
            }
            R.id.Primo -> {
                setFilter_por(item,R.id.Antipasto,R.id.Secondo,R.id.Dessert)
                return true
            }
            R.id.Secondo -> {
                setFilter_por(item,R.id.Primo,R.id.Antipasto,R.id.Dessert)
                return true
            }
            R.id.Dessert -> {
                setFilter_por(item,R.id.Primo,R.id.Secondo,R.id.Antipasto)
                return true
            }

            //tutte le ricette
            R.id.Tutte -> {
                searchBar.text.clear()
                menu.findItem(R.id.Facile).setChecked(false)
                menu.findItem(R.id.Media).setChecked(false)
                menu.findItem(R.id.Difficile).setChecked(false)
                menu.findItem(R.id.Veloce).setChecked(false)
                menu.findItem(R.id.Media_durata).setChecked(false)
                menu.findItem(R.id.Lunga).setChecked(false)
                menu.findItem(R.id.Antipasto).setChecked(false)
                menu.findItem(R.id.Primo).setChecked(false)
                menu.findItem(R.id.Secondo).setChecked(false)
                menu.findItem(R.id.Dessert).setChecked(false)
                checkati = controlCheck()
                adapter1?.filter?.filter(checkati)
                return true
            }

            else -> return false
        }
    }

    private fun setFilter(item:MenuItem, id1:Int, id2:Int)
    {
        searchBar.text.clear()
        menu.findItem(id1).setChecked(false)
        menu.findItem(id2).setChecked(false)
        setCheck(item)
        checkati = controlCheck()
        adapter1?.filter?.filter(checkati)
    }

    private fun setFilter_por(item:MenuItem, id1:Int, id2:Int, id3:Int)
    {
        searchBar.text.clear()
        menu.findItem(id1).setChecked(false)
        menu.findItem(id2).setChecked(false)
        menu.findItem(id3).setChecked(false)
        setCheck(item)
        checkati = controlCheck()
        adapter1?.filter?.filter(checkati)
    }

    private fun setCheck(item: MenuItem?) {
        item!!.setChecked(!item.isChecked)
    }

    private fun controlCheck(): String {
        var controllo = ""

        if (menu.findItem(R.id.Facile).isChecked)
            controllo += "Facile;"
        if (menu.findItem(R.id.Media).isChecked)
            controllo += "Media;"
        if (menu.findItem(R.id.Difficile).isChecked)
            controllo += "Difficile;"
        if (menu.findItem(R.id.Veloce).isChecked)
            controllo += "Veloce;"
        if (menu.findItem(R.id.Media_durata).isChecked)
            controllo += "Media_durata;"
        if (menu.findItem(R.id.Lunga).isChecked)
            controllo += "Lunga;"
        if (menu.findItem(R.id.Antipasto).isChecked)
            controllo += "Antipasto;"
        if (menu.findItem(R.id.Primo).isChecked)
            controllo += "Primo;"
        if (menu.findItem(R.id.Secondo).isChecked)
            controllo += "Secondo;"
        if (menu.findItem(R.id.Dessert).isChecked)
            controllo += "Dessert;"

        if (controllo == "")
            controllo = "Tutte"

        //Toast.makeText(context as MainActivity, controllo, Toast.LENGTH_SHORT).show()
        return controllo
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
                var tmp = dur.split(" ")
                dur = tmp[0] + " " + getString(R.string.minutes)

                mRecipeArrayList.add(Recipe(newRecipe!!.ident, newRecipe.name, diff, dur, port))
                //rigeneriamo l'adapter così va a rilggere i dati e si ridisegna
                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i cambiamenti dei nodi avvenuti direttamente dal DB
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildChanged: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente che arriva come snapshot e cambiare le relative informazioni
                val newRecipe =
                    snapshot.getValue(Recipe::class.java) //in questo modo ritorna un oggetto di tipo ricetta
                val recipeKey = snapshot.key
                mRecipeArrayList.find { e -> e.toString().equals(recipeKey) }?.set(newRecipe!!)

                adapter1?.notifyDataSetChanged()
            }

            //se vogliamo gestire anche i nodi rimossi
            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TAG", "onChildRemoved: ${snapshot.key!!}")

                //prendiamo i dati dallo snapshot e vediamo a quale utente corrisponde l'utente da eliminare anche in locale
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
        //rimuoviamo il listener
        mRecipeReference.removeEventListener(mRecipesChildListener)
    }

    override fun onPause() {
        super.onPause()
        //svuoto l'arrayList di ricette per evitare che si duplichino quando si rientra
        mRecipeArrayList.clear()
    }

}
