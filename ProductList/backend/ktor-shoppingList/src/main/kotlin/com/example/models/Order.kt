package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Order(
    val id: Int = 0,
    val customerId: Int = 0,
    val totalPrice: Double = -1.0,
    )

object OrderTable : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val customerId = integer("customerId").references(CustomerTable.id)
    val totalPrice = double("totalPrice")
}

fun ResultRow.toOrder() = Order(
    id = this[OrderTable.id],
    customerId = this[OrderTable.customerId],
    totalPrice = this[OrderTable.totalPrice]
)

fun getAllOrders() : List<Order> {
    return transaction {
        OrderTable.selectAll().map { it.toOrder() }
    }
}

fun getOrdersByCustomerId(customerId: Int) : List<Order> {
    return transaction {
        OrderTable.select { OrderTable.customerId eq customerId }.map { it.toOrder() }
    }
}

fun getOrderById(id : Int) : List<Order> {
    return transaction {
        OrderTable.select { OrderTable.id eq id }.map { it.toOrder() }
    }
}

fun placeOrder(customerId: Int, totalPrice: Double) {
    transaction {
        val customerCart = getShoppingCartByCustomerId(customerId)
        val orderId : Int
        if (customerCart.isNotEmpty()) {
            orderId = OrderTable.insert {
                it[OrderTable.customerId] = customerId
                it[OrderTable.totalPrice] = totalPrice
            } get OrderTable.id

            for(cart in customerCart) {
                insertOrderDetailsRow(orderId, cart.productId)
            }

            // after placing an order delete all products from cart
            deleteShoppingCartByCustomerId(customerId)

        } else {
            // TODO empty cart
        }
    }
}

fun updateOrder(order: Order){
    transaction {
        OrderTable.update({ ProductTable.id eq order.id }) {
            it[customerId] = order.customerId
            it[totalPrice] = order.totalPrice
        }
    }
}

fun deleteOrder(orderId : Int) {
    transaction {
        OrderTable.deleteWhere { OrderTable.id eq orderId }
        deleteOrderDetailsByOrderId(orderId)
    }
}


