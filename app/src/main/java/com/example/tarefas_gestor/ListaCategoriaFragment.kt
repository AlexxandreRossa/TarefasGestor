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
import com.example.tarefas_gestor.databinding.FragmentListaCategoriaBinding

private const val ID_LOADER_CATEGORIA = 0

class ListaCategoriaFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding:FragmentListaCategoriaBinding? = null

    private val binding get() = _binding!!

    var categoriaSelecionado : Categoria? = null

        set(value){
            field = value
            val mostarEliminarAlterar = (value != null)
            val activity = activity as MainActivity
            activity.mostraOpcaoMenu(R.id.editar, mostarEliminarAlterar)
            activity.mostraOpcaoMenu(R.id.eliminar, mostarEliminarAlterar)
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaCategoriaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private var adapterCategoria: AdapterCategoria? =null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCategoria = AdapterCategoria(this)
        binding.RecyclerViewCategoria.adapter = adapterCategoria
        binding.RecyclerViewCategoria.layoutManager = LinearLayoutManager(requireContext())


        val loader=LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_CATEGORIA, null, this)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_lista_categoria
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            TarefasContentProvider.ENDERECO_CATEGORIAS,
            TabelaCategorias.CAMPOS,
            null,null,
            TabelaCategorias.CAMPO_NOME

        )
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterCategoria!!.cursor=null
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterCategoria!!.cursor=data
    }

    fun processaOptionMenu(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.adicionar -> {
                adicionarCategoria()
                true
            }
            R.id.editar -> {
                editarCategoria()
                true
            }
            R.id.eliminar -> {
                eliminarCategoria()
                true
            }
            else -> false
        }
    }

    private fun eliminarCategoria() {
        val acao = ListaCategoriaFragmentDirections.actionListaMarcasFragmentToFragmentEliminarMarca(categoriaSelecionado!!)
        findNavController().navigate(acao)
    }

    private fun editarCategoria() {
        val acao = ListaCategoriaFragmentDirections.actionListaMarcasFragmentToEditarMarcaFragment(categoriaSelecionado!!)
        findNavController().navigate(acao)
    }

    private fun adicionarCategoria() {
        val acao = ListaCategoriaFragmentDirections.actionListaMarcasFragmentToEditarMarcaFragment(null)
        findNavController().navigate(acao)
    }

}