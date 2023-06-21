package com.example.tarefas_gestor

import android.content.ContentValues
import android.database.Cursor
import android.media.audiofx.AudioEffect.Descriptor
import android.provider.BaseColumns
import java.io.Serializable
import java.util.Calendar
data class Tarefa(
    var nome: String,
    var descricao: String,
    var data_vencimento: Calendar? = null,
    var id_categoria: Long,
    var id: Long = -1
) : Serializable {
    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaTarefas.CAMPO_NOME, nome)
        valores.put(TabelaTarefas.CAMPO_DESCRICAO, descricao)
        valores.put(TabelaTarefas.CAMPO_DATA_VENCIMENTO, data_vencimento?.timeInMillis)
        valores.put(TabelaTarefas.CAMPO_FK_CATEGORIA, id_categoria)

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor) : Tarefa {
            val posId = cursor.getColumnIndex(BaseColumns._ID)
            val posNome = cursor.getColumnIndex(TabelaTarefas.CAMPO_NOME)
            val posDescricao = cursor.getColumnIndex(TabelaTarefas.CAMPO_DESCRICAO)
            val posDataVencimento = cursor.getColumnIndex(TabelaTarefas.CAMPO_DATA_VENCIMENTO)
            val posCategoriaFK = cursor.getColumnIndex(TabelaTarefas.CAMPO_FK_CATEGORIA)

            val id = cursor.getLong(posId)
            val nome = cursor.getString(posNome)
            val descricao = cursor.getString(posDescricao)

            var dataVencimento: Calendar?

            if (cursor.isNull(posDataVencimento)) {
                dataVencimento = null
            } else {
                dataVencimento = Calendar.getInstance()
                dataVencimento.timeInMillis = cursor.getLong(posDataVencimento)
            }

            val categoriaId = cursor.getLong(posCategoriaFK)

            return Tarefa(nome,descricao,dataVencimento,categoriaId,id)
        }
    }
}