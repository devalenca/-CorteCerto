package com.example.cortecerto.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cortecerto.R
import com.example.cortecerto.adapter.ServicosAdapter
import com.example.cortecerto.databinding.ActivityHomeBinding
import com.example.cortecerto.model.Servicos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicosAdapter: ServicosAdapter
    private val listaServicos: MutableList<Servicos> = mutableListOf()
    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate((layoutInflater))
        setContentView(binding.root)

        supportActionBar?.hide()


        getNomeUser { nome ->
            // Use a variável nome aqui
           binding.txtNomeUsuario.text = "Bem-vindo(a), $nome"
        }

        val recyclerViewServicos = binding.recyclerViewServicos
        recyclerViewServicos.layoutManager = GridLayoutManager(this, 2)
        servicosAdapter = ServicosAdapter(this, listaServicos)
        recyclerViewServicos.setHasFixedSize(true)
        recyclerViewServicos.adapter = servicosAdapter
        getServicos()

        binding.btnAgendar.setOnClickListener{
            val intent = Intent(this, Agendamento::class.java)
            getNomeUser { nome ->
                // Use a variável nome aqui
                intent.putExtra("nome", nome)
            }
            startActivity(intent)
        }
    }

    private fun getNomeUser(callback: (String) -> Unit) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            // Consulte o Firestore para obter os dados do usuário atual
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nome = document.getString("nome")
                        if (nome != null) {
                            // Chame o callback com o nome
                            callback(nome)
                        }
                    } else {
                        println("Documento não existe")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Erro ao recuperar dados: $exception")
                }

        }
    }
    private fun getServicos(){
        val servico1 = Servicos(R.drawable.img1, "Corte de cabelo")
        listaServicos.add(servico1)

        val servico2 = Servicos(R.drawable.img2, "Corte de barba")
        listaServicos.add(servico2)

        val servico3 = Servicos(R.drawable.img3, "Lavagem de cabelo")
        listaServicos.add(servico3)

        val servico4 = Servicos(R.drawable.img4, "Tratamento de cabelo")
        listaServicos.add(servico4)
    }
}