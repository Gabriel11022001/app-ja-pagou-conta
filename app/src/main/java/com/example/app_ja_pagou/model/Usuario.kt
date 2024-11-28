package com.example.app_ja_pagou.model

import java.util.Date

data class Usuario(
    var id: Int = 0,
    var nomeCompleto: String = "",
    var email: String = "",
    var senha: String = "",
    var senhaConfirmacao: String = "",
    var telefone: String = "",
    var dataNascimento: Date? = null
)