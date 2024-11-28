package com.example.app_ja_pagou.repositorio

import android.content.Context
import android.database.sqlite.SQLiteDatabase

open class Repositorio(contexto: Context): GerenciadorBancoDados(contexto = contexto) {

    protected var bancoDados: SQLiteDatabase = super.getWritableDatabase()

}