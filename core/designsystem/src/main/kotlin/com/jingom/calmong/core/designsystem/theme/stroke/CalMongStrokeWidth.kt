package com.jingom.calmong.core.designsystem.theme.stroke

import androidx.compose.ui.unit.dp

/**
 * Figma `other.json`의 `borderWidth` primitive subset.
 *
 * 화면과 컴포넌트에서는 직접 사용하지 않고 [DefaultCalMongStrokeWidths]의 semantic 역할을 사용한다.
 * 값은 Tailwind borderWidth와 1:1이다(`DEFAULT`=1, `2`, `4`). 현재 semantic이 쓰는 값만 둔다.
 */
internal object CalMongStrokeWidth {
    /** borderWidth/0 */
    val none = 0.dp

    /** borderWidth/DEFAULT */
    val hairline = 1.dp

    /** borderWidth/2 */
    val thin = 2.dp

    /** borderWidth/4 */
    val thick = 4.dp
}
