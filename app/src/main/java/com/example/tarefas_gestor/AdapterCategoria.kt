package com.example.tarefas_gestor

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterCategoria(val fragment: ListaCategoriaFragment) : RecyclerView.Adapter<AdapterCategoria.ViewHolderCategoria>() {

    var cursor: Cursor? = null
        set(value){
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolderCategoria(contentor: View) : RecyclerView.ViewHolder(contentor) {
        private val textViewMarca = contentor.findViewById<TextView>(R.id.textViewCategoriaCategoria)

        init {
            contentor.setOnClickListener{
                viewHolderSeleccionado?.desSeleciona()
                seleciona()
            }
        }
        internal var categoria:Categoria?=null
            set(value){
                field = value
                textViewMarca.text=categoria?.nome ?: ""
            }

        fun seleciona(){
            viewHolderSeleccionado = this
            fragment.categoriaSelecionado = categoria
            itemView.setBackgroundResource(R.color.item_selecionado)
        }

        fun desSeleciona(){
            itemView.setBackgroundResource(android.R.color.white)
        }
    }

    private var viewHolderSeleccionado : ViewHolderCategoria?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCategoria.ViewHolderCategoria {
        return ViewHolderCategoria(
            fragment.layoutInflater.inflate(R.layout.item_categoria,parent,false)
        )

    }
    override fun getItemCount(): Int {
        return cursor?.count ?:0
    }

    override fun onBindViewHolder(holder: ViewHolderCategoria, position: Int) {
        cursor!!.moveToPosition(position)
        holder.categoria=Categoria.fromCursor(cursor!!)
    }

}


