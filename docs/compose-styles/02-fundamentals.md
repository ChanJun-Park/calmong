# 02. Fundamentals of Styles — 기초

> 원문: <https://developer.android.com/develop/ui/compose/styles/fundamentals> · 갱신 2026-06-11

## 적용하는 3가지 방법
1. **컴포넌트의 `Style` 파라미터에 직접** — 컴포넌트가 `style` 파라미터를 노출하는 경우.
2. **`Modifier.styleable { }`** — `style` 파라미터가 없는 레이아웃 컴포저블에 적용.
3. **커스텀 디자인 시스템** — 내 컴포넌트에서 `Modifier.styleable{}`를 쓰고 `style` 파라미터를 노출.

```kotlin
// 1) 컴포넌트 파라미터
BaseButton(onClick = {}, style = { background(Color.Blue) }) { BaseText("Click me") }

// 2) Modifier.styleable
Row(modifier = Modifier.styleable { background(Color.Blue) }) { BaseText("Content") }
```

## 지원 속성 (그룹별)
> Styles는 modifier 속성의 상당수를 지원하지만 **전부는 아니다.** 상호작용·커스텀 드로잉·속성 스태킹은 여전히 modifier 몫.

| 그룹 | 속성 | 자식 상속 |
|---|---|---|
| **레이아웃/크기** | `contentPadding*`(안쪽), `externalPadding*`(바깥), `fillWidth/Height/Size`, `width/height/size`(Dp·DpSize·Float 비율), 위치 `left/top/right/bottom` | ✗ |
| **시각 외형** | `background`/`foreground`(Color·Brush), `borderWidth/borderColor/borderBrush`, `shape`, `dropShadow/innerShadow` | ✗ (단 `shape`은 `clip`·`border`가 함께 사용) |
| **변형** | `translationX/Y`, `scaleX/Y`, `rotationX/Y/Z`, `alpha`, `zIndex`, `transformOrigin` | ✗ |
| **타이포** | `textStyle`, `fontSize`, `fontWeight`, `fontStyle`, `fontFamily` | ✅ |
| **콘텐츠 색** | `contentColor`, `contentBrush` (아이콘 색에도 적용) | ✅ |
| **문단** | `lineHeight`, `letterSpacing`, `textAlign`, `textDirection`, `lineBreak`, `hyphens` | ✅ |
| **장식** | `textDecoration`, `textIndent`, `baselineShift` | ✅ |

> `borderWidth`는 레이아웃에 참여한다(공간 차지 → 측정 크기에 영향).

## 독립 Style 정의 & 재사용
```kotlin
val style = Style { background(Color.Blue) }

BaseButton(onClick = {}, style = style) { BaseText("Button") }        // 파라미터로
val styleState = remember { MutableStyleState(null) }                 // styleable로
Column(Modifier.styleable(styleState, style)) { BaseText("Column") }  // 같은 style 재사용 가능
```

## 핵심 규칙: 속성은 "덮어쓰기"(last-write-wins)
modifier는 **누적(additive)**, Style은 **마지막 설정값이 이김**.
```kotlin
style = {
    background(Color.Red)
    background(TealColor)   // ← 최종 background는 Teal
    contentPadding(64.dp)   // 4방향 64
    contentPaddingTop(16.dp) // top만 16, 나머지는 64 (합산 아님)
}
```

## 여러 Style 합성 — `then`
```kotlin
val style1 = Style { background(Color.Red); contentPadding(32.dp) }
val style2 = Style { contentPaddingHorizontal(8.dp); background(Color.LightGray) }
BaseButton(style = style1 then style2, onClick = {}) { ... }
// 결과: 배경 LightGray, padding 32(단 좌우는 8) — 같은 속성은 뒤가 이김
```

## 상속 (Inheritance)
> 실험 중에는 opt-in 필요: `ComposeFoundationFlags.isInheritedTextStyleEnabled = true`

`contentColor`·텍스트 관련 속성은 자식 컴포저블로 전파된다. 자식이 같은 속성을 설정하면 그 자식에 한해 부모를 덮어쓴다.

### 우선순위 (높음 → 낮음)
| 우선 | 방법 | 예 |
|---|---|---|
| 1 | 컴포저블 직접 인자 | `Text(color = Color.Red)` |
| 2 | `style` 파라미터 | `Text(style = Style { contentColor(Color.Red) })` |
| 3 | modifier 체인 | `Modifier.styleable { contentColor(Color.Red) }` |
| 4 | 부모 스타일 | 상속 가능 속성(타이포/색)을 부모에서 전파 |

## 커스텀 속성 (기존 속성 조합만)
```kotlin
fun StyleScope.outlinedBackground(color: Color) { border(1.dp, color); background(color) }
val s = Style { outlinedBackground(Color.Blue) }
```
> **새 styleable 속성 생성은 미지원**(필요 시 feature request). 기존 속성에 매핑하는 확장만 가능.

## CompositionLocal 읽기 (토큰 접근)
```kotlin
val buttonStyle = Style {
    contentPadding(12.dp)
    shape(RoundedCornerShape(50))
    background(Brush.verticalGradient(LocalCustomColors.currentValue.background))
}
```
> ⚠️ Style **정의 시점**에 CompositionLocal이 읽히는 함정 → [07 Don't: Composition에서 style 만들지 말 것](07-dos-donts.md) 참고. 권장은 `StyleScope` 확장(`val StyleScope.colors get() = Local....currentValue`)으로 접근.

## calmong 메모
- 우리 토큰 매핑: `CalMongTheme.strokeWidths.* → borderWidth`, `shapes.* → shape`, `spacings.inset.* → contentPadding`, `spacings.gap.* → (모서리 아닌 컨테이너는 layout이 담당)`, `colors.*.foreground → contentColor`.
- 상속되는 건 타이포·`contentColor`뿐 — 배경/외곽선/패딩은 컴포넌트마다 명시해야 한다.
