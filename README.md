# 📝 Notes App — PAM Minggu 5 | Navigasi Antar Layar
**Muhammad Fajri Firdaus — NIM 123140050 — Informatika ITERA**

---

## 📂 CARA MEMASANG FILE KE PROJECT

> Project kamu adalah **Compose Multiplatform (KMP)**, bukan Android biasa.
> Semua file `.kt` baru ini diletakkan di **`commonMain`**.

### Struktur tujuan di project kamu:

```
PAM Tugas 5/                          ← root project kamu
└── composeApp/
    └── src/
        └── commonMain/
            └── kotlin/
                └── org/example/project/
                    │
                    ├── App.kt              ← ⚠️ GANTI file ini (hapus yang lama)
                    │
                    ├── navigation/         ← folder BARU
                    │   ├── Screen.kt
                    │   └── AppNavigation.kt
                    │
                    ├── model/              ← folder BARU
                    │   └── Note.kt
                    │
                    ├── viewmodel/          ← folder BARU
                    │   ├── NotesViewModel.kt
                    │   └── ProfileViewModel.kt  ← diambil dari tugas 4
                    │
                    ├── screens/            ← folder BARU
                    │   ├── NotesScreen.kt
                    │   ├── FavoritesScreen.kt
                    │   ├── ProfileScreen.kt
                    │   ├── NoteDetailScreen.kt
                    │   ├── AddNoteScreen.kt
                    │   ├── EditNoteScreen.kt
                    │   ├── SettingsScreen.kt
                    │   └── AboutScreen.kt
                    │
                    ├── components/         ← folder BARU
                    │   └── NoteCard.kt
                    │
                    └── ui/theme/           ← folder BARU
                        └── Theme.kt
```

---

## ⚙️ Dependency yang perlu ditambah di `composeApp/build.gradle.kts`

Buka file `composeApp/build.gradle.kts` dan tambahkan di bagian `commonMain.dependencies`:

```kotlin
commonMain.dependencies {
    // ... dependency yang sudah ada ...

    // ── Tambahkan ini ──
    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
}
```

---

## ✅ Langkah-langkah

1. **Extract** ZIP ini
2. **Copy semua folder** (`navigation/`, `model/`, `viewmodel/`, `screens/`, `components/`, `ui/`) ke:
   ```
   composeApp/src/commonMain/kotlin/org/example/project/
   ```
3. **Ganti `App.kt`** lama dengan `App.kt` yang baru dari ZIP ini
4. **Tambahkan dependency** navigation ke `composeApp/build.gradle.kts`
5. **Sync Gradle** (`File → Sync Project with Gradle Files`)
6. **Run** → pilih target Android

---

## 🗺️ Navigation Flow

```
ModalNavigationDrawer (Bonus ✨)
│
├── Bottom Tab 1: notes → NotesScreen
│     ├── FAB → add_note → AddNoteScreen
│     └── tap card → note_detail/{noteId} → NoteDetailScreen
│                          └── edit → edit_note/{noteId} → EditNoteScreen
│
├── Bottom Tab 2: favorites → FavoritesScreen
│     └── tap card → note_detail/{noteId} → NoteDetailScreen
│
├── Bottom Tab 3: profile → ProfileScreen (dari tugas minggu 4)
│
├── Drawer → settings → SettingsScreen
└── Drawer → about    → AboutScreen
```

---

## ✅ Checklist Tugas

- [x] Bottom Navigation (3 tabs: Notes, Favorites, Profile)
- [x] Note List → Note Detail (passing `noteId` via `NavType.IntType`)
- [x] FAB → Add Note screen
- [x] Back navigation proper (`popBackStack()`)
- [x] Edit Note screen (passing `noteId` sebagai argument)
- [x] **Navigation Drawer BONUS (+10%)**

---
*© 2026 PAM Minggu 5 — IF25-22017 ITERA*
