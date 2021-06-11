package br.com.glrmeslp.stock.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.glrmeslp.stock.R
import br.com.glrmeslp.stock.database.StockDatabase
import br.com.glrmeslp.stock.dialog.ProductFormDialogFragment
import br.com.glrmeslp.stock.model.Product
import br.com.glrmeslp.stock.ui.adapter.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TITLE_APPBAR = "Products List"

class ProductsListActivity : AppCompatActivity(),
    ProductFormDialogFragment.ProductFormDialogListener {

    private lateinit var fabNewProduct: FloatingActionButton
    private lateinit var listOfProducts: RecyclerView

    private val dao by lazy {
        StockDatabase
            .getInstance(this)
            .productDao()
    }

    private val adapter by lazy {
        ProductAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        title = TITLE_APPBAR
        configureFabNewProduct()
        configureRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(IO).launch {
            val products = dao.getAll()
            withContext(Main) {
                adapter.update(products)
            }
        }
    }

    private fun configureRecyclerView() {
        listOfProducts = findViewById(R.id.rv_activity_products_list)
        configureAdapter()
    }

    private fun configureAdapter() {
        listOfProducts.adapter = adapter
        configureOnItemClickListener()
        configureOnItemClickRemoveContextMenuListener()
    }

    private fun configureOnItemClickRemoveContextMenuListener() {
        adapter.onItemClickRemoveContextMenuListener =
            object : ProductAdapter.IOnItemClickRemoveContextMenuListener {
                override fun onItemClick(product: Product, position: Int) {
                    CoroutineScope(IO).launch {
                        dao.delete(product)
                        withContext(Main) {
                            adapter.remove(product)
                        }
                    }
                }
            }
    }

    private fun configureOnItemClickListener() {
        adapter.onItemClickListener = object : ProductAdapter.IOnItemClickListener {
            override fun onItemClick(product: Product) {
                showEditProductFormDialog(product)
            }
        }
    }

    private fun configureFabNewProduct() {
        fabNewProduct = findViewById(R.id.fab_activity_products_list)
        fabNewProduct.setOnClickListener {
            showNewProductFormDialog()
        }
    }

    private fun showEditProductFormDialog(product: Product) {
        val dialog = ProductFormDialogFragment(R.string.edit_product, product)
        dialog.show(supportFragmentManager, "ProductFormDialogFragment")
    }

    private fun showNewProductFormDialog() {
        val dialog = ProductFormDialogFragment(R.string.new_product)
        dialog.show(supportFragmentManager, "ProductFormDialogFragment")
    }

    override fun onDialogPositiveClick(product: Product, title: Int) {
        when (title) {
            R.string.new_product -> {
                CoroutineScope(IO).launch {
                    dao.insertAll(product)
                    val products = dao.getAll()
                    withContext(Main) {
                        adapter.update(products)
                    }
                }
            }
            R.string.edit_product -> {
                CoroutineScope(IO).launch {
                    dao.update(product)
                    val products = dao.getAll()
                    withContext(Main) {
                        adapter.update(products)
                    }
                }
            }
        }
    }
}