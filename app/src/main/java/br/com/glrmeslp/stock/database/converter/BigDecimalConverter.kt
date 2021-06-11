package br.com.glrmeslp.stock.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {

    @TypeConverter
    fun toDouble(value: BigDecimal): Double{
        return value.toDouble()
    }

    @TypeConverter
    fun toBigDecimal(value: Double): BigDecimal{
        return BigDecimal(value)
    }
}