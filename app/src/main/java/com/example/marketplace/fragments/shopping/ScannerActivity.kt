package com.example.marketplace.fragments.shopping

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.newapptester1.R
import com.example.newapptester1.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ScannerActivity : AppCompatActivity() {
    lateinit var selectBtn: Button
    lateinit var predictBtn: Button
    lateinit var captureBtn: Button
    lateinit var result: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)
        permission
        val labels = application.assets.open("labels.txt").bufferedReader().readLine()
        val cnt = 0
        try {
            val bufferedReader = BufferedReader(InputStreamReader(assets.open("labels.txt")))
            var line = bufferedReader.readLine()
            while (line != null) {

                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        var imageProcessor = ImageProcessor.Builder()
            .add(NormalizeOp(0.0f, 225.0f))
            // Remove TransformToGrayscaleOp here

            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))

            .build()
        selectBtn = findViewById(R.id.selectImage)
        predictBtn = findViewById(R.id.predictBtn)
        captureBtn = findViewById(R.id.captureBtn)
        result = findViewById(R.id.textResult)
        imageView = findViewById(R.id.imageView)


        selectBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 10)
        })


        captureBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 12)
        })


        predictBtn.setOnClickListener {
            if (::bitmap.isInitialized && bitmap != null) {
                // Prediction logic
                var tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)

                tensorImage = imageProcessor.process(tensorImage)
                val model = MobilenetV110224Quant.newInstance(this)
                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputFeature0.loadBuffer(tensorImage.buffer)
                val outputs = model.process(inputFeature0)
                val outputFeatureO = outputs.outputFeature0AsTensorBuffer.floatArray
                var maxIdx = 0
                outputFeatureO.forEachIndexed { index, fl ->
                    if (outputFeatureO[maxIdx] < fl) {
                        maxIdx = index
                    }

                }
                result.text = labels[maxIdx].toString()
                model.close()
            } else {
                Log.e("ScannerActivity", "Bitmap is null or not initialized")
            }
        }

    }


    val permission: Unit
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(
                        this@ScannerActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        11
                    )
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 11) {
            if (grantResults.size > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == 10) {
            if (data != null) {
                val uri = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView!!.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == 12) {
            if (data != null && data.extras != null) {
                bitmap = (data.extras!!["data"] as Bitmap?)!!
                imageView!!.setImageBitmap(bitmap)
            }
        }
    }
}
