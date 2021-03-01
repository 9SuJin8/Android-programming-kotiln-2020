package com.example.room

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.example.room.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.coroutines.runBlocking
import java.lang.NumberFormatException


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDao: MyDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listView = findViewById<ListView>(R.id.listview)
        var arrayOfListView = ArrayList<String>()
        var arrayOfListView2 = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, arrayOfListView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.adapter = adapter

        myDao = MyDatabase.getDatabase(this).getMyDao()
        runBlocking {
            with(myDao) {
                insertStudent(Student(1, "james"))
                insertStudent(Student(2, "john"))

                insertClass(ClassInfo(1, "c-lang", "Mon 9:00", "E301", 1))
                insertClass(ClassInfo(2, "android prog", "Tue 9:00", "E302", 4))
                insertClass(ClassInfo(3,"java","Wed 9:00", "E303",5))
                insertClass(ClassInfo(4,"ios","Thu 9:00","E304",2))

                insertEnrollment(Enrollment(1, 1))
                insertEnrollment(Enrollment(1, 2))
            }
        }

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this, Observer {
            for((id,name) in it) {
                val str = StringBuilder().apply {
                        append(id)
                        append("-")
                        append(name)
                        append("\n")
                }.toString()
                arrayOfListView.add(str)
                arrayOfListView2 = arrayOfListView
                adapter.notifyDataSetChanged()
            }
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val p = position + 1
            runBlocking {
                val results = myDao.getStudentsWithEnrollment(p)
                if(results.isNotEmpty()){
                    val str = StringBuilder().apply {
                        append(results[0].student.id)
                        append("-")
                        append(results[0].student.name)
                        append(":")
                        for (c in results[0].enrollments){
                            append(c.cid)
                            val cls_result = myDao.getClassInfo(c.cid)
                            if(cls_result.isNotEmpty())
                                append("(${cls_result[0].name})")
                            append(",")
                        }
                    }
                    binding.textQueryStudent.text = str
                } else
                    binding.textQueryStudent.text = ""
            }
        }

        binding.queryStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            runBlocking {
                val results = myDao.getStudentsWithEnrollment(id)
                if (results.isNotEmpty()) {
                    val str = StringBuilder().apply {
                        append(results[0].student.id)
                        append("-")
                        append(results[0].student.name)
                        append(":")
                        for (c in results[0].enrollments) {
                            append(c.cid)
                            val cls_result = myDao.getClassInfo(c.cid)
                            if (cls_result.isNotEmpty())
                                append("(${cls_result[0].name})")
                            append(",")
                        }
                    }
                    binding.textQueryStudent.text = str
                } else {
                    binding.textQueryStudent.text = ""
                }
            }
        }

        binding.addStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            val name = binding.editStudentName.text.toString()
            if (id > 0 && name.isNotEmpty()) {
                runBlocking {
                    myDao.insertStudent(Student(id, name))
                    val s = "${id}${name}"
                    arrayOfListView2.add(s)
                    arrayOfListView.clear()
                    arrayOfListView = arrayOfListView2
                    adapter.notifyDataSetChanged()
                }
            }
        }

        binding.buttonEnroll.setOnClickListener {
            val index = listView.checkedItemPosition + 1
            val classid = binding.addEnrollment.text.toString().toInt()
            if(index > 0) {
                runBlocking {
                    try {
                        myDao.insertEnrollment(Enrollment(index, classid))
                        adapter.notifyDataSetChanged()
                    } catch (e: SQLiteConstraintException){
                        Toast.makeText(this@MainActivity,"wrong enrollment",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonRemove.setOnClickListener {
            val index = listView.checkedItemPosition + 1
            val position = listView.checkedItemPosition
            if(index > 0) {
                runBlocking {
                    val results = myDao.getStudentsWithEnrollment(index)
                    val enrollmentResult = myDao.getStudentsEnrollment(index)
                    var id: Int
                    var name: String
                    if(results.isNotEmpty() and enrollmentResult.isNotEmpty()){
                        id = results[0].student.id
                        name = results[0].student.name
                        var x = "${id}${name}"
                        val sid = enrollmentResult[0].sid
                        val cid = enrollmentResult[0].cid
                        myDao.deleteEnrollement(Enrollment(sid,cid,null))
                        myDao.deleteStudent(Student(id, name))
                        arrayOfListView2.remove(x)
                        arrayOfListView.clear()
                        arrayOfListView = arrayOfListView2
                        adapter.notifyDataSetChanged()
                    }
                    else if(results.isNotEmpty()){
                        id = results[0].student.id
                        name = results[0].student.name
                        var x = "${id}${name}"
                        myDao.deleteStudent(Student(id, name))
                        arrayOfListView2.remove(x)
                        arrayOfListView.clear()
                        adapter.notifyDataSetInvalidated()
                        arrayOfListView = arrayOfListView2
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }
}

