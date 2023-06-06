package com.example.tarefas_gestor

import android.database.sqlite.SQLiteDatabase
class TabelaCategorias(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, nome TEXT NOT NULL, cor TEXT NOT NULL)")
    }

    companion object {
        const val NOME_TABELA = "categorias"
    }
}