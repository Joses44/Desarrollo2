package com.example.desarrollo.data

import com.example.desarrollo.model.CategoryWithProducts
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    val categoriesWithProducts: Flow<List<CategoryWithProducts>> = productDao.getCategoriesWithProducts()
}
