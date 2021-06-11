package br.com.glrmeslp.stock.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import br.com.glrmeslp.stock.R
import br.com.glrmeslp.stock.model.Product
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal

class ProductFormDialogFragment(private val title: Int) : DialogFragment() {

    private lateinit var product: Product
    private lateinit var fieldName: TextInputEditText
    private lateinit var fieldPrice: TextInputEditText
    private lateinit var fieldQuantity: TextInputEditText
    private lateinit var fieldId: TextView

    internal lateinit var listener: ProductFormDialogListener

    constructor(title: Int, product: Product) : this(title) {
        this.product = product
    }

    interface ProductFormDialogListener {
        fun onDialogPositiveClick(product: Product, title: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ProductFormDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.product_form, null)

            initializationComponents(view)
            tryToFillForm()

            builder.setView(view)
                .setTitle(title)
                .setPositiveButton(
                    R.string.done
                ) { _, _ ->
                    product = setProduct()
                    listener.onDialogPositiveClick(product,title)
                }
                .setNegativeButton(R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setProduct(): Product {
        return if (title == R.string.new_product) {
            Product(
                fieldName.text.toString(),
                tryToConvertPrice(fieldPrice.text.toString()),
                tryToConvertQuantity(fieldQuantity.text.toString())
            )
        } else{
            product.name = fieldName.text.toString()
            product.price = tryToConvertPrice(fieldPrice.text.toString())
            product.quantity = tryToConvertQuantity(fieldQuantity.text.toString())
            return product
        }
    }

    private fun initializationComponents(view: View) {
        fieldId = view.findViewById(R.id.product_form_id)
        fieldName = view.findViewById(R.id.et_product_form_name)
        fieldQuantity = view.findViewById(R.id.et_product_form_quantity)
        fieldPrice = view.findViewById(R.id.et_product_form_price)
    }

    private fun tryToFillForm() {
        if(title == R.string.edit_product){
            fieldName.setText(product.name)
            fieldPrice.setText(product.price.toString())
            fieldQuantity.setText(product.quantity.toString())
            fieldId.text = product.uid.toString()
            fieldId.visibility = View.VISIBLE
        }

    }

    private fun tryToConvertQuantity(quantity: String): Int {
        return try {
            quantity.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun tryToConvertPrice(price: String): BigDecimal {
        return try {
            BigDecimal(price)
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO;
        }
    }
}