package com.example.cookingapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class FirebaseStoreManager {

    private val mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var mProgress: ProgressDialog

    fun uploadImageFirebase(mContext:Context, byte: ByteArray, name:String, uploadingMessage:String, uploadingDoneMessage:String, uploadingErrorMessage:String){
        mProgress = ProgressDialog(mContext)
        mProgress.setMessage(uploadingMessage)
        mProgress.show()

        mStorageReference.child("images/${name}.jpg").putBytes(byte).addOnSuccessListener {
            Toast.makeText(mContext, uploadingDoneMessage, Toast.LENGTH_SHORT).show()
            mProgress.hide()
        }.addOnFailureListener{
            Toast.makeText(mContext, uploadingErrorMessage, Toast.LENGTH_SHORT).show()
            mProgress.hide()
        }
    }

    fun onCaptureImageData(mContext: Context, data: Intent, imgName:String, uploadingMessage:String, uploadingDoneMessage:String, uploadingErrorMessage:String){
        val thumb:Bitmap = data.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumb.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val bb = bytes.toByteArray();

        uploadImageFirebase(mContext, bb, imgName, uploadingMessage, uploadingDoneMessage, uploadingErrorMessage)
    }

    fun onDeleteImage(imgName: String){
        val imgStorageReference:StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cookingapp-97c73.appspot.com/images/$imgName.jpg")
        imgStorageReference.delete().addOnSuccessListener {
            Log.e("IMAGE DELETION", "Image deleted")
        }.addOnFailureListener{
            Log.e("IMAGE DELETION", "Error")
        }
    }


}