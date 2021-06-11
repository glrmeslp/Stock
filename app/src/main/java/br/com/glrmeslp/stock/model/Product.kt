package br.com.glrmeslp.stock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
class Product (
    @ColumnInfo(name = "name")var name: String,
    @ColumnInfo(name = "price")var price: BigDecimal,
    @ColumnInfo(name = "quantity")var quantity: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}