package com.example.app_ja_pagou.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.app_ja_pagou.R

class DialogConfirmar(
    private val contexto: Context,
    private val msgApresentar: String,
    private val onConfirmar: (Int) -> Unit
) {

    private var builderAlertDialogConfirmar: AlertDialog.Builder
    private var alertDialogConfirmar: AlertDialog
    var idEntidadeRealizarOperacaoConfirmar: Int = 0

    init {
        this.builderAlertDialogConfirmar = AlertDialog.Builder(this.contexto)
        this.builderAlertDialogConfirmar.setCancelable(false)

        val layoutInflater: LayoutInflater = LayoutInflater.from(this.contexto)
        val view: View = layoutInflater.inflate(R.layout.dialog_confirmar, null, false)
        val txtMensagemConfirmar: TextView = view.findViewById(R.id.txt_mensagem)
        val btnConfirmar: AppCompatButton = view.findViewById(R.id.btn_confirmar)
        val btnNaoConfirmar: AppCompatButton = view.findViewById(R.id.btn_nao_confirmar)

        txtMensagemConfirmar.text = this.msgApresentar
        btnConfirmar.setOnClickListener { this.onConfirmar(this.idEntidadeRealizarOperacaoConfirmar) }
        btnNaoConfirmar.setOnClickListener { this.esconder() }

        this.builderAlertDialogConfirmar.setView(view)

        this.alertDialogConfirmar = this.builderAlertDialogConfirmar.create()
    }

    fun apresentar() {
        this.alertDialogConfirmar.show()
    }

    fun esconder() {
        this.alertDialogConfirmar.dismiss()
    }

}