package org.example.project.model

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val category: NoteCategory = NoteCategory.PERSONAL,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val color: NoteColor = NoteColor.DEFAULT
)

enum class NoteCategory(val label: String) {
    PERSONAL("Personal"),
    WORK("Work"),
    IDEAS("Ideas"),
    IMPORTANT("Important")
}

enum class NoteColor(val hex: Long) {
    DEFAULT(0xFFFFFFFF),
    YELLOW(0xFFFFF9C4),
    GREEN(0xFFE8F5E9),
    BLUE(0xFFE3F2FD),
    PINK(0xFFFCE4EC),
    PURPLE(0xFFF3E5F5)
}
