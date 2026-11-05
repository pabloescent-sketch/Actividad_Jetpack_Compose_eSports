package com.example.jetpackequiposesports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackequiposesports.ui.theme.JetpackEquiposEsportsTheme

// ACTIVIDAD 1: Data class para representar a un jugador
data class Jugador(
    val id: Int,
    val nombre: String,
    val nickname: String,
    val rol: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackEquiposEsportsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaGestionEsports()
                }
            }
        }
    }
}

@Composable
fun PantallaGestionEsports() {
    // Lista reactiva inicial con al menos 5 jugadores
    val listaJugadores = remember {
        mutableStateListOf(
            Jugador(1, "Alex Pérez", "AlexPro", "Atacante"),
            Jugador(2, "Sergio Gómez", "ShadowX", "Defensor"),
            Jugador(3, "Marta Gil", "MartaGG", "Soporte"),
            Jugador(4, "David Ruiz", "Destroyer", "Atacante"),
            Jugador(5, "Lucía Sanz", "SniperQueen", "Tirador")
        )
    }

    // Estados del formulario para añadir nuevos jugadores (con rememberSaveable para giros de pantalla)
    var nombreInput by rememberSaveable { mutableStateOf("") }
    var nicknameInput by rememberSaveable { mutableStateOf("") }
    var rolInput by rememberSaveable { mutableStateOf("") }
    var mensajeError by rememberSaveable { mutableStateOf("") }

    // Estados para el diálogo de eliminación
    var jugadorAEliminar by remember { mutableStateOf<Jugador?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // Estados para el diálogo de edición
    var jugadorAEditar by remember { mutableStateOf<Jugador?>(null) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var nombreEditInput by rememberSaveable { mutableStateOf("") }
    var nicknameEditInput by rememberSaveable { mutableStateOf("") }
    var rolEditInput by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Título principal
        Text(
            text = "TEAM TITANS - Gestión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        // ACTIVIDAD 2: Formulario para añadir nuevos jugadores
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Añadir Nuevo Jugador",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombreInput,
                    onValueChange = { nombreInput = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = nicknameInput,
                    onValueChange = { nicknameInput = it },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = rolInput,
                    onValueChange = { rolInput = it },
                    label = { Text("Rol (ej. Atacante)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (nombreInput.isBlank() || nicknameInput.isBlank() || rolInput.isBlank()) {
                            mensajeError = "Por favor, rellena todos los campos."
                        } else {
                            mensajeError = ""
                            // Generamos un ID único basado en el tamaño o timestamp actual
                            val nuevoId = if (listaJugadores.isEmpty()) 1 else listaJugadores.maxOf { it.id } + 1
                            listaJugadores.add(
                                Jugador(
                                    id = nuevoId,
                                    nombre = nombreInput.trim(),
                                    nickname = nicknameInput.trim(),
                                    rol = rolInput.trim()
                                )
                            )
                            // Limpiar campos
                            nombreInput = ""
                            nicknameInput = ""
                            rolInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir jugador")
                }
            }
        }

        Text(
            text = "Plantilla Actual (${listaJugadores.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // ACTIVIDAD 3: Mostrar la plantilla mediante LazyColumn con key
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = listaJugadores,
                key = { jugador -> jugador.id } // Uso obligatorio del parámetro key con el id único
            ) { jugador ->
                ItemJugador(
                    jugador = jugador,
                    onEditClick = {
                        jugadorAEditar = jugador
                        nombreEditInput = jugador.nombre
                        nicknameEditInput = jugador.nickname
                        rolEditInput = jugador.rol
                        mostrarDialogoEditar = true
                    },
                    onDeleteClick = {
                        jugadorAEliminar = jugador
                        mostrarDialogoEliminar = true
                    }
                )
            }
        }
    }

    // ACTIVIDAD 4: AlertDialog de confirmación para eliminar
    if (mostrarDialogoEliminar && jugadorAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${jugadorAEliminar?.nickname}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        jugadorAEliminar?.let { listaJugadores.remove(it) }
                        mostrarDialogoEliminar = false
                        jugadorAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        jugadorAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ACTIVIDAD 5: AlertDialog o Formulario para editar un jugador
    if (mostrarDialogoEditar && jugadorAEditar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEditar = false },
            title = { Text("Editar Jugador") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nombreEditInput,
                        onValueChange = { nombreEditInput = it },
                        label = { Text("Nombre") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nicknameEditInput,
                        onValueChange = { nicknameEditInput = it },
                        label = { Text("Nickname") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rolEditInput,
                        onValueChange = { rolEditInput = it },
                        label = { Text("Rol") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nombreEditInput.isNotBlank() && nicknameEditInput.isNotBlank() && rolEditInput.isNotBlank()) {
                            val index = listaJugadores.indexOfFirst { it.id == jugadorAEditar?.id }
                            if (index != -1) {
                                // Mantiene el identificador original (id)
                                listaJugadores[index] = Jugador(
                                    id = jugadorAEditar!!.id,
                                    nombre = nombreEditInput.trim(),
                                    nickname = nicknameEditInput.trim(),
                                    rol = rolEditInput.trim()
                                )
                            }
                            mostrarDialogoEditar = false
                            jugadorAEditar = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEditar = false
                        jugadorAEditar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Componente visual para cada tarjeta de jugador en la LazyColumn
@Composable
fun ItemJugador(
    jugador: Jugador,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jugador.nickname,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Nombre: ${jugador.nombre}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rol: ${jugador.rol}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar jugador"
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar jugador",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEsports2() {
    JetpackEquiposEsportsTheme {
        PantallaGestionEsports()
    }
}