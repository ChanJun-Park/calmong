@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.theme.style

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.StyleScope
import com.jingom.calmong.core.designsystem.theme.color.CalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.LocalCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.elevation.CalMongElevations
import com.jingom.calmong.core.designsystem.theme.elevation.LocalCalMongElevations
import com.jingom.calmong.core.designsystem.theme.layout.CalMongLayout
import com.jingom.calmong.core.designsystem.theme.layout.LocalCalMongLayout
import com.jingom.calmong.core.designsystem.theme.shape.CalMongShapes
import com.jingom.calmong.core.designsystem.theme.shape.LocalCalMongShapes
import com.jingom.calmong.core.designsystem.theme.spacing.CalMongSpacings
import com.jingom.calmong.core.designsystem.theme.spacing.LocalCalMongSpacings
import com.jingom.calmong.core.designsystem.theme.stroke.CalMongStrokeWidths
import com.jingom.calmong.core.designsystem.theme.stroke.LocalCalMongStrokeWidths

/**
 * Style 레시피가 [androidx.compose.foundation.style.Style] 블록 안에서 CalMong 디자인 토큰에 접근하는 다리.
 *
 * Style의 [StyleScope]는 `CompositionLocalAccessorScope`라 `CompositionLocal.currentValue`로
 * 토큰을 읽을 수 있다. **반드시 이 확장으로 접근**하고, `@Composable` 함수가 토큰을 읽어 Style을
 * 반환하는 패턴은 쓰지 않는다 — CompositionLocal은 Style *정의 시점*에 읽히므로, 그 방식은
 * 라이트/다크 전환 시 값이 굳어버린다.
 *
 * 즉 웹의 CSS Custom Property(`var(--token)`)를 Style 안에서 읽는 것과 같은 역할이다.
 */
val StyleScope.colors: CalMongColorScheme
    get() = LocalCalMongColorScheme.currentValue

val StyleScope.shapes: CalMongShapes
    get() = LocalCalMongShapes.currentValue

val StyleScope.spacings: CalMongSpacings
    get() = LocalCalMongSpacings.currentValue

val StyleScope.strokeWidths: CalMongStrokeWidths
    get() = LocalCalMongStrokeWidths.currentValue

val StyleScope.elevations: CalMongElevations
    get() = LocalCalMongElevations.currentValue

val StyleScope.layout: CalMongLayout
    get() = LocalCalMongLayout.currentValue
