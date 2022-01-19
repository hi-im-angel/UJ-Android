package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class ShoppingCartModel(
    var customerId: Int = 0,
    var productId: Int = 0,
    var productName: String = "",
    var productDescription: String = "",
)

open class ShoppingCartRealmModel(
    var customerId: Int = 0,
    var productId: Int = 0,
    var productName: String = "",
    var productDescription: String = "",
) : RealmObject()