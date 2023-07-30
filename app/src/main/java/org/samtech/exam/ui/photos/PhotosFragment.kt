package org.samtech.exam.ui.photos

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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.samtech.exam.R
import org.samtech.exam.utils.Utils.customToast
import java.io.ByteArrayOutputStream
import java.util.UUID


@Suppress("DEPRECATION")
class PhotosFragment : Fragment(), View.OnClickListener {

    //TODO MAKE MVVM
    //IMPROVE THE FLOW -> CAPTURE -> PICK ==> SHOW IMAGEVIEW => UPLOAD BUTTON
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private val PICK_IMAGE = 100
    private lateinit var dummyContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_photos, container, false)

        dummyContext = inflater.context
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLoadPhoto = view.findViewById<ImageButton>(R.id.fragment_add_loadphoto)
        val btnTakePhoto = view.findViewById<ImageButton>(R.id.fragment_add_takephoto)
        imageView = view.findViewById(R.id.fragment_add_imagetaken)

        btnTakePhoto.setOnClickListener(this)
        btnLoadPhoto.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fragment_add_loadphoto) {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            gallery.type = "image/*"
            startActivityForResult(gallery, PICK_IMAGE)
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
            uploadImagefromByter(imageBitmap)
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            imageView.setImageURI(imageUri)
            uploadImagefromUri(imageUri)
        }


    }

    fun uploadImagefromByter(paramBitMap: Bitmap) {
        val storage = Firebase.storage
        val storageRef = storage.reference
            .child(UUID.randomUUID().toString())
        val baos = ByteArrayOutputStream()
        paramBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            customToast(dummyContext, "Error en la carga de foto " + exception.message)
        }
        uploadTask.addOnSuccessListener { taskSnapshot ->
            customToast(
                dummyContext,
                "La foto se ha subido de manera exitosa\n" + taskSnapshot.bytesTransferred
            )
        }
    }

    fun uploadImagefromUri(paramImageUri: Uri?) {
        if (paramImageUri != null) {
            val storage = Firebase.storage
            val storageRef = storage.reference
                .child(UUID.randomUUID().toString())

            storageRef.putFile(paramImageUri)
                .addOnFailureListener { exception ->
                    customToast(dummyContext, "Error en la carga de imagen " + exception.message)
                }
                .addOnSuccessListener { taskSnapshot ->
                    customToast(
                        dummyContext,
                        "La imagen se ha subido de manera exitosa\n" + taskSnapshot.bytesTransferred
                    )
                }
        }
    }
}