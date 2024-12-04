package com.example.app_ja_pagou.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_ja_pagou.model.ContaPagar
import com.example.app_ja_pagou.model.TipoConta
import com.example.app_ja_pagou.utils.Constantes
import com.example.app_ja_pagou.utils.FormatarData

class ContaRepositorio(contexto: Context): Repositorio(contexto) {

    fun salvarConta(contaSalvar: ContaPagar): Int {

        if (contaSalvar.id != 0) {

            return this.editarConta(contaSalvar)
        }

        return this.cadastrarConta(contaSalvar)
    }

    private fun cadastrarConta(contaCadastrar: ContaPagar): Int {
        val contentValuesCadastrarConta: ContentValues = ContentValues()
        contentValuesCadastrarConta.put("titulo_conta", contaCadastrar.tituloConta)
        contentValuesCadastrarConta.put("valor", contaCadastrar.valor)
        contentValuesCadastrarConta.put("data_pagamento", FormatarData.obterDataFormatada("dd/MM/yyyy", contaCadastrar.dataPagamento!!))
        contentValuesCadastrarConta.put("data_notificar", FormatarData.obterDataFormatada("dd/MM/yyyy", contaCadastrar.dataNotificacao!!))
        contentValuesCadastrarConta.put("status", contaCadastrar.status)
        contentValuesCadastrarConta.put("usuario_id", contaCadastrar.usuarioId)
        contentValuesCadastrarConta.put("tipo_conta_id", contaCadastrar.tipoConta!!.id)
        contentValuesCadastrarConta.put("mes_pagamento", contaCadastrar.mesPagamentoConta)

        return super.bancoDados.insertOrThrow(Constantes.NOME_TB_CONTAS, null, contentValuesCadastrarConta).toInt()
    }

    private fun editarConta(contaEditar: ContaPagar): Int {

        return 0
    }

    fun buscarContasMes(mes: String): List<ContaPagar> {
        val contasMes: ArrayList<ContaPagar> = arrayListOf()
        val query: String = "SELECT tcp.id, tcp.titulo_conta, tcp.valor, tcp.mes_pagamento, tcp.data_notificar, tcp.data_pagamento, tcp.status, ttc.id AS tipo_conta_id" +
                ", ttc.nome AS tipo_conta_nome FROM ${ Constantes.NOME_TB_CONTAS } AS tcp, ${ Constantes.NOME_TB_TIPOS_CONTA} AS ttc WHERE mes_pagamento = ? " +
                "AND tcp.tipo_conta_id = ttc.id;"
        val cursor: Cursor = super.bancoDados.rawQuery(query, arrayOf(mes))

        if (cursor != null) {

            while (cursor.moveToNext()) {
                val conta: ContaPagar = ContaPagar(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    tituloConta = cursor.getString(cursor.getColumnIndex("titulo_conta")),
                    mesPagamentoConta = cursor.getString(cursor.getColumnIndex("mes_pagamento")),
                    valor = cursor.getDouble(cursor.getColumnIndex("valor")),
                    dataNotificacao = FormatarData.obterObjetoDateAPartirDeDataInformada(
                        "dd/MM/yyyy",
                        cursor.getString(cursor.getColumnIndex("data_notificar"))
                    ),
                    dataPagamento = FormatarData.obterObjetoDateAPartirDeDataInformada(
                        "dd/MM/yyyy",
                        cursor.getString(cursor.getColumnIndex("data_pagamento"))
                    ),
                    status = cursor.getString(cursor.getColumnIndex("status")),
                    // usuarioId = cursor.getInt(cursor.getColumnIndex("usuario_id")),
                    tipoConta = TipoConta(
                        id = cursor.getInt(cursor.getColumnIndex("tipo_conta_id")),
                        nome = cursor.getString(cursor.getColumnIndex("tipo_conta_nome"))
                    )
                )

                contasMes.add(conta)
            }

            cursor.close()
        }

        return contasMes
    }

    fun buscarContaPeloId(idConta: Int): ContaPagar? {
        var conta: ContaPagar? = null

        return conta
    }

    fun deletarConta(idConta: Int) {

    }

}