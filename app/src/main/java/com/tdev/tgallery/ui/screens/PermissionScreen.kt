package com.tdev.tgallery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tdev.tgallery.ui.theme.Bg
import com.tdev.tgallery.ui.theme.TextDim
import com.tdev.tgallery.ui.theme.TextPrimary

@Composable
fun PermissionScreen(onRequest: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {
            Text(
                text = "TGallery",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Fotoğraf ve videolarını görmek için depolama iznine ihtiyaç var.",
                color = TextDim,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable { onRequest() }
                    .padding(horizontal = 32.dp, vertical = 12.dp)
            ) {
                Text("İzin Ver", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
