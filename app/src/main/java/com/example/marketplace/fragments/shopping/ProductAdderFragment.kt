import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.marketplace.data.Product
import com.example.newapptester1.R
import com.example.newapptester1.databinding.ProductAdderFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class ProductAdderFragment : Fragment() {
    private lateinit var binding: ProductAdderFragmentBinding
    private val selectedImages = mutableListOf<Uri>()
    private val selectedColors = mutableListOf<Int>()
    private val productStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore

    private val selectImagesActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data

                if (intent?.clipData != null) {
                    val count = intent.clipData?.itemCount ?: 0
                    (0 until count).forEach {
                        val imageUri = intent.clipData?.getItemAt(it)?.uri
                        imageUri?.let {
                            selectedImages.add(it)
                        }
                    }
                } else {
                    val imageUri = intent?.data
                    imageUri?.let { selectedImages.add(it) }
                }
                updateImages()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductAdderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("Product color")
                .setPositiveButton("Select", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                        selectedColors.add(envelope.color)
                        updateColor()
                    }
                })
                .setNegativeButton("Cancel") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }

        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }
        binding.saveButton.setOnClickListener {
            if (validateInformation()) {
                saveProduct()
            } else {
                Toast.makeText(requireContext(), "Check your Inputs", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveProduct -> {
                if (!validateInformation()) {
                    Toast.makeText(requireContext(), "Check your Inputs", Toast.LENGTH_LONG).show()
                    return true
                }
                saveProduct()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    private fun updateColor() {
        var colors = ""
        selectedColors.forEach() {
            colors = "$colors ${Integer.toHexString(it)}"
        }
        binding.tvSelectedColors.text = colors
    }

    private fun saveProduct() {
        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPercentage = binding.edOfferPercentage.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()
        val size = getSizeList(binding.edSizes.text.toString().trim())
        val imagesByteArrays = getImagesByteArrays()
        val images = arrayListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading()
            }
            try {
                async {
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStorage = productStorage.child("products/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    hideLoading()
                }
            }
            val product = Product(
                UUID.randomUUID().toString(),
                name,
                category,
                price.toFloat(),
                if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                if (description.isEmpty()) null else description,
                if (selectedColors.isEmpty()) null else selectedColors,
                size,
                images
            )
            firestore.collection("Products").add(product).addOnSuccessListener {
                hideLoading()
            }.addOnFailureListener {
                hideLoading()

                Log.e("Firestore", "Error adding product")
            }
        }
    }

    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBmp = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    private fun getSizeList(sizeStr: String): List<String>? {
        if (sizeStr.isEmpty())
            return null
        return sizeStr.split(",")
    }

    private fun validateInformation(): Boolean {
        return binding.edPrice.text.toString().trim().isNotEmpty() &&
                binding.edName.text.toString().trim().isNotEmpty() &&
                binding.edCategory.text.toString().trim().isNotEmpty() &&
                selectedImages.isNotEmpty()
    }
}
