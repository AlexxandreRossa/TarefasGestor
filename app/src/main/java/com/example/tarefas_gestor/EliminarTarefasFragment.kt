package com.example.tarefas_gestor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
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
            binding.textViewDataVencimento.text = DateFormat.format("yyyy-MM-dd", tarefa.data_vencimento)
        }
        binding.textViewCategoria.text = tarefa.id_categoria
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminar -> {
                eliminar()
                true
            }
            R.id.cancelar -> {
                voltaListaLivros()
                true
            }
            else -> false
        }
    }

    private fun voltaListaLivros() {
        findNavController().navigate(R.id.action_eliminarLivroFragment_to_ListaLivrosFragment)
    }

    private fun eliminar() {
    }
}