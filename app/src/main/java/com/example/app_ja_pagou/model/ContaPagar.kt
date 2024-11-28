package com.example.app_ja_pagou.model

import java.util.Date

data class ContaPagar(
    var id: Int = 0,
    var tituloConta: String = "",
    var valor: Double = 0.0,
    var dataNotificacao: Date? = null,
    var status: String = "",
    var tipoConta: TipoConta? = null,
    var usuarioId: Int = 0,
    var dataPagamento: Date? = null,
    var mesPagamentoConta: String = ""
)