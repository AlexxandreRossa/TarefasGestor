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
import com.example.tarefas_gestor.databinding.FragmentEditarCategoriaBinding
class EditarCategoriaFragment : Fragment() {

    private var categoria: Categoria?= null
    private var _binding: FragmentEditarCategoriaBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditarCategoriaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar

        val categoria = EditarCategoriaFragmentArgs.fromBundle(requireArguments()).categoria

        if (categoria != null){
            activity.atualizaNome ("Editar Categoria")

            binding.editTextNomeCategoria.setText(categoria.nome)
        }else{
            activity.atualizaNome("Nova Categoria")
        }

        this.categoria = categoria
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun voltarlistaCategoria(){
        findNavController().navigate(R.id.action_EditarCategoriaFragment_to_ListaCategoriasFragment)
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.guardar -> {
                guardarCategoria()
                true
            }
            R.id.cancelar -> {
                voltarlistaCategoria()
                true
            }
            else -> false
        }
    }

    private fun guardarCategoria() {
        val nome = binding.editTextNomeCategoria.text.toString()
        val cor = binding.editTextCorCategoria.text.toString()
        if (nome.isBlank()) {
            binding.editTextNomeCategoria.error = "Campo Obrigatório"
            binding.editTextNomeCategoria.requestFocus()
            return
        }

        if (nome.isBlank()) {
            binding.editTextCorCategoria.error = "Campo Obrigatório"
            binding.editTextCorCategoria.requestFocus()
            return
        }


        if (categoria == null){
            val categoria = Categoria(nome,cor)

            insereCategoria(categoria)
        }else{
            val categoria = categoria!!
            categoria.nome = nome
            categoria.cor = cor

            alteraCategoria(categoria)
        }

    }

    private fun alteraCategoria(categoria: Categoria) {
        val enderecoCategoria = Uri.withAppendedPath(TarefasContentProvider.ENDERECO_CATEGORIAS, categoria.id.toString())
        val CategoriasAlteradas = requireActivity().contentResolver.update(enderecoCategoria, categoria.toContentValues(), null,null)

        if(CategoriasAlteradas == 1){
            Toast.makeText(requireContext(), "Categoria alterada com sucesso", Toast.LENGTH_LONG).show()
            voltarlistaCategoria()
        }else{
            binding.editTextNomeCategoria.error = "Impossivel Guardar categoria"
        }
    }

    private fun insereCategoria(categoria: Categoria) {
        val id = requireActivity().contentResolver.insert(
            TarefasContentProvider.ENDERECO_CATEGORIAS,
            categoria.toContentValues()
        )

        if (id == null) {
            binding.editTextNomeCategoria.error =
                "Impossivel guardar categoria"
            return
        }


        Toast.makeText(
            requireContext(),
            "Categoria Salva",
            Toast.LENGTH_LONG
        ).show()
        voltarlistaCategoria()
    }

}