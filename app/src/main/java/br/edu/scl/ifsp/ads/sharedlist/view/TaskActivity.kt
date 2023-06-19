package br.edu.scl.ifsp.ads.sharedlist.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.edu.scl.ifsp.ads.sharedlist.R
import br.edu.scl.ifsp.ads.sharedlist.controller.TaskController
import br.edu.scl.ifsp.ads.sharedlist.databinding.ActivityTaskBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.util.*


import kotlin.random.Random

class TaskActivity : BaseActivity() {

    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        supportActionBar?.subtitle = getString(R.string.task_info)

        val receivedTask = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        }else{
            intent.getParcelableExtra(EXTRA_TASK)
        }
        receivedTask?.let{_receivedTask ->
            with(atb){
                with(_receivedTask){
                    titleEt.setText(title)
                    descriptionEt.setText(description)
                    creationUserTv.text = creationUser
                    creationDateTv.text = creationDate
                    dueDateTv.text = dueDate
                    checkboxCompletionCb.isChecked = isCompleted
                    completionUserTv.text = completionUser

                }
            }

        val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
        with(atb){
            titleEt.isEnabled = false
            descriptionEt.isEnabled = !viewTask
            creationUserTv.isEnabled = false
            creationUserTv.visibility = View.VISIBLE
            createdByTv.visibility = View.VISIBLE
            createdByTv.isEnabled = false
            creationDateTv.isEnabled = false
            createdOnTv.isEnabled = false
            creationDateTv.visibility = View.VISIBLE
            createdOnTv.visibility = View.VISIBLE
            completionUserTv.isEnabled = false
            completedByTv.isEnabled = false
            dueDateTv.visibility = View.VISIBLE
            dueDateBt.isEnabled = !viewTask
            dueDateBt.visibility = View.VISIBLE
            checkboxCompletionCb.isEnabled = !viewTask
            checkboxCompletionCb.visibility = View.VISIBLE
            completionUserTv.visibility = if(viewTask && checkboxCompletionCb.isChecked) View.VISIBLE else View.GONE
            completedByTv.visibility = if(viewTask && checkboxCompletionCb.isChecked) View.VISIBLE else View.GONE
            saveBt.visibility = if(viewTask) View.GONE else View.VISIBLE

            }

        }

        val calendarCreationDate: Calendar = Calendar.getInstance()
        atb.creationDateTv.text = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(calendarCreationDate.time)

        val calendarDueDate: Calendar = Calendar.getInstance()
        val year = calendarDueDate.get(Calendar.YEAR)
        val month = calendarDueDate.get(Calendar.MONTH)

        val day = calendarDueDate.get(Calendar.DAY_OF_MONTH)
        atb.dueDateBt.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                atb.dueDateTv.text = "" + mDay + "/" + (mMonth+1) + "/" + mYear
            }, year, month, day)
            datePicker.show()
        }

        atb.saveBt.setOnClickListener{

            if(atb.checkboxCompletionCb.isChecked) {
                atb.completionUserTv.text = FirebaseAuth.getInstance().currentUser?.email.toString()
            }else{
                atb.completionUserTv.visibility = View.GONE
            }

            atb.creationUserTv.text = FirebaseAuth.getInstance().currentUser?.email.toString()

            val task: Task = Task(
                id = receivedTask?.id,
                title = atb.titleEt.text.toString(),
                description = atb.descriptionEt.text.toString(),
                creationUser = atb.creationUserTv.text.toString(),
                creationDate = atb.creationDateTv.text.toString(),
                dueDate = atb.dueDateTv.text.toString(),
                isCompleted = atb.checkboxCompletionCb.isChecked,
                completionUser = atb.completionUserTv.text.toString(),

            )

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_TASK, task)
            setResult(RESULT_OK, resultIntent)
            finish()



        }
    }

    private fun generateId(): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt()
    }
}