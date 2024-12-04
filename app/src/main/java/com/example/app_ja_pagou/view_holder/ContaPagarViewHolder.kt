package com.example.app_ja_pagou.view_holder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.app_ja_pagou.R

class ContaPagarViewHolder(view: View): ViewHolder(view) {

    val txtTituloConta: TextView = view.findViewById(R.id.txt_titulo_conta)
    val txtValorConta: TextView = view.findViewById(R.id.txt_valor_conta)
    val txtDataPagamentoConta: TextView = view.findViewById(R.id.txt_data_pagamento_conta)
    val txtDataNotificacaoConta: TextView = view.findViewById(R.id.txt_data_notificacao_conta)
    val txtStatusConta: TextView = view.findViewById(R.id.txt_status_conta)
    val imgStatusConta: ImageView = view.findViewById(R.id.img_status_conta)
    val btnDeletarConta: ImageButton = view.findViewById(R.id.btn_deletar_conta)
    val btnMarcarContaComoPaga: ImageButton = view.findViewById(R.id.btn_marcar_conta_como_paga)

}