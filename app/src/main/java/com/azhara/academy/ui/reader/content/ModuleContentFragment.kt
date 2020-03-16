package com.azhara.academy.ui.reader.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.ModuleEntity
import com.azhara.academy.ui.reader.CourseReaderViewModel
import com.azhara.academy.viewmodel.ViewModelFactory
import com.azhara.academy.vo.Status
import kotlinx.android.synthetic.main.fragment_module_content.*

/**
 * A simple [Fragment] subclass.
 */
class ModuleContentFragment : Fragment() {

    companion object{
        val TAG = ModuleContentFragment::class.java.simpleName

        fun newInstance(): ModuleContentFragment{
            return ModuleContentFragment()
        }
    }

    private lateinit var viewModel: CourseReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null){
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(requireActivity(), factory)[CourseReaderViewModel::class.java]

            viewModel.selectModule.observe(viewLifecycleOwner, Observer { moduleSelected ->
                if (moduleSelected != null){
                    when(moduleSelected.status){
                        Status.LOADING -> progress_bar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            progress_bar.visibility = View.GONE
                            if (moduleSelected.data?.contentEntity != null){
                                populateWebView(moduleSelected.data)

                            }
                            setButtonNextPrevState(moduleSelected.data)
                            if (!moduleSelected.data?.read!!){
                                viewModel.readContent(moduleSelected.data)
                            }
                        }
                        Status.ERROR -> {
                            progress_bar.visibility = View.GONE
                            Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    btn_next.setOnClickListener {
                        viewModel.setNextPage()
                    }
                    btn_prev.setOnClickListener {
                        viewModel.setPrevPage()
                    }
                }
            })

        }
    }

    private fun setButtonNextPrevState(module: ModuleEntity?) {
        if (activity != null){
            when(module?.position){
                0 ->{
                    btn_next.isEnabled = true
                    btn_prev.isEnabled = false
                }
                viewModel.getModuleSize() - 1 ->{
                    btn_prev.isEnabled = true
                    btn_next.isEnabled = false
                }
                else ->{
                    btn_next.isEnabled = true
                    btn_prev.isEnabled = true
                }
            }
        }
    }

    private fun populateWebView(content: ModuleEntity) {
        web_view.loadData(content.contentEntity?.content, "text/html", "UTF-8")
    }


}
