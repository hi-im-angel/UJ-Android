package pl.edu.uj.ii.skwarczek.productlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.uj.ii.skwarczek.productlist.R

class CartItemFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.cart_item_fragment, container, false)
        return root
    }
}