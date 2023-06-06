package com.example.tarefas_gestor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val NOME_BASE_DADOS = "tarefas.db"
private const val VERSAO_BASE_DADOS = 1

class BdTarefasOpenHelper(
    context: Context?
) : SQLiteOpenHelper(context, NOME_BASE_DADOS, null, VERSAO_BASE_DADOS) {

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}