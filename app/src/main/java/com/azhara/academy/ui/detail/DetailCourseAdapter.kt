package com.azhara.academy.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.ModuleEntity
import kotlinx.android.synthetic.main.items_module_list.view.*

class DetailCourseAdapter: RecyclerView.Adapter<DetailCourseAdapter.DetailCourseViewHolder>() {

    private var listModules = ArrayList<ModuleEntity>()

    fun setModule(modules: List<ModuleEntity>?) {
        if (modules == null) return
        listModules.clear()
        listModules.addAll(modules)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailCourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_module_list, parent, false)
        return DetailCourseViewHolder(view)
    }

    override fun getItemCount(): Int = listModules.size

    override fun onBindViewHolder(
        holder: DetailCourseViewHolder,
        position: Int
    ) {
        holder.bind(listModules[position])
    }

    inner class DetailCourseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(module: ModuleEntity){
            with(itemView){
                text_module_title.text = module.title
            }
        }
    }
}