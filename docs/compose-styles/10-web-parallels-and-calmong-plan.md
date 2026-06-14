# 10. 웹 CSS/디자인 시스템과의 대응, 그리고 calmong 적용 사고

> 작성: 2026-06-14 · 성격: 설계 사고(아직 도입 아님, [09](09-limitations.md) 보류 근거 참조)
> 출발점: "Style API는 웹의 CSS와 닮았다 — 로직과 스타일링을 분리한다."

## 0. 직관 다듬기
이 Style API가 닮은 건 **raw CSS(전역 cascade·selector·specificity)가 아니라**, 그 위에서 진화한
**CSS-in-JS variants/recipe 계열**(Stitches · vanilla-extract · CVA)과 **headless 컴포넌트**(Radix UI) 패턴이다.
즉 "동작/로직과 시각을 분리"한다는 점에서, 웹이 ~10년에 걸쳐 도달한 지점을 Android가 한 번에 가져온 셈.

**핵심 명제**
> calmong은 이미 **토큰 층**(웹 = CSS Custom Properties + Style Dictionary)을 웹 수준으로 갖췄다.
> Style API는 그 위에 비어 있던 **"레시피/변형 + 상태 스타일" 층**(웹 = CVA/Stitches + Radix 분리)을 채워준다.
> → "웹식 디자인 시스템"의 빠진 퍼즐을 Android에서 거의 그대로 재현 가능.

---

## 1. 웹 프론트엔드의 스타일/디자인 시스템 층위
아래로 갈수록 구체적:

| 층 | 웹 도구·개념 | 역할 |
|---|---|---|
| **토큰** | Design Tokens(W3C DTCG) + Style Dictionary, CSS Custom Properties `--color-primary` | 값의 단일 출처. 런타임 테마 스왑(`:root[data-theme=dark]`) |
| **유틸리티(atomic)** | Tailwind (`px-4 rounded-md bg-primary`) | 토큰에 묶인 단일 목적 클래스 |
| **레시피/변형(variants)** | CVA · Stitches `variants` · vanilla-extract `recipes` | `intent×size` 같은 **이산 축**을 타입 안전하게 조합 |
| **동작(headless)** | Radix UI / Headless UI | 접근성·키보드·상태 머신만, **시각 0** |
| **합성** | `clsx` + `tailwind-merge` | 클래스 합치고 충돌 시 마지막이 이김 |
| **배포 컴포넌트** | shadcn/ui (= Radix 동작 + Tailwind/CVA 시각) | 위를 조립한 실제 컴포넌트 |

웹의 통찰 3가지:
- **동작과 시각을 물리적으로 분리**(Radix=동작, Tailwind/CVA=시각).
- **상태는 선택자/데이터 속성**으로: `:hover`, `:focus-visible`, `[data-state=open]`, `aria-disabled`.
- **변형은 문자열이 아니라 이산 축**: `intent: primary|secondary`, `size: sm|md|lg` + *compound variants*(특정 조합만 다르게).

---

## 2. Style API ↔ 웹 개념 매핑

| 웹 | Compose Style API |
|---|---|
| CSS class / styled-component | `Style { ... }` 객체 |
| CSS Custom Property `var(--x)` | `StyleScope` 확장으로 읽는 `CompositionLocal`(우리 `CalMongTheme` 토큰) |
| Tailwind 유틸리티 | atomic `Style { contentPadding(...) }` |
| CVA/Stitches **recipe + variants** | **변형 enum → Style 반환 함수**(§3-b) |
| `clsx` + `tailwind-merge`(마지막 승) | `then` + Style의 **last-write-wins**(속성 단위 덮어쓰기) ← 거의 그대로 |
| `:hover/:focus/:active/:disabled` | `hovered/focused/pressed/disabled { }` |
| `[data-state=playing]` 커스텀 상태 | `StyleStateKey` 커스텀 상태 |
| Radix(동작) + Tailwind(시각) 분리 | **modifier(clickable/semantics) + `style: Style` 파라미터** |
| ThemeProvider / `data-theme` 스왑 | `CalMongTheme` 라이트/다크 토큰 스왑(Style 그대로, 토큰만 변경) |
| 미디어/컨테이너 쿼리 | `CalMongTheme.windowWidthClass`로 Style 선택 |
| CSS inheritance(font/color) | Style 상속(타이포·`contentColor`만) |

---

## 3. calmong에 가져올 구체 패턴

### (a) 동작/시각 분리 = Radix + shadcn 패턴 (= Style API 철학)
```kotlin
@Composable
fun CalMongButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,              // 시각 = 호출부가 덮어쓸 수 있는 Style
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interaction) { it.isEnabled = enabled }
    Row(
        modifier
            .semantics { role = Role.Button }                            // 동작/접근성 = modifier
            .clickable(enabled, interactionSource = interaction, indication = null, onClick = onClick)
            .styleable(styleState, CalMongStyles.buttonBase then style), // 기본 then 덮어쓰기
        content = content,
    )
}
```

### (b) CVA "recipe + variants" → Compose 레시피 함수 (핵심 이식)
웹 CVA가 하던 걸 **타입 안전한 Kotlin enum + `when`** 으로. CSS 문자열보다 안전하다.
```kotlin
enum class ButtonIntent { Primary, Secondary, Danger }
enum class ButtonSize  { Sm, Md, Lg }

// recipe: 변형 축 조합 → Style. colors/shapes/spacings 는 StyleScope 확장(= CSS var 읽기)
fun calMongButton(
    intent: ButtonIntent = ButtonIntent.Primary,   // = CVA defaultVariants
    size: ButtonSize = ButtonSize.Md,
): Style = Style {
    shape(shapes.surface.small)                                     // base

    when (intent) {                                                // variant: intent
        ButtonIntent.Primary   -> { background(colors.primary.background.default);   contentColor(colors.primary.foreground.default) }
        ButtonIntent.Secondary -> { background(colors.secondary.background.default); contentColor(colors.secondary.foreground.default) }
        ButtonIntent.Danger    -> { background(colors.functional.common.negative.subtle); contentColor(colors.functional.common.negative.default) }
    }
    when (size) {                                                  // variant: size
        ButtonSize.Sm -> contentPadding(horizontal = spacings.inset.compact,     vertical = spacings.inset.compact)
        ButtonSize.Md -> contentPadding(horizontal = spacings.inset.default,     vertical = spacings.inset.compact)
        ButtonSize.Lg -> contentPadding(horizontal = spacings.inset.comfortable, vertical = spacings.inset.default)
    }
    if (intent == ButtonIntent.Danger && size == ButtonSize.Lg) {   // = compoundVariants
        borderColor(colors.functional.common.negative.default); borderWidth(strokeWidths.emphasis)
    }

    pressed  { animate { background(colors.functional.stateLayer.solid.pressed) } }  // = :active
    disabled { animate { alpha(0.5f) } }                                            // = :disabled
}
```
> `colors`/`shapes`/`spacings`/`strokeWidths`는 **`StyleScope` 확장**으로 우리 `Local*`를 읽게 만든다(= CSS `var(--x)`).
> 그러면 라이트/다크는 **토큰만 바뀌고 레시피는 그대로** → [07 dos-don'ts](07-dos-donts.md)의 "subsystem 값 변경엔 단일 Style"과 일치.

### (c) 상태 = pseudo-class / data-attribute
- 내장 `pressed/hovered/focused/disabled` → 우리 `functional.stateLayer.{soft|solid}` · `colors.*.stroke` 상태색과 1:1.
- 커스텀(`[data-state]`)은 `StyleStateKey`로: 로딩·펼침·선택 등(`loadingKey`, `expandedKey`).

### (d) 반응형 = 미디어 쿼리 → WindowWidthClass
```kotlin
val cardStyle = when (CalMongTheme.windowWidthClass) {
    WindowWidthClass.Compact -> calMongCard(size = Md)
    else                      -> calMongCard(size = Lg)
}
```

### (e) 멀티파트(slots) = Radix slots
부위가 여럿(탭/다이얼로그)이면 **부위별 Style 파라미터**(또는 styles 묶음 객체)로 노출 — 웹 `classNames={{ root, trigger, content }}`와 동형.

---

## 4. 웹엔 있지만 Style API엔 없는 것 / 베끼지 말 것

**격차(이식 시 우회):**
- **변형(variants)은 Style 빌트인이 아님** — enum+함수로 직접 구현(오히려 타입 안전). `compoundVariants`/`defaultVariants`는 `if`/기본 인자로.
- **커스텀 shape·shape 애니메이션·무한 애니메이션·커스텀 속성 미지원**([09](09-limitations.md)). Tailwind식 임의값 자유도는 토큰으로 제약.
- **선택자/자손 결합자 없음** — `.card .title` 같은 하향 선택 불가. 상속은 타이포·`contentColor`만. 부위 스타일은 명시 전달.
- **Material 컴포넌트엔 아직 Style 못 얹음** — 자체 원자 컴포넌트부터.

**의도적으로 안 베낄 것:**
- **CSS cascade·specificity 전쟁** — Style의 last-write-wins + 명시 우선순위가 더 단순. 전역 캐스케이드 흉내 금지(웹도 `@layer`로 줄이는 중).
- **유틸리티 폭발(atomic 남발)** — 우리 토큰이 이미 제약 스케일. atomic은 반복이 실제로 아플 때만(YAGNI).
- **`@Composable`에서 CompositionLocal 읽어 Style 반환** — CSS-in-JS 런타임 함정과 유사. 반드시 `StyleScope` 확장으로([07](07-dos-donts.md)).

---

## 5. 결론 & 다음 후보
- calmong은 토큰 층을 이미 갖췄고, Style API로 **변형은 enum 레시피 / 토큰은 StyleScope 확장 / 상태는 StyleState / 반응형은 WindowWidthClass**로 웹식 디자인 시스템을 재현 가능.
- **단 지금은 alpha+스냅샷이라 도입 보류**. 이 문서는 "안정화되면 갈 그림"의 설계 사고다.
- 안정화 시 후보 작업:
  1. `theme/style/CalMongStyles.kt`(레시피 모음) + `StyleScope` 토큰 확장.
  2. `calMongButton(intent, size)` 레시피 PoC(실험 모듈).
  3. 자체 원자 컴포넌트(Button/Card)부터 `style: Style` 파라미터 노출로 전환.
