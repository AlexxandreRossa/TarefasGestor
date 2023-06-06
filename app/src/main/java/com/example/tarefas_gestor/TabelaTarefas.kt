package com.example.tarefas_gestor

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaTarefas(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_NOME TEXT NOT NULL, $CAMPO_DESCRICAO TEXT, $CAMPO_DATA_VENCIMENTO INTEGER, $CAMPO_FK_CATEGORIA INTEGER NOT NULL, FOREIGN KEY ($CAMPO_FK_CATEGORIA) REFERENCES ${TabelaCategorias.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    companion object {
        const val NOME_TABELA = "tarefas"

        const val CAMPO_NOME = "nome"
        const val CAMPO_DESCRICAO = "descricao"
        const val CAMPO_DATA_VENCIMENTO = "data_vencimento"
        const val CAMPO_FK_CATEGORIA = "id_categoria"

        val CAMPOS = arrayOf(BaseColumns._ID, CAMPO_NOME, CAMPO_DESCRICAO, CAMPO_DATA_VENCIMENTO, CAMPO_FK_CATEGORIA)
    }
}