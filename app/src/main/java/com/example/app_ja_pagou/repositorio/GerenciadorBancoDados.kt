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
        this.bancoDados!!.execSQL("CREATE TABLE IF NOT EXISTS ${ Constantes.NOME_TB_CONTAS}(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo_conta TEXT NOT NULL," +
                "valor DECIMAL NOT NULL," +
                "data_pagamento TEXT NOT NULL," +
                "data_notificar TEXT NOT NULL," +
                "status TEXT NOT NULL," +
                "mes_pagamento TEXT NOT NULL," +
                "tipo_conta_id INTEGER NOT NULL," +
                "FOREIGN KEY(tipo_conta_id) REFERENCES ${ Constantes.NOME_TB_TIPOS_CONTA }(id));")
        Log.d("criar_tabela_contas_pagar", "Tabela de contas a pagar criada com sucesso...")

        // registrar contas de teste divididas entre todos os meses do ano
        this.registrarContasTeste()
    }

    private fun criarTabelaTiposConta() {
        Log.d("criar_tabela_tipos_conta", "Criando a tabela de tipos de conta...")

        this.bancoDados!!.execSQL("CREATE TABLE IF NOT EXISTS tb_tipos_conta(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL);")

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

        for (contador in 0 until 10) {
            val nomeTipoConta: String = "Tipo de conta ${ contador }"
            val contentValuesTipoContaTeste = ContentValues()
            contentValuesTipoContaTeste.put("nome", nomeTipoConta)

            this.bancoDados!!.insertOrThrow(Constantes.NOME_TB_TIPOS_CONTA, null, contentValuesTipoContaTeste)
        }

    }

    private fun registrarContasTeste() {
        val meses = mutableMapOf<String, Int>()

        meses.put("Janeiro", 1)
        meses.put("Fevereiro", 2)
        meses.put("Março", 3)
        meses.put("Abril", 4)
        meses.put("Maio", 5)
        meses.put("Junho", 6)
        meses.put("Julho", 7)
        meses.put("Agosto", 8)
        meses.put("Setembro", 9)
        meses.put("Outubro", 10)
        meses.put("Novembro", 11)
        meses.put("Dezembro", 12)

        meses.forEach { mesMap ->
            this.registrarContasMes(mesMap.key, mesMap.value)
        }

    }

    private fun registrarContasMes(nomeMes: String, numeroMes: Int) {

        for (contador in 0 until 100) {
            val tituloConta: String = "Conta do mês de ${ nomeMes } ${ (contador + 1) }"
            val valorConta: Double = 100 + contador.toDouble()
            val status: String = if (contador % 2 == 0) "aguardando pagamento" else "pagamento efetivado"
            val idTipoConta: Int = if (contador % 2 == 0) 1 else 2
            val dataPagamentoConta: String = if (numeroMes < 10) "11/0${ numeroMes }/2025" else "11/${ numeroMes }/2025"
            val dataNotificacaoConta: String = if (numeroMes < 10) "10/0${ numeroMes }/2025" else "10/${ numeroMes }/2025"

            val contentValuesCadastrarContaTeste: ContentValues = ContentValues()
            contentValuesCadastrarContaTeste.put("titulo_conta", tituloConta)
            contentValuesCadastrarContaTeste.put("valor", valorConta)
            contentValuesCadastrarContaTeste.put("status", status)
            contentValuesCadastrarContaTeste.put("tipo_conta_id", idTipoConta)
            contentValuesCadastrarContaTeste.put("mes_pagamento", nomeMes)
            contentValuesCadastrarContaTeste.put("data_notificar", dataNotificacaoConta)
            contentValuesCadastrarContaTeste.put("data_pagamento", dataPagamentoConta)

            // registrar as contas para o mês em questão
            this.bancoDados!!.insertOrThrow(Constantes.NOME_TB_CONTAS, null, contentValuesCadastrarContaTeste)
        }

    }

}