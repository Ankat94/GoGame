package com.example.gogame

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import com.example.gogame.databinding.ActivityCreateContactBinding
import com.example.gogame.databinding.PhotoBottomSheetBinding
import com.example.gogame.model.ContactViewModel
import com.example.gogame.model.MainViewModel
import com.example.gogame.model.User
import com.example.shaadi.room.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kira.store.mvvm.crm.factory.UserFactory
import java.io.File


class CreateContact() : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityCreateContactBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var bottomBinding: PhotoBottomSheetBinding
    lateinit var contactViewModel: ContactViewModel
    var user = User()
    var id: Long = -1

    val GALLERY_CODE = 110
    val CAPTURE_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_contact)
        contactViewModel = ViewModelProvider(this, UserFactory(applicationContext)).get(ContactViewModel::class.java)

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
            binding.header.setText("Edit Contact")
            contactViewModel.newLiveData.observe(this, {
                user = it
                binding.user = it
                if (it.thumbnail == null) {
                    bottomBinding.removeBox.visibility = View.GONE
                }
                if (it.favourite) {
                    binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.dislike))
                } else {
                    binding.favorite.setImageDrawable(resources.getDrawable(R.drawable.like))
                }
                hideRemove(it)
            })
            contactViewModel.getUser(id)
        }


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
                    contactViewModel.updateUser(user)
                }
                else {
                    contactViewModel.insertUser(user)
                }

                startActivity(Intent(this, MainActivity::class.java))
            }

            binding.trashContact -> {
                contactViewModel.deleteUser(user)
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