# Paparazzi 스크린샷 테스트 도입 + CalMongButton 시각 회귀 고정

> 작성일: 2026-06-21
> 관련 커밋: fd3e0da (도입 + 갤러리 golden), 111c3bc (상호작용 상태 golden)
> 목표: 그동안 컴파일로만 검증하던 컴포넌트의 *생김새*(색·여백·상태 표현)를 golden 이미지로 고정해, 눈으로만 잡히던 시각 회귀를 자동으로 잡는다.

## 한눈에 보기
- Paparazzi(JVM에서 layoutlib로 Compose를 렌더 → PNG)로 스크린샷 테스트를 `:core:designsystem`에 도입.
- 최대 리스크였던 **AGP 9.2.1 호환성**을 추측 대신 실제 record/verify로 확인 → 동작함.
- `CalMongButton` 갤러리(intent×size×rest)와 상호작용 상태 매트릭스(hover/pressed/focused/disabled)를 light/dark로 고정 → golden 4장.
- 정적 렌더에서 상호작용 상태를 **결정적으로** 강제하는 기법(`MutableStyleState` 플래그 직접 세팅)을 확립.
- 결과물:
  - `gradle/libs.versions.toml` — `paparazzi` 버전 + 플러그인 alias
  - `core/designsystem/build.gradle.kts` — 플러그인 적용 1줄
  - `core/designsystem/src/test/.../component/CalMongButtonScreenshotTest.kt`
  - `core/designsystem/src/test/.../component/CalMongButtonStateScreenshotTest.kt`
  - `core/designsystem/src/test/snapshots/images/*.png` — golden 4장 (git에 커밋)

## 1. Paparazzi 플러그인 도입

### 배경 / 왜
- 그전까지 컴포넌트 검증은 "컴파일 + detekt + spotless"가 전부였다. 색·여백·상태 오버레이가 의도대로 보이는지는 **눈으로 확인 불가**였고, 실제로 "soft/solid 선택과 press scale은 봐야 안다"고 계속 미뤄둔 상태였다.
- 선택지: ① Paparazzi(layoutlib, 에뮬레이터 불필요, JVM 단위테스트) ② Roborazzi(Robolectric 기반) ③ 공식 Compose Preview Screenshot Testing(`com.android.compose.screenshot`). 사용자가 **Paparazzi**를 지정.
- 가장 큰 불확실성: 이 프로젝트는 **AGP 9.2.1**(tools 32.x)인데, Paparazzi 최신(`2.0.0-alpha05`)도 내부적으로 `com.android.tools` **31.13.2 = AGP 8.13** 기준이다. Paparazzi는 AGP 내부 API에 깊게 의존해 **메이저 버전(8→9) 차이는 보통 깨진다**. → "추측하지 말고 실제로 붙여 확인"으로 접근.

### 무엇을 했나
1. 버전 카탈로그에 버전 + 플러그인 alias 추가 (`gradle/libs.versions.toml`):
   ```toml
   [versions]
   paparazzi = "2.0.0-alpha05"

   [plugins]
   paparazzi = { id = "app.cash.paparazzi", version.ref = "paparazzi" }
   ```
2. `:core:designsystem`에 플러그인 적용 (`core/designsystem/build.gradle.kts`):
   ```kotlin
   plugins {
       id("calmong.android.library")
       id("calmong.android.library.compose")
       alias(libs.plugins.paparazzi)
   }
   ```
   → convention plugin으로 추출하지 않고 **직접 적용**. 이유: 지금 스크린샷이 필요한 모듈은 여기 하나뿐 → YAGNI. 다른 모듈(`:feature:*`)도 필요해지면 그때 `calmong.android.test.screenshot` 같은 convention plugin으로 추출한다.

### 직접 해보기
1. 위 두 파일을 수정한다.
2. 플러그인이 AGP 9에서 *configuration*이라도 되는지 먼저 확인:
   ```bash
   ./gradlew :core:designsystem:tasks --group=verification
   ```
   → `recordPaparazzi(Debug)` / `verifyPaparazzi(Debug)` 태스크가 보이면 configuration 통과.

### 알아두면 좋은 점
- configuration 통과 ≠ 렌더링 통과. **진짜 관문은 layoutlib 렌더링**이라 golden을 실제로 record해봐야 안다(→ 2장).
- 환경 요건: layoutlib는 JDK 17+ 필요. 이 환경은 `java -version` = 17, Gradle 데몬 JVM = 21이라 충족.
- Paparazzi 버전 조회:
  ```bash
  curl -s "https://repo1.maven.org/maven2/app/cash/paparazzi/paparazzi-gradle-plugin/maven-metadata.xml" | grep -oE "<release>[^<]+"
  ```
- 호환 버전 추정: 플러그인 POM에서 `com.android.tools` 버전을 보면 AGP 환산값을 알 수 있다(AGP = tools − 23). alpha05 → tools 31.13.2 = AGP 8.13. 즉 **공식 지원 매트릭스상으론 AGP 9 미지원인데, 실제로는 돌았다.**

## 2. 갤러리 golden — CalMongButton 기본 시각 고정

### 배경 / 왜
- 첫 Style API 컴포넌트라 intent(5종)×size(3종)×disabled의 *생김새*를 고정해, 이후 토큰/레시피 변경 시 회귀를 잡고 싶었다.

### 무엇을 했나
- `CalMongButtonScreenshotTest.kt` 작성. 핵심:
  ```kotlin
  @get:Rule
  val paparazzi = Paparazzi(
      deviceConfig = DeviceConfig.PIXEL_5,
      renderingMode = RenderingMode.SHRINK,   // 디바이스 프레임 대신 콘텐츠 크기에 맞춰 자름
  )

  @Test fun gallery_light() { paparazzi.snapshot { ButtonGallery(darkTheme = false) } }
  @Test fun gallery_dark()  { paparazzi.snapshot { ButtonGallery(darkTheme = true) } }
  ```
- 다크/라이트는 `CalMongTheme(darkTheme = ...)`에 **명시적으로 인자를 넘겨** 구분(`isSystemInDarkTheme()`에 의존하지 않음).
- contentColor가 자식 `Text`로 상속되도록 스냅샷 안에서 `ComposeFoundationFlags.isInheritedTextStyleEnabled = true` 설정(앱 진입점에서 1회 설정하는 것과 동일 역할).

### 직접 해보기
1. golden 생성:
   ```bash
   ./gradlew :core:designsystem:recordPaparazziDebug
   ```
2. 생성물 확인 — `core/designsystem/src/test/snapshots/images/*.png`. PNG를 직접 열어 **눈으로** 의도대로인지 본다.
3. 검증(멱등성) — 코드/golden을 안 바꾸면 통과해야 한다:
   ```bash
   ./gradlew :core:designsystem:verifyPaparazziDebug
   ```

### 알아두면 좋은 점
- golden PNG는 **git에 커밋**한다(이게 회귀의 기준선). 실패 diff는 `build/` 아래로 떨어져 자동 무시됨.
- `CalMongButton(style = ...)`의 `style` 파라미터가 실험 API라, 호출하는 테스트 파일도 `@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)`가 필요하다(둘 다). 처음에 `ExperimentalFoundationStyleApi`를 빠뜨려 컴파일 실패 → 추가로 해결.

## 3. 상호작용 상태 golden — 정적 렌더에서 상태를 강제하는 법 (핵심 기법)

### 배경 / 왜
- Style API의 진짜 가치는 hover/pressed/focused/disabled 같은 **상태 표현**인데, 스크린샷은 정적 1프레임이다. "어떻게 상태를 켜느냐"가 관건.
- 자연스러운 후보: `interactionSource`에 `PressInteraction.Press` 등을 emit. 하지만 ① emit→수집이 **비동기**(LaunchedEffect 수집, 구독 전 emit은 유실 가능) ② `animate { }`가 rest→target으로 진행 중인 **중간 프레임**이 잡힐 수 있어 **결정적이지 않다**.

### 무엇을 했나
- `MutableStyleState`의 상태 플래그를 **직접 세팅**하는 방식 채택. `javap`로 확인한 public setter:
  ```
  setEnabled / setHovered / setPressed / setFocused / setSelected ...
  ```
- 상태를 컴포지션 시점에 확정하면, `animate { }`가 **목표값에서 시작**(첫 프레임이 곧 settle 상태) → 매번 동일한 golden. 이는 `docs/compose-styles/03-state-animations.md`의 MediaPlayer 패턴(`styleState.playerState = state`)과 동일한 발상.
- `CalMongButtonStateScreenshotTest.kt`의 칩 렌더 핵심:
  ```kotlin
  val state = remember { MutableStyleState(MutableInteractionSource()) }
  state.apply {                 // 매 컴포지션 기본값 리셋 후 해당 상태만 ON (write-before-read)
      isEnabled = true; isHovered = false; isPressed = false; isFocused = false
      configure()               // 예: { isPressed = true }
  }
  Box(Modifier.styleable(state, calMongButtonStyle(intent) then CalMongPressEffect)) { Text(text) }
  ```
- `rememberUpdatedStyleState`(비동기 수집)를 **쓰지 않고** 순수 `MutableStyleState`를 쓰는 게 포인트 — 그래야 수동 플래그가 덮어써지지 않는다. `styleable`은 상태를 읽기만 하지 구독하지 않는다.
- 구성: 행=상태(Rest/Hovered/Pressed/Focused/Disabled), 열=intent(Primary/Neutral/Inverted), light/dark 2장.

### 직접 해보기
1. 특정 테스트만 record:
   ```bash
   ./gradlew :core:designsystem:recordPaparazziDebug --tests "*CalMongButtonStateScreenshotTest*"
   ```
2. PNG를 열어 확인 — Pressed 행에서 칩이 **작아졌는지**(scale 0.96), Hovered에서 오버레이로 색이 미묘하게 달라졌는지, Focused에 인디고 링이 있는지, Disabled가 흐린지.

### 알아두면 좋은 점
- 결과로 그동안 못 보던 게 전부 확인됨: `CalMongPressEffect`의 pressed scale, `stateLayer.{soft|solid}`를 base에 `compositeOver`한 hover/pressed 색, focus 링, disabled alpha, 다크에서 Inverted가 흰 표면으로 역상.
- 이 기법은 앞으로 만들 **모든 인터랙터블 컴포넌트의 상태 스냅샷 표준**으로 재사용한다.

## 핵심 명령어·파일 모음
```bash
# golden 생성/갱신 (의도대로 바뀐 뒤)
./gradlew :core:designsystem:recordPaparazziDebug
# 특정 테스트만
./gradlew :core:designsystem:recordPaparazziDebug --tests "*CalMongButtonStateScreenshotTest*"
# 검증 (기준선과 비교 — CI/리뷰용)
./gradlew :core:designsystem:verifyPaparazziDebug
# 전체 게이트
./gradlew :core:designsystem:verifyPaparazziDebug :core:designsystem:detekt :core:designsystem:spotlessCheck

# 사용 가능한 Paparazzi 버전 조회
curl -s "https://repo1.maven.org/maven2/app/cash/paparazzi/paparazzi-gradle-plugin/maven-metadata.xml" | grep -oE "<release>[^<]+"
```
```
gradle/libs.versions.toml                                  # paparazzi 버전 + 플러그인 alias
core/designsystem/build.gradle.kts                         # alias(libs.plugins.paparazzi)
core/designsystem/src/test/.../component/CalMongButtonScreenshotTest.kt
core/designsystem/src/test/.../component/CalMongButtonStateScreenshotTest.kt
core/designsystem/src/test/snapshots/images/*.png          # golden 4장 (커밋 대상)
```

## 함정 / 주의
- **AGP 9 미지원 표기 ≠ 실제 미동작**: Paparazzi alpha05는 공식적으로 AGP 8.13 기준이지만 AGP 9.2.1에서 record/verify가 돌았다. 추측으로 포기하지 말고 한 번 돌려볼 것. (단, alpha라 향후 버전업 시 깨질 수 있음 — verify가 회귀 감지 역할.)
- **실험 API opt-in 누락**: 상태 selector(`pressed`/`hovered`/...)와 `then`, 그리고 `CalMongButton(style=)` 호출 모두 import/opt-in 필요. 테스트 파일 헤더에 `ExperimentalFoundationApi` + `ExperimentalFoundationStyleApi` 둘 다 넣는다.
- **레이아웃 오버플로는 컴파일로 안 잡힌다**: 첫 상태 매트릭스 record에서 라벨(88dp)+칩 3열이 `PIXEL_5` 폭(393dp)을 넘어, 3열이 화면 밖으로 밀려 오른쪽에 **세로 띠로 잘려** 나왔다. → `DeviceConfig.PIXEL_5.copy(screenWidth = 1600, screenHeight = 2200)`로 캔버스를 넓혀 해결(`RenderingMode.SHRINK`가 콘텐츠에 맞춰 다시 자름). **이게 스크린샷 테스트의 효용** — 눈으로만 잡히는 회귀.
- **상태가 비동기로 들어오면 비결정적**: `interactionSource.emit(...)` 대신 `MutableStyleState` 플래그를 직접 세팅해야 settle된 프레임이 결정적으로 잡힌다.
