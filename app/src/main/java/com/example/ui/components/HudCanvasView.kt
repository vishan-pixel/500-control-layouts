package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HudButtonInfo
import kotlin.random.Random

@Composable
fun HudMiniPreview(
    buttons: List<HudButtonInfo>,
    modifier: Modifier = Modifier,
    categoryColor: Color = Color(0xFFA855F7),
    hasLightningEffect: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lightning_anim")
    val lightningPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Box(
        modifier = modifier
            .aspectRatio(16f / 9.5f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0D0A1A))
            .border(
                1.dp,
                if (hasLightningEffect) {
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFFFD700),
                            Color(0xFF22D3EE),
                            Color(0xFFFFD700)
                        )
                    )
                } else {
                    Brush.horizontalGradient(listOf(categoryColor.copy(alpha = 0.6f), Color(0xFF3B2D6B)))
                },
                RoundedCornerShape(10.dp)
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(3.dp)) {
            val canvasW = size.width
            val canvasH = size.height

            // Simulated game background grid
            drawRect(
                color = if (hasLightningEffect) Color(0xFF0B071E) else Color(0xFF140E26),
                size = size
            )

            // Dynamic lightning crackles if VIP lightning layout
            if (hasLightningEffect) {
                val rand = Random((lightningPhase * 100).toInt())
                val path = Path()
                var currentX = canvasW * 0.15f
                var currentY = canvasH * 0.2f
                path.moveTo(currentX, currentY)
                for (step in 1..5) {
                    currentX += (canvasW * 0.15f) + (rand.nextFloat() * 10f - 5f)
                    currentY += (rand.nextFloat() * 20f - 10f)
                    path.lineTo(currentX, currentY.coerceIn(0f, canvasH))
                }
                drawPath(
                    path = path,
                    color = Color(0xFF22D3EE).copy(alpha = 0.7f),
                    style = Stroke(width = 2.5f)
                )
                drawPath(
                    path = path,
                    color = Color(0xFFFFD700).copy(alpha = 0.4f),
                    style = Stroke(width = 4.5f)
                )
            }

            // Draw buttons
            buttons.forEach { btn ->
                val x = btn.xPercent * canvasW
                val y = btn.yPercent * canvasH
                val w = (btn.widthPercent * canvasW).coerceAtLeast(6f)
                val h = (btn.heightPercent * canvasH).coerceAtLeast(6f)

                if (btn.isJoystick) {
                    // Analog Joystick Rendering
                    val stickRadius = (w.coerceAtLeast(h) * 0.5f)
                    val stickCenter = Offset(x + stickRadius, y + stickRadius)
                    // Outer base circle
                    drawCircle(
                        color = if (hasLightningEffect) Color(0xFFFFD700).copy(alpha = 0.35f) else Color(0xFF06B6D4).copy(alpha = 0.35f),
                        radius = stickRadius,
                        center = stickCenter
                    )
                    drawCircle(
                        color = if (hasLightningEffect) Color(0xFFFFD700) else Color(0xFF22D3EE),
                        radius = stickRadius,
                        center = stickCenter,
                        style = Stroke(width = 2f)
                    )
                    // Inner thumb knob
                    drawCircle(
                        color = if (hasLightningEffect) Color(0xFF22D3EE).copy(alpha = 0.85f) else Color(0xFF06B6D4).copy(alpha = 0.85f),
                        radius = stickRadius * 0.45f,
                        center = stickCenter
                    )
                    drawCircle(
                        color = Color.White,
                        radius = stickRadius * 0.45f,
                        center = stickCenter,
                        style = Stroke(width = 1.5f)
                    )
                } else {
                    val btnColor = if (btn.isTwoRoles || btn.isSwipe) {
                        if (hasLightningEffect) Color(0xFFFFD700) else categoryColor
                    } else {
                        Color(btn.colorHex)
                    }

                    // Button body
                    drawRoundRect(
                        color = btnColor.copy(alpha = if (btn.isTwoRoles || btn.isSwipe) 0.85f else 0.45f),
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(3f, 3f)
                    )

                    // Button outline
                    drawRoundRect(
                        color = btnColor.copy(alpha = 0.9f),
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(3f, 3f),
                        style = Stroke(width = if (btn.isTwoRoles || btn.isSwipe) 2f else 1f)
                    )
                }
            }
        }

        // Overlay tag
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (hasLightningEffect) "⚡ VIP LIGHTNING" else "HUD MAP",
                color = if (hasLightningEffect) Color(0xFFFFD700) else categoryColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HudFullDiagram(
    buttons: List<HudButtonInfo>,
    modifier: Modifier = Modifier,
    categoryColor: Color = Color(0xFFA855F7),
    hasLightningEffect: Boolean = false,
    selectedButtonId: String? = null,
    onButtonSelect: ((HudButtonInfo) -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "full_lightning_anim")
    val lightningPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF090614))
            .border(
                1.5.dp,
                if (hasLightningEffect) {
                    Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFF22D3EE), Color(0xFFA855F7)))
                } else {
                    Brush.linearGradient(listOf(categoryColor, Color(0xFF3B2D6B)))
                },
                RoundedCornerShape(14.dp)
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
            val canvasW = size.width
            val canvasH = size.height

            // Screen frame
            drawRoundRect(
                color = if (hasLightningEffect) Color(0xFF0C0721) else Color(0xFF130D29),
                size = size,
                cornerRadius = CornerRadius(10f, 10f)
            )

            // Lightning branch effects
            if (hasLightningEffect) {
                val rand = Random((lightningPhase * 150).toInt())
                for (branch in 1..2) {
                    val path = Path()
                    var startX = if (branch == 1) canvasW * 0.2f else canvasW * 0.6f
                    var startY = if (branch == 1) canvasH * 0.3f else canvasH * 0.6f
                    path.moveTo(startX, startY)
                    for (seg in 1..4) {
                        startX += (rand.nextFloat() * 40f - 20f)
                        startY += (rand.nextFloat() * 30f - 15f)
                        path.lineTo(startX.coerceIn(10f, canvasW - 10f), startY.coerceIn(10f, canvasH - 10f))
                    }
                    drawPath(path, color = Color(0xFF22D3EE).copy(alpha = 0.8f), style = Stroke(width = 2.5f))
                    drawPath(path, color = Color(0xFFFFD700).copy(alpha = 0.4f), style = Stroke(width = 5f))
                }
            }

            // Crosshair in the center
            val cx = canvasW * 0.5f
            val cy = canvasH * 0.45f
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(cx - 8f, cy),
                end = Offset(cx + 8f, cy),
                strokeWidth = 2f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(cx, cy - 8f),
                end = Offset(cx, cy + 8f),
                strokeWidth = 2f
            )

            // Draw all buttons
            buttons.forEach { btn ->
                val x = btn.xPercent * canvasW
                val y = btn.yPercent * canvasH
                val w = btn.widthPercent * canvasW
                val h = btn.heightPercent * canvasH

                val isSelected = btn.id == selectedButtonId
                val btnColor = when {
                    isSelected -> Color.White
                    hasLightningEffect && (btn.isTwoRoles || btn.isJoystick) -> Color(0xFFFFD700)
                    btn.isTwoRoles -> Color(0xFFF43F5E)
                    btn.isSwipe -> Color(0xFFFF66C4)
                    else -> Color(btn.colorHex)
                }

                if (btn.isJoystick) {
                    val stickRadius = (w.coerceAtLeast(h) * 0.5f)
                    val stickCenter = Offset(x + stickRadius, y + stickRadius)
                    drawCircle(
                        color = (if (hasLightningEffect) Color(0xFFFFD700) else Color(0xFF06B6D4)).copy(alpha = 0.35f),
                        radius = stickRadius,
                        center = stickCenter
                    )
                    drawCircle(
                        color = if (isSelected) Color.White else (if (hasLightningEffect) Color(0xFFFFD700) else Color(0xFF22D3EE)),
                        radius = stickRadius,
                        center = stickCenter,
                        style = Stroke(width = if (isSelected) 3.5f else 2f)
                    )
                    drawCircle(
                        color = (if (hasLightningEffect) Color(0xFF22D3EE) else Color(0xFF06B6D4)).copy(alpha = 0.85f),
                        radius = stickRadius * 0.45f,
                        center = stickCenter
                    )
                    drawCircle(
                        color = Color.White,
                        radius = stickRadius * 0.45f,
                        center = stickCenter,
                        style = Stroke(width = 1.5f)
                    )
                } else {
                    drawRoundRect(
                        color = btnColor.copy(alpha = if (btn.isTwoRoles || btn.isSwipe || isSelected) 0.65f else 0.35f),
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    drawRoundRect(
                        color = if (isSelected) Color.White else btnColor,
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(6f, 6f),
                        style = Stroke(width = if (isSelected) 3f else 1.5f)
                    )
                }
            }
        }

        // Overlay Interactive button layers
        buttons.forEach { btn ->
            val isSpecial = btn.isTwoRoles || btn.isSwipe
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = (btn.xPercent * 340).dp,
                            top = (btn.yPercent * 190).dp
                        )
                )
            }
        }

        // Top indicator badge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                .border(0.5.dp, (if (hasLightningEffect) Color(0xFFFFD700) else categoryColor).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (hasLightningEffect) "⚡ VIP LIGHTNING GOD INTERACTIVE HUD" else "INTERACTIVE HUD PHOTO VIEW",
                color = if (hasLightningEffect) Color(0xFFFFD700) else categoryColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
