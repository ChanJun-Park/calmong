# CalMong Radius System

CalMong의 radius 시스템은 수치가 아니라 **컴포넌트 역할**을 기준으로 사용한다.
화면과 컴포넌트에서 `RoundedCornerShape(12.dp)`를 직접 만들지 않고
`CalMongTheme.shapes`의 semantic shape을 선택한다.

Figma 문서:
[Design System / Radius System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=38-2)

```text
primitive radius      semantic shape            component
radius/xl (12)   ->   surface.card          ->  Calendar Card
radius/full      ->   control.pill          ->  Filter Chip
```

## Source of truth

| 대상 | 위치 |
|---|---|
| Figma primitive | `radius.json` |
| Figma semantic alias | `tokens/semantic.radius.json` |
| Kotlin primitive | `src/main/kotlin/.../theme/shape/CalMongRadius.kt` |
| Semantic shape | `src/main/kotlin/.../theme/shape/CalMongShapes.kt` |
| Theme 연결 | `src/main/kotlin/.../theme/Theme.kt` |
| Preview | `src/main/kotlin/.../theme/shape/ShapeTokenPreview.kt` |

Radius는 Light/Dark에 따라 바뀌지 않으므로 단일 모드로 관리한다.

## 기본 규칙

1. 제품 코드에서 `RoundedCornerShape(...)`를 직접 생성하지 않는다.
2. radius 크기가 아니라 `control`, `surface`, `content` 역할로 선택한다.
3. 중첩된 자식의 radius는 부모보다 크지 않아야 한다.
4. 부모와 자식 사이의 안쪽 여백이 작을수록 자식 radius도 작아져야 한다.
5. `full`은 의도적으로 원형이나 capsule인 요소에만 사용한다.
6. Bottom Sheet는 위쪽 모서리에만 `surface.sheet`을 적용한다.
7. 이미지와 thumbnail은 포함하는 surface보다 같거나 작은 radius를 사용한다.
8. 새로운 수치가 필요하면 기존 primitive로 표현할 수 없는 이유부터 확인한다.
9. semantic 역할이 없다면 primitive를 직접 쓰지 말고 역할을 먼저 추가한다.

## Primitive Scale

Primitive는 Figma와 Kotlin 사이의 1:1 값이다. 제품 코드에서 직접 사용하지 않는다.

| Figma | Kotlin | 값 | 용도 |
|---|---|---:|---|
| `none` | `none` | 0.dp | 사각형, edge-to-edge 영역 |
| `sm` | `sm` | 2.dp | 매우 미세한 rounding |
| `DEFAULT` | `base` | 4.dp | Material extra-small |
| `md` | `md` | 6.dp | compact control |
| `lg` | `lg` | 8.dp | 기본 control, 작은 surface |
| `xl` | `xl` | 12.dp | 큰 control, card, image |
| `2xl` | `xxl` | 16.dp | elevated surface |
| `3xl` | `xxxl` | 24.dp | dialog, sheet |
| `full` | `full` | 999.dp | capsule, avatar, badge |

`DEFAULT`, `2xl`, `3xl`은 Kotlin 식별자 규칙에 맞춰 각각 `base`, `xxl`,
`xxxl`로 표현한다.

## 사용 진입점

```kotlin
@Composable
fun CalendarCard() {
    Surface(
        shape = CalMongTheme.shapes.surface.card,
        color = CalMongTheme.colors.neutral.background.default,
    ) {
        // content
    }
}
```

Material3 컴포넌트는 `CalMongTheme`이 `MaterialTheme.shapes`를 제공하므로
기본 shape을 그대로 사용할 수 있다. CalMong 고유 역할이 필요할 때
`CalMongTheme.shapes`를 사용한다.

## Control Shapes

Control은 사용자가 누르거나 값을 입력하는 요소다.

| 토큰 | Radius | 사용처 |
|---|---:|---|
| `control.compact` | 6.dp | 작은 입력, compact 버튼, 좁은 toolbar control |
| `control.default` | 8.dp | 일반 버튼, 입력 필드, checkbox container |
| `control.large` | 12.dp | 큰 CTA, 검색창, 넓은 선택 control |
| `control.pill` | 999.dp | Chip, segmented item, capsule button |

### 선택 기준

- 높이 32.dp 안팎의 조밀한 control은 `compact`를 우선한다.
- 일반적인 40~48.dp control은 `default`를 사용한다.
- 48.dp 이상의 강조 control은 `large`를 사용할 수 있다.
- 양 끝이 완전히 둥근 형태 자체가 의미일 때만 `pill`을 사용한다.

```kotlin
Button(
    onClick = onClick,
    shape = CalMongTheme.shapes.control.default,
) {
    Text("저장")
}
```

## Surface Shapes

Surface는 콘텐츠를 묶고 elevation 또는 계층을 만드는 컨테이너다.

| 토큰 | Radius | 사용처 |
|---|---:|---|
| `surface.small` | 8.dp | Tooltip, 작은 popup, compact panel |
| `surface.card` | 12.dp | 일반 Card, 일정 Card |
| `surface.elevated` | 16.dp | Menu, floating Card, elevated panel |
| `surface.sheet` | 상단 24.dp | Bottom Sheet |
| `surface.dialog` | 24.dp | Dialog, 큰 modal surface |

Surface의 radius는 elevation과 크기가 커질수록 증가하지만, elevation만으로
무조건 큰 radius를 사용하지 않는다. 화면 내 시각적 위계와 컨테이너 크기를 함께
고려한다.

```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    shape = CalMongTheme.shapes.surface.sheet,
) {
    // sheet content
}
```

## Content Shapes

Content는 surface 안에 들어가는 이미지, avatar, badge 같은 시각 콘텐츠다.

| 토큰 | Radius | 사용처 |
|---|---:|---|
| `content.thumbnail` | 8.dp | 일정 목록 thumbnail, 작은 미디어 |
| `content.image` | 12.dp | 일반 콘텐츠 이미지, hero image |
| `content.avatar` | 999.dp | 사용자 avatar, pet portrait |
| `content.badge` | 999.dp | 상태 badge, count badge |

```kotlin
Image(
    painter = painter,
    contentDescription = null,
    modifier =
        Modifier
            .size(48.dp)
            .clip(CalMongTheme.shapes.content.avatar),
)
```

## 중첩 규칙

같은 방향의 모서리가 중첩될 때 안쪽 radius는 바깥 radius보다 작거나 같아야 한다.

```text
권장
surface.dialog 24
  content.image 12

surface.card 12
  content.thumbnail 8

금지
surface.card 12
  child container 24
```

부모와 자식 사이에 padding이 있는 경우 다음 관계를 기준으로 한다.

```text
inner radius ≈ max(outer radius - padding, 0)
```

정확한 계산 토큰을 매번 만들기보다 현재 semantic scale에서 가장 가까운 작은
역할을 선택한다.

## Component Recipes

| 컴포넌트 | Shape |
|---|---|
| 기본 Button | `control.default` |
| 큰 CTA Button | `control.large` |
| Filter Chip | `control.pill` |
| Text Field | `control.default` |
| Search Field | `control.large` |
| Calendar Card | `surface.card` |
| Floating Menu | `surface.elevated` |
| Tooltip | `surface.small` |
| Bottom Sheet | `surface.sheet` |
| Dialog | `surface.dialog` |
| Thumbnail | `content.thumbnail` |
| Content Image | `content.image` |
| Avatar / Pet Portrait | `content.avatar` |
| Badge | `content.badge` |

## Material3 Mapping

`CalMongTheme`은 Material3의 범용 shape scale을 다음과 같이 연결한다.

| Material3 | Radius |
|---|---:|
| `extraSmall` | 4.dp |
| `small` | 8.dp |
| `medium` | 12.dp |
| `large` | 16.dp |
| `extraLarge` | 24.dp |

Material3 shape은 라이브러리의 일반적인 크기 체계다. `surface.sheet`처럼 특정
모서리만 둥근 CalMong 역할은 `CalMongTheme.shapes`에서 직접 가져온다.

## 금지 예시

```kotlin
// 금지: 화면에서 수치 직접 사용
Surface(shape = RoundedCornerShape(12.dp))

// 금지: primitive를 제품 코드에서 직접 사용
Surface(shape = RoundedCornerShape(CalMongRadius.xl))

// 금지: 역할과 무관하게 모두 full 적용
Card(shape = CalMongTheme.shapes.control.pill)

// 금지: 자식이 부모보다 더 둥근 중첩
Surface(shape = CalMongTheme.shapes.surface.card) {
    Surface(shape = CalMongTheme.shapes.surface.dialog) {}
}
```

## 변경 체크리스트

1. `radius.json`에 primitive 값과 `CORNER_RADIUS` scope를 정의한다.
2. `tokens/semantic.radius.json`에 역할과 primitive alias를 정의한다.
3. `CalMongRadius.kt`를 같은 값으로 동기화한다.
4. `CalMongShapes.kt`에 semantic 역할을 추가하거나 매핑을 변경한다.
5. 필요한 경우 `calMongMaterialShapes()` 매핑을 갱신한다.
6. Figma Radius 변수와 문서 페이지를 동기화한다.
7. `CalMongShapesTest`의 primitive, semantic, Material3 계약을 갱신한다.
8. `ShapeTokenPreview`와 실제 컴포넌트 Preview를 확인한다.
9. 이 문서의 표와 component recipe를 갱신한다.
