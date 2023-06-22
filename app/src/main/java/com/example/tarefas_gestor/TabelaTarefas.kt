package com.example.tarefas_gestor

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns

class TabelaTarefas(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_NOME TEXT NOT NULL, $CAMPO_DESCRICAO TEXT, $CAMPO_DATA_VENCIMENTO INTEGER, $CAMPO_FK_CATEGORIA INTEGER NOT NULL, FOREIGN KEY ($CAMPO_FK_CATEGORIA) REFERENCES ${TabelaCategorias.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    override fun consulta(
        colunas: Array<String>,
        selecao: String?,
        argsSelecao: Array<String>?,
        groupby: String?,
        having: String?,
        orderby: String?
    ): Cursor {
        val sql = SQLiteQueryBuilder()
        sql.tables = "$NOME_TABELA INNER JOIN ${TabelaTarefas.NOME_TABELA} ON ${TabelaTarefas.CAMPO_NOME} = $CAMPO_FK_CATEGORIA"
        return sql.query(db,colunas,selecao,argsSelecao,groupby,having,orderby)
    }

    companion object {
        const val NOME_TABELA = "tarefas"

        const val CAMPO_NOME = "nome"
        const val CAMPO_DESCRICAO = "descricao"
        const val CAMPO_DATA_VENCIMENTO = "data_vencimento"
        const val CAMPO_FK_CATEGORIA = "id_categoria"
        const val CAMPO_NOME_CATEGORIA = "nome_categoria"
        const val CAMPO_COR_CATEGORIA = "cor_categoria"


        val CAMPOS = arrayOf(BaseColumns._ID, CAMPO_NOME, CAMPO_DESCRICAO, CAMPO_DATA_VENCIMENTO, CAMPO_FK_CATEGORIA,
            CAMPO_NOME_CATEGORIA,
            CAMPO_COR_CATEGORIA)
    }
}