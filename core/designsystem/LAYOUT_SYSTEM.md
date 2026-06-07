# CalMong Layout System

CalMong의 layout은 웹 breakpoint(px)가 아니라 **Android 창 크기(dp)** 를 기준으로 한다.
화면은 `if (width > 768.dp)`처럼 임의 분기하지 않고 `CalMongTheme.windowWidthClass`로 골격을 고르고,
넓은 화면에서 콘텐츠가 늘어지지 않게 `CalMongTheme.layout.contentMaxWidth`로 제한한다.

Spacing(내부 여백·간격)은 [`SPACING_SYSTEM.md`](SPACING_SYSTEM.md)가 담당한다 — layout은 거리를 다루지 않는다.

Figma 문서:
[Design System / Layout System](https://www.figma.com/design/y1auG7nkgZKt00DtduF90Q/Design-System?node-id=64-2&t=GVS1GPXUrigPp389-1)

```text
primitive                         semantic                       사용처
breakpoint 600/840/1200    ->   WindowWidthClass            ->  화면 골격(single/2-pane/list-detail)
container/md (448) …        ->   contentMaxWidth.form …      ->  폼·다이얼로그 최대폭
```

## 두 축

### 1) WindowWidthClass — 반응형 골격
Material WindowSizeClass와 같은 dp 임계값(`layout.json` breakpoint와 동기화).

| 클래스 | 너비(dp) | 대상 | 네비게이션 | 캘린더 골격 |
|---|---|---|---|---|
| `Compact` | `<600` | 폰 세로 | Bottom bar | 단일 페인(월 그리드 → 날짜 탭 시 agenda 시트) |
| `Medium` | `600–839` | 폰 가로·소형 폴더블·태블릿 세로 | Nav rail | 2-pane(월 + 그날 agenda) |
| `Expanded` | `840–1199` | 태블릿·대형 폴더블 | Nav rail/Drawer | list-detail(캘린더 + 상세) |
| `Large` | `≥1200` | 대형 태블릿·데스크톱 | Drawer | list-detail(여유 폭) |

> Tailwind의 sm/md/lg(640/768/1024)는 채택하지 않는다. Android 표준 600/840/1200을 쓴다.
> 웹 breakpoint 값이 필요하면 Figma 목업 프레임 폭 용도로만 별도 참고.

### 2) contentMaxWidth — 콘텐츠 최대폭
Tailwind container 스케일을 dp 상한으로 직역. `Modifier.widthIn(max = …)`에 쓴다.

| 역할 | 값 | 출처 | 용도 |
|---|---|---|---|
| `form` | 448dp | container/md | 단일 컬럼 폼·다이얼로그(일정 입력) |
| `prose` | 672dp | container/2xl | 읽는 텍스트(다이어리·설정 설명) |
| `wide` | 768dp | container/3xl | 태블릿 중앙 정렬 본문 |

## 코드에서 쓰기

```kotlin
// 앱 루트: 실제 창 크기에서 1회 산출해 전달
val widthClass = WindowWidthClass.fromWidth(maxWidthDp) // 또는 Material WindowSizeClass에서 매핑
CalMongTheme(windowWidthClass = widthClass) { App() }

// 골격 분기
when (CalMongTheme.windowWidthClass) {
    WindowWidthClass.Compact -> CalendarSinglePane()
    WindowWidthClass.Medium -> CalendarTwoPane()
    else -> CalendarListDetail()
}

// 콘텐츠 폭 제한 — 태블릿에서도 일정 입력 폼은 448dp 중앙
Column(
    Modifier
        .widthIn(max = CalMongTheme.layout.contentMaxWidth.form)
        .align(Alignment.CenterHorizontally),
) { ScheduleInputForm() }
```

`WindowWidthClass`는 런타임 창 크기에 의존하므로 색/간격과 달리 앱 루트에서 산출해 `CalMongTheme`에 넘긴다(미제공 시 Compact 가정). `contentMaxWidth`는 정적 dp 토큰이라 그대로 쓴다.

## 정의 위치
- primitive: `theme/layout/CalMongWindow.kt`(breakpoint·WindowWidthClass), `theme/layout/CalMongLayout.kt`(container)
- semantic: `CalMongLayout`(contentMaxWidth) — 같은 파일
- Figma 변수: `layout.json`(breakpoint Android dp + container), alias 원본 `tokens/semantic.layout.json`
