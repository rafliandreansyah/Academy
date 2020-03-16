package com.azhara.academy.ui.reader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azhara.academy.R
import com.azhara.academy.ui.reader.content.ModuleContentFragment
import com.azhara.academy.ui.reader.list.ModuListFragment
import com.azhara.academy.viewmodel.ViewModelFactory

class CourseReaderActivity : AppCompatActivity(), CourseReaderCallback {

    companion object{
        const val EXTRA_COURSE_ID = "course_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_reader)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[CourseReaderViewModel::class.java]

        val bundle = intent.extras
        if (bundle != null){
            val courseId = bundle.getString(EXTRA_COURSE_ID)
            if (courseId != null){
                viewModel.setSelectedCourse(courseId)
                populateFragment()
            }
        }
    }

    private fun populateFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(ModuListFragment.TAG)
        if (fragment == null){
            fragment = ModuListFragment.newInstance()
            fragmentTransaction.add(R.id.frame_container, fragment, ModuListFragment.TAG)
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    override fun moveTo(position: Int, moduleId: String) {
        val fragment = ModuleContentFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragment, ModuleContentFragment.TAG)
            .addToBackStack(null)
            .commit()
    }
}
