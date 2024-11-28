package com.example.app_ja_pagou.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_ja_pagou.model.Usuario
import com.example.app_ja_pagou.utils.Constantes
import com.example.app_ja_pagou.utils.FormatarData

class UsuarioRepositorio(contexto: Context): Repositorio(contexto) {

    fun cadastrarUsuario(usuarioCadastrar: Usuario): Int {
        val contentValuesDadosUsuario: ContentValues = ContentValues()
        contentValuesDadosUsuario.put("nome_completo", usuarioCadastrar.nomeCompleto)
        contentValuesDadosUsuario.put("email", usuarioCadastrar.email)
        contentValuesDadosUsuario.put("telefone", usuarioCadastrar.telefone)
        contentValuesDadosUsuario.put("senha", usuarioCadastrar.senha)
        contentValuesDadosUsuario.put("data_nascimento", FormatarData.obterDataFormatada("dd/MM/yyyy", usuarioCadastrar.dataNascimento!!))

        return super.bancoDados.insertOrThrow(Constantes.NOME_TB_USUARIOS, null, contentValuesDadosUsuario).toInt()
    }

    fun buscarUsuarioPeloEmailSenha(email: String, senha: String): Usuario? {
        var usuario: Usuario? = null
        val query: String = "SELECT * FROM ${ Constantes.NOME_TB_USUARIOS } WHERE email = ? AND senha = ?"
        val cursor: Cursor = super.bancoDados.rawQuery(query, arrayOf(
            email,
            senha
        ))

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                usuario = Usuario(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nomeCompleto = cursor.getString(cursor.getColumnIndex("nome_completo")),
                    email = cursor.getString(cursor.getColumnIndex("email")),
                    telefone = cursor.getString(cursor.getColumnIndex("telefone")),
                    dataNascimento = FormatarData.obterObjetoDateAPartirDeDataInformada("dd/MM/yyyy", cursor.getString(cursor.getColumnIndex("data_nascimento")))
                )
            }

            cursor.close()
        }

        return usuario
    }

    fun buscarUsuarioPeloEmail(email: String): Usuario? {
        var usuario: Usuario? = null

        return usuario
    }

}