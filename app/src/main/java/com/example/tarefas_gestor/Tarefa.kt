package com.example.tarefas_gestor

import android.content.ContentValues
data class Tarefa(
    var nome: String,
    var descricao: Int,
    var data_vencimento: String,
    var id_categoria: Int,
    var id: Long = -1
) {
    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaTarefas.CAMPO_NOME, nome)
        valores.put(TabelaTarefas.CAMPO_DESCRICAO, descricao)
        valores.put(TabelaTarefas.CAMPO_DATA_VENCIMENTO, data_vencimento)
        valores.put(TabelaTarefas.CAMPO_FK_CATEGORIA, id_categoria)

        return valores
    }
}