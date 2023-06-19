package br.edu.scl.ifsp.ads.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.ads.sharedlist.R
import br.edu.scl.ifsp.ads.sharedlist.databinding.ActivityTaskBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

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
                    creationDateEt.setText(creationDate)
                    dueDateEt.setText(dueDate)
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
            creationDateEt.isEnabled = false
            completionUserTv.isEnabled = false
            completedByTv.isEnabled = false
            dueDateEt.isEnabled = !viewTask
            checkboxCompletionCb.isEnabled = !viewTask
            checkboxCompletionCb.visibility = View.VISIBLE
            completionUserTv.visibility = if(viewTask && checkboxCompletionCb.isChecked) View.VISIBLE else View.GONE
            completedByTv.visibility = if(viewTask && checkboxCompletionCb.isChecked) View.VISIBLE else View.GONE
            saveBt.visibility = if(viewTask) View.GONE else View.VISIBLE

            }

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
                creationDate = atb.creationDateEt.text.toString(),
                dueDate = atb.dueDateEt.text.toString(),
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