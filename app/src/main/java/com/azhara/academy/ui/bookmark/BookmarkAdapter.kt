package com.azhara.academy.ui.bookmark

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
import kotlinx.android.synthetic.main.items_bookmark.view.*

class BookmarkAdapter(private val callBack: BookmarkFragmentCallBack): RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    private val listCourses = ArrayList<CourseEntity>()

    fun setCourses(courses: List<CourseEntity>?) {
        if (courses == null) return
        this.listCourses.clear()
        this.listCourses.addAll(courses)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun getItemCount(): Int = listCourses.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(listCourses[position])
    }

    inner class BookmarkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(course: CourseEntity){
            with(itemView){
                tv_item_title.text = course.title
                tv_item_description.text = course.description
                tv_item_date.text = resources.getString(R.string.deadline_date, course.deadline)
                setOnClickListener {
                    val intent = Intent(context, DetailCourseActivity::class.java).apply {
                        putExtra(DetailCourseActivity.EXTRA_COURSE, course.courseId)
                    }
                    itemView.context.startActivity(intent)
                    img_share.setOnClickListener {
                        callBack.onShareClick(course)
                    }
                    Glide.with(itemView.context)
                        .load(course.imagePath)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh_black_24dp).error(R.drawable.ic_error))
                        .into(img_poster)
                }
            }
        }
    }
}