package com.example.cookingapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.row_ingredient.*
import kotlinx.android.synthetic.main.row_ingredient.view.*
import kotlinx.android.synthetic.main.row_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.view.*
import java.time.LocalDateTime


class RecipeActivity : AppCompatActivity() {
    //diciamo che vogliamo il riferimento al nodo users all'interno del quale vogliamo mettere le informazioni
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var mRecipeReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Recipes")
    private val mUserRecipesReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users_recipes").child(userID)

    private val OPERATION_CAPTURE_PHOTO = 1

    var editable = false
    var prefer = false
    var actualDose = 0
    var initialDose = 0
    var chiamante = ""

    //dichiarazione attributi ricetta letti da db
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)


        //CONTROLLO CHIAMANTI E SETTAGGIO AZIONI
        chiamante = intent.getStringExtra("chiamante").toString()
        when (chiamante) {
            "home" -> {
                Log.v("chiamante", "homepage")
                fab_edit.hide()
                btn_ingredient_add.visibility = View.INVISIBLE
                val nome = intent.getStringExtra("recipe_data")
                et_recipe_name.setText(nome)
                setEditable(Color.TRANSPARENT, false)
                et_dosi.isEnabled = true
                et_dosi.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

                //leggere ricetta DB

            }
            "addRicetta" -> {
                et_recipe_name.setText("Inserisci il nome della ricetta")
                btn_ingredient_add.visibility = View.VISIBLE
                setEditable(Color.BLACK, true)
                editable = true
                fab_edit.setImageResource(R.mipmap.ic_save_foreground)
            }
            else -> {
                setEditable(Color.TRANSPARENT, false)
                btn_ingredient_add.visibility = View.INVISIBLE
                val idRicetta = intent.getStringExtra("recipe_data")
                if (idRicetta != null) {
                    leggiRicettaDB(idRicetta)
                }
            }

        }

        et_dosi.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )

    }


    override fun onStart() {
        super.onStart()
        fab_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!editable)
                    editRecipe()
                else
                    saveRecipe()
            }

        })



        btn_ingredient_add.setOnClickListener {
            onAddIngredient()
        }

        et_dosi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (chiamante == "home") {
                    if (!et_dosi.text.isEmpty()) {
                        actualDose = et_dosi.text.toString().toInt()
                        Log.e("valore actual", actualDose.toString())
                    }
                    dosesProportion()
                }
            }
        })


        img_recipe.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    OPERATION_CAPTURE_PHOTO
                )
            } else {
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, OPERATION_CAPTURE_PHOTO)
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPERATION_CAPTURE_PHOTO && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            img_recipe.performClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    img_recipe.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT)
            }
        }
    }


    fun setEditable(colore: Int, action: Boolean) {
        //add black underline
        et_recipe_name.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_preparazione.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_portata.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_difficolta.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)


        //set editText editable
        et_recipe_name.isEnabled = action
        et_preparazione.isEnabled = action
        et_portata.isEnabled = action
        et_dosi.isEnabled = action
        et_difficolta.isEnabled = action
        et_cottura.isEnabled = action


        et_preparazione_descrizione.isEnabled = action
        et_conservazione.isEnabled = action

        if (et_preparazione_descrizione != null && et_conservazione != null) {
            et_preparazione_descrizione.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
            et_conservazione.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        }
    }

    fun editRecipe() {

        setEditable(Color.BLACK, true)

        Log.v("editRecipe", "editRecipe")
        editable = true

        //set btn add ingredient visible
        btn_ingredient_add.visibility = View.VISIBLE

        var childCountParent = linear_ingredienti.childCount
        for ((index) in (1 until childCountParent).withIndex()) {
            if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && linear_ingredienti[index].spin_ingredient_units != null && linear_ingredienti[index].iv_ingredient_delete != null) {
                linear_ingredienti[index].et_recipe_ingredient.isEnabled = true
                linear_ingredienti[index].et_ingredient_qty.isEnabled = true
                linear_ingredienti[index].spin_ingredient_units.isEnabled = true
                linear_ingredienti[index].iv_ingredient_delete.isEnabled = true
                if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && et_preparazione_descrizione != null && et_conservazione != null) {
                    linear_ingredienti[index].et_recipe_ingredient.background.setColorFilter(
                        Color.BLACK,
                        PorterDuff.Mode.SRC_IN
                    )
                    linear_ingredienti[index].et_ingredient_qty.background.setColorFilter(
                        Color.BLACK,
                        PorterDuff.Mode.SRC_IN
                    )
                }
                if (linear_ingredienti[index].iv_ingredient_delete != null)
                    linear_ingredienti[index].iv_ingredient_delete.visibility = View.VISIBLE
            }
        }

        fab_edit.setImageResource(R.mipmap.ic_save_foreground)

    }

    fun saveRecipe() {
        var childCountParent = linear_ingredienti.childCount

        //prendo tutti i campi da inserire nel DB
        var nome = (et_recipe_name.text).toString()
        var difficoltà = et_difficolta.text.toString()
        var preparazione = et_preparazione.text.toString()
        var cottura = et_cottura.text.toString()
        var dosi = et_dosi.text.toString()
        var portata = et_portata.text.toString()
        var ingredienti = ArrayList<String>()
        var descrizione = et_preparazione_descrizione.text.toString()
        var conservazione = et_conservazione.text.toString()

        //remove black underline
        setEditable(Color.TRANSPARENT, false)

        et_recipe_name.setTextColor(Color.BLACK)
        et_preparazione.setTextColor(resources.getColor(R.color.scritte))
        et_portata.setTextColor(resources.getColor(R.color.scritte))
        et_dosi.setTextColor(resources.getColor(R.color.scritte))
        et_difficolta.setTextColor(resources.getColor(R.color.scritte))
        et_cottura.setTextColor(resources.getColor(R.color.scritte))

        Log.v("saveRecipe", "SaveRecipe")
        editable = false

        //set btn add ingredient not visible
        btn_ingredient_add.visibility = View.INVISIBLE


        for ((index) in (1 until childCountParent).withIndex()) {
            if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && linear_ingredienti[index].spin_ingredient_units != null && linear_ingredienti[index].iv_ingredient_delete != null) {
                linear_ingredienti[index].et_recipe_ingredient.isEnabled = false
                linear_ingredienti[index].et_ingredient_qty.isEnabled = false
                linear_ingredienti[index].spin_ingredient_units.isEnabled = false
                linear_ingredienti[index].iv_ingredient_delete.isEnabled = false
                if (linear_ingredienti[index].et_recipe_ingredient != null && linear_ingredienti[index].et_ingredient_qty != null && et_preparazione_descrizione != null && et_conservazione != null) {
                    linear_ingredienti[index].et_recipe_ingredient.background.setColorFilter(
                        Color.TRANSPARENT,
                        PorterDuff.Mode.SRC_IN
                    )
                    linear_ingredienti[index].et_ingredient_qty.background.setColorFilter(
                        Color.TRANSPARENT,
                        PorterDuff.Mode.SRC_IN
                    )


                    var tmp =
                        linear_ingredienti[index].et_recipe_ingredient.text.toString() + ";" + linear_ingredienti[index].et_ingredient_qty.text.toString() +
                                ";" + linear_ingredienti[index].spin_ingredient_units.selectedItem.toString()
                    ingredienti.add(tmp)
                }
                if (linear_ingredienti[index].iv_ingredient_delete != null)
                    linear_ingredienti[index].iv_ingredient_delete.visibility = View.INVISIBLE
            }

            /*
            et_shoplist_product.isEnabled = false
            et_shoplist_qty.isEnabled = false
            spin_shoplist_units.isEnabled = false
            iv_shoplist_delete.isEnabled = false*/
        }

        fab_edit.setImageResource(R.mipmap.ic_pencil_foreground)

        //inserimento nel DB
        val nCottura = cottura.split(" ")
        val nPreparazione = preparazione.split(" ")
        val durata = (nCottura[0].toInt()) + (nPreparazione[0].toInt())
        val id_tmp = nome + LocalDateTime.now()
        var id = ""
        //per togliere i . e i : dall'id (non supportati da firebase)
        for (i in id_tmp.indices) {
            if (id_tmp[i] == '.')
                continue
            if (id_tmp[i] == ':')
                continue
            id += id_tmp[i]


        }

        val ricetta = Recipe(
            id, nome, difficoltà, preparazione, cottura,
            "$durata minuti", dosi, portata, ingredienti, descrizione, conservazione, prefer
        )
        mRecipeReference?.child(ricetta.toString())?.setValue(ricetta)

    }

    fun setPrefer(view: View) {
        if (!prefer) {
            img_heart.setImageResource(R.drawable.heart_red)
            prefer = true
        } else {
            img_heart.setImageResource(R.drawable.heart)
            prefer = false
        }
    }

    fun onAddIngredient() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toInflate: View = inflater.inflate(R.layout.row_ingredient, linear_ingredienti, false)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linear_ingredienti.addView(toInflate, linear_ingredienti.childCount)
        } else {
            linear_ingredienti.addView(toInflate, linear_ingredienti.childCount - 1)
        }
    }

    fun onDeleteIngredient(v: View) {
        linear_ingredienti.removeView(v.parent as View)
    }

    fun dosesProportion() {
        ///TODO: realizzare funzione di proporzione fra le dosi, usare variabili lette da db

        if (initialDose != 0) {
            Log.v("valore actual", actualDose.toString())
            Log.v("valore initial", initialDose.toString())
        }
        initialDose = actualDose
    }

    fun leggiRicettaDB(idRicetta: String) {
        mRecipeReference.orderByChild("ident").equalTo(idRicetta)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    println(dataSnapshot.key)

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }



}