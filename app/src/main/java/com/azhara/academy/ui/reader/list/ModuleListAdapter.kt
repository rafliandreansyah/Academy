package com.azhara.academy.ui.reader.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.ModuleEntity

class ModuleListAdapter internal constructor(private val listener: MyAdapterClickListener): RecyclerView.Adapter<ModuleListAdapter.ModuleListViewHolder>(){
    private val listModule = ArrayList<ModuleEntity>()

    internal fun setModules(modules: List<ModuleEntity>){
        if (modules == null) return
        listModule.clear()
        listModule.addAll(modules)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModuleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_module_list, parent, false)
        return ModuleListViewHolder(view)
    }

    override fun getItemCount(): Int = listModule.size

    override fun onBindViewHolder(holder: ModuleListViewHolder, position: Int) {
        holder.bind(listModule[position])
        if (holder.itemViewType == 0){
            holder.textModuleTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorTextSecondary))
        }else{
            holder.itemView.setOnClickListener {
                listener.onItemClicked(holder.adapterPosition, listModule[holder.adapterPosition].moduleId)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val modulePosition = listModule[position].position
        return when{
            modulePosition == 0 -> 1
            listModule[modulePosition - 1].read -> 1
            else -> 0
        }
    }

    inner class ModuleListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textModuleTitle = itemView.findViewById<TextView>(R.id.text_module_title)
        private val textTitle: TextView = itemView.findViewById(R.id.text_module_title)
        fun bind(modules: ModuleEntity){
            textTitle.text = modules.title
        }
    }
}

internal interface MyAdapterClickListener{
    fun onItemClicked(position: Int, moduleId: String)
}