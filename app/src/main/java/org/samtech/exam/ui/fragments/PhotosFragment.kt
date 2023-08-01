package org.samtech.exam.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.ui.viewmodels.PhotosViewModel
import org.samtech.exam.utils.Utils.customToast
import java.util.UUID


@Suppress("DEPRECATION")
class PhotosFragment : Fragment(), View.OnClickListener {

    private val opPhotosViewModel: PhotosViewModel by activityViewModels {
        PhotosViewModel.PhotosViewModelFactory(
            Singleton.instance!!.photossFSRepository
        )
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private val PICK_IMAGE = 100
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_photos, container, false)

        ctx = inflater.context
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryRV = view.findViewById<RecyclerView>(R.id.fr_photos_gallery_rv)
        val btnLoadPhoto = view.findViewById<ImageButton>(R.id.fr_photos_gallery)
        val btnTakePhoto = view.findViewById<ImageButton>(R.id.fr_photos_take_photo)
        imageView = view.findViewById(R.id.fr_photos_photo_taken)

        btnTakePhoto.setOnClickListener(this)
        btnLoadPhoto.setOnClickListener(this)
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE);

    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fr_photos_gallery) {
            openGallery()
        }

        if (v?.id == R.id.fr_photos_take_photo) {
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
            val msgImgFromByte = opPhotosViewModel.storeImgFromByte(imageBitmap)
            customToast(ctx, msgImgFromByte)
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data?.clipData != null) {
                val count = data.clipData?.itemCount

                for (i in 0 until count!!) {
                    val multipleUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    val storeFromGalleryBulk =
                        opPhotosViewModel.storePhoto(UUID.randomUUID().toString(), multipleUri)
                    customToast(ctx, storeFromGalleryBulk)
                }

            } else if (data?.data != null) {
                val singleUri: Uri = data.data!!
                imageView.setImageURI(singleUri)
                val storeFromGalery =
                    opPhotosViewModel.storePhoto(UUID.randomUUID().toString(), singleUri)
                customToast(ctx, storeFromGalery)
            }
        }
    }
}