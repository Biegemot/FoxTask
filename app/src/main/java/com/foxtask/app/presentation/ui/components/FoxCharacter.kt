package com.foxtask.app.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.foxtask.app.R
import com.foxtask.app.data.local.entities.Item
import com.foxtask.app.data.models.FoxOutfit
import com.foxtask.app.data.models.ItemCategory
import com.foxtask.app.presentation.ui.theme.*

@Composable
fun FoxCharacter(
    outfit: FoxOutfit,
    level: Int,
    itemsMap: Map<Int, Item> = emptyMap(),
    modifier: Modifier = Modifier,
    animationProgress: Float = 1f
) {
    val context = LocalContext.current
    val size = 200.dp
    val baseScale = 0.8f + (level * 0.02f).coerceAtMost(0.3f)
    
    // Cache for drawable resource IDs to avoid repeated getIdentifier calls
    val drawableCache = remember { mutableMapOf<Int, Int>() }
    
    fun getDrawableId(item: Item): Int {
        return drawableCache.getOrPut(item.id) {
            context.resources.getIdentifier(item.drawableResName, "drawable", context.packageName)
        }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Базовый лис
        Canvas(modifier = Modifier.matchParentSize()) {
            val scale = baseScale * animationProgress
            val centerX = size.toPx() / 2
            val centerY = size.toPx() / 2 + 20.dp.toPx()

            withTransform({
                translate(centerX, centerY)
                scale(scale, scale, pivot = Offset.Zero)
                translate(-centerX, -centerY)
            }) {
                drawFoxBase(this)
            }
        }

        // Слои аксессуаров
        outfit.hatItemId?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size * 0.8f)
                            .align(Alignment.TopCenter)
                            .offset(y = (-20).dp)
                    )
                }
            }
        }

        // Glasses/Mask
        val faceSlot = if (outfit.glassesItemId != null) outfit.glassesItemId else outfit.maskItemId
        faceSlot?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size * 0.7f)
                            .align(Alignment.Center)
                            .offset(y = (-5).dp)
                    )
                }
            }
        }

        // Cloak
        outfit.cloakItemId?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size * 1.1f)
                            .align(Alignment.Center)
                    )
                }
            }
        }

        // Scarf/Bandana
        val neckSlot = if (outfit.scarfItemId != null) outfit.scarfItemId else outfit.bandanaItemId
        neckSlot?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size * 0.7f)
                            .align(Alignment.Center)
                            .offset(y = 25.dp)
                    )
                }
            }
        }

        // Fur color overlay (simplified: we just change base fox color, no overlay layer currently)
        // In future, could draw a semi-transparent colored rectangle over fox.

        // Background theme
        outfit.backgroundThemeId?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }

        // Maori pattern
        outfit.maoriPatternItemId?.let { itemId ->
            val item = itemsMap[itemId]
            item?.let {
                val drawableId = getDrawableId(it)
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size * 0.5f)
                            .align(Alignment.TopCenter)
                            .offset(y = 30.dp)
                    )
                }
            }
        }
    }
}

fun drawFoxBase(drawScope: DrawScope) {
    val cx = drawScope.size.width / 2
    val cy = drawScope.size.height / 2

    val paint = Paint().apply {
        color = FoxBaseOrange
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    // Body (ellipse)
    drawContext.canvas.drawOval(
        left = cx - 60f,
        top = cy + 20f,
        right = cx + 60f,
        bottom = cy + 80f,
        paint = paint
    )

    // Head (circle)
    drawContext.canvas.drawCircle(
        x = cx,
        y = cy - 20f,
        radius = 45f,
        paint = paint
    )

    // Ears (triangles)
    val earPaint = Paint().apply {
        color = FoxBaseOrange
        style = PaintingStyle.Fill
        isAntiAlias = true
    }
    val path = Path().apply {
        moveTo(cx - 35f, cy - 50f)
        lineTo(cx - 55f, cy - 90f)
        lineTo(cx - 15f, cy - 65f)
        close()
    }
    drawContext.canvas.drawPath(path, earPaint)

    val path2 = Path().apply {
        moveTo(cx + 35f, cy - 50f)
        lineTo(cx + 55f, cy - 90f)
        lineTo(cx + 15f, cy - 65f)
        close()
    }
    drawContext.canvas.drawPath(path2, earPaint)

    // Eyes
    val eyePaint = Paint().apply {
        color = Color.Black
        style = PaintingStyle.Fill
    }
    drawContext.canvas.drawCircle(cx - 15f, cy - 25f, 6f, eyePaint)
    drawContext.canvas.drawCircle(cx + 15f, cy - 25f, 6f, eyePaint)

    // Nose
    drawContext.canvas.drawCircle(cx, cy - 5f, 6f, eyePaint)

    // Nose highlight (white)
    val highlightPaint = Paint().apply {
        color = Color.White
        style = PaintingStyle.Fill
        alpha = 0.5f
    }
    drawContext.canvas.drawCircle(cx - 2f, cy - 7f, 2f, highlightPaint)
}
