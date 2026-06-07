# calmong 색상 토큰

색상을 3개 tier로 나눠 관리한다. 화면/컴포넌트는 **semantic 토큰만** 쓴다.

```
primitive (raw 팔레트)        →  semantic (의미 부여)         →  component
Tailwind indigo/600 …            primary.background.default       Button 배경
```

| tier | 정의 위치(Figma) | 정의 위치(코드) | 비고 |
|---|---|---|---|
| **primitive** | `color.json` (Tailwind Tokens 플러그인 export) | `theme/color/TailwindPalette.kt` | raw 팔레트. 직접 사용 금지 |
| **semantic** | `tokens/semantic.color.{light,dark}.json` | `theme/color/CalMongColorScheme.kt` + `…Schemes.kt` | primitive를 alias 참조 |

## 층위(layer) 구조

- **1단계**: `primary` · `secondary` · `neutral` · `functional`
- **2단계**
  - brand(`primary`/`secondary`) · `neutral` → `background` / `foreground` / `stroke`
  - `functional` → `common` / `general` / `specific`
- **3단계**: 각 2단계의 variant
  - `background`: base, default, raised1, raised2, dimmed, inverted *(brand는 default/subtle/bold/dimmed/inverted)*
  - `foreground`: static, default, subtle, decorative, alpha, inverted *(brand는 default/subtle/inverted)*
  - `stroke`: divider, subtle, default, static *(brand는 default/subtle)* — **각 변형이 상태별 색을 가짐(아래 4단계)**
  - `functional.common.*`: default, decorative, subtle
  - `functional.general`: overlay, highlight, shadow, disabled
  - `functional.specific`: like, link
- **4단계**: 상호작용 상태(`InteractionStates`) — default, hover, focused, pressed, activated, disabled
  - `functional.stateLayer.{soft|solid}`: 컴포넌트 위에 덧대는 **반투명 오버레이**. 요소 밝기로 고른다(테마 모드 무관) — `soft`=옅은 요소 위(검정 알파), `solid`=짙은 요소 위(흰색 알파). 배경/채움 상태는 이 레이어를 z축으로 올려 표현(배경색 자체는 안 바꿈).
  - `*.stroke.{divider|subtle|default|static}` · `brand.stroke.{default|subtle}`: 외곽선은 색이 직접 바뀌므로 각 stroke 변형이 상태별 **불투명** 색을 보유. `default`/`static`은 비텍스트 대비 3:1 충족, `divider`/`subtle`은 장식용 헤어라인.

### 콘텐츠 색 규칙 (onColor 없음)
- 브랜드 채움 위 콘텐츠 = `brand.foreground.default` (채움 위에서 접근성 충족하도록 매핑됨)
- functional은 **`subtle` 배경 + `default` 텍스트** 패턴 권장 (솔리드 채움 위 텍스트는 지양 — 대비 한계)
- 이미지/스크림/어두운 고정면 위 콘텐츠 = `neutral.foreground.static` (모드 독립 흰색)
- `static`은 모드와 무관하게 동일(흰색). 테마 표면 위 본문은 `foreground.default`를 쓴다.

### brand 매핑
- `primary` = Indigo, `secondary` = Amber

### functional 매핑
- `positive` = Green, `negative` = Red, `informative`/`link` = Blue, `attention` = Amber, `like` = Rose

## 코드에서 쓰기

```kotlin
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

CalMongTheme {                                   // light/dark 자동 (system 따름)
    Surface(color = CalMongTheme.colors.neutral.background.base) {
        Text(
            text = "...",
            color = CalMongTheme.colors.neutral.foreground.default,
        )
    }
}
```

Material3 컴포넌트(Button/Card 등)는 `CalMongTheme`이 `MaterialTheme.colorScheme`로 매핑해 두므로 자동으로 브랜드 색을 따른다. calmong 고유 토큰이 필요할 때만 `CalMongTheme.colors`를 직접 쓴다.

상태 처리 예:

```kotlin
val colors = CalMongTheme.colors

// 배경/채움: 컴포넌트 위에 stateLayer를 덧댐 (짙은 채움이면 solid, 옅으면 soft)
Box(Modifier.background(colors.primary.background.default)) {     // 짙은 Indigo 채움
    Text("확인", color = colors.primary.foreground.default)        // 흰 콘텐츠
    if (pressed) Box(Modifier.matchParentSize().background(colors.functional.stateLayer.solid.pressed))
}

// 외곽선: stroke 색 자체를 상태에 맞게 선택
val borderColor = if (focused) colors.neutral.stroke.default.focused else colors.neutral.stroke.default.default
```

## Figma에 semantic 토큰 반영하기

`tokens/semantic.color.{light,dark}.json`은 DTCG 포맷이며 값이 primitive를 `{indigo.600}` 형태로 **alias**한다. 그대로 import하면 Light/Dark 2개 모드를 가진 "Semantic" 변수 컬렉션이 된다.

> 주의: Tailwind Tokens 플러그인은 *import 전용*이라 이 파일을 직접 못 읽는다.
> alias 토큰 import는 **Tokens Studio for Figma**(무료)나 Figma 변수 import 플러그인을 쓴다.

### Tokens Studio 기준 순서
1. Tokens Studio 플러그인 설치 → 파일 패널에서 primitive(`color.json`)를 한 set으로 먼저 로드 (alias가 풀리도록).
2. `semantic.color.light.json` / `semantic.color.dark.json`를 각각 set으로 추가 import.
3. **Themes**에서 Light/Dark 테마를 만들고 각 semantic set을 매핑.
4. "Export to Figma variables" → primitive와 semantic 컬렉션이 생성되고 semantic이 primitive를 참조.

### alias가 안 풀릴 때
primitive 변수의 경로명이 `indigo/600`(슬래시)인지 확인. alias `{indigo.600}`(점)은 같은 계층을 가리킨다. primitive set이 먼저 로드돼 있어야 resolve된다.

## 값 바꿀 때

semantic 값은 **코드와 Figma 두 곳**에 있다. 한쪽을 바꾸면 다른 쪽도 맞춘다.
- 코드: `theme/color/CalMongColorSchemes.kt`
- Figma JSON: `tokens/semantic.color.{light,dark}.json`
