# CalMong Figma Design System

Naver Calendar screenshots를 분석해 CalMong 캘린더 앱에 적용할 디자인 시스템 초안을 Figma에 생성하는 로컬 플러그인입니다.

## 실행 방법

1. Figma Desktop을 연다.
2. `Plugins > Development > Import plugin from manifest...`를 선택한다.
3. 이 폴더의 `manifest.json`을 선택한다.
4. `Plugins > Development > CalMong Design System Builder`를 실행한다.

실행하면 `CalMong Design System` 페이지가 생성되고, 아래 산출물이 배치됩니다.

- Color styles: 브랜드, 캘린더 의미 색상, 표면/텍스트/구분선
- Text styles: Android 캘린더 앱에 맞춘 제목, 본문, 캡션 스케일
- Foundations: 색상, 타이포, 간격, radius, elevation
- Components: app bar, FAB, month cell, event pill, schedule row, settings row, checkbox, switch, diary card, habit template row
- Screen patterns: month view, agenda panel, drawer/settings/input/diary 패턴

## 추출 기준

스크린샷에서 반복 사용되는 항목만 디자인 시스템 후보로 추출했습니다.

- 상단 앱바: 뒤로가기/닫기/메뉴, 중앙 타이틀, 우측 액션
- 캘린더 그리드: 요일 헤더, 날짜 상태, 오늘/선택/비활성 상태, 주 구분선
- 일정 표시: 일정 pill, dot indicator, 컬러 calendar marker, vertical event rail
- 입력/설정: 1-line list row, trailing chevron/value/switch, section header
- 플로팅 액션: primary FAB, today floating pill
- 다이어리/습관: diary card, month chip, template option row
- 컨트롤: checkbox, switch, icon button

## 다음 단계

Figma에서 생성된 컴포넌트를 검토한 뒤, 실제 앱 구현 시 `:core:designsystem` 모듈로 토큰과 원자 컴포넌트를 옮기면 됩니다.

