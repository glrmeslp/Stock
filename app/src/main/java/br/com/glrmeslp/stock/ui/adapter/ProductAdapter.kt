package br.com.glrmeslp.stock.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.glrmeslp.stock.R
import br.com.glrmeslp.stock.model.Product
import br.com.glrmeslp.stock.ui.adapter.viewholder.ProductViewHolder

class ProductAdapter(
        products: List<Product> = listOf()
) : RecyclerView.Adapter<ProductViewHolder>() {

    private val products = products.toMutableList()
    lateinit var onItemClickListener: IOnItemClickListener
    lateinit var onItemClickRemoveContextMenuListener: IOnItemClickRemoveContextMenuListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product,parent,false).run {
                    ProductViewHolder(this).run {
                        configureItemClick(onItemClickListener)
                        configureContextMenu(onItemClickRemoveContextMenuListener)
                        return this
                    }
                }
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        products[position].run {
            holder.setProduct(this)
        }
    }

//    fun insert(product: Product) {
//        products.add(product)
//        notifyDataSetChanged()
//    }

    fun remove(product: Product) {
        products.remove(product)
        notifyDataSetChanged()
    }

    fun update(products: MutableList<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    interface IOnItemClickListener {
        fun onItemClick(product: Product)
    }

    interface IOnItemClickRemoveContextMenuListener {
        fun onItemClick(product: Product, position: Int)
    }
}