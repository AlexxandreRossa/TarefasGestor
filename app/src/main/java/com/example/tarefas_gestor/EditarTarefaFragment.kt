package com.example.tarefas_gestor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import android.net.Uri
import com.example.tarefas_gestor.databinding.FragmentEditarTarefaBinding
import java.util.Calendar

private const val ID_LOADER_CATEGORIAS = 0

class EditarTarefaFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var tarefa: Tarefa?= null
    private var _binding: FragmentEditarTarefaBinding? = null
    private var dataVencimento : Calendar? = null

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

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            if (dataVencimento == null) dataVencimento = Calendar.getInstance()
            dataVencimento!!.set(year, month, dayOfMonth)
        }


        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_CATEGORIAS, null, this)


        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar


        val tarefa = EditarTarefaFragmentArgs.fromBundle(requireArguments()).tarefa

        if (tarefa != null) {
            activity.atualizaNome(R.string.editar_tarefa_label)

            binding.textviewNome.setText(tarefa.nome)
            if (tarefa.data_vencimento != null) {
                dataVencimento = tarefa.data_vencimento
                binding.calendarView.date = dataVencimento!!.timeInMillis
            }
        } else {
            activity.atualizaNome(R.string.nova_tarefa_label)

        this.tarefa = tarefa
    }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun voltaListaTarefas() {
        findNavController().navigate(R.id.action_EditarTarefaFragment_to_ListaTarefasFragment)
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

    private fun guardar() {

        val nome = binding.textviewNome.text.toString()
        if (nome.isBlank()) {
            binding.textviewNome.error = getString(R.string.nome_obrigatorio)
            binding.textviewNome.requestFocus()
            return
        }

        val categoriaId = binding.spinnerCategorias.selectedItemId

        val novadataVencimento = Calendar.getInstance()
        novadataVencimento.set(2023, 6, 10)

        if (tarefa == null) {
            val tarefa = Tarefa(
                nome,
                ("Fazer os TPC's"),
                novadataVencimento,
                Categoria("","",categoriaId)

            )


            insereTarefa(tarefa)

        } else {
            val tarefa = tarefa!!
            tarefa.nome = nome
            tarefa.descricao = ("Fazer os TPC's")
            tarefa.data_vencimento = novadataVencimento
            tarefa.categoria.id = categoriaId

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
            binding.textviewNome.error = getString(R.string.erro_guardar_tarefa)
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
            binding.textviewNome.error = getString(R.string.erro_guardar_tarefa)
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

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (_binding != null) {
            binding.spinnerCategorias.adapter = null
        }
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

        val idCategoria = tarefa!!.categoria.id

        val ultimaCategoria = binding.spinnerCategorias.count - 1
        for (i in 0..ultimaCategoria) {
            if (idCategoria == binding.spinnerCategorias.getItemIdAtPosition(i)) {
                binding.spinnerCategorias.setSelection(i)
                return
            }
        }
    }

}
