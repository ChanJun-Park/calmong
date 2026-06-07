@file:Suppress("UnusedPrivateMember")

package com.jingom.calmong.core.designsystem.theme.spacing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "Spacing tokens", showBackground = true)
@Composable
private fun SpacingTokensPreview() {
    CalMongTheme {
        val spacing = CalMongTheme.spacings

        Surface(color = CalMongTheme.colors.neutral.background.base) {
            Column(
                modifier = Modifier.padding(spacing.inset.default),
                verticalArrangement = Arrangement.spacedBy(spacing.section.related),
            ) {
                SpacingRow(
                    label = "gap",
                    values =
                        listOf(
                            spacing.gap.tight,
                            spacing.gap.compact,
                            spacing.gap.default,
                            spacing.gap.relaxed,
                            spacing.gap.loose,
                        ),
                )
                SpacingRow(
                    label = "inset",
                    values =
                        listOf(
                            spacing.inset.compact,
                            spacing.inset.default,
                            spacing.inset.comfortable,
                            spacing.inset.spacious,
                        ),
                )
                SpacingRow(
                    label = "section",
                    values =
                        listOf(
                            spacing.section.related,
                            spacing.section.default,
                            spacing.section.distinct,
                            spacing.section.page,
                        ),
                )
            }
        }
    }
}

@Composable
private fun SpacingRow(
    label: String,
    values: List<Dp>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(CalMongTheme.spacings.gap.compact)) {
        Text(
            text = label,
            color = CalMongTheme.colors.neutral.foreground.default,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(CalMongTheme.spacings.gap.default)) {
            values.forEach { value ->
                Column(verticalArrangement = Arrangement.spacedBy(CalMongTheme.spacings.gap.tight)) {
                    Box(
                        modifier =
                            Modifier
                                .width(value)
                                .height(24.dp)
                                .background(
                                    color = CalMongTheme.colors.primary.background.default,
                                    shape = CalMongTheme.shapes.control.compact,
                                ),
                    )
                    Text(
                        text = value.toString(),
                        color = CalMongTheme.colors.neutral.foreground.subtle,
                    )
                }
            }
        }
    }
}
