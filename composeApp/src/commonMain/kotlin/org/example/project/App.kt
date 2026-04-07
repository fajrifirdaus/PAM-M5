package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.navigation.AppNavigation
import org.example.project.ui.theme.NotesAppTheme
import org.example.project.viewmodel.NotesViewModel

// ═══════════════════════════════════════════════════
// APP.KT — Entry point untuk Compose Multiplatform
// Menggantikan App.kt lama (single screen profile)
// Tugas PAM Minggu 5: Navigasi Antar Layar
// ═══════════════════════════════════════════════════

@Composable
fun App() {
    val notesViewModel: NotesViewModel = viewModel()
    val uiState by notesViewModel.uiState.collectAsStateWithLifecycle()

    NotesAppTheme(darkTheme = uiState.isDarkMode) {
        AppNavigation(notesViewModel = notesViewModel)
    }
}
