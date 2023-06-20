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
import com.example.tarefas_gestor.databinding.FragmentSobreBinding

private const val ID_LOADER_TAREFAS = 0


class ListaTarefasFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentListaTarefasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListaTarefasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterTarefas = AdapterTarefas()
        binding.recyclerviewtarefas.adapter = adapterTarefas
        binding.recyclerviewtarefas.layoutManager = LinearLayoutManager(requireContext())


        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_TAREFAS, null, this)
    }

    companion object {

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         *
         * This will always be called from the process's main thread.
         *
         * @param id The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                requireContext(),
                TarefasContentProvider.ENDERECO_TAREFAS,
                TabelaTarefas.CAMPOS,
                null, null,
                TabelaTarefas.CAMPO_NOME
            )
        }

        /**
         * Called when a previously created loader has finished its load.  Note
         * that normally an application is *not* allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See [ FragmentManager.openTransaction()][androidx.fragment.app.FragmentManager.beginTransaction] for further discussion on this.
         *
         *
         * This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         *
         *
         *  *
         *
         *The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a [android.database.Cursor]
         * and you place it in a [android.widget.CursorAdapter], use
         * the [android.widget.CursorAdapter.CursorAdapter] constructor *without* passing
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
        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            TODO("Not yet implemented")
        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         *
         * This will always be called from the process's main thread.
         *
         * @param loader The Loader that is being reset.
         */
        override fun onLoaderReset(loader: Loader<Cursor>) {
            TODO("Not yet implemented")
        }

    }
}