package com.example.desarrollo.model

import com.example.desarrollo.R

object SampleData {

    val categories = listOf(
        Category(name = "Frutas frescas"),
        Category(name = "Verduras orgánicas"),
        Category(name = "Productos orgánicos"),
        Category(name = "Productos lácteos")
    )

    val products = listOf(
        Product(
            id = 1,
            name = "Manzanas Fuji",
            price = 1200,
            unit = "kg",
            stock = "150 kg",
            imageRes = R.drawable.manzanafuji,
            description = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule. Perfectas para meriendas saludables o como ingrediente en postres. Estas manzanas son conocidas por su textura firme y su sabor equilibrado entre dulce y ácido.",
            rating = 4.3,
            categoryId = "Frutas frescas"
        ),
        Product(
            id = 2,
            name = "Naranjas Valencia",
            price = 1000,
            unit = "kg",
            stock = "200 kg",
            imageRes = R.drawable.naranjas,
            description = "Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos y refrescantes. Cultivadas en condiciones climáticas óptimas que aseguran su dulzura y jugosidad.",
            rating = 4.7,
            categoryId = "Frutas frescas"
        ),
        Product(
            id = 3,
            name = "Plátanos Cavendish",
            price = 800,
            unit = "kg",
            stock = "250 kg",
            imageRes = R.drawable.platanos,
            description = "Plátanos maduros y dulces, perfectos para el desayuno o como snack energético. Estos plátanos son ricos en potasio y vitaminas, ideales para mantener una dieta equilibrada.",
            rating = 4.8,
            categoryId = "Frutas frescas"
        ),
        Product(
            id = 4,
            name = "Zanahorias Orgánicas",
            price = 900,
            unit = "kg",
            stock = "100 kg",
            imageRes = R.drawable.zanahoria,
            description = "Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins. Excelente fuente de vitamina A y fibra, ideales para ensaladas, jugos o como snack saludable.",
            rating = 4.5,
            categoryId = "Verduras orgánicas"
        ),
        Product(
            id = 5,
            name = "Espinacas frescas",
            price = 700,
            unit = "bolsa",
            stock = "80 bolsas",
            imageRes = R.drawable.espinaca,
            description = "Espinacas frescas y nutritivas, perfectas para ensaladas y batidos verdes. Estas espinacas son cultivadas bajo prácticas orgánicas que garantizan su calidad y valor nutricional.",
            rating = 4.1,
            categoryId = "Verduras orgánicas"
        ),
        Product(
            id = 6,
            name = "Pimientos tricolor",
            price = 1500,
            unit = "kg",
            stock = "120 kg",
            imageRes = R.drawable.pimiento,
            description = "Pimientos rojos, amarillos y verdes, ideales para salteados y platos coloridos. Ricos en antioxidantes y vitaminas, estos pimientos añaden un toque vibrante y saludable a cualquier receta.",
            rating = 4.4,
            categoryId = "Verduras orgánicas"
        ),
        Product(
            id = 7,
            name = "Miel orgánica",
            price = 5000,
            unit = "frasco",
            stock = "50 frascos",
            imageRes = R.drawable.miel,
            description = "Miel pura y orgánica, rica en antioxidantes, perfecta para endulzar naturalmente.",
            rating = 4.8,
            categoryId = "Productos orgánicos"
        ),
        Product(
            id = 8,
            name = "Quinoa orgánica",
            price = 800,
            unit = "kg",
            stock = "130 kg",
            imageRes = R.drawable.quinoa,
            description = "Quinoa orgánica cultivada en los Andes, conocida como un superalimento por su alto contenido en proteínas, fibra y minerales. Es libre de gluten y versátil en la cocina, perfecta para ensaladas, guisos y platos saludables.",
            rating = 4.6,
            categoryId = "Productos orgánicos"
        ),
        Product(
            id = 9,
            name = "Leche entera",
            price = 800,
            unit = "litro",
            stock = "120 litros",
            imageRes = R.drawable.lecheentera,
            description = "Leche entera fresca y nutritiva, proveniente de ganado local. Rica en calcio, proteínas y vitaminas esenciales, ideal para fortalecer los huesos y mantener una alimentación equilibrada. Perfecta para desayunos, batidos y recetas caseras.",
            rating = 4.2,
            categoryId = "Productos lácteos"
        )
    )
}
