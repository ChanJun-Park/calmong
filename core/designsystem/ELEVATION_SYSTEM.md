# CalMong Elevation System

CalMong의 elevation은 shadow 수치가 아니라 **표면의 계층 역할**을 기준으로 선택한다.
Light에서는 shadow가 깊이를 주로 표현하고, Dark에서는 밝아지는 surface 색이 깊이를
주로 표현한다.

Surface 내부 여백과 요소 간격은
[`SPACING_SYSTEM.md`](SPACING_SYSTEM.md)를 함께 따른다.

Figma 문서:
[Design System / Elevation System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=47-2)

```text
primitive shadow       semantic elevation          component
shadow/sm         ->   raised1                 ->  Calendar Card
shadow/lg         ->   overlay                 ->  Floating Menu
```

## Source of truth

| 대상 | 위치 |
|---|---|
| Figma primitive | `shadow.json` |
| Figma semantic alias | `tokens/semantic.elevation.{light,dark}.json` |
| Kotlin primitive | `src/main/kotlin/.../theme/elevation/CalMongShadows.kt` |
| Semantic elevation | `src/main/kotlin/.../theme/elevation/CalMongElevations.kt` |
| Theme 연결 | `src/main/kotlin/.../theme/Theme.kt` |
| Preview | `src/main/kotlin/.../theme/elevation/ElevationTokenPreview.kt` |

## 기본 규칙

1. 제품 코드에서 `Modifier.shadow(...)`나 `dropShadow(...)` 수치를 직접 만들지 않는다.
2. `CalMongTheme.elevations`의 역할을 선택하고 `surface`와 `shadows`를 함께 사용한다.
3. shadow에 전달한 shape과 실제 `Surface`의 shape은 같아야 한다.
4. Dark에서는 일반 surface에 shadow를 추가하지 않고 `raised1`, `raised2` 색을 사용한다.
5. `overlay`, `modal`은 다른 콘텐츠 위에 실제로 떠 있는 임시 surface에만 사용한다.
6. pressed 상태를 shadow 증감으로 표현하지 않는다. 색상 시스템의 state layer를 쓴다.
7. shadow만으로 정보나 접근성 상태를 전달하지 않는다.
8. custom elevation을 적용한 Material3 surface는 `shadowElevation`과
   `tonalElevation`을 `0.dp`로 둔다.

## Primitive Scale

Primitive는 Figma와 Kotlin 사이의 1:1 값이다. 제품 코드에서 직접 사용하지 않는다.

| Figma | Kotlin | Layer | offset | blur | spread | color |
|---|---|---:|---|---:|---:|---|
| `none` | `none` | - | 0, 0 | 0 | 0 | transparent |
| `2xs` | `xxs` | 1 | 0, 1 | 0 | 0 | black 5% |
| `xs` | `xs` | 1 | 0, 1 | 2 | 0 | black 5% |
| `sm` | `sm` | 1 | 0, 1 | 3 | 0 | black 10% |
| `sm` | `sm` | 2 | 0, 1 | 2 | -1 | black 10% |
| `DEFAULT` | `base` | 1 | 0, 4 | 6 | -1 | black 10% |
| `DEFAULT` | `base` | 2 | 0, 2 | 4 | -2 | black 10% |
| `md` | `md` | 1 | 0, 10 | 15 | -3 | black 10% |
| `md` | `md` | 2 | 0, 4 | 6 | -4 | black 10% |
| `lg` | `lg` | 1 | 0, 20 | 25 | -5 | black 10% |
| `lg` | `lg` | 2 | 0, 8 | 10 | -6 | black 10% |
| `2xl` | `xxl` | 1 | 0, 25 | 50 | -12 | black 25% |
| `inner` | `inner` | 1 | 0, 2 | 4 | 0 | black 5% |

Figma의 `xl`은 `lg`와 값이 같아서 Kotlin에서는 `xl = lg` alias로 유지한다.
`inner`는 primitive 호환을 위해 보존하지만 현재 semantic elevation에는 연결하지 않는다.

## Semantic Roles

| 역할 | Light surface / shadow | Dark surface / shadow | 사용처 |
|---|---|---|---|
| `flat` | `default` / none | `default` / none | 화면과 같은 평면의 section |
| `raised1` | `default` / `sm` | `raised1` / none | Card, 고정 panel |
| `raised2` | `default` / `DEFAULT` | `raised2` / none | floating Card, 강조 panel |
| `overlay` | `default` / `lg` | `raised2` / `sm` | Menu, dropdown, tooltip |
| `modal` | `default` / `2xl` | `raised2` / `xs` | Dialog, modal surface |

Dark의 `overlay`와 `modal`에 남는 작은 shadow는 겹치는 경계를 보조한다. 주된 위계는
`neutral.background.raised1`과 `raised2`가 만든다.

## Compose 사용법

```kotlin
@Composable
fun CalendarCard() {
    val elevation = CalMongTheme.elevations.raised1
    val shape = CalMongTheme.shapes.surface.card

    Surface(
        modifier = Modifier.elevationShadow(elevation, shape),
        shape = shape,
        color = elevation.surface,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
    ) {
        // content
    }
}
```

`elevationShadow`는 Figma primitive의 다중 shadow layer를 순서대로 적용한다. shadow 뒤에
`clip`을 먼저 적용하면 바깥 shadow가 잘릴 수 있으므로 shadow를 적용한 뒤 `Surface`가
shape clipping을 담당하게 한다.

## Component Recipes

| 컴포넌트 | Elevation | Shape |
|---|---|---|
| 일반 Calendar Card | `raised1` | `surface.card` |
| 선택된 Floating Card | `raised2` | `surface.elevated` |
| Dropdown / Menu | `overlay` | `surface.elevated` |
| Tooltip | `overlay` | `surface.small` |
| Dialog | `modal` | `surface.dialog` |
| Bottom Sheet | `overlay` | `surface.sheet` |
| 화면 내부 section | `flat` | 문맥에 맞는 surface shape |

## Material3와의 관계

`CalMongTheme`은 Material3 typography를 그대로 사용하지만 elevation은 CalMong의
Light/Dark 규칙을 사용한다. Material3 `Card`, `Surface`, `Dialog`의 기본 elevation과
custom elevation을 중복 적용하지 않는다.

```kotlin
// 금지: Material shadow와 CalMong shadow가 중복됨
Surface(
    modifier = Modifier.elevationShadow(elevation, shape),
    shadowElevation = 6.dp,
)

// 금지: 표면색을 빼서 Dark hierarchy가 사라짐
Surface(
    modifier = Modifier.elevationShadow(elevation, shape),
    color = MaterialTheme.colorScheme.surface,
)
```

## Figma 변수 구조

- `Shadow`: 단일 모드 primitive collection
- `Elevation`: `Light`, `Dark` 모드를 가진 semantic collection
- semantic surface variable 경로: `raised1/surface`
- semantic effect variable 경로:
  `raised1/layer1/color`, `raised1/layer1/offsetY`,
  `raised1/layer2/radius` 등
- surface 변수는 `Semantic` color collection의
  `neutral/background/{default,raised1,raised2}`에 alias한다.

Figma effect는 하나의 변수가 다중 layer 전체를 표현할 수 없으므로 각 layer의
`color`, `offsetX`, `offsetY`, `radius`, `spread`를 primitive Shadow 변수에 alias한다.

## 변경 체크리스트

1. `shadow.json` primitive 값을 확인한다.
2. `CalMongShadows.kt`를 같은 값으로 동기화한다.
3. `semantic.elevation.{light,dark}.json`의 alias를 갱신한다.
4. `CalMongElevations.kt`의 surface와 shadow 역할 매핑을 갱신한다.
5. Figma `Elevation` 변수와 문서 페이지를 동기화한다.
6. `CalMongElevationsTest`의 primitive와 semantic 계약을 갱신한다.
7. Light/Dark `ElevationTokenPreview`를 확인한다.
8. 이 문서의 역할 표와 component recipe를 갱신한다.
