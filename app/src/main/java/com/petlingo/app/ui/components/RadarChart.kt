package com.petlingo.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable fun RadarChart(values: List<Float>, modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxWidth().height(300.dp)) {
        val n = values.size.coerceAtLeast(3); val c = center; val r = size.minDimension * .38f
        fun p(i:Int, scale:Float): Offset { val a = -PI/2 + 2*PI*i/n; return Offset(c.x+(cos(a)*r*scale).toFloat(), c.y+(sin(a)*r*scale).toFloat()) }
        for (level in 1..5) { val path=Path(); repeat(n){i-> val pt=p(i,level/5f); if(i==0) path.moveTo(pt.x,pt.y) else path.lineTo(pt.x,pt.y)}; path.close(); drawPath(path,Color(0xFFB8B8B8),style=Stroke(1f)) }
        repeat(n){ drawLine(Color(0xFFB8B8B8), c, p(it,1f),1f) }
        val data=Path(); values.forEachIndexed { i,v-> val pt=p(i,(v/5f).coerceIn(0f,1f)); if(i==0)data.moveTo(pt.x,pt.y) else data.lineTo(pt.x,pt.y)}; data.close(); drawPath(data,Color(0x997E9360)); drawPath(data,Color(0xFF657A4B),style=Stroke(4f))
    }
}
