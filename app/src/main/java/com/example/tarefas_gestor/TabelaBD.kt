package com.example.tarefas_gestor

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

abstract class TabelaBD(val db: SQLiteDatabase, val nome: String) {
    abstract fun cria()

    fun insere(valores: ContentValues) =
        db.insert(nome, null, valores)

    fun consulta(
        colunas: Array<String>,
        selecao: String?,
        argsSelecao: Array<String>?,
        groupby: String?,
        having: String?,
        orderby: String?
    ) : Cursor = db.query(nome, colunas, selecao, argsSelecao, groupby, having, orderby)

    fun altera(valores: ContentValues, where: String, argsWhere: Array<String>) =
        db.update(nome, valores, where, argsWhere)

    fun elimina(where: String, argsWhere: Array<String>) =
        db.delete(nome, where, argsWhere)
}