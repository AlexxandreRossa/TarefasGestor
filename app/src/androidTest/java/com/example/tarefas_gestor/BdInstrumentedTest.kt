package com.example.tarefas_gestor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.Calendar
import java.util.Date

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

        val dataVencimento1 = Calendar.getInstance()
        dataVencimento1.set(2023, 6, 5)

        val dataVencimento2 = Calendar.getInstance()
        dataVencimento2.set(2023, 6, 6)

        val tarefa1 = Tarefa("Levar o Lixo","Ir meter os sacos no lixo",dataVencimento1,categoria.id)
        insereTarefa(bd, tarefa1)

        val tarefa2 = Tarefa("Varrer o chão","Pegar na vasoura e varrer o chão",dataVencimento2,categoria.id)
        insereTarefa(bd, tarefa2)
    }

    private fun insereTarefa(bd: SQLiteDatabase, tarefa: Tarefa) {
        tarefa.id = TabelaTarefas(bd).insere(tarefa.toContentValues())
        assertNotEquals(-1, tarefa.id)
    }

    @Test
    fun consegueLerCategorias() {
        val bd = getWritableDatabase()

        val categEscola = Categoria("Escola","Vermelho")
        insereCategoria(bd, categEscola)

        val categHobby = Categoria("Hoobie","Verde")
        insereCategoria(bd, categHobby)

        val tabelaCategorias = TabelaCategorias(bd)

        val cursor = tabelaCategorias.consulta(
            TabelaCategorias.CAMPOS,
            "${BaseColumns._ID}=?",
            arrayOf(categHobby.id.toString()),
            null,
            null,
            null
        )

        assert(cursor.moveToNext())

        val categBD = Categoria.fromCursor(cursor)

        assertEquals(categHobby, categBD)

        val cursorTodasCategorias = tabelaCategorias.consulta(
            TabelaCategorias.CAMPOS,
            null, null, null, null,
            TabelaCategorias.CAMPO_NOME

        )

        assert(cursorTodasCategorias.count > 1)
    }

    @Test
    fun consegueLerTarefas() {
        val bd = getWritableDatabase()

        val categoria = Categoria("Part-time","Vermelho")
        insereCategoria(bd, categoria)

        val dataVencimento1 = Calendar.getInstance()
        dataVencimento1.set(2023, 6, 9)

        val dataVencimento2 = Calendar.getInstance()
        dataVencimento2.set(2023, 6, 10)

        val tarefa1 = Tarefa("Ir Trabalhar","Começar a trabalhar às 09h de Sábado",dataVencimento1, categoria.id)
        insereTarefa(bd, tarefa1)

        val tarefa2 = Tarefa("Ir Trabalhar","Começar a trabalhar às 10h de Domingo",dataVencimento2, categoria.id)
        insereTarefa(bd, tarefa2)

        val tabelaTarefas = TabelaTarefas(bd)

        val cursor = tabelaTarefas.consulta(
            TabelaTarefas.CAMPOS,
            "${BaseColumns._ID}=?",
            arrayOf(tarefa1.id.toString()),
            null,
            null,
            null
        )

        assert(cursor.moveToNext())

        val tarefaBD = Tarefa.fromCursor(cursor)

        assertEquals(tarefa1, tarefaBD)

        val cursorTodosLivros = tabelaTarefas.consulta(
            TabelaTarefas.CAMPOS,
            null, null, null, null,
            TabelaTarefas.CAMPO_NOME
        )

        assert(cursorTodosLivros.count > 1)
    }
}