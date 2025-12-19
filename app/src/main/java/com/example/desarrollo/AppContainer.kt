import android.content.Context
import com.example.desarrollo.data.CartRepository
import com.example.desarrollo.data.AuthManager
import com.example.desarrollo.network.RetrofitClient
import com.example.desarrollo.network.ApiService
import com.example.desarrollo.viewmodel.CartViewModelFactory
// Importa el DAO si lo necesitas para otras repositorios (ej. ProductRepository)
// import com.example.desarrollo.data.AppDatabase
// import com.example.desarrollo.data.ProductDao

interface AppContainer {
    val authManager: AuthManager
    val apiService: ApiService
    val cartRepository: CartRepository
    val cartViewModelFactory: CartViewModelFactory
    // Añade otras dependencias aquí (ej. productRepository)
}

// Implementación concreta
class DefaultAppContainer(private val applicationContext: Context) : AppContainer {

    // Si usas Room/DAO, descomenta esto (necesitas importar AppDatabase)
    // private val database by lazy { AppDatabase.getDatabase(applicationContext) }

    // 1. GESTIÓN DE AUTENTICACIÓN
    override val authManager: AuthManager by lazy {
        // ✅ SOLUCIÓN: Llamada al constructor directo de la clase AuthManager
        AuthManager(applicationContext)
    }

    // 2. SERVICIO DE RED (API Service)
    override val apiService: ApiService by lazy {
        RetrofitClient.getApiService(authManager)
    }

    // 3. REPOSITORIOS (Data Layer)

    // ✅ SOLUCIÓN: CartRepository ahora solo acepta ApiService (modelo backend-only)
    override val cartRepository: CartRepository by lazy {
        CartRepository(apiService = apiService)
    }

    /* // Si tuvieras ProductRepository (ejemplo), se inyectaría así:
    // override val productRepository: ProductRepository by lazy {
    //     ProductRepository(
    //         productDao = database.productDao(),
    //         apiService = apiService
    //     )
    // }
    */

    // 4. FACTORY DE VIEWMODEL (UI Layer)

    // Se usa para instanciar el CartViewModel con el CartRepository inyectado
    override val cartViewModelFactory: CartViewModelFactory by lazy {
        CartViewModelFactory(cartRepository)
    }
}