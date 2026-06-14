# 03. State and animations in Styles — 상태와 애니메이션

> 원문: <https://developer.android.com/develop/ui/compose/styles/state-animations> · 갱신 2026-06-11

## StyleState
읽기 전용·안정적 인터페이스로 요소의 활성 상태(enabled/pressed/focused 등)를 추적. `StyleScope` 안에서 `state`로 접근해 조건부 로직 작성.

## 내장 상호작용 상태
`pressed`, `hovered`, `selected`, `enabled`(→ `disabled`), `toggled`. (커스텀도 가능 — 아래)

```kotlin
BaseButton(
    style = outlinedButtonStyle then {
        background(Color.White)
        hovered { background(lightPurple); border(2.dp, lightPurple) }
        focused { background(lightBlue) }
    },
    onClick = {},
) { BaseText("Open in Studio") }
```

### 중첩 상태 (조합)
```kotlin
hovered {
    background(lightPurple)
    pressed { background(lightOrange) } // 호버 중 누름(마우스 기기)
}
pressed { background(lightRed) }        // 호버 없이 누름(터치 기기)
focused { background(lightBlue) }
```

## 커스텀 컴포넌트에서 (`Modifier.styleable`)
`interactionSource`를 `styleState`에 연결해야 상태가 흐른다.
```kotlin
@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) { it.isEnabled = enabled }
    Row(
        modifier = modifier
            .clickable(onClick = onClick, enabled = enabled,
                       interactionSource = interactionSource, indication = null)
            .styleable(styleState, baseGradientButtonStyle then style),
        content = content,
    )
}
```
> 핵심: ① `interactionSource` 파라미터 노출(없으면 생성) ② `rememberUpdatedStyleState(interactionSource)`로 `styleState` 생성, `isEnabled` 반영 ③ 같은 `interactionSource`를 `clickable`/`focusable`에, `styleState`를 `styleable`에 연결.

## 애니메이션 — `animate { }`
상태 변화 블록 안의 새 속성을 `animate`로 감싸면 자동 전환(= `animate*AsState` 대체).
```kotlin
val animatingStyle = Style {
    border(3.dp, Color.Black); background(Color.White); size(100.dp)
    pressed {
        animate { borderColor(Color.Magenta); background(Color(0xFFB39DDB)) }
    }
}
```

### animationSpec 지정
```kotlin
pressed {
    animate { borderColor(Color.Magenta) }
    animate(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { scale(1.2f) }
}
```
> `transformOrigin(TransformOrigin.Center)`와 함께 쓰면 중심 기준 확대.
> ⚠️ **무한 애니메이션은 미지원** — `rememberInfiniteTransition` 계속 사용([09](09-limitations.md)).

## 커스텀 상태 (`StyleStateKey`)
예: 미디어 플레이어의 재생 상태별 스타일.
```kotlin
// 1) 키 정의 (기본값 지정)
enum class PlayerState { Stopped, Playing, Paused }
val playerStateKey = StyleStateKey(PlayerState.Stopped)

// 2) 확장 함수
var MutableStyleState.playerState
    get() = this[playerStateKey]
    set(value) { this[playerStateKey] = value }

fun StyleScope.playerPlaying(block: () -> Unit) =
    state(playerStateKey, block, { key, s -> s[key] == PlayerState.Playing })
fun StyleScope.playerPaused(block: () -> Unit) =
    state(playerStateKey, block, { key, s -> s[key] == PlayerState.Paused })

// 3) 컴포넌트에서 상태 연결
@Composable
fun MediaPlayer(state: PlayerState, style: Style = Style, modifier: Modifier = Modifier) {
    val styleState = remember { MutableStyleState(null) }
    styleState.playerState = state   // 들어온 상태와 연결
    Box(modifier.styleable(styleState, style)) { /* ... */ }
}

// 사용
val style = Style {
    borderColor(Color.Gray)
    playerPlaying { animate { borderColor(Color.Green) } }
    playerPaused  { animate { borderColor(Color.Blue) } }
}
```
> 상태를 ViewModel에 연결해 구동 가능.

## calmong 메모
- 우리 색 시스템의 상태 표현과 정확히 맞물린다: 채움 상태는 `functional.stateLayer.{soft|solid}`의 `pressed/hovered/...`, 외곽선 상태는 `colors.*.stroke.*`의 InteractionStates → `pressed/hovered/focused { animate { background/borderColor(...) } }`.
- 상호작용 자체(`clickable`/`indication`)는 modifier 책임. **Ripple과 겹침 주의**: `pressed` 사용 시 `clickable`에 `indication = null`([09](09-limitations.md)).
