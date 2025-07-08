package com.nyansapoai.teaching.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.nyansapoai.teaching.Database

class LocalDatabaseDriverFactory(
    private val context: Context
) {
    fun create(): SqlDriver {
        val driver = AndroidSqliteDriver(
            Database.Schema,
            context,
            "appDatabase.sq"
        )

        driver.execute(null, "PRAGMA foreign_keys=OFF;", 0)

        return driver
    }
}