# CalMong Color System

Surface 색을 Light/Dark elevation과 조합하는 규칙은
[`ELEVATION_SYSTEM.md`](ELEVATION_SYSTEM.md)를 함께 따른다.

CalMong의 색상 시스템은 색상 값이 아니라 **역할(role)** 을 기준으로 사용한다.
화면과 컴포넌트는 Tailwind primitive를 직접 참조하지 않고
`CalMongTheme.colors`의 semantic 토큰을 사용해야 한다.

Shape와 radius 사용 규칙은 [`RADIUS_SYSTEM.md`](RADIUS_SYSTEM.md)를 참고한다.

```text
primitive                  semantic                          component
Tailwind Indigo600    ->   primary.background.default  ->   Filled Button
Tailwind Gray900      ->   neutral.foreground.default  ->   Body Text
```

Figma 문서:
[Design System / Color System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=21-2)

## Source of truth

| 대상 | 위치 |
|---|---|
| Kotlin 토큰 구조 | `src/main/kotlin/.../theme/color/CalMongColorScheme.kt` |
| Light/Dark 매핑 | `src/main/kotlin/.../theme/color/CalMongColorSchemes.kt` |
| Primitive 값 | `src/main/kotlin/.../theme/color/TailwindPalette.kt` |
| Figma semantic JSON | `tokens/semantic.color.light.json`, `tokens/semantic.color.dark.json` |
| Figma 페이지 설계 | `tokens/figma-doc-page.md` |

색상 값을 바꾸거나 토큰을 추가할 때 Kotlin, JSON, Figma 변수를 함께 갱신한다.
현재 semantic 컬렉션은 Light/Dark 모드와 110개 변수를 갖는다.

## 기본 규칙

1. 제품 코드에서 `Indigo600`, `Gray900` 같은 primitive를 직접 사용하지 않는다.
2. 배경과 그 위 콘텐츠는 반드시 같은 의미의 조합을 사용한다.
3. 브랜드 채움 위 콘텐츠는 `primary.foreground.default` 또는
   `secondary.foreground.default`를 사용한다.
4. 브랜드색 텍스트나 아이콘은 `primary.background.default` 또는
   `secondary.background.default`를 사용한다.
5. functional 색상은 `subtle` 배경과 `default` 텍스트 조합을 우선한다.
6. 이미지, 스크림, 어두운 고정면 위 흰 콘텐츠는 `neutral.foreground.static`을 사용한다.
7. 배경 상호작용은 원래 색을 바꾸지 않고 `stateLayer`를 위에 겹친다.
8. 외곽선 상호작용은 해당 `stroke`의 상태별 색으로 교체한다.
9. 비활성 상태를 opacity로 임의 처리하지 않고 disabled 토큰을 사용한다.
10. Light/Dark 값을 조건문으로 직접 고르지 않는다. `CalMongTheme`이 모드를 결정한다.

## 사용 진입점

```kotlin
@Composable
fun Example() {
    val colors = CalMongTheme.colors

    Surface(color = colors.neutral.background.base) {
        Text(
            text = "오늘의 일정",
            color = colors.neutral.foreground.default,
        )
    }
}
```

Material3 컴포넌트는 `CalMongTheme`이 `MaterialTheme.colorScheme`에 semantic
토큰을 매핑하므로 기본 색상을 그대로 사용할 수 있다. CalMong 고유 역할이
필요할 때만 `CalMongTheme.colors`에 직접 접근한다.

## Primary

Primary는 CalMong의 핵심 브랜드 색상인 **Indigo**다. 주요 행동, 현재 선택,
핵심 강조에 사용한다. 한 화면에서 primary 강조가 너무 많으면 정보 위계가
사라지므로 가장 중요한 행동에 제한한다.

### Background

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `primary.background.default` | Indigo600 | Indigo600 | Filled 버튼, 선택된 핵심 컨트롤, 주요 브랜드 채움 |
| `primary.background.subtle` | Indigo50 | Indigo950 | Tonal 버튼, 선택 행, 옅은 브랜드 배너 |
| `primary.background.bold` | Indigo700 | Indigo400 | default보다 강한 강조, 제한적인 강조 면 |
| `primary.background.dimmed` | Indigo100 | Indigo900 | 약한 선택 상태, 낮은 우선순위 브랜드 면 |
| `primary.background.inverted` | Indigo900 | Indigo100 | 반전된 브랜드 표면 |

`default` 위 콘텐츠는 `primary.foreground.default`를 사용한다.
`subtle` 위 본문은 보통 `neutral.foreground.default`, 브랜드 라벨은
`primary.background.default`를 사용한다.

### Foreground

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `primary.foreground.default` | White | White | `primary.background.default` 위 라벨과 아이콘 |
| `primary.foreground.subtle` | Indigo200 | Indigo200 | 브랜드 채움 위 보조 콘텐츠 |
| `primary.foreground.inverted` | Indigo50 | Indigo900 | `primary.background.inverted` 위 콘텐츠 |

`primary.foreground.default`는 일반 흰 배경 위의 브랜드 텍스트용 토큰이 아니다.
Outlined/Text 버튼의 라벨처럼 브랜드색 콘텐츠가 필요하면
`primary.background.default`를 foreground로 사용한다.

### Stroke

| 토큰 | 설명 |
|---|---|
| `primary.stroke.default.{state}` | Outlined 버튼, focus ring 등 명확한 브랜드 외곽선 |
| `primary.stroke.subtle.{state}` | 선택 영역, 장식 박스 등 옅은 브랜드 외곽선 |

`default`는 명확한 컴포넌트 경계에, `subtle`은 장식적 경계에 사용한다.

상태 값은 `default -> hover -> focused -> pressed -> activated -> disabled` 순서다.

| Stroke | Light | Dark |
|---|---|---|
| `primary.stroke.default` | Indigo600, 700, 800, 800, 700, Gray200 | Indigo500, 400, 300, 300, 400, Gray700 |
| `primary.stroke.subtle` | Indigo200, 300, 400, 400, 300, Gray100 | Indigo800, 700, 600, 600, 700, Gray800 |

## Secondary

Secondary는 보조 브랜드 색상인 **Amber**다. primary와 경쟁하지 않는 보조
행동, 따뜻한 강조, 펫과 보상 관련 표현에 사용한다.

### Background

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `secondary.background.default` | Amber500 | Amber400 | Secondary Filled 버튼, 보조 브랜드 채움 |
| `secondary.background.subtle` | Amber50 | Amber950 | Secondary Tonal 버튼, 옅은 강조 표면 |
| `secondary.background.bold` | Amber600 | Amber300 | 강한 보조 강조 |
| `secondary.background.dimmed` | Amber100 | Amber900 | 낮은 강도의 보조 강조 |
| `secondary.background.inverted` | Amber900 | Amber100 | 반전된 secondary 표면 |

### Foreground

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `secondary.foreground.default` | Gray950 | Gray950 | `secondary.background.default` 위 라벨과 아이콘 |
| `secondary.foreground.subtle` | Gray800 | Gray800 | secondary 채움 위 보조 콘텐츠 |
| `secondary.foreground.inverted` | Amber50 | Gray950 | `secondary.background.inverted` 위 콘텐츠 |

Amber 채움은 밝기 때문에 흰색 대신 Gray950 콘텐츠를 사용한다.

### Stroke

| 토큰 | 설명 |
|---|---|
| `secondary.stroke.default.{state}` | Secondary Outlined 버튼과 명확한 보조 외곽선 |
| `secondary.stroke.subtle.{state}` | 옅은 Amber 경계와 장식선 |

상태 값은 `default -> hover -> focused -> pressed -> activated -> disabled` 순서다.

| Stroke | Light | Dark |
|---|---|---|
| `secondary.stroke.default` | Amber600, 700, 800, 800, 700, Gray200 | Amber400, 300, 200, 200, 300, Gray700 |
| `secondary.stroke.subtle` | Amber200, 300, 400, 400, 300, Gray100 | Amber800, 700, 600, 600, 700, Gray800 |

## Neutral

Neutral은 브랜드와 무관한 화면 구조, 텍스트, 아이콘, 구분선을 담당한다.
대부분의 화면 면적은 neutral로 구성하고 브랜드 색은 강조에만 사용한다.

### Background

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `neutral.background.base` | Gray50 | Gray950 | 앱과 화면의 최하단 배경 |
| `neutral.background.default` | White | Gray900 | 기본 Surface, Card, Dialog 내부 |
| `neutral.background.raised1` | White | Gray800 | Sheet, 떠 있는 Card, 1단계 elevation |
| `neutral.background.raised2` | White | Gray700 | Menu, Tooltip, 2단계 elevation |
| `neutral.background.dimmed` | Gray100 | Black | Inset, well, 가라앉은 영역 |
| `neutral.background.inverted` | Gray900 | Gray50 | Snackbar 등 반전 표면 |

Light 모드는 주로 그림자로 elevation을 표현한다. Dark 모드는 그림자보다
`default -> raised1 -> raised2`로 밝아지는 표면색 차이를 사용한다.

### Foreground

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `neutral.foreground.static` | White | White | 이미지, 스크림, 고정된 어두운 면 위 콘텐츠 |
| `neutral.foreground.default` | Gray900 | Gray50 | 제목, 본문, 기본 아이콘 |
| `neutral.foreground.subtle` | Gray500 | Gray400 | 설명, 메타데이터, 보조 아이콘 |
| `neutral.foreground.decorative` | Gray300 | Gray600 | placeholder, 비활성 텍스트, 장식 아이콘 |
| `neutral.foreground.alpha` | Black 60% | White 60% | 투명도가 필요한 오버레이 콘텐츠 |
| `neutral.foreground.inverted` | White | Gray900 | `neutral.background.inverted` 위 콘텐츠 |

`static`은 테마 표면 위 본문에 사용하지 않는다. 일반 텍스트는 반드시
`default`, `subtle`, `decorative` 중 의미에 맞는 토큰을 선택한다.

### Stroke

| 토큰 | 설명과 사용처 |
|---|---|
| `neutral.stroke.divider.{state}` | 목록과 카드 내부의 가장 약한 구분선 |
| `neutral.stroke.subtle.{state}` | 장식 박스, 약한 입력 경계 |
| `neutral.stroke.default.{state}` | 입력 필드, 버튼 등 인지해야 하는 컴포넌트 경계 |
| `neutral.stroke.static.{state}` | 더 강한 고정 경계, 높은 대비가 필요한 외곽선 |

`divider`와 `subtle`은 장식용이므로 컴포넌트 존재를 전달하는 유일한 수단으로
사용하지 않는다. `default`와 `static`은 비텍스트 대비 3:1을 목표로 한다.

상태 값은 `default -> hover -> focused -> pressed -> activated -> disabled` 순서다.

| Stroke | Light | Dark |
|---|---|---|
| `neutral.stroke.divider` | Gray100, 200, 300, 300, 200, 50 | Gray800, 700, 600, 600, 700, 900 |
| `neutral.stroke.subtle` | Gray200, 300, 400, 400, 300, 100 | Gray700, 600, 500, 500, 600, 800 |
| `neutral.stroke.default` | Gray500, 600, 700, 700, 600, 200 | Gray500, 400, 300, 300, 400, 700 |
| `neutral.stroke.static` | Gray600, 700, 800, 800, 700, 300 | Gray400, 300, 200, 200, 300, 700 |

## Functional Common

Functional common은 결과와 상태를 전달한다.

| 그룹 | 계열 | 의미 | 대표 사용처 |
|---|---|---|---|
| `positive` | Green | 성공, 완료, 정상 | 저장 완료, 목표 달성, 유효한 입력 |
| `negative` | Red | 오류, 실패, 위험 | 입력 오류, 삭제 경고, 실패 상태 |
| `informative` | Blue | 정보, 안내, 진행 | 도움말, 정보 배너, 중립 알림 |
| `attention` | Amber | 주의, 확인 필요 | 마감 임박, 확인 대기, 경고 |

각 그룹은 동일한 세 가지 역할을 갖는다.

| Variant | 설명 |
|---|---|
| `default` | 제목, 핵심 아이콘, 상태를 직접 전달하는 콘텐츠 |
| `decorative` | 그래프, 큰 아이콘, 배경 장식처럼 보조적인 시각 요소 |
| `subtle` | 상태 배너와 배지의 옅은 배경 |

### Light/Dark 매핑

| 토큰 | Light | Dark |
|---|---|---|
| `functional.common.positive.default` | Green600 | Green500 |
| `functional.common.positive.decorative` | Green500 | Green400 |
| `functional.common.positive.subtle` | Green50 | Green950 |
| `functional.common.negative.default` | Red600 | Red500 |
| `functional.common.negative.decorative` | Red500 | Red400 |
| `functional.common.negative.subtle` | Red50 | Red950 |
| `functional.common.informative.default` | Blue600 | Blue500 |
| `functional.common.informative.decorative` | Blue500 | Blue400 |
| `functional.common.informative.subtle` | Blue50 | Blue950 |
| `functional.common.attention.default` | Amber500 | Amber400 |
| `functional.common.attention.decorative` | Amber400 | Amber300 |
| `functional.common.attention.subtle` | Amber50 | Amber950 |

상태 메시지는 다음 조합을 기본으로 한다.

```kotlin
val colors = CalMongTheme.colors

Surface(color = colors.functional.common.negative.subtle) {
    Column {
        Text(
            text = "저장하지 못했습니다",
            color = colors.functional.common.negative.default,
        )
        Text(
            text = "네트워크 연결을 확인하세요.",
            color = colors.neutral.foreground.default,
        )
    }
}
```

functional `default`를 넓은 솔리드 배경으로 사용하고 흰 텍스트를 올리는 패턴은
색상별 대비가 일정하지 않으므로 기본 패턴으로 사용하지 않는다.

## Functional General

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `functional.general.overlay` | Black 50% | Black 70% | Modal, Dialog, Bottom Sheet 뒤 scrim |
| `functional.general.highlight` | Indigo100 | Indigo900 | 검색 결과, 선택 범위, 임시 하이라이트 |
| `functional.general.shadow` | Black 10% | Black 40% | Card, Menu, Dialog elevation 그림자 |
| `functional.general.disabled` | Gray200 | Gray700 | 비활성 컨테이너와 채움 |

`disabled` 위 콘텐츠는 `neutral.foreground.decorative`를 사용한다. 비활성 상태를
표현하기 위해 정상 색상 전체에 임의 opacity를 적용하지 않는다.

## Functional Specific

Specific은 일반적인 성공/오류가 아니라 CalMong 제품 안에서 고정된 의미를 갖는다.

| 토큰 | Light | Dark | 설명과 사용처 |
|---|---|---|---|
| `functional.specific.like.default` | Rose500 | Rose400 | 선택된 좋아요 하트와 라벨 |
| `functional.specific.like.decorative` | Rose400 | Rose300 | 좋아요 관련 장식 |
| `functional.specific.like.subtle` | Rose50 | Rose950 | 좋아요 관련 옅은 배경 |
| `functional.specific.link.default` | Blue600 | Blue400 | 링크 텍스트와 밑줄 |
| `functional.specific.link.decorative` | Blue500 | Blue300 | 링크 관련 보조 아이콘 |
| `functional.specific.link.subtle` | Blue50 | Blue950 | 링크 안내의 옅은 배경 |

선택되지 않은 좋아요 아이콘은 `neutral.foreground.decorative`를 사용한다.
링크는 색상만으로 구분하지 않고 밑줄이나 링크 아이콘을 함께 사용한다.

## Interaction States

상태 순서는 모든 `InteractionStates`에서 동일하다.

| 상태 | 의미 |
|---|---|
| `default` | 상호작용 전 기본 상태 |
| `hover` | 포인터가 올라온 상태 |
| `focused` | 키보드 또는 접근성 focus 상태 |
| `pressed` | 누르고 있는 순간 |
| `activated` | 선택 또는 활성 상태가 유지되는 상태 |
| `disabled` | 상호작용할 수 없는 상태 |

### State layer

배경이나 채움의 상호작용 상태는 배경색 자체를 교체하지 않고, 원래 요소 위에
반투명 레이어를 겹쳐 표현한다.

| 토큰 | 대상 | default | hover | focused | pressed | activated | disabled |
|---|---|---|---|---|---|---|---|
| `functional.stateLayer.soft` | 흰색, 연한 tonal, ghost 요소 | 투명 | Black 8% | Black 10% | Black 12% | Black 16% | Black 38% |
| `functional.stateLayer.solid` | 브랜드 filled, 이미지, 짙은 요소 | 투명 | White 8% | White 10% | White 12% | White 16% | White 38% |

State layer는 요소의 밝기로 선택하며 Light/Dark 테마 여부로 선택하지 않는다.

```kotlin
@Composable
fun PressedPrimaryButton(pressed: Boolean) {
    val colors = CalMongTheme.colors

    Box(Modifier.background(colors.primary.background.default)) {
        Text(
            text = "확인",
            color = colors.primary.foreground.default,
        )
        if (pressed) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(colors.functional.stateLayer.solid.pressed),
            )
        }
    }
}
```

### Stroke state

외곽선은 overlay를 겹치지 않고 상태에 맞는 stroke 색 자체를 선택한다.

```kotlin
val colors = CalMongTheme.colors
val borderColor =
    when {
        !enabled -> colors.neutral.stroke.default.disabled
        focused -> colors.primary.stroke.default.focused
        pressed -> colors.neutral.stroke.default.pressed
        else -> colors.neutral.stroke.default.default
    }
```

## Component Recipes

### Button

| 종류 | 배경 | 라벨/아이콘 | 외곽선 | Pressed layer |
|---|---|---|---|---|
| Primary Filled | `primary.background.default` | `primary.foreground.default` | 없음 | `stateLayer.solid.pressed` |
| Primary Tonal | `primary.background.subtle` | `primary.background.default` | 없음 | `stateLayer.soft.pressed` |
| Primary Outlined | `neutral.background.default` | `primary.background.default` | `primary.stroke.default.default` | `stateLayer.soft.pressed` |
| Primary Text | 투명 | `primary.background.default` | 없음 | `stateLayer.soft.pressed` |
| Secondary Filled | `secondary.background.default` | `secondary.foreground.default` | 없음 | `stateLayer.solid.pressed` |
| Disabled | `functional.general.disabled` | `neutral.foreground.decorative` | 없음 | 없음 |

### Surface and elevation

| 역할 | 토큰 |
|---|---|
| 화면 바닥 | `neutral.background.base` |
| 기본 Card | `neutral.background.default` |
| Sheet / Elevated Card | `neutral.background.raised1` |
| Menu / Tooltip | `neutral.background.raised2` |
| 제목 | `neutral.foreground.default` |
| 본문 | `neutral.foreground.subtle` |
| 구분선 | `neutral.stroke.divider.default` |
| 그림자 | `functional.general.shadow` |

### Text field

| 요소 또는 상태 | 토큰 |
|---|---|
| 컨테이너 | `neutral.background.default` |
| 라벨 | `neutral.foreground.subtle` |
| 입력값 | `neutral.foreground.default` |
| Placeholder | `neutral.foreground.decorative` |
| Rest border | `neutral.stroke.default.default` |
| Hover border | `neutral.stroke.default.hover` |
| Focus border | `primary.stroke.default.focused` |
| Disabled border | `neutral.stroke.default.disabled` |
| Error border/helper | `functional.common.negative.default` |

### Status banner

```text
background = functional.common.{status}.subtle
title/icon = functional.common.{status}.default
decorative icon = functional.common.{status}.decorative
body = neutral.foreground.default
```

## Material3 Mapping

`CalMongTheme`은 다음 핵심 Material3 role을 자동으로 연결한다.

| Material3 | CalMong |
|---|---|
| `primary` / `onPrimary` | `primary.background.default` / `primary.foreground.default` |
| `primaryContainer` | `primary.background.subtle` |
| `secondary` / `onSecondary` | `secondary.background.default` / `secondary.foreground.default` |
| `background` / `onBackground` | `neutral.background.base` / `neutral.foreground.default` |
| `surface` / `onSurface` | `neutral.background.default` / `neutral.foreground.default` |
| `surfaceVariant` | `neutral.background.raised1` |
| `inverseSurface` | `neutral.background.inverted` |
| `error` / `errorContainer` | `functional.common.negative.default` / `.subtle` |
| `outline` / `outlineVariant` | `neutral.stroke.default.default` / `neutral.stroke.subtle.default` |
| `scrim` | `functional.general.overlay` |

Material3의 `tertiary`는 별도 세 번째 브랜드색이 없으므로 secondary의 별칭이다.
Material tonal elevation tint는 사용하지 않고 명시적인 neutral raised surface를 쓴다.

## 금지 예시

```kotlin
// 금지: primitive 직접 사용
Modifier.background(Indigo600)

// 금지: 화면마다 임의 색상 추가
Modifier.background(Color(0xFF4F46E5))

// 금지: 테마별 값을 직접 분기
val color = if (isSystemInDarkTheme()) Gray50 else Gray900

// 금지: disabled를 임의 opacity로 표현
Modifier.alpha(if (enabled) 1f else 0.38f)
```

필요한 의미가 기존 semantic 토큰에 없다면 primitive나 임의 hex를 사용해 우회하지
말고 semantic 역할을 먼저 설계한다.

## 변경 체크리스트

1. `CalMongColorScheme.kt`에 역할과 KDoc을 정의한다.
2. `CalMongColorSchemes.kt`에 Light/Dark primitive 매핑을 추가한다.
3. `semantic.color.light.json`, `semantic.color.dark.json`을 동기화한다.
4. Figma `Semantic` 변수 컬렉션을 동기화한다.
5. Figma Color System 페이지에 스와치와 사용 예시를 추가한다.
6. `ColorTokenPreview.kt` 또는 대상 컴포넌트 Preview로 두 테마를 확인한다.
7. 텍스트와 비텍스트 대비, disabled/focus 상태를 확인한다.
8. 이 문서의 설명과 표를 갱신한다.
