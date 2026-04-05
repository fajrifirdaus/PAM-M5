package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.model.Note
import org.example.project.model.NoteCategory
import org.example.project.model.NoteColor

// ═══════════════════════════════════════════════════
// UI STATE — Data class untuk seluruh UI state
// ═══════════════════════════════════════════════════

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isDarkMode: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val noteToDelete: Int? = null,
    val snackbarMessage: String? = null
)

// ═══════════════════════════════════════════════════
// VIEWMODEL — Business Logic Layer
// ═══════════════════════════════════════════════════

class NotesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private var nextId = 4

    init {
        // Seed data — sample notes
        _uiState.update { state ->
            state.copy(
                notes = listOf(
                    Note(
                        id = 1,
                        title = "Belajar Jetpack Compose",
                        content = "Jetpack Compose adalah toolkit UI modern dari Google untuk membangun antarmuka Android secara deklaratif. Dengan Compose, kita bisa membangun UI yang reaktif dan lebih mudah dipahami.",
                        category = NoteCategory.WORK,
                        isFavorite = true,
                        color = NoteColor.BLUE
                    ),
                    Note(
                        id = 2,
                        title = "Rencana Project PAM",
                        content = "Minggu 5: Navigasi antar layar\nMinggu 6: Networking & REST API\nMinggu 7: Local Database (Room)\nMinggu 8: Ujian Tengah Semester",
                        category = NoteCategory.IMPORTANT,
                        isFavorite = true,
                        color = NoteColor.YELLOW
                    ),
                    Note(
                        id = 3,
                        title = "Ide Aplikasi",
                        content = "Membuat aplikasi catatan dengan fitur kategorisasi, pencarian, dan sinkronisasi cloud. Target pengguna adalah mahasiswa yang ingin mengorganisir catatan kuliah.",
                        category = NoteCategory.IDEAS,
                        isFavorite = false,
                        color = NoteColor.GREEN
                    )
                )
            )
        }
    }

    // ── Getters ──

    fun getNoteById(id: Int): Note? =
        _uiState.value.notes.find { it.id == id }

    val filteredNotes: List<Note>
        get() = _uiState.value.notes.filter { note ->
            val query = _uiState.value.searchQuery.lowercase()
            query.isEmpty() ||
                    note.title.lowercase().contains(query) ||
                    note.content.lowercase().contains(query)
        }

    val favoriteNotes: List<Note>
        get() = _uiState.value.notes.filter { it.isFavorite }

    // ── Actions ──

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(noteId: Int) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == noteId) note.copy(isFavorite = !note.isFavorite)
                    else note
                }
            )
        }
    }

    fun addNote(title: String, content: String, category: NoteCategory, color: NoteColor) {
        if (title.isBlank()) return
        val newNote = Note(
            id = nextId++,
            title = title.trim(),
            content = content.trim(),
            category = category,
            color = color
        )
        _uiState.update { state ->
            state.copy(
                notes = state.notes + newNote,
                snackbarMessage = "Catatan berhasil ditambahkan"
            )
        }
    }

    fun updateNote(id: Int, title: String, content: String, category: NoteCategory, color: NoteColor) {
        if (title.isBlank()) return
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == id) note.copy(
                        title = title.trim(),
                        content = content.trim(),
                        category = category,
                        color = color
                    )
                    else note
                },
                snackbarMessage = "Catatan berhasil diperbarui"
            )
        }
    }

    fun requestDeleteNote(noteId: Int) {
        _uiState.update { it.copy(showDeleteDialog = true, noteToDelete = noteId) }
    }

    fun confirmDeleteNote() {
        val id = _uiState.value.noteToDelete ?: return
        _uiState.update { state ->
            state.copy(
                notes = state.notes.filter { it.id != id },
                showDeleteDialog = false,
                noteToDelete = null,
                snackbarMessage = "Catatan dihapus"
            )
        }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, noteToDelete = null) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
