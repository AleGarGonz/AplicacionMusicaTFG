package com.example.aplicacionmusicatfg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Footer(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth().background(Color(0xFFB8DAD9))) {

        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))
        SignUp()
        Spacer(modifier = Modifier.size(24.dp))

    }

}

@Composable
fun SignUp() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "New?",
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF000000)
        )
        Text(
            text = "\tSign up.",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )
    }

}