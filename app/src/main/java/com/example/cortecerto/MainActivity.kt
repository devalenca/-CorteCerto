package com.example.cortecerto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cortecerto.databinding.ActivityMainBinding
import com.example.cortecerto.view.Home
import com.example.cortecerto.view.Recuperacao
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler
import android.os.Looper

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
            val delayMillis = 1500 // 1500 milissegundos = 1,5 segundos
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                email.isEmpty() -> {
                    mensagem(it, "O e-mail não pode estar vazio!", "#FF0000")
                }senha.isEmpty() ->{
                    mensagem(it, "Preencha a senha!", "#FF0000")
                }senha.length <=5 -> {
                    mensagem(it, "A senha precisa ter pelo menos 6 caracteres!", "#FF0000")
                }else ->{
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener{ login ->
                    if (login.isSuccessful){
                        mensagem(it, "Login realizado com sucesso!", "#FF44AF49")
                        Handler(Looper.getMainLooper()).postDelayed({
                            // Código a ser executado após o atraso
                            navegar(Intent(this, Home::class.java))
                        }, delayMillis.toLong())
                    } else {
                        mensagem(it, "Email ou senha incorretos", "#FF0000")
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
    private fun mensagem(view: View, mensagem:String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem,Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            navegar(Intent(this, Home::class.java))
        }
    }

   private fun navegar(intent: Intent){
       startActivity(intent)
    }
}

