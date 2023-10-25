package com.example.cortecerto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cortecerto.R
import com.example.cortecerto.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate((layoutInflater))
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome")

        binding.txtNomeUsuario.text = "Bem-vindo(a), $nome"
    }
}