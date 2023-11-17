package com.example.cortecerto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cortecerto.databinding.ActivityCadastroBinding
import com.example.cortecerto.databinding.ActivityMainBinding
import com.example.cortecerto.ui.theme.CorteCertoTheme
import com.example.cortecerto.view.Home
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler
import android.os.Looper

class Cadastro: AppCompatActivity() {

    private lateinit var binding : ActivityCadastroBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate((layoutInflater))
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegistro.setOnClickListener{

            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                nome.isEmpty() -> {
                    mensagem(it, "O nome não pode estar vazio!", "#FF0000")}
                email.isEmpty() -> {
                    mensagem(it, "O e-mail não pode estar vazio!", "#FF0000")
                }senha.isEmpty() ->{
                mensagem(it, "Preencha a senha!", "#FF0000")
            }senha.length <=5 -> {
                mensagem(it, "A senha precisa ter pelo menos 6 caracteres!", "#FF0000")
            }else -> {
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{ cadastro ->
                    if (cadastro.isSuccessful){
                        val delayMillis = 1500 // 1500 milissegundos = 1,5 segundos
                        mensagem(it, "Cadastrado no banco de dados!", "#FF44AF49")
                        Handler(Looper.getMainLooper()).postDelayed({
                            navegar(Intent(this, Home::class.java))
                        }, delayMillis.toLong())
                        val currentUser = auth.currentUser
                        if (currentUser != null) {

                            val uid = currentUser.uid
                            salvarUsuario(it, nome, email, uid)

                        } else {
                            mensagem(it, "Erro ao puxar UID", "#FF0000")
                        }
                    }
                }

            }


            }
        }
    }
    private fun mensagem(view: View, mensagem:String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem,Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun salvarUsuario(view: View, nome:String, email:String, id:String){
        val db = FirebaseFirestore.getInstance()
        val dadosUsuario = hashMapOf(
            "id" to id,
            "nome" to nome,
            "email" to email
        )

        db.collection("usuarios").document(id).set(dadosUsuario).addOnCompleteListener{
        }.addOnFailureListener{
            mensagem(view, "Erro no servidor", "#FF0000")
        }

    }

    private fun navegar(intent: Intent){
        startActivity(intent)
    }
}

