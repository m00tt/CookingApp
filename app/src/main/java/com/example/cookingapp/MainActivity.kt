package com.example.cookingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shoplist.*
import kotlinx.android.synthetic.main.fragment_shoplist.view.*
import kotlinx.android.synthetic.main.row_shoplist.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }

    fun onAddField(v: View) {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_shoplist, container, false)
        // Add the new row before the add field button.
        parent_lineal_layout.addView(rowView, parent_lineal_layout.childCount - 2)
    }

    fun onDelete(v: View) {
        //if(parent_lineal_layout.childCount > 2)
            parent_lineal_layout.removeView(v.parent as View)
    }

    @SuppressLint("ResourceType")
    fun getShoplistItems(v:View) {
        var go = false
        var ret_str = ""
        val item_count = parent_lineal_layout.childCount - 2
        var index = 0
        if (item_count >= 1) {
            ret_str = ""
            for (item in 0 until item_count) {
                if (parent_lineal_layout.getChildAt(index).et_shoplist_product != null) {
                    if (parent_lineal_layout[index].et_shoplist_product.text.isEmpty() || parent_lineal_layout[index].et_shoplist_qty.text.isEmpty()) {
                        Toast.makeText(this, resources.getString(R.string.shoplist_compiler_check), Toast.LENGTH_SHORT).show()
                        go = false
                        break
                    }else{
                        ret_str += parent_lineal_layout[index].et_shoplist_qty.text
                        if(resources.getStringArray(R.array.shoplist_units)[6].toString() != parent_lineal_layout[index].spin_shoplist_units.selectedItem.toString()) {
                                ret_str += parent_lineal_layout[index].spin_shoplist_units.selectedItem.toString()
                                ret_str += " ${resources.getString(R.string.shoplist_separator_preposition)}"
                        }
                        ret_str += " ${parent_lineal_layout[index].et_shoplist_product.text} \n"
                        index += 1
                        go = true
                    }
                }
            }

            if(go) {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, ret_str)
                sendIntent.type = "text/plain"
                Intent.createChooser(sendIntent, "Share via")
                startActivity(sendIntent)
            }

        }else {
            Toast.makeText(
                this,
                resources.getString(R.string.shopllist_fab_hint),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}