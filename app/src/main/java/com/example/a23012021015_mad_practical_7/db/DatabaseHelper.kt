package com.example.a23012021015_mad_practical_7.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "persons.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_PERSON = "Person"

        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_EMAIL = "emailId"
        private const val COL_PHONE = "phoneNo"
        private const val COL_ADDRESS = "address"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_PERSON (" +
                    "$COL_ID TEXT PRIMARY KEY, " +
                    "$COL_NAME TEXT, " +
                    "$COL_EMAIL TEXT, " +
                    "$COL_PHONE TEXT, " +
                    "$COL_ADDRESS TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PERSON")
        onCreate(db)
    }

    fun insertPerson(person: Person) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID, person.id)
            put(COL_NAME, person.name)
            put(COL_EMAIL, person.emailId)
            put(COL_PHONE, person.phoneNo)
            put(COL_ADDRESS, person.address)
        }
        db.insert(TABLE_PERSON, null, values)
    }

    fun updatePerson(person: Person) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, person.name)
            put(COL_EMAIL, person.emailId)
            put(COL_PHONE, person.phoneNo)
            put(COL_ADDRESS, person.address)
        }
        db.update(TABLE_PERSON, values, "$COL_ID=?", arrayOf(person.id))
    }

    fun deletePerson(person: Person) {
        val db = writableDatabase
        db.delete(TABLE_PERSON, "$COL_ID=?", arrayOf(person.id))
    }

    fun getPerson(id: String): Person? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PERSON,
            null,
            "$COL_ID=?",
            arrayOf(id),
            null, null, null
        )

        val person = if (cursor.moveToFirst()) {
            Person(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                emailId = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS))
            )
        } else {
            null
        }

        cursor.close()
        return person
    }

    val allPersons: List<Person>
        get() {
            val list = ArrayList<Person>()
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_PERSON", null)

            if (cursor.moveToFirst()) {
                do {
                    list.add(
                        Person(
                            id = cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                            emailId = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                            phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                            address = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS))
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
            return list
        }
}
