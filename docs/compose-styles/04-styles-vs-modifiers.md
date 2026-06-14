# 04. Styles vs Modifiers — 무엇을 언제

> 원문: <https://developer.android.com/develop/ui/compose/styles/styles-vs-modifiers> · 갱신 2026-06-11

## 핵심
- **공존한다. 대체가 아니다.** 내부적으로 Style은 modifier다.
- modifier로는 Style의 모든 걸 할 수 있지만, **그 역은 성립하지 않는다**(Style은 modifier의 부분집합).

## 빠른 선택
- **Styles를 골라라:** 기존 컴포넌트의 기본값을 **덮어쓸** 때, **고성능 애니메이션**, **테마 차원의 속성 묶음** 정의.
- **Modifiers를 골라라:** **동작 추가**(clickable·제스처), 일회성 고유 레이아웃, **누적(additive)** 속성이 필요할 때.

## 비교표
| 항목 | Modifiers | Styles |
|---|---|---|
| 주 목적 | 동작·시맨틱·복잡한 레이아웃. 개별 요소를 즉석 조작, 테마에서 내려오지 않음 | 시각 외형·개별 크기·테마화 속성. 테마 레벨에서 동작, 컴포넌트 레벨에서 덮어쓰기, 자식으로 전파 |
| 결합 로직 | **누적**(합쳐져 새 결과) | **덮어쓰기**(마지막이 이김), 우선순위 계층의 단일 레이어 |
| 테밍 | 테마로 끌어올리기 어려움 | 설계상 테마화 가능(CompositionLocal 접근), 한 번 정의 후 재사용 |
| 성능 | 보통 3단계(Composition/Layout/Draw) 갱신. 좋은 애니메이션엔 lambda 버전 필요 | Composition 단계 생략, Layout/Draw만 → recomposition·객체 할당 감소 |
| 애니메이션 | `animate*AsState` 등 별도 프리미티브 | 내장 `animate { }` |

## Modifier의 한계 (Styles가 메우는 지점)
- 보통 Composition 단계에서 생성 → 색 같은 작은 변화도 lambda 버전 아니면 전체 재실행 유발.
- 조건부 modifier는 fluent 체인 안 if-else로 지저분. 애니메이션엔 수동 상태 보일러플레이트, "auto-animate" 부재.
- **누적이라 덮어쓰기 불가** — 기본 border를 못 바꾸고 위에 하나 더 그릴 뿐.
- 전역 테마로 추상화하기 어려움 → 테마가 raw 값만 저장하게 됨.

## Styles의 한계
- Style은 특화된 modifier — **보완은 되지만 대체는 안 됨**.
- **시각 설정에 한정** — 클릭/제스처/접근성 시맨틱 같은 동작 처리 불가.
- Style을 최종 상태로 **해석(resolve)하는 비용이 단일 modifier보다 큼** — 모든 가능한 속성값을 담은 자료구조 생성 + 상속 속성 조회.

## 언제 Style을 선호하나
- **테마 차원 일관성** — 모든 컴포넌트에 반복 modifier 넘기는 대신 테마에 Style 하나로.
- **잦은 애니메이션** — Layout/Draw에서만 평가 → Composition 우회. 시각 속성 애니메이션은 Style로.
- **덮어쓰기 vs 스태킹** — 기본 속성을 *교체*해야 하면 Style(last-write-wins). 누적이 필요하면 modifier.
- **Material 커스터마이즈** — Material 컴포넌트가 `style` 파라미터를 주면 그게 권장 경로(내부 속성 접근 가능). *(현재는 Material 지원 전 — [09](09-limitations.md))*

## calmong 메모
- 우리 디자인 시스템 철학("색·치수를 직접 박지 말고 semantic 토큰만")은 **Style의 last-write-wins + 테마화**와 잘 맞는다 — 기본 Style을 테마에 두고 호출부에서 일부만 덮어쓰는 그림.
- 단, `showCalMongToast`처럼 노출/제스처/접근성은 계속 modifier·호출부 책임.
