package com.example.tarefas_gestor

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.database.Cursor
import android.widget.CalendarView

class AdapterTarefas(val fragment: ListaTarefasFragment) : RecyclerView.Adapter<AdapterTarefas.ViewHolderTarefa>() {

    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolderTarefa(contentor: View) : ViewHolder(contentor) {
        private val textViewNome = contentor.findViewById<TextView>(R.id.textViewNome)
        private val textViewCategoria = contentor.findViewById<TextView>(R.id.textViewCategoria)

        init {
            contentor.setOnClickListener {
                viewHolderSeleccionado?.desSeleciona()
                seleciona()
            }
        }



        internal var tarefa: Tarefa? = null
            set(value) {
                field = value
                textViewNome.text = tarefa?.nome ?: ""
                textViewCategoria.text = tarefa?.categoria.toString()
            }


        fun seleciona() {
            viewHolderSeleccionado = this
            fragment.tarefaSelecionada = tarefa
            itemView.setBackgroundResource(R.color.item_selecionado)
        }

        fun desSeleciona() {
            itemView.setBackgroundResource(android.R.color.white)
        }

        private var viewHolderSeleccionado : ViewHolderTarefa? = null

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTarefa {
        return ViewHolderTarefa(
            fragment.layoutInflater.inflate(R.layout.item_tarefa, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }


    override fun onBindViewHolder(holder: ViewHolderTarefa, position: Int) {
        cursor!!.moveToPosition(position)
        holder.tarefa = Tarefa.fromCursor(cursor!!)
    }
}