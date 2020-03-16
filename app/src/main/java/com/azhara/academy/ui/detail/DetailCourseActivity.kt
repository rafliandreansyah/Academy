package com.azhara.academy.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.ui.reader.CourseReaderActivity
import com.azhara.academy.viewmodel.ViewModelFactory
import com.azhara.academy.vo.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_course.*
import kotlinx.android.synthetic.main.content_detail_course.*

class DetailCourseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }
    internal lateinit var viewModel: DetailCourseViewModel
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_course)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = DetailCourseAdapter()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailCourseViewModel::class.java]

        val extras = intent.extras
        if (extras != null){
            val courseId = extras.getString(EXTRA_COURSE)
            if (courseId != null){
                viewModel.setSelectedCourse(courseId)

                viewModel.courseModule.observe(this, Observer { courseWithModuleResource ->
                    if (courseWithModuleResource != null){
                        when(courseWithModuleResource.status){
                            Status.LOADING -> progress_bar.visibility = View.VISIBLE
                            Status.SUCCESS -> {
                                progress_bar.visibility = View.GONE
                                adapter.setModule(courseWithModuleResource.data?.mModules)
                                adapter.notifyDataSetChanged()
                                courseWithModuleResource.data?.mCourse?.let { populateCourse(it) }
                            }
                            Status.ERROR -> {
                                progress_bar.visibility = View.GONE
                                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                })

            }
        }

        with(rv_module){
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@DetailCourseActivity)
            setHasFixedSize(true)
            this.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(rv_module.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }


    private fun populateCourse(course: CourseEntity) {
        text_title.text = course.title
        text_desc.text = course.description
        text_date.text = resources.getString(R.string.deadline_date, course.deadline)

        Glide.with(this)
            .load(course.imagePath)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_black_24dp).error(R.drawable.ic_error))
            .into(img_poster)

        btn_start.setOnClickListener {
            val intent = Intent(this, CourseReaderActivity::class.java).apply {
                putExtra(CourseReaderActivity.EXTRA_COURSE_ID, course.courseId)
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        viewModel.courseModule.observe(this, Observer { courseWithModule ->
            if (courseWithModule != null){
                when(courseWithModule.status){
                    Status.LOADING -> progress_bar.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        if (courseWithModule.data != null){
                            progress_bar.visibility = View.GONE
                            val state = courseWithModule.data.mCourse.bookmarked
                            setBookmarked(state)
                        }
                    }
                    Status.ERROR -> {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark){
            viewModel.setBookmark()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBookmarked(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state){
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_24dp)
        }else{
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_selector)
        }
    }
}
