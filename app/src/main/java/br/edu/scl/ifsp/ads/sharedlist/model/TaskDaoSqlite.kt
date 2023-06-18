package br.edu.scl.ifsp.ads.sharedlist.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.scl.ifsp.ads.sharedlist.R
import java.sql.SQLException

class TaskDaoSqlite (context: Context): TaskDao {
    companion object Constants{
        private const final val TASK_DATABASE_FILE = "tasks"
        private const val TASK_TABLE = "task"
        private const val ID_COLUMN = "id"
        private const val TITLE_COLUMN = "title"
        private const val DESCRIPTION_COLUMN = "description"
        private const val CREATION_USER_COLUMN = "creation_user"
        private const val CREATION_DATE_COLUMN = "creation_date"
        private const val DUE_DATE_COLUMN = "due_date"


        private const val CREATE_TASK_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS $TASK_TABLE (" +
                "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE_COLUMN TEXT NOT NULL, " +
                "$DESCRIPTION_COLUMN TEXT NOT NULL, " +
                "$CREATION_USER_COLUMN TEXT NOT NULL, " +
                "$CREATION_DATE_COLUMN TEXT NOT NULL, " +
                "$DUE_DATE_COLUMN TEXT NOT NULL)";

    }

    //referencia para o banco de dados
    private val taskSqliteDatabase: SQLiteDatabase

    // bloco init é sempre executado depois de todos os construtores
    init{
        //criando ou abrindo o banco
        taskSqliteDatabase = context.openOrCreateDatabase(TASK_DATABASE_FILE,
            Context.MODE_PRIVATE, null)
        try{
            taskSqliteDatabase.execSQL(CREATE_TASK_TABLE_STATEMENT)
        }catch (se: SQLException){
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createTask(task: Task) {
        taskSqliteDatabase.insert(TASK_TABLE, null, task.toContentValues()).toInt()
    } //contentValues é um hashamp; funcao single expression


    override fun retrieveTask(id: Int): Task? {
        val cursor = taskSqliteDatabase.rawQuery(
            "SELECT * FROM $TASK_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )
        val task = if(cursor.moveToFirst()){
            //preenche contato com dados da posicao atual do cursor
            cursor.rowToTask()
        }else{
            null
        }
        cursor.close()
        return task
    }

    override fun retrieveTasks(): MutableList<Task> {
        val taskList = mutableListOf<Task>()
        val cursor = taskSqliteDatabase.rawQuery("SELECT * FROM $TASK_TABLE ORDER BY $TITLE_COLUMN",
            null)
        while (cursor.moveToNext()){
            taskList.add(cursor.rowToTask())
        }
        cursor.close()
        return taskList
    }

    override fun updateTask(task: Task): Int = taskSqliteDatabase.update(TASK_TABLE,
        task.toContentValues(), "$ID_COLUMN = ?", arrayOf(task.id.toString()))

    override fun deleteTask(task: Task): Int = taskSqliteDatabase.delete(TASK_TABLE,
        "$ID_COLUMN = ?", arrayOf(task.id.toString()))

    private fun Task.toContentValues()= with(ContentValues()){
        put(TITLE_COLUMN, title)
        put(DESCRIPTION_COLUMN, description)
        put(CREATION_USER_COLUMN, creationUser)
        put(CREATION_DATE_COLUMN, creationDate)
        put(DUE_DATE_COLUMN, dueDate)
        this
    }


    private fun Cursor.rowToTask() = Task(
        id = getInt(getColumnIndexOrThrow(ID_COLUMN)),
        title = getString(getColumnIndexOrThrow(TITLE_COLUMN)),
        description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
        creationUser = getString(getColumnIndexOrThrow(CREATION_USER_COLUMN)),
        creationDate = getString(getColumnIndexOrThrow(CREATION_DATE_COLUMN)),
        dueDate = getString(getColumnIndexOrThrow(DUE_DATE_COLUMN)),
    )
}