import java.security.MessageDigest

// Clase para representar un usuario con correo y contraseña
class Usuario(private val correo: String, private val contraseñaHash: String) {
    fun getCorreo(): String {
        return correo
    }

    fun getContraseñaHash(): String {
        return contraseñaHash
    }
}

// Clase para representar una tabla hash
class HashTabla<C : Comparable<C>, V> {
    // Diccionario para almacenar las parejas clave-valor
    private val diccionario: HashMap<C, V> = HashMap()

    // Método para insertar una nueva pareja clave-valor en la tabla hash
    fun insertar(clave: C, valor: V) {
        diccionario[clave] = valor
    }

    // Método para buscar un usuario en la tabla hash a través del correo y contraseña
    fun buscarUsuario(correo: C, contraseña: C): Usuario? {
        // Obtener el valor asociado a la clave (correo)
        val contraseñaNodo = diccionario[correo] as? String
        // Verificar si la contraseña coincide utilizando el cifrado hashing
        return if (contraseñaNodo != null && verificarContraseña(contraseñaNodo, contraseña)) {
            Usuario(correo as String, contraseñaNodo)
        } else {
            null
        }
    }

    // Verificar si la contraseña coincide utilizando el cifrado hashing
    private fun verificarContraseña(contraseñaHashAlmacenada: String, contraseña: C): Boolean {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(contraseña.toString().toByteArray())

        // Convertir el hash a una representación hexadecimal
        val hashString = hashBytes.joinToString("") { "%02x".format(it) }

        // Comparar los hashes
        return hashString == contraseñaHashAlmacenada
    }
}

fun main() {
    // Crear una tabla hash para almacenar usuarios
    val tablaHash = HashTabla<String, String>()

    // Registro de usuario
    println("Registro")
    println("Ingresa tu correo:")
    val correoRegistro = readLine() ?: ""
    println("Ingresa tu contraseña:")
    val contraseñaRegistro = readLine() ?: ""

    // Crear un objeto Usuario con contraseña cifrada
    val md = MessageDigest.getInstance("SHA-256")
    val hashBytes = md.digest(contraseñaRegistro.toByteArray())
    val contraseñaCifrada = hashBytes.joinToString("") { "%02x".format(it) }
    val nuevoUsuario = Usuario(correoRegistro, contraseñaCifrada)

    // Insertar el nuevo usuario en la tabla hash
    tablaHash.insertar(nuevoUsuario.getCorreo(), nuevoUsuario.getContraseñaHash())

    // Ingreso de usuario
    println("Ingreso")
    println("Ingresa tu correo:")
    val correoIngreso = readLine() ?: ""
    println("Ingresa tu contraseña:")
    val contraseñaIngreso = readLine() ?: ""

    // Buscar un usuario por correo y contraseña en la tabla hash
    val usuario = tablaHash.buscarUsuario(correoIngreso, contraseñaIngreso)
    if (usuario != null) {
        println("Usuario encontrado, bienvenido")
    } else {
        println("Usuario no encontrado")
    }
}

