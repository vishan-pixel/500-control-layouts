package com.example.model

enum class LayoutCategory(
    val id: String,
    val displayName: String,
    val iconEmoji: String,
    val colorHex: Long
) {
    ALL("all", "All Layouts", "🔥", 0xFFA855F7),
    JOYSTICK("joystick", "Joystick Controls", "🕹️", 0xFF06B6D4),
    CRYSTAL("crystal", "Crystal PvP", "💎", 0xFFF43F5E),
    MACE("mace", "Mace 1.21+", "🔨", 0xFFFBBF24),
    YOUTUBER("youtuber", "YouTubers", "🌟", 0xFFEF4444),
    ANCHOR("anchor", "Anchor PvP", "⚓", 0xFF3B82F6),
    CART("cart", "Cart PvP", "🛒", 0xFFFB923C),
    MACRO_SWIPE("macro", "1-Button Macros", "⚡", 0xFFA855F7),
    SWORD_AXE("sword_axe", "Sword & Axe", "⚔️", 0xFF10B981),
    LEGACY_189("legacy", "1.8.9 Legacy", "🏹", 0xFF8B5CF6),
    VIP("vip", "VIP God Controls", "👑", 0xFFFFD700),
    MY_UPLOADS("my_uploads", "My Controls", "📁", 0xFF34D399);

    companion object {
        fun fromId(id: String): LayoutCategory = entries.firstOrNull { it.id == id } ?: ALL
    }
}

enum class LauncherType(val displayName: String, val shortName: String) {
    ALL("All Launchers", "All"),
    POJAV("PojavLauncher", "Pojav"),
    MOJO("Mojo Launcher", "Mojo"),
    ZALITH("Zalith Launcher", "Zalith"),
    HOLY("Holy Launcher", "Holy")
}

enum class FingerStyle(val label: String) {
    ALL("All Grips"),
    TWO_FINGER("2-Finger"),
    THREE_FINGER("3-Finger Claw"),
    FOUR_FINGER("4-Finger Pro"),
    FIVE_FINGER("5-Finger God"),
    TABLET("Tablet Claw")
}

data class HudButtonInfo(
    val id: String,
    val name: String,
    val xPercent: Float, // 0.0 to 1.0 (relative to landscape screen)
    val yPercent: Float, // 0.0 to 1.0
    val widthPercent: Float,
    val heightPercent: Float,
    val colorHex: Long,
    val keycodes: List<Int>,
    val isSwipe: Boolean = false,
    val isTwoRoles: Boolean = false,
    val roleDesc: String = "",
    val isJoystick: Boolean = false
)

data class ControlLayout(
    val id: String,
    val fileName: String, // e.g. "crystal_1.json", "crystal_34.json"
    val name: String,
    val category: LayoutCategory,
    val targetLaunchers: List<LauncherType>,
    val gameVersions: List<String>,
    val fingerStyle: FingerStyle,
    val description: String,
    val macroGuide: String,
    val author: String,
    val channelInfo: String? = null,
    val rating: Float = 4.9f,
    val downloadsCount: Int = 1200,
    val tags: List<String> = emptyList(),
    val previewButtons: List<HudButtonInfo> = emptyList(),
    val jsonContent: String = "",
    val isFlagship: Boolean = false,
    val hasJoystick: Boolean = false,
    val isUserUploaded: Boolean = false,
    val isVip: Boolean = false,
    val hasLightningEffect: Boolean = false
)

data class CreatorProfile(
    val name: String,
    val handle: String,
    val platform: String,
    val discord: String,
    val subscribers: String,
    val description: String,
    val isFeaturedOwner: Boolean = false
)
