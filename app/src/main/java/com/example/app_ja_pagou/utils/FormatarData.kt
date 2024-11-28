package com.example.app_ja_pagou.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatarData {

    companion object {

        // formatar data no formato passado como argumento
        fun obterDataFormatada(formato: String, data: Date): String {
            val formatadorData: SimpleDateFormat = SimpleDateFormat(formato, Locale.getDefault())

            return formatadorData.format(data)
        }

        // obter objeto do tipo Date a partir da data informada no formato de string
        fun obterObjetoDateAPartirDeDataInformada(formato: String, dataString: String): Date {
            val formatadorData: SimpleDateFormat = SimpleDateFormat(formato, Locale.getDefault())

            return formatadorData.parse(dataString)!!
        }

    }

}