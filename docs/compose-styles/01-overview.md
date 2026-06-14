# 01. Styles in Compose — 개요

> 원문: <https://developer.android.com/develop/ui/compose/styles> · 갱신 2026-06-11

## 무엇인가
- Compose 요소/컴포넌트를 **"스타일링"하는 새 패러다임**. 전통적으로 modifier로 하던 일.
- **modifier를 대체하지 않는다.** 대신 **스타일링용 *파라미터*(padding, color 등)를 대체**한다.
- 공식 권고: 유연성·성능을 위해 스타일링 파라미터 대신 Style로 전환할 것.

## 왜 (Benefits)
- **상태 기반 스타일 단순화** — `hovered/focused/pressed`에 따른 변화를 선언적으로. 보일러플레이트 대폭 감소.
- **상태 전환 애니메이션 내장** — `animateColorAsState`식 recomposition 없이 속성 애니메이션.
- **컴포넌트 API 간결화** — 시각 파라미터 난립 대신 `style` 하나로.
- **성능** — Style은 **Draw/Layout 단계**에서 동작, **Composition 단계를 건너뜀** → recomposition 감소.
- **표준화** — 표준 속성 집합으로 어떤 컴포넌트든 styleable.

## 핵심 3개념
| 개념 | 설명 |
|---|---|
| `Style` | UI 요소의 외형을 정의하는 인터페이스(표준 styleable 속성). CSS 스타일과 유사. **속성은 덮어쓰기** — 같은 속성을 두 번 설정하면 최종값 하나만 남는다. |
| `StyleScope` | Style 내부 `applyStyle()`의 receiver scope. `background/padding/border/...` 같은 속성 함수와 현재 `StyleState` 접근(`state`)을 제공. `CompositionLocalAccessorScope`·`Density`를 구현 → 테마/CompositionLocal 접근, dp↔px 변환 가능. |
| `StyleState` | 현재 상태(`isEnabled/isPressed/isHovered/isFocused/isSelected/isChecked` + 커스텀) 제공. 조건부 스타일에 사용. |

## 의존성 추가
1. `settings.gradle.kts`에 **스냅샷 maven 저장소** 추가.
2. Compose 버전을 `1.12.0-alpha03`로:
```toml
compose = "1.12.0-alpha03"
```
```toml
androidx-compose-foundation = { group = "androidx.compose.foundation", name = "foundation", version.ref = "compose" }
# ui / ui-graphics / runtime / ui-tooling(-preview) / ui-test-* 도 동일 version.ref = "compose"
```

## 참고
- 상태: `@Experimental` — 곧 바뀔 수 있고, Material 컴포넌트 지원은 추후.
- 공식 에이전트 스킬: <https://github.com/android/skills>.

## calmong 메모
- 우리는 catalog(`gradle/libs.versions.toml`)로 버전을 단일화하고 convention plugin으로 Compose를 주입한다 — 도입하려면 catalog의 `compose` 버전 상향 + 스냅샷 저장소 등록이 선행되어야 한다. **alpha+스냅샷이라 아직 보류**([README](README.md) 결론 참고).
