# CalMong 디자인 시스템 추출안

## 방향

현재 참고 화면은 고밀도 캘린더 앱입니다. 따라서 디자인 시스템은 마케팅성 장식보다 반복 업무 화면의 일관성, 읽기 쉬운 일정 밀도, 빠른 입력을 우선합니다.

## Foundations

### Color

| Token | Hex | 용도 |
| --- | --- | --- |
| `brand.primary` | `#6B4EE6` | 활성 switch, focus, AI/assistant action |
| `action.fab` | `#3F3F46` | 주요 생성 FAB |
| `calendar.sunday` | `#E83E63` | 일요일/공휴일/강조 날짜 |
| `calendar.saturday` | `#4A67E8` | 토요일 |
| `calendar.personal` | `#C63D5A` | 기본 일정 |
| `calendar.work` | `#2DAE91` | 업무/초대 일정 |
| `calendar.family` | `#F7CBD8` | 가족 일정 |
| `calendar.habit` | `#6423C8` | 습관 |
| `surface.default` | `#FFFFFF` | 기본 배경 |
| `surface.subtle` | `#F5F6F8` | 카드/템플릿 행 배경 |
| `surface.selected` | `#E9ECEF` | 선택 날짜/피커 선택 행 |
| `border.default` | `#E6E8EB` | 구분선/카드 stroke |
| `text.primary` | `#202124` | 주요 텍스트 |
| `text.secondary` | `#777A80` | 보조 텍스트 |
| `text.disabled` | `#C9CCD1` | 비활성 날짜/placeholder |

### Typography

Android Compose에서는 `sp` 기준으로 사용합니다. Figma 플러그인은 같은 숫자의 px 스타일로 생성합니다.

| Style | Size / Line | Weight | 용도 |
| --- | --- | --- | --- |
| `Display/Month` | 32 / 40 | 700 | 월 캘린더 헤더 |
| `Title/Large` | 28 / 36 | 700 | 전체 화면 제목 |
| `Title/Medium` | 22 / 30 | 700 | 섹션 타이틀 |
| `Body/Large` | 20 / 30 | 400 | 설정/입력 행 |
| `Body/Medium` | 18 / 26 | 400 | 일정명, 카드 본문 |
| `Label/Large` | 16 / 22 | 600 | 버튼, 날짜 보조 라벨 |
| `Caption` | 14 / 20 | 400 | 캡션, 보조 정보 |

### Spacing

`4dp` grid 기반입니다.

- `space.1`: 4
- `space.2`: 8
- `space.3`: 12
- `space.4`: 16
- `space.5`: 20
- `space.6`: 24
- `space.8`: 32
- `space.10`: 40
- `space.12`: 48

### Radius

- `radius.sm`: 4, 일정 pill
- `radius.md`: 8, 카드/리스트 컨테이너
- `radius.lg`: 16, 입력 field/피커 선택 행
- `radius.full`: 999, FAB/today chip/month chip

## Components

### App Bar

- `AppBar/Month`: 메뉴, 월 타이틀, 보조 액션, 검색
- `AppBar/Modal`: 닫기/뒤로가기, 중앙 제목, 저장/더보기

### Calendar

- `Calendar/MonthGrid`: 7열 그리드, 요일 헤더, 주 구분선
- `Calendar/DateCell`: default, today, selected, outside month, weekend
- `Calendar/EventPill`: filled, tinted, leading rail
- `Calendar/EventDot`: 6-8px dot, calendar color 기반
- `Calendar/AgendaRow`: 시간, 색상 rail, 제목, 보조 설명

### Controls

- `Control/FAB`: 72x72, dark fill, white icon
- `Control/TodayPill`: floating rounded pill
- `Control/Checkbox`: outlined/checked
- `Control/Switch`: on/off

### Lists

- `List/SettingsRow`: leading icon optional, label, trailing value/chevron/switch
- `List/CalendarRow`: color dot/checkbox, calendar name, share/link icons
- `List/HabitTemplateRow`: icon, centered label, chevron, subtle surface

### Diary

- `Diary/Card`: date header, title, preview, optional sticker
- `Diary/MonthChip`: list grouping label

## Figma 구성 제안

1. `00 일반`: colors, type, spacing, radius, elevation
2. `01 컴포넌트`: reusable components and variants
3. `02 화면`: month, agenda, drawer, settings, schedule input, diary patterns
4. `03 문서`: 추출 결정 사항, Compose 구현 매핑, 운영 규칙
5. `99 References`: 원본 스크린샷을 배치하고 annotation 유지
