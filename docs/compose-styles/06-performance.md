# 06. Performance benefits with Styles — 성능

> 원문: <https://developer.android.com/develop/ui/compose/styles/performance> · 갱신 2026-06-11

## 왜 빠른가
Styles는 **Layout/Draw 단계**에서 동작하며 **항상 Composition 단계를 건너뛴다**. modifier에서 좋은 성능을 내려고 lambda 버전을 만들 필요가 없다.

세 가지 최적화:
- **Phase shifting** — 보통 Draw 단계를 타깃. 값 변경 시 영향받은 단계(예: Redraw)만 무효화 → 전체 Recomposition/Relayout 회피.
- **Lazy allocation** — 애니메이션 자원 할당을 실제 시작 시점까지 지연 → 초기 composition 작업 감소.
- **Reduced object overhead** — 체인 modifier는 속성마다 객체 할당. Style은 단일 lambda로 여러 속성 적용 → 메모리 할당 대폭 감소. 테마에 정의하면 그 lambda를 모든 컴포넌트가 공유.

## 벤치마크 (Compose 1.11.0-alpha06, 예시 수치)
| 테스트 | 설명 | 시간 | 할당 |
|---|---|---:|---:|
| `basic_box_border_change` | Box border 색 토글(갱신 성능) | **-59.91%** | **-77.22%** |
| `input_state_basic_box` | style 기반 hover/focus/press vs 수동 상태 수집 | -5.24% | -14.72% |
| `basic_box` | 체인 modifier 5개 Box 초기 composition/layout | -4.78% | -6.60% |
| `basic_text` | 하드코딩 문자열 BasicText 5개 | +0.62% | +2.41% |
| `basic_text_provided_color` | style vs `CompositionLocalProvider`로 텍스트 색 | +5.86% | +9.82% |

## 해석
- **빛나는 곳**: 잦은 **속성 업데이트/애니메이션**(border·배경 색 변경 등) — 객체 할당 회피 효과가 큼(-77% 할당).
- **이득이 작거나 약간 손해인 곳**: 정적 텍스트, CompositionLocal로 충분한 단순 색 전달 — Style 해석 비용이 더 들 수 있음([04](04-styles-vs-modifiers.md)의 "resolve 비용").

## calmong 메모
- 우리 인터랙티브 컴포넌트(누름/호버 시 stateLayer·stroke 색 전환)에 적용하면 recomposition 절감 효과가 클 후보. 반대로 정적 표시(Toast 등)는 성능 동기가 약하다.
