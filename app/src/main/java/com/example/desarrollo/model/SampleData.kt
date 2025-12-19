package com.example.desarrollo.model

import com.example.desarrollo.R

object SampleData {

    // 🎯 Categorías sincronizadas (1 al 4)
    val categories = listOf(
        Category(id = 1L, name = "FRUTAS", iconRes = 1),
        Category(id = 2L, name = "VEGETALES", iconRes = 2),
        Category(id = 3L, name = "DESPENSA", iconRes = 3),
        Category(id = 4L, name = "LACTEOS", iconRes = 4)
    )

    val products = listOf(
        Product(
            id = 1L, // 🎯 Sincronizado con el ID 1 del JSON
            name = "Manzanas fuji",
            price = 1200,
            unit = "kg",
            stock = 150,
            imageRes = 101,
            description = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.",
            rating = 4.8,
            categoryId = 1L
        ),
        Product(
            id = 2L, // 🎯 Sincronizado con el ID 2 del JSON
            name = "Naranjas valencia",
            price = 1000,
            unit = "kg",
            stock = 200,
            imageRes = 102,
            description = "Jugosas y ricas en vitamina C, ideales para zumos frescos.",
            rating = 4.5,
            categoryId = 1L
        ),
        Product(
            id = 3L,
            name = "Plátanos cavendish",
            price = 800,
            unit = "kg",
            stock = 250,
            imageRes = 103,
            description = "Plátanos maduros y dulces, perfectos para el desayuno.",
            rating = 4.2,
            categoryId = 1L
        ),
        Product(
            id = 4L,
            name = "Zanahorias Orgánicas",
            price = 900,
            unit = "kg",
            stock = 100,
            imageRes = 104,
            description = "Zanahorias crujientes cultivadas sin pesticidas.",
            rating = 4.7,
            categoryId = 2L
        ),
        Product(
            id = 5L,
            name = "Espinacas frescas",
            price = 700,
            unit = "bolsa",
            stock = 80,
            imageRes = 105,
            description = "Espinacas frescas y nutritivas, perfectas para ensaladas.",
            rating = 4.6,
            categoryId = 2L
        ),
        Product(
            id = 6L,
            name = "Pimientos tricolor",
            price = 1500,
            unit = "kg",
            stock = 120,
            imageRes = 106,
            description = "Pimientos rojos, amarillos y verdes, ideales para salteados.",
            rating = 4.4,
            categoryId = 2L
        ),
        Product(
            id = 7L,
            name = "Miel orgánica",
            price = 5000,
            unit = "frasco",
            stock = 50,
            imageRes = 107,
            description = "Miel pura y orgánica, rica en antioxidantes.",
            rating = 4.9,
            categoryId = 3L
        ),
        Product(
            id = 8L,
            name = "Quinoa orgánica",
            price = 800,
            unit = "bolsa",
            stock = 130,
            imageRes = 108,
            description = "Quinoa orgánica cultivada en los Andes, un superalimento.",
            rating = 4.3,
            categoryId = 3L
        ),
        Product(
            id = 9L,
            name = "Leche entera",
            price = 800,
            unit = "litro",
            stock = 120,
            imageRes = 109,
            description = "Leche entera fresca y nutritiva, proveniente de ganado local.",
            rating = 4.5,
            categoryId = 4L
        )
    )
}