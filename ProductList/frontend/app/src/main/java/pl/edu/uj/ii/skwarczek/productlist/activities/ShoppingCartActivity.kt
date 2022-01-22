package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ShoppingCartAdapter
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingCartActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var cartRecyclerView: RecyclerView
    private var shoppingCartAdapter: ShoppingCartAdapter? = null
    private lateinit var backArrowButton: Button
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        initView()
        initRecyclerView()

        backArrowButton.setOnClickListener{
            val intent = Intent(this, WishMakingActivity::class.java)
            startActivity(intent)
            finish()
        }

        shoppingCartAdapter?.setOnClickDeleteButton {
            deleteShoppingCartItem(it.customerId, it.productId)
        }

        getShoppingCartItemsByCustomerIdFromCache(currentUser.uid)
    }

    private fun getShoppingCartItemsByCustomerIdFromCache(customerId: String){
        val cartList = RealmHelper.getShoppingCartsByCustomerId(customerId)
        shoppingCartAdapter?.addItems(cartList)
    }

    private fun deleteShoppingCartItem(customerId: String, productId: Int){

        val alert = AlertDialog.Builder(this)
        alert.setMessage("Are you sure you want to delete this wish from your shopping cart? \n\nIt will be lost forever!")
        alert.setCancelable(true)
        alert.setPositiveButton("Yes") { dialog, _ ->
            RealmHelper.deleteShoppingCartByCustomerIdAndProductId(customerId, productId)
            deleteShoppingCartByProductIdAndCustomerIdFromBackend(customerId, productId)
            getShoppingCartItemsByCustomerIdFromCache(currentUser.uid)
            Toast.makeText(this, "Wish removed from shopping cart!", Toast.LENGTH_SHORT).show()
        }
        alert.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun deleteShoppingCartByProductIdAndCustomerIdFromBackend(customerId: String, productId: Int){
        val service = RetrofitService.create()
        val call = service.deleteShoppingCartByCustomerIdAndProductIdCall(customerId, productId)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful) {
                    Log.d("DELETE CART SUCCESS", response.message())
                    Toast.makeText(applicationContext, "Wish added to cart!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("DELETE CART FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("DELETE CART FAIL", t.message.toString())
            }
        })
    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        cartRecyclerView = findViewById(R.id.shopping_cart_recycler_view)
        backArrowButton = findViewById(R.id.back_arrow_cart_button)
    }

    private fun initRecyclerView(){
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        shoppingCartAdapter = ShoppingCartAdapter()
        cartRecyclerView.adapter = shoppingCartAdapter
    }

}