@file:Suppress("UnusedPrivateMember")
@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "CalMongIconButton - Light", showBackground = true)
@Preview(name = "CalMongIconButton - Dark", showBackground = true, uiMode = 0x20)
@Composable
private fun CalMongIconButtonPreview() {
    ComposeFoundationFlags.isInheritedTextStyleEnabled = true
    CalMongTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // intent 5종
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CalMongButtonIntent.entries.forEach { intent ->
                    CalMongIconButton(
                        onClick = {},
                        imageVector = Icons.Filled.Add,
                        contentDescription = "추가",
                        intent = intent,
                    )
                }
            }
            // 비활성
            CalMongIconButton(
                onClick = {},
                imageVector = Icons.Filled.Add,
                contentDescription = "추가",
                enabled = false,
            )
        }
    }
}
