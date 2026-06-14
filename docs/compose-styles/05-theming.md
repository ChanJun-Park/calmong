# 05. Theming with Styles — 테밍

> 원문: <https://developer.android.com/develop/ui/compose/styles/theming> · 갱신 2026-06-11
> ⚠️ `@Experimental` — Material 지원은 추후. **calmong에 가장 직접 관련된 문서.**

## 적용 경로 (앱 위치에 따라)
1. **완전 커스텀 디자인 시스템(비 Material)** → *권장*: 테마 값을 소비하는 component style을 정의하고, 디자인 시스템 컴포넌트에 `style` 파라미터를 노출.
2. **Material 사용** → *권장*: Material의 Styles 통합을 기다리되, 내 컴포넌트에는 가능한 곳에 Style 적용.

## Style 레이어 (새 추상화 층)
서브시스템과 컴포넌트 사이의 다리 = **Styles**.

| 레이어 | 책임 | 예 |
|---|---|---|
| 서브시스템 값 | 이름 붙은 값 | `val Primary = Color(0xFF34A85E)` |
| **Atomic Styles** | 정확히 한 속성만 바꾸는 Style | `val largeSize = Style { size(100.dp, 40.dp) }` |
| **Component Styles** | 컴포넌트별 설정 묶음 | `Style { contentPadding(16.dp); shape(...); background(...) }` |
| 컴포넌트 | Style을 소비하는 실제 UI | `Button(style = buttonStyle) { ... }` |

## Atomic vs Monolithic
작은 단일 목적 스타일("원자")로 쪼갠 뒤 `then`으로 합성.
```kotlin
val paddingAtomic = Style { contentPadding(16.dp) }
val roundedAtomic = Style { shape(RoundedCornerShape(8.dp)) }
val primaryBgAtomic = Style { background(Color.Blue) }
// 합성
val buttonStyle = paddingAtomic then roundedAtomic then primaryBgAtomic
```

## 커스텀 디자인 시스템에 적용 (Jetsnack 패턴 — 우리와 동일 골격)
**1) Style 모음 객체 + 기본값**
```kotlin
object JetsnackStyles {
    val buttonStyle: Style = Style {
        shape(shapes.medium)
        background(colors.brand)
        contentColor(colors.textPrimary)
        contentPaddingVertical(8.dp)
        contentPaddingHorizontal(24.dp)
        textStyle(typography.labelLarge)
        disabled { animate { background(colors.brandSecondary) } }
    }
    val cardStyle: Style = Style {
        shape(shapes.medium); background(colors.uiBackground); contentColor(colors.textPrimary)
    }
}
```
**2) 테마에 제공 + `StyleScope` 확장으로 서브시스템 접근**
```kotlin
@Immutable
class JetsnackTheme(val colors: JetsnackColors = LightJetsnackColors, ...) {
    companion object {
        val colors: JetsnackColors @Composable @ReadOnlyComposable get() = LocalJetsnackTheme.current.colors
        val styles: JetsnackStyles = JetsnackStyles
        // ...
    }
}
// ★ StyleScope 안에서 토큰에 접근하는 확장 (CompositionLocal 함정 회피)
val StyleScope.colors: JetsnackColors get() = LocalJetsnackTheme.currentValue.colors
val StyleScope.shapes: Shapes        get() = LocalJetsnackTheme.currentValue.shapes
```
**3) 컴포넌트에서 소비**
```kotlin
Box(modifier = modifier
    .clickable(interactionSource, indication = null, role = Role.Button) { ... }
    .styleable(styleState, JetsnackTheme.styles.buttonStyle, style)  // 기본 then 들어온 style
) { Text(text) }
```

## 그 밖
- 전역 테마 외에 **인라인** 또는 **static 정의**로도 사용 가능.
- **조건부로 Style 객체를 통째 스왑하지 말 것** — 스타일이 근본적으로 다를 때만. 평소엔 *시각 정의 안에서 동적 토큰을 읽어라*(스왑 대신 토큰 접근). → 단, 화이트라벨처럼 *근본적으로 다른* 테마면 통째 교체 OK([07](07-dos-donts.md)).

## calmong 메모 (핵심)
- **`CalMongTheme`은 이미 이 패턴이다**: `@Immutable` 클래스 + companion `@ReadOnlyComposable` accessor(`colors/shapes/elevations/spacings/strokeWidths/layout`) + `Local*` CompositionLocal + `CalMongTheme { }` provider. → Jetsnack 구조와 1:1.
- 안정화 시 도입 스케치:
  1. `theme/style/CalMongStyles.kt` — `object CalMongStyles { val toast/button/card: Style }`.
  2. `val StyleScope.colors get() = LocalCalMongColorScheme.currentValue` 등 **StyleScope 확장** 추가(우리 `Local*`를 그대로 사용).
  3. `CalMongTheme.companion`에 `val styles` 노출.
- 토큰→Style 매핑이 자연스럽다: `colors.* / shapes.* / strokeWidths.* / spacings.*` 그대로 Style 블록에서 호출.
