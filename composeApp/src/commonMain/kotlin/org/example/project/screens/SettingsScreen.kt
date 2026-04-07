package org.example.project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════
// SETTINGS SCREEN — Accessible via Navigation Drawer
// ═══════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Appearance section
            SettingsSectionTitle("Tampilan")

            SettingsItem(
                title = "Mode Gelap",
                subtitle = if (isDarkMode) "Aktif" else "Nonaktif",
                icon = if (isDarkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                trailing = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )
                }
            )

            Spacer(Modifier.height(8.dp))

            // App section
            SettingsSectionTitle("Aplikasi")

            SettingsItem(
                title = "Versi Aplikasi",
                subtitle = "1.0.0",
                icon = Icons.Rounded.Info
            )

            SettingsItem(
                title = "Pengembang",
                subtitle = "Muhammad Fajri Firdaus",
                icon = Icons.Rounded.Person
            )

            SettingsItem(
                title = "NIM",
                subtitle = "123140050",
                icon = Icons.Rounded.Numbers
            )

            SettingsItem(
                title = "Mata Kuliah",
                subtitle = "IF25-22017 Pengembangan Aplikasi Mobile",
                icon = Icons.Rounded.School
            )

            Spacer(Modifier.height(8.dp))

            // Navigation info section
            SettingsSectionTitle("Navigasi")

            SettingsItem(
                title = "Bottom Navigation",
                subtitle = "3 tabs: Notes, Favorites, Profile",
                icon = Icons.Rounded.ViewQuilt
            )

            SettingsItem(
                title = "Navigation Drawer",
                subtitle = "Geser dari kiri atau tap ikon menu",
                icon = Icons.Rounded.Menu
            )

            SettingsItem(
                title = "Back Navigation",
                subtitle = "Tombol back di setiap screen",
                icon = Icons.Rounded.ArrowBack
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    )
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            trailing?.invoke()
        }
    }
}
