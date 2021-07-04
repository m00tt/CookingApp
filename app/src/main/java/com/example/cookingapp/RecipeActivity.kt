package com.example.cookingapp

import android.accessibilityservice.GestureDescription
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.row_ingredient.*
import kotlinx.android.synthetic.main.row_ingredient.view.*
import kotlinx.android.synthetic.main.row_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.HashMap
import java.util.jar.Manifest
import kotlin.concurrent.thread


class RecipeActivity : AppCompatActivity() {
    //diciamo che vogliamo il riferimento al nodo users all'interno del quale vogliamo mettere le informazioni
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var mRecipeReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Recipes")
    private val mUserRecipesReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users_recipes").child(userID)
    private val mFavouriteReference: DatabaseReference  = FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app").getReference("Favourites").child(userID)


    private val OPERATION_CAPTURE_PHOTO = 1

    var editable = false
    var prefer = false
    var actualDose = 0
    var chiamante = ""
    var idRicetta=""
    var ricetta=Recipe()
    lateinit var fotoRicetta: Intent
    //dichiarazione attributi ricetta letti da db
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        et_min.isEnabled=false
        et_min2.isEnabled=false
        et_min.setTextColor(Color.LTGRAY)
        et_min2.setTextColor(Color.LTGRAY)
        et_min.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        et_min2.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        //CONTROLLO CHIAMANTI E SETTAGGIO AZIONI
        chiamante = intent.getStringExtra("chiamante").toString()
        when (chiamante) {
            "home" -> {
                Log.v("chiamante", "homepage")
                fab_edit.hide()
                btn_ingredient_add.visibility = View.INVISIBLE

                idRicetta = intent.getStringExtra("recipe_data")!!
                leggiRicettaDB(idRicetta)

                setEditable(Color.TRANSPARENT, false)
                et_preparazione.setTextColor(Color.LTGRAY)
                et_dosi.setTextColor(Color.BLACK)
                et_cottura.setTextColor(Color.LTGRAY)

                et_dosi.isEnabled = true
                et_dosi.background.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)

                //leggere ricetta DB

            }
            "addRicetta" -> {
                btn_ingredient_add.visibility = View.VISIBLE
                setEditable(Color.BLACK, true)
                editable = true
                fab_edit.setImageResource(R.mipmap.ic_save_foreground)
                img_heart.visibility = View.INVISIBLE

            }
            else -> {
                setEditable(Color.TRANSPARENT, false)

                et_preparazione.setTextColor(Color.LTGRAY)
                et_dosi.setTextColor(Color.LTGRAY)
                et_cottura.setTextColor(Color.LTGRAY)
                btn_ingredient_add.visibility = View.INVISIBLE

                idRicetta = intent.getStringExtra("recipe_data")!!
                leggiRicettaDB(idRicetta)
            }

        }

        et_dosi.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        et_preparazione.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )
        et_cottura.setInputType(
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED
        )

        btn_back_recipe.setOnClickListener{
            finish()
        }

    }



    override fun onStart() {
        super.onStart()
        fab_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!editable)
                    editRecipe()
                else {
                    saveRecipe()
                }
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
            if(editable) {
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

        imgDeleteImage.setOnClickListener {
            if(editable){
                FirebaseStoreManager().onDeleteImage(this, idRicetta, resources.getString(R.string.deletingImageWaiting), resources.getString(R.string.deletingDoneImage), resources.getString(R.string.uploading_error))
                img_recipe.setImageBitmap(null)
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
                    fotoRicetta = data
                }
            }
        }
    }


    fun setEditable(colore: Int, action: Boolean) {
        //add black underline
        et_recipe_name.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_recipe_name.setTextColor(resources.getColor(R.color.scritte))
        et_preparazione.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_dosi.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)
        et_cottura.background.setColorFilter(colore, PorterDuff.Mode.SRC_IN)

        et_preparazione.setTextColor(Color.BLACK)
        et_dosi.setTextColor(Color.BLACK)
        et_cottura.setTextColor(Color.BLACK)
        //set editText editable
        et_recipe_name.isEnabled = action
        et_preparazione.isEnabled = action
        sp_portata.isEnabled = action
        et_dosi.isEnabled = action
        sp_difficolta.isEnabled = action
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

        editable = true

        imgDeleteImage.visibility = View.VISIBLE

        //set btn add ingredient visible
        btn_ingredient_add.visibility = View.VISIBLE

        val childCountParent = linear_ingredienti.childCount
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

        val childCountParent = linear_ingredienti.childCount

        //prendo tutti i campi da inserire nel DB
        val nome = (et_recipe_name.text).toString()
        var difficoltà = sp_difficolta.selectedItem.toString()
        val preparazione = et_preparazione.text.toString()
        val cottura = et_cottura.text.toString()
        et_preparazione.setText(preparazione)
        et_cottura.setText(cottura)
        val dosi = et_dosi.text.toString()
        var portata = sp_portata.selectedItem.toString()
        val ingredienti = ArrayList<String>()
        val descrizione = et_preparazione_descrizione.text.toString()
        val conservazione = et_conservazione.text.toString()

        when(sp_difficolta.selectedItemPosition)
        {
            0->difficoltà="Facile"
            1->difficoltà="Media"
            2->difficoltà="Difficile"
        }
        when(sp_portata.selectedItemPosition)
        {
            0->portata="Antipasto"
            1->portata="Primo"
            2->portata="Secondo"
            3->portata="Dessert"
        }
        //remove black underline
        setEditable(Color.TRANSPARENT, false)

        et_recipe_name.setTextColor(resources.getColor(R.color.scritte))
        et_preparazione.setTextColor(Color.LTGRAY)
        et_dosi.setTextColor(Color.LTGRAY)
        et_cottura.setTextColor(Color.LTGRAY)

        Log.v("saveRecipe", "SaveRecipe")


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


                    val tmp =
                        linear_ingredienti[index].et_recipe_ingredient.text.toString() + ";" + linear_ingredienti[index].et_ingredient_qty.text.toString() +
                                ";" + linear_ingredienti[index].spin_ingredient_units.selectedItem.toString()
                    ingredienti.add(tmp)
                }
                if (linear_ingredienti[index].iv_ingredient_delete != null)
                    linear_ingredienti[index].iv_ingredient_delete.visibility = View.INVISIBLE
            }

        }


        inserisciRicetta(nome, difficoltà, preparazione, cottura, dosi, portata, ingredienti, descrizione, conservazione)
        //ESEMPIO AGGIUNTA FOTO NELLO STORAGE (imgName = ID della ricetta)

        FirebaseStoreManager().onCaptureImageData(this, fotoRicetta, idRicetta, resources.getString(R.string.photo_uploading_message), resources.getString(R.string.uploading_done), resources.getString(R.string.uploading_error))


        editable = false
        fab_edit.setImageResource(R.mipmap.ic_pencil_foreground)
    }

    fun setPrefer(view: View) {
        if (!prefer) {
            img_heart.setImageResource(R.drawable.heart_red)
            prefer = true
            favourite("inserisci")
        } else {
            img_heart.setImageResource(R.drawable.heart)
            prefer = false
            favourite("togli")
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
        Log.e("ciao", ricetta.dosi)
        var newQuantita = 0
        var childCountParent = linear_ingredienti.childCount
        var c = 0
        for ((index) in (1 until childCountParent).withIndex()) {
            Log.v("INDEX",index.toString())
            if (index > 0) {
                var tmp = ricetta.ingredienti[c].split(";")
                newQuantita = (((tmp[1].toInt() * actualDose) / ricetta.dosi.toInt()))
                Log.v("nome ingrediente", tmp[0])
                Log.v("dose attuale", actualDose.toString())
                Log.v("newQuantita", newQuantita.toString())
                linear_ingredienti[index].et_ingredient_qty.setText(newQuantita.toString())

                c++
            }
        }

    }

    fun leggiRicettaDB(idRicetta: String) {
        mRecipeReference.orderByChild("ident").equalTo(idRicetta)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    //prendo il nodo interessato e lo metto in un oggetto di tipo Ricetta
                    ricetta = dataSnapshot.getValue(Recipe::class.java)!!
                    Log.e("nome", ricetta.name)
                    //setto tutti i campi con i dati relativi a quella determinata ricetta
                    et_recipe_name.setText(ricetta.name)
                    when(ricetta.difficoltà)
                    {
                        "Facile"->{ sp_difficolta.setSelection(0) }
                        "Media"->{sp_difficolta.setSelection(1)}
                        "Difficile"->{sp_difficolta.setSelection(2)}
                    }
                    val prepTMP = ricetta.preparazione.split(" ")
                    val cottTMP = ricetta.cottura.split(" ")
                    et_preparazione.setText(prepTMP[0])
                    et_cottura.setText(cottTMP[0])
                    et_dosi.setText(ricetta.dosi)
                    when(ricetta.portata)
                    {
                        "Antipasto"->{ sp_portata.setSelection(0) }
                        "Primo"->{sp_portata.setSelection(1)}
                        "Secondo"->{sp_portata.setSelection(2)}
                        "Dessert"->{sp_portata.setSelection(3)}
                    }
                    val ingredienti: ArrayList<String> = ricetta.ingredienti
                    var count = 1
                    for (i in ingredienti) {
                        val tmp = i.split(";")
                        onAddIngredient()

                        linear_ingredienti[count].et_recipe_ingredient.setText(tmp[0])
                        linear_ingredienti[count].et_ingredient_qty.setText(tmp[1])

                        var selection = 0
                        when (tmp[2]) {
                            "ML" -> {
                                selection = 0
                            }
                            "CL" -> {
                                selection = 1
                            }
                            "L" -> {
                                selection = 2
                            }
                            "GR" -> {
                                selection = 3
                            }
                            "HG" -> {
                                selection = 4
                            }
                            "KG" -> {
                                selection = 5
                            }
                            "Qty" -> {
                                selection = 6
                            }
                        }
                        linear_ingredienti[count].spin_ingredient_units.setSelection(selection)
                        Log.v("sd", i)
                        linear_ingredienti[count].et_recipe_ingredient.isEnabled=false
                        linear_ingredienti[count].et_ingredient_qty.isEnabled=false
                        linear_ingredienti[count].spin_ingredient_units.isEnabled=false
                        linear_ingredienti[count].iv_ingredient_delete.visibility=View.INVISIBLE
                        count++
                    }
                    et_preparazione_descrizione.setText(ricetta.descrizione)
                    et_conservazione.setText(ricetta.conservazione)

                    //CATTURA IMMAGINE RICETTA DAL DB
                    val mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
                    val idImgRef = mStorageReference.child("images/${idRicetta}.jpg")
                    thread {
                        idImgRef.getBytes(1024 * 1024).addOnSuccessListener {
                            img_recipe.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                        }.addOnFailureListener {
                            Log.e("IMAGE DOWNLOAD", "Error")
                        }
                    }

                    if (ricetta.preferiti) {
                        img_heart.setImageResource(R.drawable.heart_red)
                    }

                    //editabilità item row_ingredient
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("The read failed: ", error.getMessage())
                }
            })
    }

    fun aggiuntaImmagine(id:String)
    {
        FirebaseStoreManager().onCaptureImageData(this, fotoRicetta, id, resources.getString(R.string.photo_uploading_message), resources.getString(R.string.uploading_done), resources.getString(R.string.uploading_error))

    }
    fun inserisciRicetta(nome: String, difficoltà: String, preparazione: String, cottura: String, dosi: String, portata: String, ingredienti: ArrayList<String>, descrizione: String, conservazione: String)
    {
        mRecipeReference.orderByChild("ident").equalTo(idRicetta).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) { //eseguo l'update della ricetta già esistente
                    Log.e("esiste","lo snapshot esiste")
                    //calcolo la durata
                    val durata = (cottura.toInt()) + (cottura.toInt())
                    //si va a modificare la ricetta già esistente
                    Log.e("dosi",dosi)
                    val ricetta_modificata=Recipe(idRicetta,nome, difficoltà, preparazione, cottura,
                        "$durata minuti", dosi, portata, ingredienti, descrizione, conservazione, prefer)
                    mRecipeReference.child(ricetta_modificata.toString()).setValue(ricetta_modificata)
                    aggiuntaImmagine(idRicetta)
                }
                else { //inserisco la ricetta nel DB
                    Log.e("non esiste", "lo snapshot non esiste")
                    //inserimento nel DB
                    val durata = (cottura.toInt()) + (cottura.toInt())
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
                    val nuova_ricetta = Recipe(
                        id, nome, difficoltà, "$preparazione minuti", "$cottura minuti",
                        "$durata minuti", dosi, portata, ingredienti, descrizione, conservazione, prefer
                    )
                    aggiuntaImmagine(id)
                    mRecipeReference.child(nuova_ricetta.toString()).setValue(nuova_ricetta)
                    //si aggiunge la ricetta alla lista delle ricette create dall'utente
                    mUserRecipesReference.child(id).setValue(id)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun favourite(comando:String) {
        if (comando.equals("inserisci")) {
            mFavouriteReference.child(idRicetta).setValue(idRicetta)
            mRecipeReference.child(idRicetta).child("preferiti").setValue(true)
        }
        if (comando.equals("togli")) {
            mFavouriteReference.child(idRicetta).removeValue()
            mRecipeReference.child(idRicetta).child("preferiti").setValue(false)
        }
    }






}