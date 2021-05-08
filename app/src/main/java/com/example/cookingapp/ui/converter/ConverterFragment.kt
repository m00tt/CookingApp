package com.example.cookingapp.ui.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import kotlinx.android.synthetic.main.fragment_converter.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/*
    Dichiarazione costanti utilizzate per funzioni di conversione
    Valori public per essere raggiungibili da altri fragments
*/
public val FROM_GR_TO_SPOON = 0
public val FROM_SPOON_TO_GR = 1
public val FROM_BUTTER_TO_OIL = 0
public val FROM_OIL_TO_BUTTER = 1
public val FROM_GLASS_TO_ML = 0
public val FROM_ML_TO_GLASS = 1
public val FROM_YOGURT_TO_MILK = 0
public val FROM_MILK_TO_YOGURT = 1




class ConverterFragment : Fragment() {

    private lateinit var converterViewModel: ConverterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        converterViewModel =
                ViewModelProvider(this).get(ConverterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_converter, container, false)
        //al textView: TextView = root.findViewById(R.id.text_converter)
        this.converterViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        return root
    }

    override fun onStart() {
        super.onStart()

        //Listener per il cambiamento delle unità da convertire
        converter_change_units.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val myResources = getResources()  //Verifica della resource table
                if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_gr_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_spoon_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_butter_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_oil_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_butter_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_oil_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_glass_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_ml_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_glass_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_ml_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_yogurt_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_milk_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_yogurt_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_milk_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_gr_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_spoon_text_title)
                }

                converter_et_fromconvert.setText("")
                converter_et_toconvert.setText("")
            }
        })

        //Listener per lo switch delle unità selezionate
        converter_change_values.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                var from_convert: String? = null

                if (converter_et_fromconvert.text.length > 0) {
                    from_convert = converter_et_toconvert.text.toString()
                }

                val myResources = getResources()  //Verifica della resource table
                if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_gr_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_spoon_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_gr_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_spoon_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_gr_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_spoon_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_butter_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_oil_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_butter_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_oil_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_butter_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_oil_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_glass_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_ml_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_glass_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_ml_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_glass_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_ml_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_yogurt_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_milk_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_yogurt_text_title)
                } else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_milk_text_title))) {
                    converter_tv_fromconvert.text = myResources.getText(R.string.converter_yogurt_text_title)
                    converter_tv_toconvert.text = myResources.getText(R.string.converter_milk_text_title)
                }


                if (!from_convert.equals(null)) {
                    converter_et_fromconvert.setText(from_convert)
                } else {
                    converter_et_fromconvert.setText("")
                    converter_et_toconvert.setText("")
                }
            }
        })

        //Listener per reset button
        converter_reset_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                converter_et_fromconvert.setText("")
            }
        })

        //Listener per catturare la variazione del testo all'interno della EditText da convertire
        converter_et_fromconvert.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            //Ogni volta che avviene un cambiamento del testo della EditText
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val myResources = getResources()  //Verifica della resource table
                val otherSymbols = DecimalFormatSymbols()
                otherSymbols.decimalSeparator = '.'
                otherSymbols.groupingSeparator = ','
                val precision = DecimalFormat("#.##", otherSymbols) //Dichiarazione della precisione double
                if (converter_et_fromconvert.text.length > 0) {
                    //Conversione tra GRAMMI e CUCCHIAI
                    if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_gr_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_spoon_text_title))) {
                        val x = converter_et_fromconvert.text.toString()
                        try {
                            var n_x = x.toDouble()
                            if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_gr_text_title))) {
                                n_x = gr_spoonConvertor(n_x, FROM_GR_TO_SPOON)
                            } else {
                                n_x = gr_spoonConvertor(n_x, FROM_SPOON_TO_GR)
                            }
                            converter_et_toconvert.setText(precision.format(n_x))
                        } catch (e: Exception) {
                            Toast.makeText(context as MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    //Conversione tra BURRO e OLIO
                    else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_butter_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_oil_text_title))) {
                        val x = converter_et_fromconvert.text.toString()
                        try {
                            var n_x = x.toDouble()
                            if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_butter_text_title))) {
                                n_x = butter_oilConvertor(n_x, FROM_BUTTER_TO_OIL)
                            } else {
                                n_x = butter_oilConvertor(n_x, FROM_OIL_TO_BUTTER)
                            }
                            converter_et_toconvert.setText(precision.format(n_x))
                        } catch (e: Exception) {
                            Toast.makeText(context as MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    //Conversione tra BICCHIERI e MILLILITRI
                    else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_glass_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_ml_text_title))) {
                        val x = converter_et_fromconvert.text.toString()
                        try {
                            var n_x = x.toDouble()
                            if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_glass_text_title))) {
                                n_x = glass_mlConvertor(n_x, FROM_GLASS_TO_ML)
                            } else {
                                n_x = glass_mlConvertor(n_x, FROM_ML_TO_GLASS)
                            }
                            converter_et_toconvert.setText(precision.format(n_x))
                        } catch (e: Exception) {
                            Toast.makeText(context as MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    //Conversione tra YOGURT e LATTE
                    else if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_yogurt_text_title)) || converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_milk_text_title))) {
                        val x = converter_et_fromconvert.text.toString()
                        try {
                            var n_x = x.toDouble()
                            if (converter_tv_fromconvert.text.equals(myResources.getText(R.string.converter_yogurt_text_title))) {
                                n_x = yogurt_milkConvertor(n_x, FROM_YOGURT_TO_MILK)
                            } else {
                                n_x = yogurt_milkConvertor(n_x, FROM_MILK_TO_YOGURT)
                            }
                            converter_et_toconvert.setText(precision.format(n_x))
                        } catch (e: Exception) {
                            Toast.makeText(context as MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    converter_et_toconvert.setText("")
                }

            }
        })
    }

    //salvataggio dello stato in modo da passare valori tra Portrait e Landscape
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("tv_fromconvert", converter_tv_fromconvert.text.toString())
        outState.putString("tv_toconvert", converter_tv_toconvert.text.toString())
        outState.putString("et_fromconvert", converter_et_fromconvert.text.toString())
        outState.putString("et_toconvert", converter_et_toconvert.text.toString())
    }

    //assegnamento dello stato salvato in precedenza
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Se l'istanza salvata non è null, allora ripristino i valori salvati
        if(savedInstanceState != null){
            converter_tv_fromconvert.setText(savedInstanceState.getString("tv_fromconvert"))
            converter_tv_toconvert.setText(savedInstanceState.getString("tv_toconvert"))
            converter_et_fromconvert.setText(savedInstanceState.getString("et_fromconvert"))
            converter_et_toconvert.setText(savedInstanceState.getString("et_toconvert"))
        }
    }


    /*
        FUNZIONI PUBLIC DI CONVERSIONE RAGGIUNGIBILI ANCHE DA ALTRI FRAGMENTS
    */

    //funzione di conversione da Grammi a Cucchiai, e viceversa
    public fun gr_spoonConvertor(value: Double, dir: Int): Double {
        //conversione da Grammi a Cucchiai
        if(dir == FROM_GR_TO_SPOON){
            return value / 15
        }
        //conversione da Cucchiai a Grammi
        else{
            return value * 15
        }

    }

    //funzione di conversione da Burro a Olio, e viceversa
    public fun butter_oilConvertor(value: Double, dir: Int): Double {
        //conversione da Burro a Olio
        if(dir == FROM_BUTTER_TO_OIL){
            return (value * 80) / 100
        }
        //conversione da Olio a Burro
        else{
            return (value * 100) / 80
        }

    }

    //funzione di conversione da Bicchieri a Millilitri, e viceversa
    public fun glass_mlConvertor(value: Double, dir: Int): Double {
        //conversione da Bicchiere a ML
        if(dir == FROM_GLASS_TO_ML){
            return value * 200
        }
        //conversione da ML a Bicchiere
        else{
            return value / 200
        }
    }

    //funzione di conversione da Yogurt a Latte, e viceversa
    public fun yogurt_milkConvertor(value: Double, dir: Int): Double {
        //conversione da Yogurt a Latte
        if(dir == FROM_YOGURT_TO_MILK){
            return (value * 80) / 100
        }
        //conversione da Latte a Yogurt
        else{
            return (value * 100) / 80
        }

    }


}