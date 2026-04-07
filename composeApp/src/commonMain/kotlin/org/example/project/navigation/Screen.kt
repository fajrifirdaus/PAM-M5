package org.example.project.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

// ═══════════════════════════════════════════════════
// CENTRALIZED ROUTES — Single Source of Truth
// Menggunakan sealed class untuk type-safe navigation
// ═══════════════════════════════════════════════════

sealed class Screen(val route: String) {

    // ── Bottom Navigation Destinations ──
    object Notes : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")

    // ── Notes Flow Destinations ──
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }

    // ── Drawer Destinations ──
    object Settings : Screen("settings")
    object About : Screen("about")
}

// ═══════════════════════════════════════════════════
// BOTTOM NAVIGATION ITEMS
// ═══════════════════════════════════════════════════

sealed class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
) {
    object Notes : BottomNavItem(
        screen = Screen.Notes,
        icon = Icons.Rounded.Home,
        label = "Notes"
    )

    object Favorites : BottomNavItem(
        screen = Screen.Favorites,
        icon = Icons.Rounded.BookmarkBorder,
        label = "Favorites"
    )

    object Profile : BottomNavItem(
        screen = Screen.Profile,
        icon = Icons.Rounded.Person,
        label = "Profile"
    )
}
