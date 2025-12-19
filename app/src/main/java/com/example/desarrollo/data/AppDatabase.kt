package com.example.desarrollo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.desarrollo.model.CartItem
import com.example.desarrollo.model.Category
import com.example.desarrollo.model.Product
import com.example.desarrollo.model.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Product::class, Category::class, CartItem::class],
    version = 4, // 🎯 Subimos a 4 por el cambio de Int a Long en los IDs
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Borra la BD vieja para aplicar los Long
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val productDao = database.productDao()
                    // Asegúrate de que SampleData.products también use Longs en sus IDs
                    productDao.insertCategories(SampleData.categories)
                    productDao.insertProducts(SampleData.products)
                }
            }
        }
    }
}