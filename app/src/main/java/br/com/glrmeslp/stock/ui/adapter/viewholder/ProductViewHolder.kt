package br.com.glrmeslp.stock.ui.adapter.viewholder

import android.annotation.SuppressLint
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.glrmeslp.stock.R
import br.com.glrmeslp.stock.model.Product
import br.com.glrmeslp.stock.ui.adapter.ProductAdapter
import java.math.BigDecimal
import java.text.NumberFormat

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var product: Product
    private val tvItemProductId: TextView = itemView.findViewById(R.id.tv_item_product_id)
    private val tvItemProductName: TextView = itemView.findViewById(R.id.tv_item_product_name)
    private val tvItemProductPrice: TextView = itemView.findViewById(R.id.tv_item_product_price)
    private val tvItemProductQuantity: TextView = itemView.findViewById(R.id.tv_item_product_quantity)

    fun setProduct(product: Product) {
        this.product = product
        setComponentsView()
    }

    @SuppressLint("SetTextI18n")
    private fun setComponentsView() {
        tvItemProductId.text = product.uid.toString()
        tvItemProductName.text = product.name
        tvItemProductPrice.text = formatToCurrency(product.price)
        tvItemProductQuantity.text = product.quantity.toString()
    }

    fun configureItemClick(onItemClickListener: ProductAdapter.IOnItemClickListener) {
        itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(product)
        })
    }

    fun configureContextMenu(onItemClickRemoveContextMenuListener: ProductAdapter.IOnItemClickRemoveContextMenuListener) {
        itemView.setOnCreateContextMenuListener(View.OnCreateContextMenuListener { menu, _, _ ->
            MenuInflater(itemView.context).inflate(R.menu.products_list_menu, menu).run {
                menu.findItem(R.id.menu_products_list_remove).setOnMenuItemClickListener(
                        MenuItem.OnMenuItemClickListener {
                            onItemClickRemoveContextMenuListener.onItemClick(
                                    product,
                                    adapterPosition
                            )
                            return@OnMenuItemClickListener true
                        })
            }
        })
    }

    private fun formatToCurrency(value: BigDecimal): String{
        NumberFormat.getCurrencyInstance().run {
            return this.format(value).toString()
        }
    }
}
