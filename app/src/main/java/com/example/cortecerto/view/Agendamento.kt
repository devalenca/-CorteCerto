package com.example.cortecerto.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.cortecerto.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore;

class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data : String = ""
    private var hora: String = ""
    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        val datePicker = binding.dataPicker
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var dia = dayOfMonth.toString()
            val mes: String

            if (dayOfMonth <10) {
                dia = "0$dayOfMonth"
            }
            if (monthOfYear < 10){
                mes = "" + (monthOfYear+1)
            }else{
                mes = (monthOfYear +1).toString()
            }

            data = "$dia / $mes / $year"
        }

        binding.timePicker.setOnTimeChangedListener{_, hourOfDay, minute ->

            val minuto: String

            if (minute < 10){
                minuto = "0$minute"
            } else{
                minuto = minute.toString()
            }

            hora = "$hourOfDay:$minuto" //19:00
        }
        binding.timePicker.setIs24HourView(true) //formato de 24horas


        val editEndereco = binding.editEndereco
        val atendimentoDomicilio = binding.domicilio
        atendimentoDomicilio.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked) {
                editEndereco.visibility = View.VISIBLE
            } else {
                editEndereco.visibility = View.GONE
            }
        }
        binding.btnAgendar.setOnClickListener {

            val barbeiro1 = binding.barbeiro1
            val barbeiro2 = binding.barbeiro2
            val barbeiro3 = binding.barbeiro3
            val endereco = binding.endereco.text.toString()

            when {
                hora.isEmpty() -> {
                    mensagem(it, "Preencha o horário", "#FF0000")
                }

                hora < "8:00" && hora > "19:00" -> {
                    mensagem(it, "Corte Certo esta fechado - horário de atendimento das 08:00 às 19:00!", "#FF0000")
                }

                data.isEmpty() -> {
                    mensagem(it, "Coloque uma data!", "#FF0000")

                }
                barbeiro1.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    getNomeUser { nome ->
                        salvarAgendamento(it, nome, "Jefferson", endereco, data, hora)
                    }
                }
                barbeiro2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    getNomeUser { nome ->
                        salvarAgendamento(it, nome, "Elizeu", endereco, data,hora)
                    }
                }
                barbeiro3.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    getNomeUser { nome ->
                        salvarAgendamento(it, nome, "Luiza", endereco, data,hora)
                    }
                }
                else -> {
                    mensagem(it, "Escolha um barbeiro!", "#FF0000")
                }
            }
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
    private fun mensagem(view: View, mensagem:String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem,Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun salvarAgendamento(view: View, cliente: String, barbeiro: String, domicilio: String, data: String, hora: String){

        val db = FirebaseFirestore.getInstance()

        val dadosUsuario = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "domicilio" to domicilio,
            "data" to data,
            "hora" to hora
        )

        db.collection("agendamento").document(cliente).set(dadosUsuario).addOnCompleteListener{
            mensagem(view, "Agendamento realizado com sucesso!", "#FF03DAC5")
        }.addOnFailureListener{
            mensagem(view, "Erro no servidor", "#FF0000")
        }
        navegar(Intent(this, Home::class.java))
    }
    private fun navegar(intent: Intent){
        startActivity(intent)
    }
}




