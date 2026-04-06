# 📝 Notes App — PAM Minggu 5

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat&logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-KMP-4285F4?style=flat&logo=jetpackcompose)
![Navigation](https://img.shields.io/badge/Navigation_Compose-2.7.0-green?style=flat)
![License](https://img.shields.io/badge/License-Academic-orange?style=flat)

**IF25-22017 — Pengembangan Aplikasi Mobile**  
Institut Teknologi Sumatera | Tahun Akademik Genap 2025/2026

</div>

---

## 👤 Identitas Mahasiswa

| Field | Detail |
|---|---|
| Nama | Muhammad Fajri Firdaus |
| NIM | 123140050 |
| Program Studi | Teknik Informatika |
| Pertemuan | 5 — Navigasi Antar Layar |
| Deadline | Sebelum Pertemuan 6 |

---

## 📋 Deskripsi Tugas

Mengembangkan **Notes App** dari tugas minggu 4 (State Management & MVVM) dengan menambahkan fitur **navigasi multi-screen** menggunakan Jetpack Compose Navigation Component di lingkungan **Kotlin Multiplatform (Compose Multiplatform)**.

---

## ✅ Checklist Fitur

### Wajib (100%)
- [x] **Bottom Navigation** dengan 3 tabs: Notes, Favorites, Profile
- [x] **Note List → Note Detail** dengan passing `noteId` (`NavType.IntType`)
- [x] **Floating Action Button** → navigate ke `AddNoteScreen`
- [x] **Back navigation** proper (`popBackStack()`) dari semua screen
- [x] **Edit Note screen** dengan passing `noteId` sebagai argument

### Bonus (+10%)
- [x] **Navigation Drawer** (`ModalNavigationDrawer`) dengan:
  - Menu utama mirror bottom nav (Notes, Favorites, Profile)
  - Settings screen (dark mode toggle)
  - About screen (info app & developer)

---

## 🗺️ Navigation Flow Diagram

![Image](https://github.com/user-attachments/assets/7e966e3b-6ebe-42cf-8f2f-40face60b66e)

---

## 📁 Struktur Folder

```
composeApp/src/commonMain/kotlin/org/example/project/
│
├── App.kt                          ← Entry point Compose Multiplatform
│
├── navigation/                     📍 Navigation Layer
│   ├── Screen.kt                   ← Sealed class semua routes + BottomNavItem
│   └── AppNavigation.kt            ← NavHost, Drawer, Bottom Nav, Scaffold
│
├── model/                          📦 Data Layer
│   └── Note.kt                     ← Data class + NoteCategory + NoteColor (light/dark)
│
├── viewmodel/                      🧠 Business Logic (MVVM)
│   ├── NotesViewModel.kt           ← State & operasi catatan (StateFlow)
│   └── ProfileViewModel.kt         ← State profil (dari Tugas Minggu 4)
│
├── screens/                        🖥️ UI Screens
│   ├── NotesScreen.kt              ← Tab 1: daftar catatan + search + FAB
│   ├── FavoritesScreen.kt          ← Tab 2: catatan favorit
│   ├── ProfileScreen.kt            ← Tab 3: profil + foto dari composeResources
│   ├── NoteDetailScreen.kt         ← Detail (menerima noteId: Int)
│   ├── AddNoteScreen.kt            ← Tambah catatan baru
│   ├── EditNoteScreen.kt           ← Edit catatan (menerima noteId: Int)
│   ├── SettingsScreen.kt           ← Pengaturan (via Drawer)
│   └── AboutScreen.kt              ← Tentang app (via Drawer)
│
├── components/                     🧩 Reusable UI Components
│   └── NoteCard.kt                 ← Card catatan (adaptive dark/light color)
│
└── ui/theme/                       🎨 Theming
    └── Theme.kt                    ← MaterialTheme dark/light + brand colors
```

---

## 🧭 Konsep Navigasi yang Diimplementasikan

### 1. Centralized Routes — Sealed Class

```kotlin
sealed class Screen(val route: String) {
    object Notes     : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")
    object AddNote   : Screen("add_note")
    object Settings  : Screen("settings")
    object About     : Screen("about")

    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}
```

### 2. NavHost Setup

```kotlin
NavHost(
    navController = navController,
    startDestination = Screen.Notes.route
) {
    composable(Screen.Notes.route) { NotesScreen(...) }

    // Argument: noteId (Int)
    composable(
        route = Screen.NoteDetail.route,
        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
    ) { backStackEntry ->
        val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
        NoteDetailScreen(noteId = noteId, ...)
    }
}
```

### 3. Navigation Actions

```kotlin
// Forward navigation
navController.navigate(Screen.NoteDetail.createRoute(noteId))

// Back navigation
navController.popBackStack()

// Tab switch (hindari duplikat di back stack)
navController.navigate(route) {
    popUpTo(navController.graph.startDestinationId) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
```

### 4. Bottom Navigation

```kotlin
NavigationBar {
    bottomNavItems.forEach { item ->
        NavigationBarItem(
            selected = currentRoute == item.screen.route,
            onClick  = { navController.navigate(item.screen.route) { ... } },
            icon     = { Icon(item.icon, item.label) },
            label    = { Text(item.label) }
        )
    }
}
```

### 5. Navigation Drawer (Bonus)

```kotlin
ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
        ModalDrawerSheet {
            NavigationDrawerItem(
                label   = { Text("Settings") },
                onClick = { navController.navigate(Screen.Settings.route) }
            )
        }
    }
) { /* Main Scaffold content */ }
```

---

## 🎨 Sistem Warna Catatan (Adaptive)

Setiap warna catatan memiliki dua varian — terang untuk light mode, gelap untuk dark mode:

```kotlin
enum class NoteColor(val lightHex: Long, val darkHex: Long) {
    DEFAULT(0xFFFFF8F0, 0xFF3D2E1E),  // krem    → coklat tua
    YELLOW( 0xFFFFF9C4, 0xFF3D3000),  // kuning  → kuning gelap
    GREEN(  0xFFE8F5E9, 0xFF0D3320),  // hijau   → hijau hutan
    BLUE(   0xFFE3F2FD, 0xFF0D2A3D),  // biru    → navy
    PINK(   0xFFFCE4EC, 0xFF3D0D1F),  // pink    → marun
    PURPLE( 0xFFF3E5F5, 0xFF2A0D3D)   // ungu    → ungu tua
}

fun NoteColor.toColor(isDarkMode: Boolean): Color =
    if (isDarkMode) Color(darkHex) else Color(lightHex)
```

| Elemen | Light Mode | Dark Mode |
|---|---|---|
| Background card | Pastel terang | Pastel digelapkan |
| Teks judul | Gelap `#1A1A2E` | Putih |
| Teks isi | Abu tua `#3D3D5C` | Putih 75% |
| Chip kategori | Biru transparan | **Biru solid** `#60A5FA` |

---

## 🛠️ Tech Stack

| Teknologi | Versi | Kegunaan |
|---|---|---|
| Kotlin | 2.0.21 | Bahasa pemrograman |
| Compose Multiplatform | BOM 2024.11.00 | UI Framework |
| Navigation Compose | 2.7.0-alpha07 | Multi-screen navigation |
| ViewModel + StateFlow | 2.8.0 | State management (MVVM) |
| Material Design 3 | — | Design system |
| Gradle KTS | 8.7 | Build system |
| JDK | 17 | Java version |
| compileSdk | 35 | Target Android version |

---

## 🚀 Cara Menjalankan

1. **Clone / extract** project
2. Buka folder `PAM Tugas 5` dengan **Android Studio** (Ladybug+)
3. Pastikan **JDK 17** dikonfigurasi:
   `File → Project Structure → SDK Location → Gradle JDK → 17`
4. Tambahkan dependency di `composeApp/build.gradle.kts`:
   ```kotlin
   commonMain.dependencies {
       implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
       implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
       implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
   }
   ```
5. **Sync Gradle** → **Run** ke emulator/device (min API 26)

---

## 📱 Daftar Screen

| Screen | Route | Argument | Cara Akses |
|---|---|---|---|
| NotesScreen | `notes` | — | Bottom Nav Tab 1 |
| FavoritesScreen | `favorites` | — | Bottom Nav Tab 2 |
| ProfileScreen | `profile` | — | Bottom Nav Tab 3 |
| NoteDetailScreen | `note_detail/{noteId}` | `noteId: Int` | Tap card di Notes/Favorites |
| AddNoteScreen | `add_note` | — | FAB di NotesScreen |
| EditNoteScreen | `edit_note/{noteId}` | `noteId: Int` | Tombol edit di NoteDetailScreen |
| SettingsScreen | `settings` | — | Navigation Drawer |
| AboutScreen | `about` | — | Navigation Drawer |

---

## 🖼️ Screenshots

> **Catatan:** Ganti bagian ini dengan screenshot aktual dari emulator/device setelah menjalankan aplikasi.

| Screen | Screenshot | Deskripsi |
|---|---|---|
| Notes (Tab 1) | ![Image](https://github.com/user-attachments/assets/b35d6c29-d47c-4b5b-9556-1705f014949b) | Tab Catatan — daftar note dengan FAB dan hamburger menu |
| Favorites (Tab 2) | ![Image](https://github.com/user-attachments/assets/cbe54e27-3b41-4b13-ae0a-0665a7c4f83d) | Tab Favorit — note yang ditandai favorit |
| Profile (Tab 3) | ![Image](https://github.com/user-attachments/assets/21d1c923-d250-405a-a1d7-2c1de694b42d) | Tab Profil — profil pengguna dengan dark mode toggle |
| Note Detail | ![Image](https://github.com/user-attachments/assets/213ae29f-e300-42ae-b50f-cd919dc81d0f) | Detail catatan — menampilkan noteId, judul, isi |
| Add Note | ![Image](https://github.com/user-attachments/assets/baee1f96-adbf-4f12-9e04-6ba04e0c115e) | Form tambah catatan — color picker + input |
| Edit Note | ![WhatsApp Image 2026-04-06 at 11 44 45 (2)](https://github.com/user-attachments/assets/9e1bf218-1a75-4e0f-8856-f361c51a1fab)
 | Form edit catatan — prefill dari data existing |
| Navigation Drawer | ![Image](https://github.com/user-attachments/assets/076171a6-dd32-437a-b177-92be403eebc7) | Navigation Drawer — menu slide dari kiri (BONUS) |
| Dark Mode | ![Image](https://github.com/user-attachments/assets/2bdeaea6-f172-45fa-ae44-b6ecfdc72bd6) | Dark mode aktif — semua screen dalam tema gelap |

---

## 🎬 Video Demo

**File:** `demo_week5.mp4` (≤ 30 detik)

Urutan demo yang disarankan:

1. Buka app → tampil `NotesScreen`
2. Buka Navigation Drawer (swipe dari kiri / tap hamburger icon)
3. Tap catatan → `NoteDetailScreen` (lihat noteId sebagai argument)
4. Tap Edit → `EditNoteScreen` → Simpan → kembali ke list
5. Tap FAB (+) → `AddNoteScreen` → isi form → Simpan
6. Tap tab Favorit → `FavoritesScreen`
7. Tap tab Profil → toggle Dark Mode → semua screen berubah tema

---

## 📊 Rubrik Penilaian

| Komponen | Bobot | Status |
|---|---|---|
| Bottom Navigation (3 tabs) | 25% | ✅ |
| Navigation with Args (noteId) | 25% | ✅ |
| Navigation Flow (forward & back) | 20% | ✅ |
| Code Structure (routes, clean code) | 20% | ✅ |
| Documentation (README, screenshots) | 10% | ✅ |
| **Navigation Drawer (Bonus)** | **+10%** | ✅ |

---

## 🔗 Referensi

- [Navigation in Compose — developer.android.com](https://developer.android.com/jetpack/compose/navigation)
- [Navigate with arguments](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Compose Multiplatform Navigation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html)
- [Material Design 3](https://m3.material.io/)

---

<div align="center">

*© 2026 Tugas PAM Minggu 5 — Muhammad Fajri Firdaus (123140050)*  
*Institut Teknologi Sumatera — IF25-22017*

</div>
