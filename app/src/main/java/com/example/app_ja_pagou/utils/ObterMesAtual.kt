package com.example.app_ja_pagou.utils

import java.time.LocalDate

class ObterMesAtual {

    companion object {

        fun getMesAtual(): String {
            val dataAtual = LocalDate.now()
            val mesAtualNumero = dataAtual.month.value

            return if (mesAtualNumero == 1) {
                "Janeiro"
            } else if (mesAtualNumero == 2) {
                "Fevereiro"
            } else if (mesAtualNumero == 3) {
                "Março"
            } else if (mesAtualNumero == 4) {
                "Abril"
            } else if (mesAtualNumero == 5) {
                "Maio"
            } else if (mesAtualNumero == 6) {
                "Junho"
            } else if (mesAtualNumero == 7) {
                "Julho"
            } else if (mesAtualNumero == 8) {
                "Agosto"
            } else if (mesAtualNumero == 9) {
                "Setembro"
            } else if (mesAtualNumero == 10) {
                "Outubro"
            } else if (mesAtualNumero == 11) {
                "Novembro"
            } else {
                "Dezembro"
            }
        }

    }

}