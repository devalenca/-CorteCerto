package com.example.cortecerto.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.cortecerto.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Handler
import android.os.Looper
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
        val servico = intent.extras?.getString("servico").toString()
        binding.txtServico.text = servico

        Log.d("@cc/Nome", nome)
        Log.d("@cc/Servico", servico)

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
            val endereco = binding.endereco.text.toString()

            when {
                hora.isEmpty() -> {
                    mensagem(it, "Preencha o horário", "#FF0000")
                }
                hora < "8:00" && hora > "19:00" -> {
                    mensagem(it, "Astro Barber esta fechado - horário de atendimento das 08:00 às 19:00!", "#FF0000")
                }
                data.isEmpty() -> {
                    mensagem(it, "Coloque uma data!", "#FF0000")

                }
                barbeiro1.isChecked && barbeiro2.isChecked && barbeiro3.isChecked -> {
                    mensagem(it, "Selecione apenas 1 barbeiro", "#FF0000")
                }
                barbeiro1.isChecked && barbeiro2.isChecked  -> {
                    mensagem(it, "Selecione apenas 1 barbeiro", "#FF0000")
                }
                barbeiro2.isChecked && barbeiro3.isChecked -> {
                    mensagem(it, "Selecione apenas 1 barbeiro", "#FF0000")
                }
                barbeiro1.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome, "Jefferson",endereco,data,hora, servico!!)
                }
                barbeiro2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome!!, "Elizeu",endereco,data,hora, servico!!)
                }
                barbeiro3.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome!!, "Luiza",endereco,data,hora, servico!!)
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

    private fun salvarAgendamento(view: View, cliente: String, barbeiro: String, domicilio:String, data: String, hora: String, servico: String){
        val delayMillis = 1500 // 1500 milissegundos = 1,5 segundos

        val db = FirebaseFirestore.getInstance()

        val dadosUsuario = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "domicilio" to domicilio,
            "data" to data,
            "hora" to hora,
            "servico" to servico
        )

        db.collection("agendamento").document(cliente).set(dadosUsuario).addOnCompleteListener{
            mensagem(view, "Agendamento realizado com sucesso!", "#FF44AF49")
            Handler(Looper.getMainLooper()).postDelayed({
                navegar(Intent(this, Home::class.java))
            }, delayMillis.toLong())
        }.addOnFailureListener{
            mensagem(view, "Erro no servidor", "#FF0000")
        }

    }
    private fun navegar(intent: Intent){
        startActivity(intent)
    }
}

