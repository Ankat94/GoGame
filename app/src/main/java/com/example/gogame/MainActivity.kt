package com.example.gogame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gogame.databinding.ActivityMainBinding
import com.example.gogame.model.User
import com.example.shaadi.room.UserRepository
import com.example.gogame.utils.AdapterListener

class MainActivity : AppCompatActivity(), AdapterListener {

    lateinit var binding : ActivityMainBinding
    lateinit var adapter: HeaderAdapter
    var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recycle.layoutManager = LinearLayoutManager(this)
        adapter = HeaderAdapter(1,this)
        binding.recycle.adapter = adapter

        binding.createContact.setOnClickListener {
            startActivity(Intent(this,CreateContact::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        users = UserRepository(this).getFavorites() as ArrayList<User>
        if (users.isEmpty()){
            adapter.header = 1
        }
        else {
            adapter.header = 2
        }
        adapter.notifyDataSetChanged()
    }


    override fun onStatusChanged(id: Long, postion: Int) {
        val intent = Intent(this,CreateContact::class.java).apply {
            putExtra("id", id)
        }
        startActivity(intent)
    }
}