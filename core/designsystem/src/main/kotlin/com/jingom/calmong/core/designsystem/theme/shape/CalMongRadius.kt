package com.jingom.calmong.core.designsystem.theme.shape

import androidx.compose.ui.unit.dp

/**
 * Figma `radius.json`을 1:1로 옮긴 primitive radius scale.
 *
 * 화면과 컴포넌트에서는 직접 사용하지 않고 [DefaultCalMongShapes]의 semantic 역할을 사용한다.
 */
internal object CalMongRadius {
    val none = 0.dp
    val sm = 2.dp
    val base = 4.dp
    val md = 6.dp
    val lg = 8.dp
    val xl = 12.dp
    val xxl = 16.dp
    val xxxl = 24.dp
    val full = 999.dp
}
