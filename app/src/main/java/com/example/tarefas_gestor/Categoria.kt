package com.example.tarefas_gestor

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Categoria(
    var nome: String,
    var cor: String,
    var id: Long = -1
) {

    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaCategorias.CAMPO_NOME, nome)
        valores.put(TabelaCategorias.CAMPO_COR, cor)

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor) : Categoria {
            val posId = cursor.getColumnIndex(BaseColumns._ID)
            val posNome = cursor.getColumnIndex(TabelaCategorias.CAMPO_NOME)
            val posCor = cursor.getColumnIndex(TabelaCategorias.CAMPO_COR)

            val id = cursor.getLong(posId)
            val nome = cursor.getString(posNome)
            val cor = cursor.getString(posCor)

            return Categoria(nome,cor, id)
        }
    }
}