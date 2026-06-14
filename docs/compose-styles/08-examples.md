# 08. Examples with Styles — 예제

> 원문: <https://developer.android.com/develop/ui/compose/styles/examples> · 갱신 2026-06-11
> ⚠️ 예제는 설명용이라 테마에서 값을 끌어오지 않는다. 실제 프로젝트에선 색·shape·타이포를 테마 클래스로 추출할 것([05](05-theming.md)).

## Base Button (다른 버튼들의 토대)
```kotlin
@Composable
fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val styleState = rememberUpdatedStyleState(interactionSource) { it.isEnabled = enabled }
    Row(
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(enabled = enabled, onClick = onClick,
                       interactionSource = interactionSource, indication = null)
            .styleable(styleState, baseButtonStyle, style),
        content = content,
        verticalAlignment = Alignment.CenterVertically,
    )
}
```

## 1) Hover 시 그림자 이동 버튼
```kotlin
BaseButton(onClick = {}, style = Style {
    background(Color.Transparent); shape(RoundedCornerShape(0.dp)); border(1.dp, Color.Black)
    contentColor(Color.Black); fontSize(16.sp); fontWeight(FontWeight.Light); letterSpacing(1.sp)
    contentPadding(vertical = 13.dp, horizontal = 20.dp)
    dropShadow(Shadow(spread = 0.dp, color = Color(0xFFFFE54C), radius = 0.dp, offset = DpOffset(7.dp, 7.dp)))
    hovered { animate(tween(200)) { dropShadow(Shadow(spread = 0.dp, color = Color(0xFFFFE54C), radius = 0.dp, offset = DpOffset(0.dp, 0.dp))) } }
    pressed { animate(tween(200)) { dropShadow(Shadow(spread = 0.dp, color = Color(0xFFFFE54C), radius = 0.dp, offset = DpOffset(0.dp, 0.dp))) } }
}) { BaseText("Button 52") }
```

## 2) 눌림 깊이 효과 (그림자 + translation)
```kotlin
val buttonStyle = Style {
    background(Color(0xFFFBEED0)); border(2.dp, Color(0xFF422800)); shape(RoundedCornerShape(30.dp))
    dropShadow(Shadow(color = Color(0xFF422800), offset = DpOffset(4.dp, 4.dp), radius = 0.dp, spread = 0.dp))
    contentColor(Color(0xFF422800)); fontWeight(FontWeight.SemiBold); fontSize(18.sp)
    contentPaddingHorizontal(25.dp); externalPadding(8.dp); height(50.dp); textAlign(TextAlign.Center)
    hovered { animate { background(Color.White) } }
    pressed {
        animate {
            dropShadow(Shadow(color = Color(0xFF422800), offset = DpOffset(2.dp, 2.dp), radius = 0.dp, spread = 0.dp))
            translation(with(density) { 2.dp.toPx() }, with(density) { 2.dp.toPx() })
        }
    }
}
```

## 3) 다층 스타일 + 눌림 (하나의 StyleState 공유)
```kotlin
val interactionSource = remember { MutableInteractionSource() }
val styleState = remember(interactionSource) { MutableStyleState(interactionSource) }

val edgeStyle  = Style { fillSize(); shape(RoundedCornerShape(16.dp)); background(Color(0xFF1CB0F6)) }
val frontStyle = Style {
    fillSize(); background(Color(0xFF1899D6)); shape(RoundedCornerShape(16.dp))
    contentPadding(vertical = 12.dp, horizontal = 16.dp)
    translationY(with(density) { (-4).dp.toPx() })
    pressed { animate { translationY(with(density) { 0.dp.toPx() }) } } // 누르면 앞면이 내려앉음
}
// edge(아래층) 위에 front(윗층) — 둘 다 같은 styleState 사용
Box(Modifier.styleable(styleState, edgeStyle)) {
    Box(Modifier.styleable(styleState, frontStyle)) { BaseText("...", style = Style { contentColor(Color.White) }) }
}
```

## 읽을 거리 (패턴 추출)
- `BaseButton` + `then`으로 변형 버튼 파생 — 우리 `CalMong*` 원자 컴포넌트에 그대로 적용 가능.
- 그림자 offset/translation 애니메이션으로 "물리적 눌림" 표현 → 우리 `elevations` + 상태와 결합 여지.
- 색/shape/typography 하드코딩은 **반드시 테마 토큰으로 교체**해서 쓸 것.
