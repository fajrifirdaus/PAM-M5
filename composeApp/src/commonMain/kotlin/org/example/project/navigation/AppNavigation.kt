package org.example.project.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.example.project.screens.*
import org.example.project.viewmodel.NotesViewModel
import org.example.project.viewmodel.ProfileViewModel

// ═══════════════════════════════════════════════════
// APP NAVIGATION — Root Navigation Component
// Menggunakan NavHost + NavController (Jetpack Compose)
// Fitur: Bottom Navigation + Navigation Drawer
// ═══════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    notesViewModel: NotesViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    // ── 1. NavController — GPS navigasi ──
    val navController = rememberNavController()

    // ── 2. State ──
    val notesUiState by notesViewModel.uiState.collectAsStateWithLifecycle()
    val isDarkMode = notesUiState.isDarkMode

    // Current route untuk selected state
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom nav items
    val bottomNavItems = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    // Routes yang menampilkan bottom bar & top bar utama
    val bottomNavRoutes = bottomNavItems.map { it.screen.route }
    val showBottomBar = currentRoute in bottomNavRoutes

    // ── 3. Drawer state ──
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ── 4. Snackbar ──
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(notesUiState.snackbarMessage) {
        notesUiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            notesViewModel.clearSnackbar()
        }
    }

    // ══════════════════════════════════════════
    // NAVIGATION DRAWER — Bonus: Side menu
    // ModalNavigationDrawer wraps everything
    // ══════════════════════════════════════════
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBottomBar, // hanya aktif di main screens
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                // Drawer header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Column {
                        Icon(
                            Icons.Rounded.NoteAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Notes App",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "PAM Minggu 5 — IF25-22017",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))

                // Main navigation items
                Text(
                    "MENU UTAMA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                bottomNavItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontWeight = FontWeight.SemiBold) },
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))

                Text(
                    "LAINNYA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                // Settings
                NavigationDrawerItem(
                    icon = { Icon(Icons.Rounded.Settings, contentDescription = "Pengaturan") },
                    label = { Text("Pengaturan", fontWeight = FontWeight.SemiBold) },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                // About
                NavigationDrawerItem(
                    icon = { Icon(Icons.Rounded.Info, contentDescription = "Tentang") },
                    label = { Text("Tentang Aplikasi", fontWeight = FontWeight.SemiBold) },
                    selected = currentRoute == Screen.About.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.About.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                Spacer(Modifier.weight(1f))

                // Dark mode toggle at drawer bottom
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isDarkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            if (isDarkMode) "Dark Mode" else "Light Mode",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { notesViewModel.toggleDarkMode() }
                    )
                }
            }
        }
    ) {
        // ══════════════════════════════════════════
        // SCAFFOLD — Main content area
        // ══════════════════════════════════════════
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                // TopAppBar hanya untuk main screens (bottom nav tabs)
                if (showBottomBar) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentRoute) {
                                    Screen.Notes.route -> "📝 Catatan Saya"
                                    Screen.Favorites.route -> "🔖 Favorit"
                                    Screen.Profile.route -> "👤 Profil"
                                    else -> "Notes App"
                                },
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            )
                        },
                        navigationIcon = {
                            // Hamburger menu untuk buka drawer
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "Buka Menu",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        actions = {
                            // Dark mode toggle di top bar
                            IconButton(onClick = { notesViewModel.toggleDarkMode() }) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Rounded.WbSunny
                                    else Icons.Rounded.NightsStay,
                                    contentDescription = "Toggle Dark Mode",
                                    tint = if (isDarkMode) Color(0xFFFFD600)
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            },
            bottomBar = {
                // ── BOTTOM NAVIGATION BAR ──
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.screen.route,
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        // popUpTo agar back stack tidak menumpuk
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        // Hindari duplikat di top stack
                                        launchSingleTop = true
                                        // Restore state saat kembali ke tab
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        item.label,
                                        fontWeight = if (currentRoute == item.screen.route)
                                            FontWeight.ExtraBold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->

            // ══════════════════════════════════════════
            // NAV HOST — Semua destinations terdaftar di sini
            // ══════════════════════════════════════════
            NavHost(
                navController = navController,
                startDestination = Screen.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                // ── Bottom Nav Tab 1: Notes ──
                composable(Screen.Notes.route) {
                    NotesScreen(
                        viewModel = notesViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddNote = {
                            navController.navigate(Screen.AddNote.route)
                        }
                    )
                }

                // ── Bottom Nav Tab 2: Favorites ──
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = notesViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        }
                    )
                }

                // ── Bottom Nav Tab 3: Profile ──
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        isDarkMode = isDarkMode
                    )
                }

                // ── Note Detail — Menerima noteId (Int) sebagai argument ──
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    // Ambil argument dari backStackEntry
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                    NoteDetailScreen(
                        noteId = noteId,
                        viewModel = notesViewModel,
                        onBack = { navController.popBackStack() },
                        onEdit = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        }
                    )
                }

                // ── Add Note — Navigate dari FAB ──
                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        viewModel = notesViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Edit Note — Menerima noteId (Int) sebagai argument ──
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    // Ambil argument dari backStackEntry
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                    EditNoteScreen(
                        noteId = noteId,
                        viewModel = notesViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Settings — Accessible via Drawer ──
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { notesViewModel.toggleDarkMode() },
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── About — Accessible via Drawer ──
                composable(Screen.About.route) {
                    AboutScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
