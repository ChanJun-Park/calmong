package com.jingom.calmong.core.designsystem.theme.spacing

import androidx.compose.ui.unit.dp

/**
 * Figma `spacing.json`에서 semantic spacing에 사용하는 primitive subset.
 *
 * 제품 코드에서는 직접 사용하지 않고 [DefaultCalMongSpacings]의 역할을 사용한다.
 */
internal object CalMongSpacing {
    val none = 0.dp
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp
    val huge = 40.dp
    val massive = 48.dp
    val section = 64.dp
}
