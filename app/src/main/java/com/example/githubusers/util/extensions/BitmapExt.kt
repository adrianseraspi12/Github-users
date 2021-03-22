package com.example.githubusers.util.extensions

import android.graphics.*

fun Bitmap.invertColor(): Bitmap {
    val colorTransform = floatArrayOf(
        -1.0f, 0f, 0f, 0f, 255f,
        0f, -1.0f, 0f, 0f, 255f,
        0f, 0f, -1.0f, 0f, 255f,
        0f, 0f, 0f, 1.0f, 0f)

    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0.5f)

    colorMatrix.set(colorTransform)

    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    val paint = Paint()
    paint.colorFilter = colorFilter

    val resultBitmap = Bitmap.createBitmap(this)

    val canvas = Canvas(resultBitmap)
    canvas.drawBitmap(resultBitmap, 0f, 0f, paint)

    return resultBitmap
}