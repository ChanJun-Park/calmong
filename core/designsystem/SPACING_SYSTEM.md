# CalMong Spacing System

CalMong의 spacing은 임의의 거리 수치가 아니라 **요소 사이 관계**를 기준으로 선택한다.
제품 코드에서는 `padding(16.dp)`처럼 값을 직접 쓰지 않고
`CalMongTheme.spacings`의 semantic 역할을 사용한다.

Figma 문서:
[Design System / Spacing System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=52-2)

```text
primitive spacing      semantic spacing          component
spacing/4 (16)    ->   inset/default        ->  Calendar Card padding
spacing/8 (32)    ->   section/default      ->  화면 section 간격
```

## Source of truth

| 대상 | 위치 |
|---|---|
| Figma primitive | `spacing.json` |
| Figma semantic alias | `tokens/semantic.spacing.json` |
| Kotlin primitive subset | `src/main/kotlin/.../theme/spacing/CalMongSpacing.kt` |
| Semantic spacing | `src/main/kotlin/.../theme/spacing/CalMongSpacings.kt` |
| Theme 연결 | `src/main/kotlin/.../theme/Theme.kt` |
| Preview | `src/main/kotlin/.../theme/spacing/SpacingTokenPreview.kt` |

Spacing은 Light/Dark에 따라 바뀌지 않으므로 단일 모드로 관리한다.

## 기본 규칙

1. `gap`은 형제 요소 사이 거리와 `Arrangement.spacedBy()`에 사용한다.
2. `inset`은 컨테이너 내부 여백과 `Modifier.padding()`에 사용한다.
3. `section`은 독립적인 콘텐츠 그룹 사이에만 사용한다.
4. 같은 관계에는 화면마다 같은 semantic 토큰을 사용한다.
5. `Spacer`보다 container padding과 `Arrangement.spacedBy()`를 우선한다.
6. 아이콘 크기, 컴포넌트 높이, 터치 영역은 spacing 토큰으로 지정하지 않는다.
7. status bar, navigation bar, IME, `Scaffold`의 system inset은 토큰으로 바꾸지 않는다.
8. 화면이 넓어져도 spacing 전체를 비례 확대하지 않는다. 바깥 여백과 column 구조를
   responsive하게 바꾼다.
9. Material3 컴포넌트의 기본 padding은 명확한 제품 요구가 없으면 유지한다.
10. semantic 역할로 설명할 수 없는 수치가 필요하면 primitive를 직접 쓰지 말고
    역할 추가가 필요한지 먼저 검토한다.

## Primitive Scale

Figma의 `Spacing` 컬렉션은 2~384px의 34개 primitive를 보존한다. Kotlin에는 현재
semantic 역할이 사용하는 값과 optical 검토용 2.dp만 둔다.

| Figma | Kotlin | 값 |
|---|---|---:|
| `0` | `none` | 0.dp |
| `0_5` | `xxs` | 2.dp |
| `1` | `xs` | 4.dp |
| `2` | `sm` | 8.dp |
| `3` | `md` | 12.dp |
| `4` | `lg` | 16.dp |
| `5` | `xl` | 20.dp |
| `6` | `xxl` | 24.dp |
| `8` | `xxxl` | 32.dp |
| `10` | `huge` | 40.dp |
| `12` | `massive` | 48.dp |
| `16` | `section` | 64.dp |

Primitive 이름은 scale 호환을 위한 내부 구현이다. 제품 코드에서는 직접 사용하지 않는다.

## Gap

`gap`은 같은 컨테이너 안에서 나란히 배치되는 형제 요소 사이 거리다.

| 토큰 | 값 | 사용처 |
|---|---:|---|
| `gap.tight` | 4.dp | 아이콘-라벨, badge 내부의 가까운 요소 |
| `gap.compact` | 8.dp | 제목-설명, 한 control 안의 요소 |
| `gap.default` | 12.dp | 일반 목록 item, form field 내부 그룹 |
| `gap.relaxed` | 16.dp | Card 안의 독립적인 콘텐츠 블록 |
| `gap.loose` | 24.dp | 같은 surface 안의 느슨한 하위 그룹 |

```kotlin
Row(
    horizontalArrangement =
        Arrangement.spacedBy(CalMongTheme.spacings.gap.compact),
) {
    Icon(...)
    Text(...)
}
```

## Inset

`inset`은 경계가 있는 container와 그 안의 콘텐츠 사이 여백이다.

| 토큰 | 값 | 사용처 |
|---|---:|---|
| `inset.compact` | 8.dp | compact control, 작은 tooltip |
| `inset.default` | 16.dp | 일반 Card, 화면 content container |
| `inset.comfortable` | 20.dp | 콘텐츠 밀도를 낮춘 Card |
| `inset.spacious` | 24.dp | Dialog, Sheet, 큰 floating surface |

```kotlin
Surface(
    shape = CalMongTheme.shapes.surface.card,
) {
    Column(
        modifier = Modifier.padding(CalMongTheme.spacings.inset.default),
    ) {
        // content
    }
}
```

좌우와 상하의 의미가 다르면 같은 semantic 토큰을 축별로 조합할 수 있다.

```kotlin
Modifier.padding(
    horizontal = spacing.inset.default,
    vertical = spacing.inset.compact,
)
```

## Section

`section`은 내부 구성 요소가 아니라 의미 단위 콘텐츠 그룹 사이 거리다.

| 토큰 | 값 | 사용처 |
|---|---:|---|
| `section.related` | 24.dp | 직접 관련된 두 콘텐츠 그룹 |
| `section.default` | 32.dp | 일반적인 화면 section |
| `section.distinct` | 48.dp | 의미가 뚜렷하게 다른 section |
| `section.page` | 64.dp | major page block, 큰 화면 영역 |

```kotlin
Column(
    verticalArrangement =
        Arrangement.spacedBy(CalMongTheme.spacings.section.default),
) {
    GoalSummarySection()
    CalendarSection()
    RecommendationSection()
}
```

## 선택 순서

1. 거리가 container의 안쪽 경계에 붙어 있으면 `inset`을 선택한다.
2. 같은 그룹의 형제 사이면 `gap`을 선택한다.
3. 독립적인 의미 단위 사이면 `section`을 선택한다.
4. 역할을 고른 뒤 필요한 밀도 단계만 선택한다.

```text
Calendar Card
┌─────────────────────────────┐
│ inset/default               │
│  Title                      │
│  gap/compact                │
│  Description                │
│  gap/relaxed                │
│  Event list                 │
└─────────────────────────────┘

section/default

Recommendation Section
```

## System Inset과의 경계

`Scaffold`가 전달하는 `innerPadding`, `WindowInsets`, IME inset은 기기 환경이 결정하는
값이므로 semantic spacing으로 교체하지 않는다. 시스템 inset 바깥에 제품 여백이 더
필요할 때만 semantic inset을 추가한다.

```kotlin
Modifier
    .padding(innerPadding)
    .padding(horizontal = CalMongTheme.spacings.inset.default)
```

## 크기 토큰과의 경계

다음 값은 spacing과 우연히 같은 수치여도 spacing 토큰을 사용하지 않는다.

- 아이콘 크기
- Button, TextField의 높이
- avatar와 thumbnail 크기
- 최소 터치 영역
- 화면 breakpoint와 최대 content width

이 값들은 이후 별도의 size/layout 시스템에서 관리한다.

## 금지 예시

```kotlin
// 금지: 반복되는 관계를 raw dp로 지정
Column(verticalArrangement = Arrangement.spacedBy(12.dp))

// 금지: 크기를 spacing으로 표현
Icon(modifier = Modifier.size(CalMongTheme.spacings.inset.spacious))

// 금지: system inset을 고정 spacing으로 대체
Modifier.padding(top = CalMongTheme.spacings.section.page)

// 금지: 의미 없는 Spacer로 구조를 조립
Header()
Spacer(Modifier.height(32.dp))
Content()
```

## Figma 변수 구조

- `Spacing`: 단일 모드 primitive collection
- `Semantic Spacing`: 단일 모드 semantic collection
- 변수 경로: `gap/compact`, `inset/default`, `section/default`
- semantic 변수는 `Spacing` primitive를 alias한다.
- scope는 Auto Layout의 padding과 item spacing에 쓰는 `GAP`이다.

## 변경 체크리스트

1. `spacing.json` primitive 값을 확인한다.
2. 필요한 값만 `CalMongSpacing.kt`에 동기화한다.
3. `semantic.spacing.json`의 역할과 alias를 갱신한다.
4. `CalMongSpacings.kt`의 semantic 매핑을 갱신한다.
5. Figma `Semantic Spacing` 변수와 문서 페이지를 동기화한다.
6. `CalMongSpacingsTest`의 primitive와 semantic 계약을 갱신한다.
7. `SpacingTokenPreview`와 실제 화면 Preview를 확인한다.
8. 이 문서의 표와 예시를 갱신한다.
