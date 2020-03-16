package com.azhara.academy.ui.reader.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.ui.reader.CourseReaderActivity
import com.azhara.academy.ui.reader.CourseReaderCallback
import com.azhara.academy.ui.reader.CourseReaderViewModel
import com.azhara.academy.viewmodel.ViewModelFactory
import com.azhara.academy.vo.Status
import kotlinx.android.synthetic.main.fragment_academy.progress_bar
import kotlinx.android.synthetic.main.fragment_modu_list.*

/**
 * A simple [Fragment] subclass.
 */
class ModuListFragment : Fragment(), MyAdapterClickListener {

    companion object{
        val TAG = ModuListFragment::class.java.simpleName

        fun newInstance(): ModuListFragment = ModuListFragment()
    }

    private lateinit var adapter: ModuleListAdapter
    private lateinit var courseReaderCallback: CourseReaderCallback
    private lateinit var viewModel: CourseReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modu_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[CourseReaderViewModel::class.java]
        adapter = ModuleListAdapter(this)


        viewModel.module.observe(viewLifecycleOwner, Observer { moduleEntities ->
            if (moduleEntities != null){
                when(moduleEntities.status){
                    Status.LOADING -> progress_bar.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        progress_bar.visibility = View.GONE
                        populateRecycleView(moduleEntities.data as List<ModuleEntity>)
                    }
                    Status.ERROR -> {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        courseReaderCallback = context as CourseReaderActivity
    }

    private fun populateRecycleView(modules: List<ModuleEntity>) {
        progress_bar.visibility = View.GONE
        adapter.setModules(modules)
        rv_module.layoutManager = LinearLayoutManager(activity)
        rv_module.setHasFixedSize(true)
        rv_module.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(rv_module.context, DividerItemDecoration.VERTICAL)
        rv_module.addItemDecoration(dividerItemDecoration)

    }

    override fun onItemClicked(position: Int, moduleId: String) {
        courseReaderCallback.moveTo(position, moduleId)
        viewModel.setSelectedModule(moduleId)
    }

}
