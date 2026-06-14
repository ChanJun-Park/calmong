# Jetpack Compose Styles API — 학습 노트

Compose의 새 **Styles API**(실험) 공식 문서를 한 페이지씩 정리한 참고 모음.
나(개발자)와 에이전트가 함께 보는 단일 출처로 둔다.

> 출처: <https://developer.android.com/develop/ui/compose/styles> 및 하위 페이지
> 문서 마지막 갱신: **2026-06-11 UTC** · 우리가 정리/확인: **2026-06-14**
> 상태: `@ExperimentalFoundationStyleApi`, 패키지 `androidx.compose.foundation.style`
> 의존성: Compose foundation **`1.12.0-alpha03`** + 스냅샷 maven 저장소 필요

## 한 줄 정의
지금까지 modifier 체인(`background()`·`padding()`·`border()`)으로 하던 **"스타일링"을 `Style` 객체로 선언적으로** 다루는 새 레이어.
modifier를 *대체*하지 않고, **스타일링용 파라미터(색·패딩 등)를 대체**한다. 내부적으로 Style은 곧 modifier다.

## 목차
| # | 문서 | 한 줄 요약 |
|---|---|---|
| 01 | [개요](01-overview.md) | 무엇이고 왜 쓰는가, 핵심 3개념, 의존성 |
| 02 | [기초(Fundamentals)](02-fundamentals.md) | 적용 3가지 방법, 속성 표, 합성(`then`)·상속·CompositionLocal |
| 03 | [상태와 애니메이션](03-state-animations.md) | `hovered/pressed/...`, `animate { }`, 커스텀 상태(`StyleStateKey`) |
| 04 | [Styles vs Modifiers](04-styles-vs-modifiers.md) | 언제 무엇을 쓰나, 두 시스템의 한계 비교 |
| 05 | [테밍(Theming)](05-theming.md) | Style 레이어, atomic vs monolithic, 커스텀 디자인 시스템 적용 |
| 06 | [성능](06-performance.md) | Composition 단계 생략, 벤치마크 수치 |
| 07 | [Do / Don't](07-dos-donts.md) | 베스트 프랙티스와 안티패턴 |
| 08 | [예제](08-examples.md) | 버튼 계열 실전 코드 |
| 09 | [현재 한계](09-limitations.md) | 미지원 항목 + Material 통합 상태 |
| 10 | [웹 대응 & calmong 적용 사고](10-web-parallels-and-calmong-plan.md) | CSS/CVA/Radix ↔ Style API 매핑, 레시피·변형 이식 설계 |

## calmong 관점 요약 (결론 먼저)
- **지금 도입 안 함.** alpha + 스냅샷 저장소 의존이라, 우리 컨벤션 플러그인의 Compose 버전 단일화 정책과 충돌하고 API가 더 바뀐다. Material 컴포넌트 지원도 아직 없다([09](09-limitations.md)).
- **그러나 우리 구조와 궁합이 매우 좋다.** `CalMongTheme`은 이미 공식 문서의 권장 패턴(`@Immutable` 테마 클래스 + companion `@ReadOnlyComposable` accessor + `CompositionLocal`)을 그대로 따른다 — Jetsnack 예제와 동일 골격([05](05-theming.md)). 안정화 시 `CalMongStyles` + `StyleScope` 확장만 추가하면 자연스럽게 얹힌다.
- **우리 토큰과의 매핑이 명확하다**: `functional.stateLayer.{soft|solid}`·`colors.*.stroke` 상태색 → `pressed/hovered/focused { animate { } }`. `strokeWidths`·`shapes`·`spacings` → `borderWidth/shape/contentPadding`.
- **stateless 표시 컴포넌트(예: 우리 Toast)는 당장 이득이 작다.** 상태 있는 인터랙티브 원자(Button 등)부터 시범 적용이 적절하다([07](07-dos-donts.md)).
- 공식 에이전트 스킬 존재: <https://github.com/android/skills> (Styles 적용 보조).

## 원본 스크랩
원문 마크다운은 `.firecrawl/styles/*.body.md`에 보관(gitignore 대상일 수 있음). 값/예제의 진실은 항상 공식 문서 링크다.
