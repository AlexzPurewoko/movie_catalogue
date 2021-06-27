package id.apwdevs.app.data.source.local

import android.content.Context
import androidx.room.Room
import id.apwdevs.app.data.BuildConfig
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.utils.Config
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object LocalInit {

    private fun initDBChiper(): SupportFactory {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(Config.DB_PASSPHRASE.toCharArray())
        return SupportFactory(passphrase)
    }

    fun init(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, "app_database.db"
        ).apply {
            if(!BuildConfig.DEBUG)
                openHelperFactory(initDBChiper())
        }.build()
    }
}