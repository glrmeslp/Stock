package br.com.glrmeslp.stock.database.dao

import androidx.room.*
import br.com.glrmeslp.stock.model.Product

@Dao
interface ProductDAO {

    @Query("SELECT * FROM product")
    fun getAll(): MutableList<Product>

    @Query("SELECT * FROM product WHERE uid IN (:productIds)")
    fun loadAllByIds(productIds: IntArray): List<Product>

    @Insert()
    fun insertAll(vararg product: Product)

    @Update
    fun update(vararg product: Product)

    @Delete
    fun delete(product: Product)

}
