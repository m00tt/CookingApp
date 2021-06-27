package com.example.cookingapp.ui.shoplist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.cookingapp.MainActivity
import com.example.cookingapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.*
import kotlinx.android.synthetic.main.row_shoplist.view.*


class ShoplistFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shoplist, container, false)
    }



    override fun onStart() {
        super.onStart()

        shoplist_fab_share.setOnClickListener {
            var go = false
            var retstr = ""
            var itemcount = 0
            var index = 0
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                itemcount = parent_lineal_layout.childCount
            }
            else{
                itemcount = parent_lineal_layout.childCount - 3
                index = 1
            }
            if (itemcount >= 1) {
                for (item in 0 until itemcount) {
                    if (parent_lineal_layout.getChildAt(index).et_shoplist_product != null) {
                        if (parent_lineal_layout[index].et_shoplist_product.text.isEmpty() || parent_lineal_layout[index].et_shoplist_qty.text.isEmpty()) {
                            Toast.makeText(
                                context as MainActivity,
                                resources.getString(R.string.shoplist_compiler_check),
                                Toast.LENGTH_SHORT
                            ).show()
                            go = false
                            break
                        } else {
                            retstr += parent_lineal_layout[index].et_shoplist_qty.text
                            if (resources.getStringArray(R.array.shoplist_units)[6].toString() != parent_lineal_layout[index].spin_shoplist_units.selectedItem.toString()) {
                                retstr += parent_lineal_layout[index].spin_shoplist_units.selectedItem.toString()
                                retstr += " ${resources.getString(R.string.shoplist_separator_preposition)}"
                            }
                            retstr += " ${parent_lineal_layout[index].et_shoplist_product.text} \n"
                            index += 1
                            go = true
                        }
                    }
                }

                if (go) {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, retstr)
                    sendIntent.type = "text/plain"
                    Intent.createChooser(sendIntent, "Share via")
                    startActivity(sendIntent)
                }

            } else {
                Toast.makeText(
                    context as MainActivity,
                    resources.getString(R.string.shopllist_fab_hint),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btn_shoplist_add.setOnClickListener {
            onAddField()
        }
    }

    //salvataggio dello stato in modo da passare valori tra Portrait e Landscape e viceversa
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val childCountParent = parent_lineal_layout.childCount
            outState.putInt("childParentCount", childCountParent)
            for((index) in (0 until childCountParent).withIndex()){
                outState.putString("product$index", parent_lineal_layout[index].et_shoplist_product.text.toString())
                outState.putString("value$index", parent_lineal_layout[index].et_shoplist_qty.text.toString())
                outState.putInt("units$index", parent_lineal_layout[index].spin_shoplist_units.selectedItemPosition)
            }
        }
        else{
            val childCountParent = parent_lineal_layout.childCount -3
            outState.putInt("childParentCount", childCountParent)
            var index = 1
            for((x) in (1..childCountParent).withIndex()){
                outState.putString("product$index", parent_lineal_layout[index].et_shoplist_product.text.toString())
                outState.putString("value$index", parent_lineal_layout[index].et_shoplist_qty.text.toString())
                outState.putInt("units$index", parent_lineal_layout[index].spin_shoplist_units.selectedItemPosition)
                index++
            }
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //Se l'istanza salvata non è null, allora ripristino i valori salvati
        if(savedInstanceState != null){
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                var index = 1
                for((x) in (1..savedInstanceState.getInt("childParentCount")).withIndex()){
                    onAddField()
                    parent_lineal_layout[index-1].et_shoplist_product.setText(savedInstanceState.getString("product$index"))
                    parent_lineal_layout[index-1].et_shoplist_qty.setText(savedInstanceState.getString("value$index"))
                    parent_lineal_layout[index-1].spin_shoplist_units.setSelection(savedInstanceState.getInt("units$index"))
                    index++
                }
            }
            else{
                for((index) in (0 until savedInstanceState.getInt("childParentCount")).withIndex()){
                    onAddField()
                    parent_lineal_layout[index+1].et_shoplist_product.setText(savedInstanceState.getString("product$index"))
                    parent_lineal_layout[index+1].et_shoplist_qty.setText(savedInstanceState.getString("value$index"))
                    parent_lineal_layout[index+1].spin_shoplist_units.setSelection(savedInstanceState.getInt("units$index"))
                }
            }


        }
    }

    fun onAddField() {
        val cnt = context as MainActivity
        val inflater = cnt.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toInflate:View = inflater.inflate(R.layout.row_shoplist, parent_lineal_layout, false)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            parent_lineal_layout.addView(toInflate, parent_lineal_layout.childCount)
        }
        else{
            parent_lineal_layout.addView(toInflate, parent_lineal_layout.childCount - 2)
        }
    }
}
