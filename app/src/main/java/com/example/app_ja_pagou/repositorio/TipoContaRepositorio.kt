package com.example.app_ja_pagou.repositorio

import android.content.Context
import android.database.Cursor
import com.example.app_ja_pagou.model.TipoConta
import com.example.app_ja_pagou.utils.Constantes

class TipoContaRepositorio(contexto: Context): Repositorio(contexto) {

    fun buscarTiposConta(): List<TipoConta> {
        val tiposConta: ArrayList<TipoConta> = ArrayList()
        val query: String = "SELECT * FROM ${ Constantes.NOME_TB_TIPOS_CONTA }"
        val cursor: Cursor = super.bancoDados.rawQuery(query, null)

        if (cursor != null) {

            while (cursor.moveToNext()) {
                val tipoConta: TipoConta = TipoConta(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nome = cursor.getString(cursor.getColumnIndex("nome"))
                )

                tiposConta.add(tipoConta)
            }

            cursor.close()
        }

        return tiposConta
    }

}