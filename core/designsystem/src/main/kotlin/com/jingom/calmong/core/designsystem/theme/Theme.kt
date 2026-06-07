package com.jingom.calmong.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.jingom.calmong.core.designsystem.theme.color.CalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.LocalCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.darkCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.lightCalMongColorScheme

@Composable
fun CalMongTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        remember(darkTheme) {
            if (darkTheme) darkCalMongColorScheme() else lightCalMongColorScheme()
        }
    CompositionLocalProvider(LocalCalMongColorScheme provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme.toMaterialColorScheme(darkTheme),
            content = content,
        )
    }
}

/**
 * calmong semantic 색상 토큰 진입점.
 *
 * `CalMongTheme.colors.primary.background.default` 처럼 접근한다.
 * Material3 컴포넌트는 [CalMongTheme]이 매핑해준 `MaterialTheme.colorScheme`을 그대로 쓰고,
 * calmong 고유 토큰이 필요하면 이 accessor를 쓴다.
 */
object CalMongTheme {
    val colors: CalMongColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongColorScheme.current
}

/**
 * semantic 토큰을 Material3 [androidx.compose.material3.ColorScheme]로 매핑.
 * Material 컴포넌트(Button/Card/Surface 등)가 브랜드 색을 자동으로 따르도록 한다.
 *
 * Material 기본 scheme에서 출발해 우리가 의미를 부여한 role만 override한다(`copy`).
 * 손대지 않은 surfaceContainer* 등은 Material 기본값을 그대로 둔다.
 */
private fun CalMongColorScheme.toMaterialColorScheme(darkTheme: Boolean) =
    (if (darkTheme) darkColorScheme() else lightColorScheme()).copy(
        primary = primary.background.default,
        onPrimary = primary.foreground.onColor,
        primaryContainer = primary.background.subtle,
        onPrimaryContainer = primary.foreground.default,
        inversePrimary = primary.background.bold,
        secondary = secondary.background.default,
        onSecondary = secondary.foreground.onColor,
        secondaryContainer = secondary.background.subtle,
        onSecondaryContainer = secondary.foreground.default,
        tertiary = secondary.background.default,
        onTertiary = secondary.foreground.onColor,
        tertiaryContainer = secondary.background.subtle,
        onTertiaryContainer = secondary.foreground.default,
        background = neutral.background.base,
        onBackground = neutral.foreground.default,
        surface = neutral.background.default,
        onSurface = neutral.foreground.default,
        surfaceVariant = neutral.background.raised1,
        onSurfaceVariant = neutral.foreground.subtle,
        surfaceTint = primary.background.default,
        inverseSurface = neutral.background.inverted,
        inverseOnSurface = neutral.foreground.inverted,
        error = functional.common.negative.default,
        onError = functional.common.negative.onColor,
        errorContainer = functional.common.negative.subtle,
        onErrorContainer = functional.common.negative.default,
        outline = neutral.stroke.default.default,
        outlineVariant = neutral.stroke.subtle.default,
        scrim = functional.general.overlay,
    )
