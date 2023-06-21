package com.example.tarefas_gestor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.text.format.DateFormat
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import android.net.Uri
import java.util.Calendar
import java.util.Date

private const val ID_LOADER_CATEGORIAS = 0

class EditarTarefaFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var tarefa: Tarefa?= null
    private var _binding: FragmentEditarTarefaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditarTarefaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_CATEGORIAS, null, this)


        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar


        val tarefa = EditarTarefaFragmentArgs.fromBundle(requireArguments()).livro

        if (tarefa != null) {
            binding.editTextTitulo.setText(tarefa.nome)
            binding.editTextIsbn.setText(tarefa.descricao)
            if (tarefa.dataVencimento != null) {
                binding.editTextDataPub.setText(
                    DateFormat.format("yyyy-MM-dd", tarefa.dataVencimento)
                )
            }
        }

        this.tarefa = tarefa
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.guardar -> {
                guardar()
                true
            }
            R.id.cancelar -> {
                voltaListaTarefas()
                true
            }
            else -> false
        }
    }

    private fun voltaListaTarefas() {
        findNavController().navigate(R.id.action_NovaTarefaFragment_to_ListaTarefasFragment)
    }

    private fun guardar() {

        val nome = binding.textview.text.toString()
        if (nome.isBlank()) {
            binding.textview.error = getString(R.string.nome_obrigatorio)
            binding.textview.requestFocus()
            return
        }

        val categoriaId = binding.spinnerCategorias.selectedItemId

        val data: Date?
        val df = SimpleDateFormat("dd-MM-yyyy")
        try {
            data = df.parse(binding.editTextDataVenc.text.toString())
        } catch (e: Exception) {
            binding.editTextDataVenc.error = getString(R.string.data_invalida)
            binding.editTextDataVenc.requestFocus()
            return
        }

        val calendario = Calendar.getInstance()
        calendario.time = data

        /*
         var nome: String,
    var descricao: String,
    var data_vencimento: Calendar? = null,
    var id_categoria: Long,
    var id: Long = -1
         */

        /* -223,7 +222,7 @@ class EditarLivroFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
            * them to you through new calls here.  You should not monitor the
            * data yourself.  For example, if the data is a [android.database.Cursor]
            * and you place it in a [android.widget.CursorAdapter], use
            * the [android.widget.CursorAdapter] constructor *without* passing
            * in either [android.widget.CursorAdapter.FLAG_AUTO_REQUERY]
            * or [android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER]
            * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
            * from doing its own observing of the Cursor, which is not needed since
            * when a change happens you will get a new Cursor throw another call
            * here.
            *  *  The Loader will release the data once it knows the application
            * is no longer using it.  For example, if the data is
            * a [android.database.Cursor] from a [android.content.CursorLoader],
            * you should not call close() on it yourself.  If the Cursor is being placed in a
            * [android.widget.CursorAdapter], you should use the
            * [android.widget.CursorAdapter.swapCursor]
            * method so that the old Cursor is not closed.
            *
            *
            *
            * This will always be called from the process's main thread.
            *
            * @param loader The Loader that has finished.
            * @param data The data generated by the Loader.
            */

        val novadataVencimento = Calendar.getInstance()
        novadataVencimento.set(2023, 6, 10)

        if (tarefa == null) {
            val livro = Tarefa(
                nome,
                ("Fazer os TPC's"),
                novadataVencimento,
                categoriaId)


            insereTarefa(tarefa)

        } else {
            val tarefa = tarefa!!
            tarefa.nome = nome
            tarefa.descricao = ("Fazer os TPC's")
            tarefa.data_vencimento = novadataVencimento
            tarefa.id_categoria = categoriaId

            alteraTarefa(tarefa)
        }
    }

    private fun alteraTarefa(tarefa: Tarefa) {
        val enderecoTarefa = Uri.withAppendedPath(TarefasContentProvider.ENDERECO_TAREFAS, tarefa.id.toString())
        val tarefasAlteradas = requireActivity().contentResolver.update(enderecoTarefa, tarefa.toContentValues(), null, null)

        if (tarefasAlteradas == 1) {
            Toast.makeText(requireContext(), R.string.tarefa_guardada_com_sucesso, Toast.LENGTH_LONG).show()
            voltaListaTarefas()
        } else {
            binding.editTextTitulo.error = getString(R.string.erro_guardar_tarefa)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (_binding != null) {
            binding.spinnerCategorias.adapter = null
        }
    }

    private fun insereTarefa(
        tarefa: Tarefa
    ) {

        val id = requireActivity().contentResolver.insert(
            TarefasContentProvider.ENDERECO_TAREFAS,
            tarefa.toContentValues()
        )

        if (id == null) {
            binding.textview.error = getString(R.string.erro_guardar_tarefa)
            return
        }

        Toast.makeText(
            requireContext(),
            getString(R.string.tarefa_guardada_com_sucesso),
            Toast.LENGTH_SHORT
        ).show()

        voltaListaTarefas()

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            TarefasContentProvider.ENDERECO_CATEGORIAS,
            TabelaCategorias.CAMPOS,
            null, null,
            TabelaCategorias.CAMPO_NOME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            binding.spinnerCategorias.adapter = null
            return
        }

        binding.spinnerCategorias.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaCategorias.CAMPO_NOME),
            intArrayOf(android.R.id.text1),
            0
        )

        mostraCategoriaSelecionadaSpinner()
    }

    private fun mostraCategoriaSelecionadaSpinner() {
        if (tarefa == null) return

        val idCategoria = tarefa!!.id_categoria

        val ultimaCategoria = binding.spinnerCategorias.count - 1
        for (i in 0..ultimaCategoria) {
            if (idCategoria == binding.spinnerCategorias.getItemIdAtPosition(i)) {
                binding.spinnerCategorias.setSelection(i)
                return
            }
        }
    }
}