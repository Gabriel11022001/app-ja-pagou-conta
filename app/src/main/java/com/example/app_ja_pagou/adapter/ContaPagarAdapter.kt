package com.example.app_ja_pagou.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.app_ja_pagou.R
import com.example.app_ja_pagou.model.ContaPagar
import com.example.app_ja_pagou.view_holder.ContaPagarViewHolder

class ContaPagarAdapter(
    private val contexto: Context,
    private val onDeletarConta: (String) -> Unit,
    private val onMarcarContaPaga: (String) -> Unit
): Adapter<ContaPagarViewHolder>() {

    private var contasPagar: ArrayList<ContaPagar> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContaPagarViewHolder {
        val layoutInflater = LayoutInflater.from(this.contexto)

        return ContaPagarViewHolder(
            layoutInflater.inflate(R.layout.conta_pagar_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {

        return this.contasPagar.size
    }

    override fun onBindViewHolder(holder: ContaPagarViewHolder, position: Int) {
        val contaPagar: ContaPagar = this.contasPagar[ position ]

        holder.txtTituloConta.text = contaPagar.tituloConta
        holder.txtValorConta.text = "R$${ contaPagar.valor }"
        holder.txtDataPagamentoConta.text = "Data de pagamento: ${ contaPagar.dataPagamento }"
        holder.txtDataNotificacaoConta.text = "Data de notificação: ${ contaPagar.dataNotificacao }"

        if (contaPagar.status == "aguardando pagamento") {
            holder.txtStatusConta.text = "Aguardando pagamento"
            holder.txtStatusConta.setTextColor(this.contexto.getColor(R.color.black))
            holder.imgStatusConta.setImageResource(R.drawable.ic_aguardando_pagamento)

            holder.btnMarcarContaComoPaga.visibility = View.VISIBLE
        } else if (contaPagar.status == "atrasada") {
            holder.txtStatusConta.text = "Atrasada"
            holder.txtStatusConta.setTextColor(this.contexto.getColor(android.R.color.holo_red_dark))
            holder.imgStatusConta.setImageResource(R.drawable.ic_conta_atrasada)

            holder.btnMarcarContaComoPaga.visibility = View.VISIBLE
        } else {
            holder.txtStatusConta.text = "Pagamento efetivado"
            holder.txtStatusConta.setTextColor(this.contexto.getColor(android.R.color.holo_green_dark))
            holder.imgStatusConta.setImageResource(R.drawable.ic_conta_paga)

            holder.btnMarcarContaComoPaga.visibility = View.GONE
        }

        // callback acionado quando o usuário clicar no botão para deletar a conta
        holder.btnDeletarConta.setOnClickListener { this.onDeletarConta(contaPagar.tituloConta) }
        // callback acionado quando o usuário clicar no botão para marcar a conta como paga
        holder.btnMarcarContaComoPaga.setOnClickListener { this.onMarcarContaPaga(contaPagar.tituloConta) }
    }

    fun setContasPagar(contasPagar: ArrayList<ContaPagar>) {
        this.contasPagar = contasPagar
        notifyDataSetChanged()
    }

}