# 09. Current limitations — 현재 한계

> 원문: <https://developer.android.com/develop/ui/compose/styles/limitations> · 갱신 2026-06-11

## 기능적 한계
- **무한 애니메이션 미지원** — `rememberInfiniteTransition`을 계속 사용해야 함.
- **속성 스코핑** — 표준 style 속성을 넘어서는 **커스텀 *속성* 생성 미지원**(기존 속성 조합 확장만 가능 — [02](02-fundamentals.md)).
- **Shapes** — **커스텀 shape 미지원**(추후 수정 예정). **shape 애니메이션도 아직 미지원**.
- **View 시스템 테마/스타일 interop 없음** — `themes.xml`/`styles.xml`에서 스타일을 끌어올 수 없음. **앞으로도 직접 지원 안 할 예정**.
- **Ripple/Indication interop** — `pressed`를 쓰면서 `clickable`에 `indication = null`을 안 주면 **둘이 동시에 보임**.

## Material 통합 상태
- Material 컴포넌트의 Styles 지원은 **추후 업데이트 예정**.
- 미지원 사용 사례는 버그 리포트: <https://issuetracker.google.com/issues/new?component=612128>

## calmong 메모 (도입 보류 근거)
- 우리 `shapes`는 `RoundedCornerShape` 위주라 커스텀 shape 한계는 당장 큰 제약은 아니나, **shape 애니메이션 불가**는 기억해 둘 것.
- **Material3 사용 중** — 우리는 `CalMongTheme`이 Material colorScheme/shapes로 매핑하는 구조라, Material 통합 전에는 Material 컴포넌트(Button/Card 등)에 Style을 직접 못 얹는다. 자체 원자 컴포넌트부터가 현실적 시범 대상.
- `pressed` 도입 시 `indication = null` 누락은 흔한 함정 — 체크리스트에 포함.
- **종합 결론: stable + Material 지원 도착까지 보류**, 그 사이 이 노트로 추적([README](README.md)).
