package com.azhara.academy.ui.academy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azhara.academy.R
import com.azhara.academy.data.source.local.entity.CourseEntity
import com.azhara.academy.ui.detail.DetailCourseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.items_academy.view.*

class AcademyAdapter: RecyclerView.Adapter<AcademyAdapter.AcademyViewHolder>() {

    private var listCourses = ArrayList<CourseEntity>()

    fun setCourses(courses: List<CourseEntity>?) {
        if (courses == null) return
        listCourses.clear()
        listCourses.addAll(courses)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AcademyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_academy, parent, false)
        return AcademyViewHolder(view)
    }

    override fun getItemCount(): Int = listCourses.size

    override fun onBindViewHolder(holder: AcademyViewHolder, position: Int) {
        holder.bind(listCourses[position])
    }

    inner class AcademyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(course: CourseEntity){
            with(itemView){
                tv_item_title.text = course.title
                tv_item_description.text = course.description
                tv_item_date.text = resources.getString(R.string.deadline_date, course.deadline)
                setOnClickListener {
                    val intent = Intent(context, DetailCourseActivity::class.java).apply {
                        putExtra(DetailCourseActivity.EXTRA_COURSE, course.courseId)
                    }
                    context.startActivity(intent)
                }
                Glide.with(context)
                    .load(course.imagePath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_black_24dp).error(R.drawable.ic_error))
                    .into(img_poster)
            }
        }
    }
}