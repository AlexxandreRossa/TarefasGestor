package com.example.tarefas_gestor

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class TarefasContentProvider : ContentProvider() {
    private var bdOpenHelper : BdTarefasOpenHelper? = null

    override fun onCreate(): Boolean {
        bdOpenHelper = BdTarefasOpenHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val bd = bdOpenHelper!!.readableDatabase

        val endereco = uriMatcher().match(uri)
        val tabela = when (endereco) {
            URI_CATEGORIAS, URI_CATEGORIA_ID -> TabelaCategorias(bd)
            URI_TAREFAS, URI_TAREFA_ID -> TabelaTarefas(bd)
            else -> null
        }

        val id = uri.lastPathSegment

        val (selecao, argsSel) = when (endereco) {
            URI_CATEGORIA_ID, URI_TAREFA_ID -> Pair("${BaseColumns._ID}=?", arrayOf(id))
            else -> Pair(selection, selectionArgs)
        }

        return tabela?.consulta(
            projection as Array<String>,
            selecao,
            argsSel as Array<String>?,
            null,
            null,
            sortOrder)
    }


    override fun getType(uri: Uri): String? {
        val endereco = uriMatcher().match(uri)

        return when(endereco) {
            URI_TAREFAS -> "vnd.android.cursor.dir/$TAREFAS"
            URI_TAREFA_ID -> "vnd.android.cursor.item/$TAREFAS"
            URI_CATEGORIAS -> "vnd.android.cursor.dir/$CATEGORIAS"
            URI_CATEGORIA_ID -> "vnd.android.cursor.item/$CATEGORIAS"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(uri)
        val tabela = when (endereco) {
            URI_CATEGORIAS -> TabelaCategorias(bd)
            URI_TAREFAS -> TabelaTarefas(bd)
            else -> return null
        }

        val id = tabela.insere(values!!)
        if (id == -1L) {
            return null
        }

        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(uri)
        val tabela = when (endereco) {
            URI_CATEGORIA_ID -> TabelaCategorias(bd)
            URI_TAREFA_ID -> TabelaTarefas(bd)
            else -> return 0
        }

        val id = uri.lastPathSegment!!
        return tabela.elimina("${BaseColumns._ID}=?", arrayOf(id))
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(uri)
        val tabela = when (endereco) {
            URI_CATEGORIA_ID -> TabelaCategorias(bd)
            URI_TAREFA_ID -> TabelaTarefas(bd)
            else -> return 0
        }

        val id = uri.lastPathSegment!!
        return tabela.altera(values!!, "${BaseColumns._ID}=?", arrayOf(id))
    }

    companion object {
        private const val AUTORIDADE = "com.example.tarefas_gestor"

        private const val CATEGORIAS = "categorias"
        private const val TAREFAS = "tarefas"

        private const val URI_CATEGORIAS = 100
        private const val URI_CATEGORIA_ID = 101
        private const val URI_TAREFAS = 200
        private const val URI_TAREFA_ID = 201

        private val ENDERECO_BASE = Uri.parse("content://$AUTORIDADE")

        val ENDERECO_CATEGORIAS = Uri.withAppendedPath(ENDERECO_BASE, CATEGORIAS)
        val ENDERECO_TAREFAS = Uri.withAppendedPath(ENDERECO_BASE, TAREFAS)


        fun uriMatcher() = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTORIDADE, CATEGORIAS, URI_CATEGORIAS)
            addURI(AUTORIDADE, "$CATEGORIAS/#", URI_CATEGORIA_ID)
            addURI(AUTORIDADE, TAREFAS, URI_TAREFAS)
            addURI(AUTORIDADE, "$TAREFAS/#", URI_TAREFA_ID)
        }
    }
}