package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.google.gson.GsonBuilder

import com.google.gson.Gson

interface RetrofitService {

    //Products
    @GET("product")
    fun getProducts() : Call<List<ProductRealmModel>>

    @GET("product/{id}")
    fun getProduct(@Path("id") id: Int) : Call<ProductRealmModel>

    @POST("product")
    fun createProduct(@Body product: ProductRealmModel) : Call<ProductRealmModel>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") id: Int) : Call<ProductRealmModel>

    //Customer
    @GET("customer")
    fun getCustomers() : Call<List<CustomerRealmModel>>

    @GET("user/{id}")
    fun getCustomer(@Path("id") id: Int) : Call<CustomerRealmModel>

    @POST("user")
    fun createCustomer(@Body product: String) : Call<CustomerRealmModel>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") id: Int) : Call<CustomerRealmModel>

    //Cart
    @GET("cart/{customerId}")
    fun getCartById(@Path("customerId") customerId : String) : Call<List<ShoppingCartModel>>

    @POST("cart/{customerId}/{productId}")
    fun postCartItem(@Path("customerId") customerId: String, @Path("productId") productId : Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}/{productId}")
    fun deleteCartItem(@Path("customerId") customerId: String, @Path("productId") productId: Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}")
    fun deleteCart(@Path("customerId") customerId: String) : Call<List<ShoppingCartModel>>

    //Orders
    @GET("order/customer/{customerId}")
    fun getCustomerOrders(@Path("customerId") customerId: String) : Call<List<OrderModel>>

    @POST("order/{customerId}")
    fun postCustomerOrder(@Path("customerId") customerId: String) : Call<OrderModel>

    //Order details
    @GET("oderDetails/{orderId}")
    fun getOrderDetailsById(@Path("orderId") orderId : Int) : Call<List<OrderDetailsModel>>

    @GET("orderDetails/customer/{customerId}")
    fun getCustomerOrderDetails(@Path("customerId") customerId: String) : Call<List<OrderDetailsModel>>


    companion object {

        var BASE_URL = "https://e0d2-185-58-160-75.ngrok.io"

        fun create() : RetrofitService {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(RetrofitService::class.java)

        }
    }
}