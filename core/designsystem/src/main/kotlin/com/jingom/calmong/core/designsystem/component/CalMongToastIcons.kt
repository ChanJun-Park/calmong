package com.jingom.calmong.core.designsystem.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/*
 * 토스트 상태 아이콘(info/error/warning)을 디자인 시스템에 자체 내장한다.
 * material-icons 라이브러리(deprecation 경로)에 의존하지 않고 :core:designsystem을 자기완결로 둔다.
 * path data는 표준 Material Symbols(filled, 24dp viewport)와 동일하다.
 * fill은 placeholder(검정)이며 실제 색은 사용처의 Icon(tint = …)이 덮는다.
 */

private const val ICON_SIZE_DP = 24
private const val ICON_VIEWPORT = 24f

private const val INFO_PATH =
    "M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2z" +
        "m1,15h-2v-6h2v6zm0,-8h-2V7h2v2z"

private const val ERROR_PATH =
    "M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2z" +
        "m1,15h-2v-2h2v2zm0,-4h-2V7h2v6z"

private const val WARNING_PATH = "M1,21h22L12,2 1,21zm12,-3h-2v-2h2v2zm0,-4h-2v-4h2v4z"

internal val CalMongToastInfoIcon: ImageVector by lazy { statusIcon("CalMongToastInfo", INFO_PATH) }

internal val CalMongToastErrorIcon: ImageVector by lazy { statusIcon("CalMongToastError", ERROR_PATH) }

internal val CalMongToastWarningIcon: ImageVector by lazy { statusIcon("CalMongToastWarning", WARNING_PATH) }

private fun statusIcon(
    name: String,
    pathData: String,
): ImageVector =
    ImageVector
        .Builder(
            name = name,
            defaultWidth = ICON_SIZE_DP.dp,
            defaultHeight = ICON_SIZE_DP.dp,
            viewportWidth = ICON_VIEWPORT,
            viewportHeight = ICON_VIEWPORT,
        ).apply {
            addPath(
                pathData = PathParser().parsePathString(pathData).toNodes(),
                fill = SolidColor(Color.Black),
            )
        }.build()
