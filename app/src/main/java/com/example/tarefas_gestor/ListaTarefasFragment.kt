package com.example.tarefas_gestor

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarefas_gestor.databinding.FragmentListaTarefasBinding


private const val ID_LOADER_TAREFAS = 0

class ListaTarefasFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentListaTarefasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var tarefaSelecionada : Tarefa? = null
        set(value) {
            field = value

            val mostrarEliminarAlterar = (value != null)

            val activity = activity as MainActivity
            activity.mostraOpcaoMenu(R.id.editar, mostrarEliminarAlterar)
            activity.mostraOpcaoMenu(R.id.eliminar, mostrarEliminarAlterar)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListaTarefasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var adapterTarefas: AdapterTarefas? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterTarefas = AdapterTarefas(this)
        binding.recyclerviewtarefas.adapter = adapterTarefas
        binding.recyclerviewtarefas.layoutManager = LinearLayoutManager(requireContext())


        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_TAREFAS, null, this)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_lista_tarefas
    }


        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                requireContext(),
                TarefasContentProvider.ENDERECO_TAREFAS,
                TabelaTarefas.CAMPOS,
                null, null,
                TabelaTarefas.CAMPO_NOME
            )
        }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (adapterTarefas != null) {
            adapterTarefas!!.cursor = null
        }
    }


        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            adapterTarefas!!.cursor = data
        }




    fun processaOpcaoMenu(item: MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.adicionar -> {
                adicionaTarefa()
                true
            }
            R.id.editar -> {
                editarTarefa()
                true
            }
            R.id.eliminar -> {
                eliminarTarefa()
                true
            }
            else -> false
        }
    }

    private fun eliminarTarefa() {

        val acao = ListaTarefasFragmentDirections.actionListaTarefasFragmentToEliminarTarefaFragment(tarefaSelecionada!!)
        findNavController().navigate(acao)

    }

    private fun editarTarefa() {
        val acao = ListaTarefasFragmentDirections.actionListaTarefasFragmentToEditarTarefaFragment(tarefaSelecionada!!)
        findNavController().navigate(acao)

    }

    private fun adicionaTarefa() {
        val acao = ListaTarefasFragmentDirections.actionListaTarefasFragmentToEditarTarefaFragment(null)
        findNavController().navigate(acao)

    }

}
