package com.example.calculator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "data"
        private const val TABLE_NAME2 = "note"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }


//  override fun onCreate(db: SQLiteDatabase?) {
//        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
//                "$COLUMN_ID INTEGER PRIMARY KEY, " +
//                "$COLUMN_TITLE TEXT, " +
//                "$COLUMN_CONTENT TEXT)")
//        db?.execSQL(createTableQuery)
//    }


    //    override fun onCreate(db: SQLiteDatabase?,oldVersion:Int,newVersion:Int) {
    //        val createTableQuery = ("DROP TABLE IF EXISTS $TABLE_NAME")
    //        db?.execSQL(createTableQuery)
    //        onCreate(db)
    //    }


    override fun onCreate(db: SQLiteDatabase) {
        // Buat tabel pertama
        val createTable1 = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()

        // Buat tabel kedua
        val createTable2 = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME2 (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT
            )
        """.trimIndent()

        try {
            db.execSQL(createTable1)
            db.execSQL(createTable2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop tabel jika ada
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME2")
        // Buat ulang tabel
        onCreate(db)
    }

    // Fungsi untuk mengecek apakah tabel sudah terbuat
    fun checkTables() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        println("Checking existing tables:")
        if (cursor.moveToFirst()) {
            do {
                val tableName = cursor.getString(0)
                println("Table found: $tableName")
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    fun insertNote(note: Note) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val db = writableDatabase
        db.insert(TABLE_NAME2, null, values)
        db.close()
    }

    fun getAllNotes(): ArrayList<Note> {
        val notesList = ArrayList<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

                val note = Note(id, title, content)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notesList
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME2, values, whereClause, whereArgs)
        db.close()
    }


    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2 WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))


        cursor.close()
        db.close()
        return Note(id, title, content)
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME2, whereClause, whereArgs)
        db.close()
    }


    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun isUserExist(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }

    fun isUsernameExist(username: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun hasRegisteredUser(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }

    fun deleteAllUsers() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME") // Hapus semua data dari tabel user
        db.close()
    }



    fun deleteUserById(id: Int): Int {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }
    }