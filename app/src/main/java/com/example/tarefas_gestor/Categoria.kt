package com.example.tarefas_gestor

import android.content.ContentValues

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
}