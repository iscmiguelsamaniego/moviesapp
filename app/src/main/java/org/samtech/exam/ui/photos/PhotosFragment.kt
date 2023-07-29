package org.samtech.exam.ui.photos

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.coroutines.runBlocking
import org.samtech.exam.R

@Suppress("DEPRECATION")
class PhotosFragment : Fragment(), View.OnClickListener {

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLoadPhoto = view.findViewById<ImageButton>(R.id.fragment_add_loadphoto)
        val btnTakePhoto = view.findViewById<ImageButton>(R.id.fragment_add_takephoto)
        //val btnSave = view.findViewById<Button>(R.id.fragment_add_save)
        imageView = view.findViewById(R.id.fragment_add_imagetaken)

        btnTakePhoto.setOnClickListener(this)
        btnLoadPhoto.setOnClickListener(this)
        //btnSave.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        if (v?.id == R.id.fragment_add_loadphoto) {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        if (v?.id == R.id.fragment_add_takephoto) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(v.context.packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }

        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

}