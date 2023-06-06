package com.example.tarefas_gestor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BdInstrumentedTest {

    private fun getAppContext(): Context =
        InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun apagaBaseDados() {
        getAppContext().deleteDatabase(BdTarefasOpenHelper.NOME_BASE_DADOS)
    }


    @Test
    fun consegueAbrirBaseDados() {
        val openHelper = BdTarefasOpenHelper(getAppContext())
        val bd = openHelper.readableDatabase
        assert(bd.isOpen)
    }

    private fun getWritableDatabase(): SQLiteDatabase {
        val openHelper = BdTarefasOpenHelper(getAppContext())
        return openHelper.writableDatabase
    }

    @Test
    fun consegueInserirCategorias() {
        val bd = getWritableDatabase()

        val categoria = Categoria("Doméstica","Verde")
        insereCategoria(bd, categoria)
    }

    private fun insereCategoria(
        bd: SQLiteDatabase,
        categoria: Categoria
    ) {
        categoria.id = TabelaCategorias(bd).insere(categoria.toContentValues())
        assertNotEquals(-1, categoria.id)
    }

    @Test
    fun consegueInserirTarefas() {
        val bd = getWritableDatabase()

        val categoria = Categoria("Doméstica","Amarelo")
        insereCategoria(bd, categoria)

        val tarefa1 = Tarefa("Levar o Lixo","Ir meter os sacos no lixo","20/06/2023",categoria.id)
        insereTarefa(bd, tarefa1)

        val tarefa2 = Tarefa("Varrer o chão","Pegar na vasoura e varrer o chão","10/06/2023",categoria.id)
        insereTarefa(bd, tarefa2)
    }

    private fun insereTarefa(bd: SQLiteDatabase, tarefa: Tarefa) {
        tarefa.id = TabelaTarefas(bd).insere(tarefa.toContentValues())
        assertNotEquals(-1, tarefa.id)
    }
}