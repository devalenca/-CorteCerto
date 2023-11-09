package com.example.cortecerto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cortecerto.databinding.ActivityMainBinding
import com.example.cortecerto.view.Home
import com.example.cortecerto.view.Recuperacao
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate((layoutInflater))
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener{
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                email.isEmpty() -> {
                    mensagem(it, "O e-mail não pode estar vazio!")
                }senha.isEmpty() ->{
                    mensagem(it, "Preencha a senha!")
                }senha.length <=5 -> {
                    mensagem(it, "A senha precisa ter pelo menos 6 caracteres!")
                }else ->{
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener{ login ->
                    if (login.isSuccessful){
                        mensagem(it, "Login realizado com sucesso!")
                        navegar(Intent(this, Home::class.java))
                    } else {
                        mensagem(it, "Email ou senha incorretos")
                    }
                    }
                }
            }
        }
        binding.btnRegistro.setOnClickListener{
            navegar(Intent(this, Cadastro::class.java))
        }
        binding.recuperarSenha.setOnClickListener{
            navegar(Intent(this, Recuperacao::class.java));
        }
    }
    private fun mensagem(view: View, mensagem: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

   private fun navegar(intent: Intent){
       startActivity(intent)
    }
}

