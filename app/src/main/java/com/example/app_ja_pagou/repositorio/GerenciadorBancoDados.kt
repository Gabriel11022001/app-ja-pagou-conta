package com.example.app_ja_pagou.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.app_ja_pagou.model.Usuario
import com.example.app_ja_pagou.utils.Constantes
import com.example.app_ja_pagou.utils.FormatarData

open class GerenciadorBancoDados(private val contexto: Context): SQLiteOpenHelper(
    contexto,
    Constantes.NOME_BANCO_DADOS,
    null,
    Constantes.VERSAO_BANCO
) {

    private var bancoDados: SQLiteDatabase? = null

    // criar o banco de dados com suas tabelas
    override fun onCreate(db: SQLiteDatabase?) {
        this.bancoDados = db!!
        this.criarTabelaUsuarios()
        this.criarTabelaTiposConta()
        this.criarTabelaContasPagar()
    }

    // atualizar o banco de dados e suas tabelas
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun criarTabelaUsuarios() {
        Log.d("criar_tabela_usuarios", "Criando tabela de usuário na base de dados...")
        this.bancoDados!!.execSQL("CREATE TABLE IF NOT EXISTS tb_usuarios(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_completo TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "telefone TEXT NOT NULL," +
                "senha TEXT NOT NULL," +
                "data_nascimento TEXT NOT NULL);")
        Log.d("criar_tabela_usuarios", "Tabela de usuários criada com sucesso na base de dados...")

        this.registrarUsuariosTeste()
    }

    private fun criarTabelaContasPagar() {
        Log.d("criar_tabela_contas_pagar", "Criando tabela de contas que serão pagas...")
        // this.bancoDados!!.execSQL("")
        Log.d("criar_tabela_contas_pagar", "Tabela de contas a pagar criada com sucesso...")
    }

    private fun criarTabelaTiposConta() {
        Log.d("criar_tabela_tipos_conta", "Criando a tabela de tipos de conta...")

        Log.d("criar_tabela_tipos_conta", "Tabela de tipos de conta criada com sucesso...")

        this.registrarTiposContaPadrao()
    }

    private fun registrarUsuariosTeste() {

        for (contador in 0 until 10) {
            val usuarioTesteCadastrar: Usuario = Usuario(
                nomeCompleto = "Usuário de teste $contador",
                email = "teste$contador@teste.com",
                telefone = "(14) 99877-0987",
                senha = "teste$contador",
                dataNascimento = FormatarData.obterObjetoDateAPartirDeDataInformada("dd/MM/yyyy", "11/02/2001")
            )

            val contentValuesCadastrarUsuario: ContentValues = ContentValues()
            contentValuesCadastrarUsuario.put("nome_completo", usuarioTesteCadastrar.nomeCompleto)
            contentValuesCadastrarUsuario.put("email", usuarioTesteCadastrar.email)
            contentValuesCadastrarUsuario.put("telefone", usuarioTesteCadastrar.telefone)
            contentValuesCadastrarUsuario.put("senha", usuarioTesteCadastrar.senha)
            contentValuesCadastrarUsuario.put("data_nascimento", FormatarData.obterDataFormatada("dd/MM/yyyy", usuarioTesteCadastrar.dataNascimento!!))

            this.bancoDados?.insertOrThrow(Constantes.NOME_TB_USUARIOS, null, contentValuesCadastrarUsuario)
        }

    }

    private fun registrarTiposContaPadrao() {

    }

}