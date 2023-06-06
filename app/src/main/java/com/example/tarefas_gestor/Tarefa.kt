package com.example.tarefas_gestor

data class Tarefa(
    var nome: String,
    var descricao: Int,
    var data_vencimento: String,
    var id_categoria: Int,
    var id: Long = -1
) {
}