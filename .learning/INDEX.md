# Learning Notes 인덱스

calmong 프로젝트를 만들며 배운 것들의 모음.

## 2026

- 2026-06-21 [Paparazzi 스크린샷 테스트 도입 + CalMongButton 시각 회귀 고정](2026-06-21/paparazzi-screenshot-testing.md) — AGP 9에서 Paparazzi 동작 확인, record/verify 워크플로, 정적 렌더에서 MutableStyleState로 상호작용 상태 강제하기
- 2026-06-06 [Android application convention plugin 도입 + NiA 패턴 정착](2026-06-06/android-application-convention-plugin.md) — `:app/build.gradle.kts`의 android 블록 추출, 공통 로직을 확장함수로 분리, 파일 구조 NiA 스타일로 재정렬
- 2026-05-31 [Gradle Convention Plugin 만들기 + `apply false` 패턴](2026-05-31/gradle-convention-plugin-setup.md) — build-logic으로 공통 빌드 설정 묶기, `compileOnly` 사용 시 루트에 `apply false` 필요한 이유
