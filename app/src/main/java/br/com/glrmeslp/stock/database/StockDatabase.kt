package br.com.glrmeslp.stock.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.glrmeslp.stock.database.converter.BigDecimalConverter
import br.com.glrmeslp.stock.database.dao.ProductDAO
import br.com.glrmeslp.stock.model.Product

private const val NAME_DATABASE = "database-stock"

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(value = [BigDecimalConverter::class])
abstract class StockDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDAO

    companion object {
        fun getInstance(context: Context): StockDatabase =
            Room.databaseBuilder(
                context,
                StockDatabase::class.java,
                NAME_DATABASE
            ).build()
    }
}