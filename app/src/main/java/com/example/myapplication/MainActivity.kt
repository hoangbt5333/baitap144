package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etId: EditText
    private lateinit var btnAdd: Button
    private lateinit var rvStudents: RecyclerView

    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etId = findViewById(R.id.etId)
        btnAdd = findViewById(R.id.btnAdd)
        rvStudents = findViewById(R.id.rvStudents)
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(studentList) { position ->
            studentList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = adapter
    }

    private fun setupClickListeners() {
        btnAdd.setOnClickListener {
            addStudent()
        }
    }

    private fun addStudent() {
        val name = etName.text.toString().trim()
        val id = etId.text.toString().trim()

        when {
            name.isEmpty() -> showToast("Vui lòng nhập họ tên")
            id.isEmpty() -> showToast("Vui lòng nhập mã số")
            else -> {
                studentList.add(Student(name, id))
                adapter.notifyItemInserted(studentList.size - 1)
                clearInputFields()
            }
        }
    }

    private fun clearInputFields() {
        etName.text.clear()
        etId.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

// StudentAdapter.kt
class StudentAdapter(
    private val students: List<Student>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(student: Student, position: Int) {
            tvName.text = student.name
            tvId.text = student.id
            btnDelete.setOnClickListener { onDeleteClick(position) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position], position)
    }

    override fun getItemCount() = students.size
}

// Student.kt
data class Student(
    val name: String,
    val id: String
)