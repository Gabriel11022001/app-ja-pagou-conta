package com.example.app_ja_pagou

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SimpleAdapter
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.app_ja_pagou.model.ContaPagar
import com.example.app_ja_pagou.model.TipoConta
import com.example.app_ja_pagou.repositorio.ContaRepositorio
import com.example.app_ja_pagou.repositorio.TipoContaRepositorio
import com.google.android.material.snackbar.Snackbar

class CadastroContaPagarActivity : AppCompatActivity() {

    private var idConta: Int = 0
    private lateinit var tipoContaRepositorio: TipoContaRepositorio
    private lateinit var contaRepositorio: ContaRepositorio
    private lateinit var edtTituloConta: EditText
    private lateinit var edtValorConta: EditText
    private lateinit var edtDataPagamentoConta: EditText
    private lateinit var edtDataNotificacaoConta: EditText
    private lateinit var spnTipoConta: Spinner
    private lateinit var swContaJaEstaPaga: Switch
    private lateinit var btnSalvarConta: AppCompatButton
    private lateinit var btnRetornar: ImageButton
    private var tiposConta: List<TipoConta>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_conta_pagar)

        // mapear os elementos de interface
        this.mapearElementosInterface()

        // mapear eventos
        this.mapearEventos()

        this.contaRepositorio = ContaRepositorio(this)
        this.tipoContaRepositorio = TipoContaRepositorio(this)

        val idConta: Int = intent.getIntExtra("conta_id", 0)

        if (idConta != 0) {
            this.idConta = idConta
            this.buscarContaPeloId()
        }

    }

    override fun onResume() {
        super.onResume()

        // apresentar os tipos de conta no spinner
        this.buscarTiposConta()
    }

    private fun buscarTiposConta() {

        try {
            this.tiposConta = this.tipoContaRepositorio.buscarTiposConta()
            // configurar o spinner dos tipos de conta
            val adapterTiposConta: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)

            val nomesTiposVenda: ArrayList<String> = arrayListOf()

            this.tiposConta!!.forEach { tipoConta ->
                nomesTiposVenda.add(tipoConta.nome)
            }

            adapterTiposConta.addAll(nomesTiposVenda)
            this.spnTipoConta.adapter = adapterTiposConta
        } catch (e: Exception) {
            this.apresentarAlerta(true, "Ocorreu um erro ao tentar-se consultar os tipos de conta: ${ e.message.toString() }")
        }
        
    }

    private fun mapearElementosInterface() {
        this.btnRetornar = findViewById(R.id.btn_retornar)
        this.btnSalvarConta = findViewById(R.id.btn_cadastrar_conta)
        this.edtTituloConta = findViewById(R.id.edt_titulo_conta)
        this.edtValorConta = findViewById(R.id.edt_valor_conta)
        this.edtDataPagamentoConta = findViewById(R.id.edt_data_pagamento)
        this.edtDataNotificacaoConta = findViewById(R.id.edt_data_notificacao)
        this.spnTipoConta = findViewById(R.id.spn_tipo_conta)
        this.swContaJaEstaPaga = findViewById(R.id.sw_conta_esta_paga)
    }

    private fun mapearEventos() {
        this.btnRetornar.setOnClickListener { this.retornar() }
        this.btnSalvarConta.setOnClickListener { this.salvarConta() }
    }

    private fun buscarContaPeloId() {

        try {

        } catch (e: Exception) {
            this.apresentarAlerta(true, "Ocorreu um erro ao tentar-se consultar a conta em questão: ${ e.message.toString() }")
        }

    }

    private fun retornar() {
        finish()
    }

    override fun onBackPressed() {
        this.retornar()
    }

    private fun fecharTeclado() {

    }

    private fun validarFormulario(): Boolean {
        var ok: Boolean = true
        val titulo: String = this.edtTituloConta.text.toString()
        val valor: Double = if (this.edtValorConta.text.toString().isBlank()) 0.0 else this.edtValorConta.text.toString().toDouble()
        val dataNotificacao: String = this.edtDataNotificacaoConta.text.toString()
        val dataPagamento: String = this.edtDataPagamentoConta.text.toString()

        if (titulo.isBlank()) {
            ok = false
            this.apresentarAlerta(true, "Informe o título da conta.")
        } else if (valor <= 0) {
            ok = false
            this.apresentarAlerta(true, "O valor da conta é inválido.")
        } else if (dataPagamento.isBlank()) {
            ok = false
            this.apresentarAlerta(true, "Informe a data de pagamento da conta.")
        } else if (dataNotificacao.isBlank()) {
            ok = false
            this.apresentarAlerta(true, "Informe a data de notificação da conta.")
        }

        return ok
    }

    private fun apresentarAlerta(erro: Boolean, msg: String) {
        val snackBarAlerta: Snackbar = Snackbar.make(findViewById(R.id.main), msg, Snackbar.LENGTH_SHORT)

        if (erro) {
            snackBarAlerta.setBackgroundTint(getColor(android.R.color.holo_red_dark))
        } else {
            snackBarAlerta.setBackgroundTint(getColor(android.R.color.holo_green_dark))
        }

        snackBarAlerta.show()
    }

    private fun obterIdTipoContaPeloNome(nomeTipoConta: String): Int {

        for (tipoConta in this.tiposConta!!) {

            if (tipoConta.nome == nomeTipoConta) {

                return tipoConta.id
            }

        }

        return 0
    }

    private fun salvarConta() {

        try {
            this.fecharTeclado()

            if (this.validarFormulario()) {
                val tipoContaNome: String = this.spnTipoConta.selectedItem.toString()
                val idTipoConta: Int = this.obterIdTipoContaPeloNome(tipoContaNome)

                val contaPagarCadastrar: ContaPagar = ContaPagar()
                contaPagarCadastrar.tituloConta = this.edtTituloConta.text.toString()
                contaPagarCadastrar.valor = this.edtValorConta.text.toString().toDouble()
                contaPagarCadastrar.tipoConta = TipoConta(id = idTipoConta)
                contaPagarCadastrar.status = if (this.swContaJaEstaPaga.isActivated) "Paga" else "Aguardando pagamento"

            }

        } catch (e: Exception) {
            // apresentar alerta de erro
            this.apresentarAlerta(true, "Ocorreu o seguinte erro: ${ e.message.toString() }")

            Log.e("erro_cadastrar_conta", "Erro: ${ e.message.toString() }", e)
        }

    }

}