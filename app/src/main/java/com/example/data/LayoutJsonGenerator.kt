package com.example.data

import com.example.model.HudButtonInfo

object LayoutJsonGenerator {

    /**
     * Generates a fully valid PojavLauncher / Mojo Launcher / Zalith Launcher controlmap JSON.
     * Uses the standard format expected by PojavLauncher v3/v4 and Mojo control loader.
     */
    fun generatePojavControlJson(
        layoutName: String,
        fileName: String,
        category: String,
        buttons: List<HudButtonInfo>,
        version: String = "1.21.x",
        macroInfo: String = ""
    ): String {
        val buttonsJson = buttons.joinToString(",\n") { btn ->
            val keycodesArray = if (btn.keycodes.isNotEmpty()) {
                btn.keycodes.joinToString(prefix = "[", postfix = "]")
            } else {
                "[-100]"
            }
            """    {
      "name": "${btn.name}",
      "keycodes": $keycodesArray,
      "x": ${"%.1f".format(btn.xPercent * 1000)},
      "y": ${"%.1f".format(btn.yPercent * 600)},
      "width": ${"%.1f".format(btn.widthPercent * 1000)},
      "height": ${"%.1f".format(btn.heightPercent * 600)},
      "opacity": 0.85,
      "strokeWidth": 2.0,
      "colorHex": "${"#%08X".format(btn.colorHex)}",
      "isSwipeable": ${btn.isSwipe},
      "isTwoRoles": ${btn.isTwoRoles},
      "isJoystick": ${btn.isJoystick},
      "type": "${if (btn.isJoystick) "joystick" else "button"}",
      "roleDescription": "${btn.roleDesc.replace("\"", "\\\"")}"
    }"""
        }

        return """{
  "formatVersion": 6,
  "name": "$layoutName",
  "fileName": "$fileName",
  "category": "$category",
  "gameVersion": "$version",
  "targetLaunchers": ["PojavLauncher", "Mojo Launcher", "Zalith Launcher"],
  "author": "Not Altino Community (@altino_b4b | Discord: Not_Altino)",
  "macroInfo": "${macroInfo.replace("\"", "\\\"")}",
  "controlDataList": [
$buttonsJson
  ]
}"""
    }
}
