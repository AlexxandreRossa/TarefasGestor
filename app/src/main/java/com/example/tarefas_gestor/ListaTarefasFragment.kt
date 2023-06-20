package com.example.tarefas_gestor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarefas_gestor.databinding.FragmentListaTarefasBinding


class ListaTarefasFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_lista_tarefas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterTarefas = AdapterTarefas()
        binding.recyclerviewtarefas.adapter = adapterTarefas
        binding.recyclerviewtarefas.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {

    }
}