package com.example.desarrollo

import android.app.Application
import com.example.desarrollo.data.AppDatabase
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.data.ProductRepository

class MyApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    
    val productRepository by lazy { ProductRepository(database.productDao()) }
    val cartRepository by lazy { CartRepository(database.cartDao()) }
}
