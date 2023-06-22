package com.example.tarefas_gestor

import android.os.Bundle
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.fragment.findNavController
import com.example.tarefas_gestor.databinding.FragmentEliminarTarefaBinding

class EliminarTarefaFragment : Fragment() {
    private lateinit var tarefa: Tarefa
    private var _binding: FragmentEliminarTarefaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEliminarTarefaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_eliminar


        tarefa = EliminarTarefaFragmentArgs.fromBundle(requireArguments()).tarefa

        binding.textViewName.text = tarefa.nome
        binding.textViewDescricao.text = tarefa.descricao
        if (tarefa.data_vencimento != null) {
            binding.textViewDataVencimento.text =
                DateFormat.format("yyyy-MM-dd", tarefa.data_vencimento)
        }
        binding.textViewCategoria.text = tarefa.categoria.nome

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

        private fun voltaListaTarefas() {
            findNavController().navigate(R.id.action_eliminarTarefaFragment_to_ListaTarefasFragment)
        }

        fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminar -> {
                eliminar()
                true
            }
            R.id.cancelar -> {
                voltaListaTarefas()
                true
            }
            else -> false
        }
    }


    private fun eliminar() {

        val enderecoTarefa = Uri.withAppendedPath(TarefasContentProvider.ENDERECO_TAREFAS, tarefa.id.toString())
        val numTarefasEliminadas = requireActivity().contentResolver.delete(enderecoTarefa, null, null)

        if (numTarefasEliminadas == 1) {
            Toast.makeText(requireContext(), getString(R.string.tarefa_eliminada_com_sucesso), Toast.LENGTH_LONG).show()
            voltaListaTarefas()
        } else {
            Snackbar.make(binding.textViewName, getString(R.string.erro_eliminar_tarefa), Snackbar.LENGTH_INDEFINITE)
        }
    }
}

