package com.example.tarefas_gestor

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tarefas_gestor.EliminarTarefaFragmentArgs.Companion.fromBundle
import com.example.tarefas_gestor.databinding.FragmentEliminarCategoriaBinding
import com.example.tarefas_gestor.EditarTarefaFragmentArgs.Companion.fromBundle
import com.example.tarefas_gestor.EditarCategoriaFragmentArgs.Companion.fromBundle
import com.google.android.material.snackbar.Snackbar
class EliminarCategoriaFragment : Fragment() {


    private lateinit var categoria: Categoria
    private var _binding: FragmentEliminarCategoriaBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEliminarCategoriaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_eliminar

        categoria = EliminarCategoriaFragmentArgs.fromBundle(requireArguments()).marcas

        binding.textViewNomeEliminarCategoria.text = categoria.nome
        binding.textViewCorEliminarCategoria.text = categoria.cor

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eliminar -> {
                eliminarCategoria()
                true
            }

            R.id.cancelar -> {
                voltaListaCategoria()
                true
            }

            else -> false
        }
    }

    private fun voltaListaCategoria() {
        findNavController().navigate(R.id.action_fragmentEliminarCategoria_to_listaCategoriasFragment)
    }

    private fun eliminarCategoria() {
        val enderecoMarcas = Uri.withAppendedPath(
            TarefasContentProvider.ENDERECO_CATEGORIAS,
            categoria.id.toString()
        )
        val numMarcasSelecionadas =
            requireActivity().contentResolver.delete(enderecoMarcas, null, null)

        if (numMarcasSelecionadas == 1) {
            Toast.makeText(
                requireContext(),
                "Marca eliminada com sucesso",
                Toast.LENGTH_LONG
            ).show()
            voltaListaCategoria()
        } else {
            Snackbar.make(
                binding.textViewNomeEliminarCategoria,
                "Erro ao eliminar categoria",
                Snackbar.LENGTH_INDEFINITE
            )

        }
    }

}