# CalMong Stroke System (외곽선 너비)

외곽선(stroke)은 두 축으로 나뉜다. **색**은 [`COLOR_SYSTEM.md`](COLOR_SYSTEM.md)의
`CalMongTheme.colors.*.stroke`가, **너비**는 이 문서의 `CalMongTheme.strokeWidths`가 담당한다.
제품 코드에서는 `border(1.dp, …)`처럼 두께를 직접 쓰지 않고 semantic 역할을 사용한다.

Figma 문서:
[Design System / Stroke System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=79-2)

```text
primitive borderWidth        semantic strokeWidth         component
borderWidth/DEFAULT (1)  ->  default                  ->  Card·입력 외곽선
borderWidth/2 (2)        ->  emphasis                 ->  Toast 강조 외곽선
borderWidth/4 (4)        ->  focus                    ->  포커스 링
```

## Source of truth

| 대상 | 위치 |
|---|---|
| Figma primitive | `other.json` (`borderWidth`) |
| Figma semantic alias | `tokens/semantic.stroke.json` |
| Kotlin primitive subset | `src/main/kotlin/.../theme/stroke/CalMongStrokeWidth.kt` |
| Semantic stroke width | `src/main/kotlin/.../theme/stroke/CalMongStrokeWidths.kt` |
| Theme 연결 | `src/main/kotlin/.../theme/Theme.kt` |
| Preview | `src/main/kotlin/.../theme/stroke/StrokeWidthTokenPreview.kt` |
| 단위 테스트 | `src/test/kotlin/.../theme/stroke/CalMongStrokeWidthsTest.kt` |

너비는 Light/Dark에 따라 바뀌지 않으므로 단일 모드로 관리한다.

## Primitive Scale

Figma `borderWidth`는 Tailwind 값(0 / DEFAULT=1 / 2 / 4 / 8)을 보존한다. Kotlin에는 현재
semantic 역할이 쓰는 값만 둔다.

| Figma | Kotlin | 값 |
|---|---|---:|
| `0` | `none` | 0.dp |
| `DEFAULT` | `hairline` | 1.dp |
| `2` | `thin` | 2.dp |
| `4` | `thick` | 4.dp |

Primitive 이름은 scale 호환용 내부 구현이다. 제품 코드에서는 직접 사용하지 않는다.

## Semantic 역할

너비는 크기가 아니라 **외곽선의 목적**으로 선택한다.

| 토큰 | 값 | 사용처 |
|---|---:|---|
| `strokeWidth.none` | 0.dp | 상태에 따라 외곽선을 조건부로 제거 |
| `strokeWidth.default` | 1.dp | 입력·카드·컨테이너의 기본 경계, 구분선 |
| `strokeWidth.emphasis` | 2.dp | 주의를 끄는 표면(토스트)·selected 상태 |
| `strokeWidth.focus` | 4.dp | 포커스 링 등 가장 또렷한 접근성 표시 |

```kotlin
// 강조 외곽선 — 색은 color 시스템, 너비는 stroke 시스템
Box(
    Modifier
        .background(colors.functional.common.negative.subtle, shape)
        .border(
            width = CalMongTheme.strokeWidths.emphasis,
            color = colors.functional.common.negative.default,
            shape = shape,
        ),
)

// 포커스 상태에서만 굵게
val width = if (focused) CalMongTheme.strokeWidths.focus else CalMongTheme.strokeWidths.default
```

## 선택 순서

1. 외곽선이 필요 없으면(채움만) `border`를 쓰지 않는다.
2. 일반 경계·구분선이면 `default`.
3. 표면이 주의를 끌어야 하거나 선택 상태면 `emphasis`.
4. 키보드/접근성 포커스 표시면 `focus`.
5. 색은 항상 color 시스템의 stroke 색을 함께 고른다(너비만 바꾸지 않는다).

## 색 시스템과의 경계

- **색**: `colors.neutral.stroke.{divider|subtle|default|static}`,
  `colors.<brand>.stroke.{default|subtle}`, 그리고 각 stroke의 InteractionStates(상태색).
- **너비**: 이 문서의 `strokeWidths.{none|default|emphasis|focus}`.

두 축은 독립적으로 조합한다. 예: 구분선은 `neutral.stroke.divider`(색) + `strokeWidth.default`(너비).

## Figma 변수 구조

- primitive: `borderWidth/0`, `borderWidth/DEFAULT`, `borderWidth/2`, `borderWidth/4`
  (Tailwind Tokens가 생성, scope `STROKE_FLOAT`)
- semantic: `Semantic` 컬렉션의 `strokeWidth/none`, `strokeWidth/default`,
  `strokeWidth/emphasis`, `strokeWidth/focus` — primitive `borderWidth`를 alias.

## 변경 체크리스트

1. `other.json`의 `borderWidth` primitive 값을 확인한다.
2. 필요한 값만 `CalMongStrokeWidth.kt`에 동기화한다.
3. `semantic.stroke.json`의 역할과 alias를 갱신한다.
4. `CalMongStrokeWidths.kt`의 semantic 매핑을 갱신한다.
5. Figma `Semantic` 컬렉션 변수와 문서 페이지를 동기화한다.
6. `CalMongStrokeWidthsTest`의 primitive와 semantic 계약을 갱신한다.
7. `StrokeWidthTokenPreview`를 확인한다.
8. 이 문서의 표와 예시를 갱신한다.
