package com.example.stickeralbum.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stickeralbum.model.Sticker

@Database(
    entities = [Sticker::class],
    version = 3,
    exportSchema = false
)
abstract class StickerDatabase : RoomDatabase() {
    abstract fun stickerDao(): StickerDao


    companion object {
        @Volatile
        private var Instance: StickerDatabase? = null

        fun getDatabase(context: Context): StickerDatabase { //fja koja vraca nasu bazu
            //prima kontekst jer roomu treba info o android okruzenju aplikacije
            return Instance ?: synchronized(this) {  //ako instance vec postoji vrati, inace je null pa
                // izvrsi ovo desno, tu cemo napraviti bazu
                Room.databaseBuilder(
                    context,
                    StickerDatabase::class.java,
                    "sticker_database"  //ime lokalne baze na telefonu
                )
                    .fallbackToDestructiveMigration(true) //ako Room vidi staru bazu i ne zna kako je migrirati na novu verziju, smije obrisati staru bazu i napraviti novu.
                    .build()
                    .also {
                        Instance = it //zelimo vratiti bazu i sacuvati je u instance. also nam to omogucava
                    }
            }
        }
    }
}


