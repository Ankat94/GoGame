package com.example.gogame

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.gogame.databinding.ActivityCreateContactBinding
import com.example.gogame.databinding.PhotoBottomSheetBinding
import com.example.gogame.model.User
import com.example.shaadi.room.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


class CreateContact : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityCreateContactBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var bottomBinding: PhotoBottomSheetBinding
    var user = User()
    var id: Long = -1

    val GALLERY_CODE = 110
    val CAPTURE_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_contact)

        binding.saveContact.setOnClickListener(this)
        binding.trashContact.setOnClickListener(this)
        binding.favorite.setOnClickListener(this)
        binding.photoContact.setOnClickListener(this)


        bottomBinding = DataBindingUtil.inflate<PhotoBottomSheetBinding>(layoutInflater, R.layout.photo_bottom_sheet, null, false)
        dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomBinding.root)

        bottomBinding.removeBox.setOnClickListener(this)
        bottomBinding.takeBox.setOnClickListener(this)
        bottomBinding.selectBox.setOnClickListener(this)

        id = intent.getLongExtra("id",-1)
        if (id != (-1).toLong()) {
            user = UserRepository(this).getUser(id)
            binding.header.setText("Edit Contact")
            binding.user = user
            if (user.thumbnail == null) {
                bottomBinding.removeBox.visibility = View.GONE
            }
            if (user.favourite) {
                binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.dislike))
            } else {
                binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.like))
            }
        }
        hideRemove(user)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when(p0) {
            binding.saveContact -> {
                if (binding.userFirst.editText!!.text.toString().isNullOrEmpty()) {
                    Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show()
                    return
                }

                val number = binding.userPhone.editText!!.text.toString()
                if (number.isNullOrEmpty() && number.length == 10) {
                    Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
                    return
                }

                user.first = binding.userFirst.editText!!.text.toString()
                user.last = binding.userLast.editText!!.text.toString()
                user.phone = number


                if (id != (-1).toLong()) {
                    UserRepository(this).updateUser(user)
                }
                else {
                    UserRepository(this).insertUser(user)
                }

                startActivity(Intent(this, MainActivity::class.java))
            }

            binding.trashContact -> {
                UserRepository(this).deleteUser(user)
                startActivity(Intent(this, MainActivity::class.java))
            }

            binding.favorite -> {
                user.favourite = !user.favourite
                if (user.favourite) {
                    binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.dislike))
                } else {
                    binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.like))
                }
            }

            binding.photoContact -> {
                dialog.show()
            }

            bottomBinding.removeBox -> {
                user.thumbnail = null
                hideRemove(user)
                binding.photoContact.setImageDrawable(resources.getDrawable(R.drawable.user))
                dialog.dismiss()
            }

            bottomBinding.takeBox -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, CAPTURE_CODE)
                } catch (e: ActivityNotFoundException) {
                    Log.d("Camera", e.toString())
                }
                dialog.dismiss()

            }

            bottomBinding.selectBox -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_CODE)
                dialog.dismiss()
            }


        }
    }

    fun hideRemove(user: User) {
        if (user.thumbnail == null) {
            bottomBinding.removeBox.visibility = View.GONE
        }
        else {
            bottomBinding.removeBox.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK ){

            var image: Bitmap? = null
            if (requestCode == GALLERY_CODE && data!!.data != null) {
                val filePath = data.data
                image = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
            }

            if (requestCode == CAPTURE_CODE) {
                image = data!!.extras!!.get("data") as Bitmap
            }

            if (image != null) {
                user.thumbnail = image
                hideRemove(user)
                binding.photoContact.setImageBitmap(user.thumbnail)
            }

        }
    }

}