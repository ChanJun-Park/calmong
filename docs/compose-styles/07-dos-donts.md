# 07. Do's and Don'ts — 베스트 프랙티스

> 원문: <https://developer.android.com/develop/ui/compose/styles/dos-donts> · 갱신 2026-06-11

## ✅ Do
- **시각은 Styles, 동작은 Modifiers** — 배경/패딩/외곽선은 Style, 클릭/제스처/접근성은 modifier.
- **디자인 시스템 컴포넌트는 `style` 파라미터 노출** — modifier 파라미터 *뒤*에 둔다.
  ```kotlin
  fun GradientButton(modifier: Modifier = Modifier, style: Style = Style) { /* consume */ }
  ```
- **시각 파라미터를 Style로 대체** — `fun OldButton(background: Color, fontColor: Color)` → `fun NewButton(style: Style = Style)`.
- **애니메이션은 Style 우선** — 상태 기반 스타일엔 내장 `animate { }`(modifier 대비 성능 이득).
- **last-write-wins 활용** — 다중 파라미터 없이 기본 border/배경을 덮어쓰기.
- **서브시스템 값 변경엔 단일 Style** — 라이트/다크 전환 등은 `CompositionLocal`/테마 값으로 *하나의* style을 동적 구성.
  ```kotlin
  val buttonStyle = Style { background(colors.brandSecondary); shape(shapes.small) }
  ```
- **근본적으로 다른 테마는 Style 통째 교체** — 화이트라벨처럼 다수 속성이 다르면 테마 레벨에서 style 세트를 스왑.

## ❌ Don't
- **상호작용 로직을 Style에 넣지 말 것** — `onClick`/제스처/비즈니스 로직 금지. Style은 "상태에 따른 다른 *시각*"만.
- **기본 style을 기본 파라미터로 주지 말 것**
  ```kotlin
  // ❌
  fun BadButton(style: Style = Style { background(Color.Red) })
  // ✅ 항상 style: Style = Style 로 받고, 기본은 내부에서 merge
  fun GoodButton(style: Style = Style) {
      val defaultStyle = Style { background(Color.Red) }
      Box(Modifier.styleable(styleState, defaultStyle, style)) { ... }
  }
  ```
- **레이아웃/화면 레벨 컴포저블에 style 파라미터 주지 말 것** — 소비자 입장에서 의미가 불명확. Style은 *컴포넌트*용.
- **Composition에서 style 만들지 말 것 (중요 함정)**
  `CompositionLocal`은 **style이 정의되는 지점**에서 읽힌다(소비 지점 아님). 정의 후 값이 바뀌면 부정확해진다.
  ```kotlin
  // ❌ @Composable 함수가 MaterialTheme.colorScheme을 읽어 Style 반환
  @Composable fun containerStyle(): Style {
      val bg = MaterialTheme.colorScheme.background
      return Style { background(bg) } // 값이 굳어버림
  }
  // ✅ StyleScope 확장으로 소비 시점에 읽기
  val StyleScope.colors get() = JetsnackTheme.LocalJetsnackTheme.currentValue.colors
  val button = Style { background(colors.brandSecondary); shape(shapes.small) }
  ```

## calmong 메모
- 우리 컴포넌트 컨벤션과 정합: `CalMongToast(modifier, style: Style = Style)`처럼 노출하고, 내부에서 `defaultStyle then style` merge. 노출/dismiss/접근성은 지금처럼 호출부·modifier.
- **반드시 `StyleScope` 확장으로 `CalMongTheme` 토큰 접근** — `@Composable`가 토큰 읽어 Style 반환하는 패턴은 금지(라이트/다크 깨짐).
- 우리 i18n 규칙(`stringResource`)은 그대로 — 텍스트 *내용*은 호출부, *스타일*만 Style.
