package com.example.app_ja_pagou

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ja_pagou.adapter.ContaPagarAdapter
import com.example.app_ja_pagou.dialog.DialogConfirmar
import com.example.app_ja_pagou.model.ContaPagar
import com.example.app_ja_pagou.repositorio.ContaRepositorio
import com.example.app_ja_pagou.utils.ObterMesAtual
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var dialogConfirmarDelecao: DialogConfirmar
    private lateinit var btnRetornar: ImageButton
    private lateinit var btnAdicionarNovaConta: AppCompatButton
    private lateinit var recyclerContasMesAtual: RecyclerView
    private lateinit var edtFiltrarContas: EditText
    private lateinit var spnMesAtualFiltro: Spinner
    private lateinit var txtTituloMesSelecionado: TextView
    private lateinit var txtNaoExistemContasMesAtual: TextView
    private lateinit var progressBarConsultandoContasMesAtual: ProgressBar
    private lateinit var contaPagarRepositorio: ContaRepositorio
    private lateinit var contaPagarAdapter: ContaPagarAdapter
    private val meses: List<String> = listOf(
        "Janeiro",
        "Fevereiro",
        "Março",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mapearElementosInterface()
        this.mapearEventos()

        this.contaPagarRepositorio = ContaRepositorio(this)

        // callback do evento de deleção de conta
        val onDeletarConta: (String) -> Unit = { tituloConta ->
            this.deletarConta(tituloConta)
        }

        // callback do evento de marcar a conta como paga
        val onMarcarContaComoPaga: (String) -> Unit = { tituloConta ->

        }

        this.contaPagarAdapter = ContaPagarAdapter(
            this,
            onDeletarConta,
            onMarcarContaComoPaga
        )

        this.recyclerContasMesAtual.layoutManager = LinearLayoutManager(this)
        this.recyclerContasMesAtual.adapter = this.contaPagarAdapter

        this.dialogConfirmarDelecao = DialogConfirmar(this, "Deseja efetivar a deleção?") { idContaDeletar ->
            // evento de deleção da conta
            this.efetivarDelecaoConta(idContaDeletar = idContaDeletar)
        }
    }

    override fun onResume() {
        super.onResume()

        // configurar o spinner contendo o mês atual
        this.configurarSpinnerMesAtualSelecionar()

        val dataAtual: LocalDate = LocalDate.now()
        val mesAtual = dataAtual.month!!.value
        var mesAtualFiltrarNome: String = ""

        when (mesAtual) {
            1 -> mesAtualFiltrarNome = "Janeiro"
            2 -> mesAtualFiltrarNome = "Fevereiro"
            3 -> mesAtualFiltrarNome = "Março"
            4 -> mesAtualFiltrarNome = "Abril"
            5 -> mesAtualFiltrarNome = "Maio"
            6 -> mesAtualFiltrarNome = "Junho"
            7 -> mesAtualFiltrarNome = "Julho"
            8 -> mesAtualFiltrarNome = "Agosto"
            9 -> mesAtualFiltrarNome = "Setembro"
            10 -> mesAtualFiltrarNome = "Outubro"
            11 -> mesAtualFiltrarNome = "Novembro"
            12 -> mesAtualFiltrarNome = "Dezembro"
            else -> Log.e("erro_mes_atual", "Não existe o mês em questão")
        }

        this.filtrarContasMesSelecionado(mesAtualFiltrarNome)
    }

    private fun configurarSpinnerMesAtualSelecionar() {
        val adapterSpinnerMesAtualSelecionado = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            this.meses
        )

        this.spnMesAtualFiltro.adapter = adapterSpinnerMesAtualSelecionado

        this.spnMesAtualFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // filtrar as contas pelo mes selecionado
                setTituloMesAtualSelecionado(position + 1)
                filtrarContasMesSelecionado(meses[ position ])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }

        }

        // definir o mês atual como o mes selecionado por padrão
        this.definirMesAtualComoMesSelecionadoPadrao()
    }

    private fun filtrarContasMesSelecionado(mesSelecionado: String) {

        try {
            this.txtNaoExistemContasMesAtual.visibility = View.GONE
            this.progressBarConsultandoContasMesAtual.visibility = View.VISIBLE
            this.recyclerContasMesAtual.visibility = View.GONE

            Handler().postDelayed(Runnable {
                this.progressBarConsultandoContasMesAtual.visibility = View.GONE

                val contasPagarMesAtual = this.contaPagarRepositorio.buscarContasMes(mesSelecionado)

                if (contasPagarMesAtual.size > 0) {
                    this.recyclerContasMesAtual.visibility = View.VISIBLE

                    val contasPagarArrayList: ArrayList<ContaPagar> = arrayListOf()

                    contasPagarMesAtual.forEach { contaPagarMesAtual ->
                        contasPagarArrayList.add(contaPagarMesAtual)
                    }

                    this.contaPagarAdapter.setContasPagar(contasPagarArrayList)
                } else {
                    // apresentar mensagem de que não existem contas para pagar no mês atual
                    this.txtNaoExistemContasMesAtual.visibility = View.VISIBLE
                }

            }, 3000)

        } catch (e: Exception) {
            // apresentar alerta de erro
            Log.e("erro_filtrar_contas_mes", e.message.toString())
        }

    }

    private fun definirMesAtualComoMesSelecionadoPadrao() {
        val dataAtual = LocalDate.now()
        val mesAtual = dataAtual.month.value

        this.spnMesAtualFiltro.setSelection(mesAtual - 1)

        this.setTituloMesAtualSelecionado(mesAtual)
    }

    private fun setTituloMesAtualSelecionado(mesAtual: Int) {
        var tituloMesAtualSelecionado: String = "Contas do mês de "

        when (mesAtual) {
            1 -> tituloMesAtualSelecionado += "Janeiro"
            2 -> tituloMesAtualSelecionado += "Fevereiro"
            3 -> tituloMesAtualSelecionado += "Março"
            4 -> tituloMesAtualSelecionado += "Abril"
            5 -> tituloMesAtualSelecionado += "Maio"
            6 -> tituloMesAtualSelecionado += "Junho"
            7 -> tituloMesAtualSelecionado += "Julho"
            8 -> tituloMesAtualSelecionado += "Agosto"
            9 -> tituloMesAtualSelecionado += "Setembro"
            10 -> tituloMesAtualSelecionado += "Outubro"
            11 -> tituloMesAtualSelecionado += "Novembro"
            12 -> tituloMesAtualSelecionado += "Dezembro"
            else -> Log.e("erro_mes_atual", "Não existe o mês em questão")
        }

        this.txtTituloMesSelecionado.text = tituloMesAtualSelecionado
    }

    private fun mapearElementosInterface() {
        this.btnRetornar = findViewById(R.id.btn_retornar)
        this.btnAdicionarNovaConta = findViewById(R.id.btn_adicionar_nova_conta)
        this.recyclerContasMesAtual = findViewById(R.id.recycler_contas_mes_atual)
        this.edtFiltrarContas = findViewById(R.id.edt_consultar_conta)
        this.spnMesAtualFiltro = findViewById(R.id.spn_mes_consultar_contas)
        this.txtTituloMesSelecionado = findViewById(R.id.txt_titulo_contas_mes)
        this.txtNaoExistemContasMesAtual = findViewById(R.id.txt_nao_existem_contas_mes_atual)
        this.progressBarConsultandoContasMesAtual = findViewById(R.id.progress_bar_carregar_contas_mes_atual)
    }

    private fun mapearEventos() {
        this.btnRetornar.setOnClickListener(this)
        this.btnAdicionarNovaConta.setOnClickListener(this)
    }

    private fun redirecionarTelaAdicionarNovaConta() {
        startActivity(Intent(this, CadastroContaPagarActivity::class.java))
    }

    private fun retornar() {
        val builderAlertDialogSair: AlertDialog.Builder = AlertDialog.Builder(this)
        builderAlertDialogSair.setTitle("Sair")
        builderAlertDialogSair.setMessage("Deseja sair do aplicativo?")

        // confirmar sair do app
        builderAlertDialogSair.setPositiveButton("Sim", { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        builderAlertDialogSair.setNegativeButton("Não, permanecer", { dialog, which ->
            dialog.dismiss()
        })

        // apresentar o dialog
        builderAlertDialogSair.create()
            .show()
    }

    override fun onBackPressed() {
        this.retornar()
    }

    override fun onClick(v: View?) {

        if (v!!.id == R.id.btn_retornar) {
            this.retornar()

            return
        }

        if (v!!.id == R.id.btn_adicionar_nova_conta) {
            this.redirecionarTelaAdicionarNovaConta()

            return
        }

    }

    private fun efetivarDelecaoConta(idContaDeletar: Int) {
        Log.d("id_conta_deletar", idContaDeletar.toString())

        try {
            this.contaPagarRepositorio.deletarConta(idConta = idContaDeletar)

            this.dialogConfirmarDelecao.esconder()
            this.apresentarNotificacao(true, "Conta deletada com sucesso.")
            this.recarregarListagemContas()
        } catch (e: Exception) {
            Log.e("erro_deletar_conta", e.message.toString())

            this.apresentarNotificacao(false, "Erro ao tentar-se deletar a conta.")
            this.dialogConfirmarDelecao.esconder()
        }

    }

    private fun recarregarListagemContas() {
        // obter o mês atual
        val mesAtual = ObterMesAtual.getMesAtual()

        // listar as contas novamente
        this.filtrarContasMesSelecionado(mesSelecionado = mesAtual)
    }

    private fun deletarConta(tituloConta: String) {
        Log.d("conta_deletar", "Título da conta que será deletada: ${ tituloConta }")
        val idContaDeletar: Int = this.contaPagarRepositorio.buscarIdContaPeloTitulo(tituloConta)
        this.dialogConfirmarDelecao.idEntidadeRealizarOperacaoConfirmar = idContaDeletar

        this.dialogConfirmarDelecao.apresentar()
    }

    private fun apresentarNotificacao(sucesso: Boolean, mensagem: String) {
        val snackBarAlertaOperacao: Snackbar = Snackbar.make(findViewById(android.R.id.content), mensagem, Snackbar.LENGTH_SHORT)

        if (sucesso) {
            snackBarAlertaOperacao.setBackgroundTint(getColor(android.R.color.holo_green_dark))
        } else {
            snackBarAlertaOperacao.setBackgroundTint(getColor(android.R.color.holo_red_dark))
        }

        snackBarAlertaOperacao.show()
    }

}