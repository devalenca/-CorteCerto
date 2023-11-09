package com.example.cortecerto.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.cortecerto.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore;

class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data : String = ""
    private var hora: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome").toString()

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

        binding.btnAgendar.setOnClickListener {

            val barbeiro1 = binding.barbeiro1
            val barbeiro2 = binding.barbeiro2
            val barbeiro3 = binding.barbeiro3

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
                    salvarAgendamento(it, nome, "Jefferson",data,hora)
                }
                barbeiro2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome, "Elizeu",data,hora)
                }
                barbeiro3.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome, "Luiza",data,hora)
                }
                else -> {
                    mensagem(it, "Escolha um barbeiro!", "#FF0000")
                }
            }
        }

        val editEndereco = binding.editEndereco
        val atendimentoDomicilio = binding.domicilio

        atendimentoDomicilio.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked) {
                editEndereco.visibility = View.VISIBLE
            } else {
                editEndereco.visibility = View.GONE
            }
        }
    }

    private fun mensagem(view: View, mensagem:String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem,Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun salvarAgendamento(view: View, cliente: String, barbeiro: String, data: String, hora: String){

        val db = FirebaseFirestore.getInstance()

        val dadosUsuario = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "data" to data,
            "hora" to hora
        )

        db.collection("agendamento").document(cliente).set(dadosUsuario).addOnCompleteListener{
            mensagem(view, "Agendamento realizado com sucesso!", "#FF03DAC5")
        }.addOnFailureListener{
            mensagem(view, "Erro no servidor", "#FF0000")
        }
    }
}


