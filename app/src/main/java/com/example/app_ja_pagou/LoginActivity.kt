package com.example.app_ja_pagou

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app_ja_pagou.model.Usuario
import com.example.app_ja_pagou.repositorio.UsuarioRepositorio
import com.example.app_ja_pagou.utils.ValidaDados
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var btnRealizarLogin: AppCompatButton
    private lateinit var containerCobreBtnRealizarLogin: ConstraintLayout
    private lateinit var edtEmailUsuario: EditText
    private lateinit var edtSenhaUsuario: EditText
    private lateinit var txtAindaNaoEstaCadastrado: TextView
    private lateinit var usuarioRepositorio: UsuarioRepositorio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // mapear elementos de interface
        this.mapearElementosInterface()

        // mapear eventos
        this.mapearEventos()

        this.usuarioRepositorio = UsuarioRepositorio(this)
    }

    private fun mapearElementosInterface() {
        this.btnRealizarLogin = findViewById(R.id.btn_realizar_login)
        this.containerCobreBtnRealizarLogin = findViewById(R.id.container_botao_entrar_carregando)
        this.edtEmailUsuario = findViewById(R.id.edt_email_login)
        this.edtSenhaUsuario = findViewById(R.id.edt_senha_login)
        this.txtAindaNaoEstaCadastrado = findViewById(R.id.txt_ainda_nao_estou_cadastrado)
    }

    private fun mapearEventos() {
        this.btnRealizarLogin.setOnClickListener { this.realizarLogin() }
        this.txtAindaNaoEstaCadastrado.setOnClickListener { this.redirecionarTelaCadastroUsuario() }
    }

    private fun apresentarNotificacaoErro(msgErro: String) {
        val snackBarErro: Snackbar = Snackbar.make(findViewById(R.id.main), msgErro, Snackbar.LENGTH_SHORT)
        snackBarErro.setBackgroundTint(getColor(android.R.color.holo_red_dark))
        snackBarErro.show()
    }

    private fun fecharTeclado() {
        val teclado = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val campoComFoco = currentFocus

        if (campoComFoco != null) {
            teclado.hideSoftInputFromWindow(campoComFoco.windowToken, 0)
        }

    }

    private fun redirecionarTelaCadastroUsuario() {

    }

    private fun validarCamposLogin(): Boolean {
        var ok: Boolean = true
        val email: String = this.edtEmailUsuario.text.toString().trim()
        val senha: String = this.edtSenhaUsuario.text.toString().trim()

        if (email.isBlank() && senha.isBlank()) {
            ok = false
            this.apresentarNotificacaoErro("Informe o e-mail e a senha.")
        } else if (email.isBlank()) {
            ok = false
            this.apresentarNotificacaoErro("Informe o e-mail.")
        } else if (!ValidaDados.validarEmail(email)) {
            ok = false
            this.apresentarNotificacaoErro("E-mail inválido.")
        } else if (senha.isBlank()) {
            ok = false
            this.apresentarNotificacaoErro("Informe a senha.")
        } else if (!ValidaDados.validarSenha(senha)) {
            ok = false
            this.apresentarNotificacaoErro("Senha inválida.")
        }

        return ok
    }

    private fun apresentarLoaderLogin() {
        this.containerCobreBtnRealizarLogin.visibility = View.VISIBLE
        // desabilitar os campos
        this.edtEmailUsuario.isEnabled = false
        this.edtSenhaUsuario.isEnabled = false
    }

    private fun esconderLoaderLogin() {
        this.containerCobreBtnRealizarLogin.visibility = View.GONE
        // habilitar os campos de login
        this.edtEmailUsuario.isEnabled = true
        this.edtSenhaUsuario.isEnabled = true
    }

    private fun realizarLogin() {
        this.fecharTeclado()

        try {

            if (this.validarCamposLogin()) {
                // apresentar o loader
                this.apresentarLoaderLogin()

                /**
                 * depois de 2 segundos, esconder o loader e processar as operações
                 */
                Handler().postDelayed(Runnable {
                    this.esconderLoaderLogin()
                    val email: String = this.edtEmailUsuario.text.toString().trim()
                    val senha: String = this.edtSenhaUsuario.text.toString().trim()
                    val usuarioLogado: Usuario? = this.usuarioRepositorio.buscarUsuarioPeloEmailSenha(email, senha)

                    if (usuarioLogado == null) {
                        this.apresentarNotificacaoErro("E-mail ou senha incorretos.")
                    } else {
                        // redirecionar o usuário para a tela home do aplicativo
                        val intentRedirecionarTelaInicial: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intentRedirecionarTelaInicial)
                        finish()
                    }

                }, 2000)

            }

        } catch (e: Exception) {
            Log.e("erro_login", "Ocorreu o seguinte erro ao tentar-se realizar login: ${ e.message.toString() }")

            this.apresentarNotificacaoErro("Erro ao tentar-se realizar login: ${ e.message.toString() }")
        }

    }

}