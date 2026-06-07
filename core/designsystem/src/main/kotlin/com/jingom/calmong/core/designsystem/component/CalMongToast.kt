package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jingom.calmong.core.designsystem.theme.CalMongTheme
import com.jingom.calmong.core.designsystem.theme.color.FunctionalVariant
import com.jingom.calmong.core.designsystem.theme.elevation.elevationShadow

/**
 * 앱 공통 토스트. 짧은 상태 메시지를 화면 위에 띄운다.
 *
 * 3개의 독립 스타일을 [CalMongToastStyle]로 구분한다(정보 제공/에러/경고).
 * 각 스타일은 `functional.common`의 의미색(subtle 배경 + default 아이콘)과 상태 아이콘으로 구별된다.
 * 앱 브랜드(런처) 아이콘은 싣지 않는다 — 구별은 상태 아이콘과 의미색이 담당.
 *
 * stateless 표시 전용 컴포넌트다. 노출 타이밍/위치/dismiss는 호출부(presentation)가 책임진다.
 *
 * @param message 표시할 메시지. 호출부에서 `stringResource(...)`로 만들어 넘긴다.
 * @param style 토스트 스타일(정보/에러/경고).
 */
@Composable
fun CalMongToast(
    message: String,
    style: CalMongToastStyle,
    modifier: Modifier = Modifier,
) {
    val palette = style.toastPalette()
    val shape = CalMongTheme.shapes.surface.small
    val spacings = CalMongTheme.spacings

    Row(
        modifier =
            modifier
                .elevationShadow(CalMongTheme.elevations.overlay, shape)
                .background(palette.container, shape)
                .border(CalMongTheme.strokeWidths.emphasis, palette.border, shape)
                .padding(horizontal = spacings.inset.default, vertical = spacings.inset.compact),
        horizontalArrangement = Arrangement.spacedBy(spacings.gap.compact),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = style.icon,
            // 메시지 텍스트가 의미를 전달하므로 아이콘은 장식(중복 단서)으로 둔다.
            contentDescription = null,
            tint = palette.icon,
        )
        Text(
            text = message,
            color = palette.text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

/** 토스트 스타일. 각 값은 의미색·상태 아이콘이 다른 독립 스타일이다. */
enum class CalMongToastStyle {
    /** 정보 제공 — informative(파랑). */
    Info,

    /** 에러 — negative(빨강). */
    Error,

    /** 경고 — attention(앰버). */
    Warning,
}

private val CalMongToastStyle.icon: ImageVector
    get() =
        when (this) {
            CalMongToastStyle.Info -> CalMongToastInfoIcon
            CalMongToastStyle.Error -> CalMongToastErrorIcon
            CalMongToastStyle.Warning -> CalMongToastWarningIcon
        }

/** 토스트 한 벌에 쓰이는 색 묶음. subtle 배경 + default 아이콘/외곽선 + 본문 텍스트(대비 확보). */
private data class ToastPalette(
    val container: Color,
    val border: Color,
    val icon: Color,
    val text: Color,
)

@Composable
@ReadOnlyComposable
private fun CalMongToastStyle.toastPalette(): ToastPalette {
    val colors = CalMongTheme.colors
    val variant: FunctionalVariant =
        when (this) {
            CalMongToastStyle.Info -> colors.functional.common.informative
            CalMongToastStyle.Error -> colors.functional.common.negative
            CalMongToastStyle.Warning -> colors.functional.common.attention
        }
    return ToastPalette(
        container = variant.subtle,
        border = variant.default,
        icon = variant.default,
        text = colors.neutral.foreground.default,
    )
}
