package com.example.cortecerto.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cortecerto.databinding.ActivityRecuperacaoBinding
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Recuperacao : AppCompatActivity() {
    private lateinit var binding: ActivityRecuperacaoBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperacaoBinding.inflate((layoutInflater))
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnEnviar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    // Sucesso ao enviar o email de redefinição de senha
                    Toast.makeText(
                        this,
                        "Email de redefinição de senha enviado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    // Falha ao enviar o email de redefinição de senha
                    Toast.makeText(
                        this,
                        "Falha ao enviar o email de redefinição de senha: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }


        }
    }
}