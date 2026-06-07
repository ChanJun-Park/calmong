# Figma 색상 시스템 문서 페이지 만들기

calmong 색상 시스템을 Figma에 문서화하는 빌드 가이드. 변수 세팅 → 페이지 구조 → 팔레트 → 예시 컴포넌트 순.
토큰 값의 단일 출처는 코드(`theme/color/CalMongColorSchemes.kt`)와 JSON(`semantic.color.{light,dark}.json`)이다. 이 페이지는 그걸 **보여주는** 용도.

---

## 0. 변수(variable)를 추가/수정해야 하나?

**추가 필요: 예 — semantic 컬렉션 1개.** 현재 Figma에는 Tailwind 플러그인이 만든 **primitive 색상만** 있고 semantic은 없다. 문서 페이지의 스와치/컴포넌트를 변수에 바인딩해야 (a) Light/Dark 모드 토글로 미리보기가 되고 (b) 값이 바뀌면 페이지가 자동 갱신된다.

| 컬렉션 | 모드 | 출처 | 비고 |
|---|---|---|---|
| `Primitive` (또는 Tailwind 플러그인이 만든 이름) | 단일 | `color.json` | 이미 있음. 그대로 둠 |
| `Semantic` (신규) | **Light / Dark** | `semantic.color.{light,dark}.json` | 새로 import. primitive를 alias 참조 |

### import 순서 (Tokens Studio for Figma, 무료)
`README.md`의 "Figma에 semantic 토큰 반영하기"와 동일:
1. primitive(`color.json`)를 set으로 먼저 로드 (alias가 풀리도록).
2. `semantic.color.light.json` / `dark.json`을 각각 set으로 추가.
3. **Themes**에서 Light/Dark 테마 생성 → 각 semantic set 매핑.
4. **Export to Figma variables** → `Semantic` 컬렉션(2 모드) 생성. 변수명은 `primary/background/default`처럼 슬래시 그룹.

> 알파 토큰(stateLayer, overlay, alpha, shadow)은 JSON에서 raw hex라 두 모드 동일값으로 들어온다 — 원래 모드 독립이라 정상.

### 변수를 수정하는 법
- **값만 변경**: Figma 변수 편집기 또는 Tokens Studio에서 값 수정 → **반드시 코드(`CalMongColorSchemes.kt`)도 같은 값으로**. 두 출처가 갈라지면 안 됨.
- **토큰 추가/이름 변경**: 코드(데이터 클래스 + 두 scheme 함수)와 JSON을 먼저 고치고 → JSON 재import(또는 해당 변수만 수동 추가). 구조 변경은 항상 코드가 먼저.
- 모드 토글 미리보기는 프레임 우측 패널의 **Layer → Variable mode**에서 Light/Dark 선택.

---

## 1. 페이지 / 섹션 구조

- 페이지 이름: **🎨 Color System**
- 최상위 프레임 1개(`Doc / Color`, 세로 Auto layout, gap 64, padding 64, fill = `neutral/background/base`) 안에 섹션 프레임을 쌓는다.
- 각 섹션 = 프레임(세로 Auto layout, gap 24). 제목 텍스트(`neutral/foreground/default`) + 내용.

섹션 순서:
1. Overview — 시스템 한 줄 설명 + 3-tier 다이어그램(primitive → semantic → component)
2. Primitive Palette
3. Semantic Tokens (4 층위)
4. Usage Examples (컴포넌트)
5. State & Interaction

> 모든 텍스트/배경 fill은 **Semantic 변수에 바인딩**한다(직접 hex 금지). 그래야 모드 토글이 먹는다.

---

## 2. Primitive Palette 섹션

Tailwind 22개 패밀리 × 11 shade. 이미 변수로 있으니 **시각 카탈로그**만 만든다.
- 패밀리당 가로 줄: 11개 사각형(48×48, 우측에 shade 번호/hex 캡션). 줄 왼쪽에 패밀리명.
- 색은 primitive 변수에 바인딩.
- 상단에 안내 텍스트: "primitive는 raw 팔레트 — 화면에서 직접 쓰지 말고 semantic을 거친다."

---

## 3. Semantic Tokens 섹션 (핵심)

4 층위를 그대로 그룹핑. 각 토큰 = 스와치(64×48) + 토큰 경로 캡션 + (alias된 primitive명). **이 섹션을 Light 프레임 / Dark 프레임 2벌**로 두거나, 한 프레임을 두고 모드 토글로 비교.

서브섹션과 토큰(스와치로 깔 것):

**primary / secondary** (각각)
- background: default · subtle · bold · dimmed · inverted
- foreground: default · subtle · inverted  *(채움 위 콘텐츠 — 캡션에 "on background.default" 명시)*
- stroke: default · subtle  *(각각 6 상태는 §5에서)*

**neutral**
- background: base · default · raised1 · raised2 · dimmed · inverted
- foreground: static · default · subtle · decorative · alpha · inverted
- stroke: divider · subtle · default · static

**functional**
- common: positive / negative / informative / attention × (default · decorative · subtle)
- general: overlay · highlight · shadow · disabled
- specific: like · link × (default · decorative · subtle)
- stateLayer: soft · solid (§5에서 상태 전개)

각 스와치 캡션 규칙 예: `primary/background/default` 아래 작은 글씨로 `→ indigo/600`.

> 콘텐츠색 규칙 콜아웃 박스 하나 추가(`functional/general/highlight` 배경 + `neutral/foreground/default` 텍스트):
> - 브랜드 채움 위 콘텐츠 = `brand/foreground/default`
> - 브랜드색 텍스트/아이콘(아웃라인·텍스트 버튼 라벨, 링크)이 필요하면 `brand/background/default`를 **텍스트색으로** 사용 (Indigo600 = 흰 배경 6.29:1 통과)
> - functional은 `subtle` 배경 + `default` 텍스트 패턴
> - 이미지/스크림 위 = `neutral/foreground/static`

---

## 4. Usage Examples (예시 컴포넌트)

"어떻게 쓰나"를 보여주는 핵심. 각 컴포넌트는 실제 변수에 바인딩 → 모드 토글로 light/dark 동시 검증. 컴포넌트 옆에 토큰 매핑 캡션을 단다.

### 4-1. Buttons (primary=Indigo 기준, secondary=Amber도 한 줄 더)
| 종류 | 배경 fill | 라벨/아이콘 | 외곽선 | 눌림 오버레이 |
|---|---|---|---|---|
| Filled | `primary/background/default` | `primary/foreground/default` | — | `functional/stateLayer/solid/pressed` |
| Tonal | `primary/background/subtle` | `primary/background/default`* | — | `functional/stateLayer/soft/pressed` |
| Outlined | `neutral/background/default` | `primary/background/default`* | `primary/stroke/default` | `functional/stateLayer/soft/pressed` |
| Text | 투명 | `primary/background/default`* | — | `functional/stateLayer/soft/pressed` |
| Disabled | `functional/general/disabled` | `neutral/foreground/decorative` | — | — |

\* 브랜드색 라벨 자리 — 위 콘텐츠색 규칙(=`background/default`를 텍스트색으로) 적용.

### 4-2. Surface / Elevation (카드 중첩)
base 위에 카드를 쌓아 elevation 사다리 시연:
- 페이지 바닥 = `neutral/background/base`
- 카드 = `neutral/background/default` (그 위 카드 = `raised1` → `raised2`)
- 그림자 = `functional/general/shadow`
- 제목 = `neutral/foreground/default`, 본문 = `neutral/foreground/subtle`
- 카드 내부 구분선 = `neutral/stroke/divider/default`
- 캡션: "light는 그림자로, dark는 색(raised가 밝아짐)으로 elevation 표현"

### 4-3. Status Banner (functional, 권장 패턴)
positive/negative/informative/attention 4종 가로 나열:
- 배경 = `functional/common/{X}/subtle`
- 아이콘 = `functional/common/{X}/decorative`
- 제목 텍스트 = `functional/common/{X}/default`
- 본문 텍스트 = `neutral/foreground/default`
- 캡션: "솔리드 채움+흰 글자 대신 **subtle 배경+default 텍스트**가 접근성 안전"

### 4-4. Text Field (stroke 상태 시연)
세로로 5개 상태:
- 컨테이너 = `neutral/background/default`
- 라벨 = `neutral/foreground/subtle`, 입력값 = `neutral/foreground/default`, placeholder = `neutral/foreground/decorative`
- 외곽선: rest=`neutral/stroke/default/default` · focused=`primary/stroke/default/focused` · disabled=`neutral/stroke/default/disabled` · error=`functional/common/negative/default`
- 에러 헬퍼텍스트 = `functional/common/negative/default`

### 4-5. Specific (like / link)
- 하트 아이콘 채워짐 = `functional/specific/like/default` (Rose), 빈 하트 = `neutral/foreground/decorative`
- 링크 텍스트 = `functional/specific/link/default` (Blue), 밑줄 동일색

---

## 5. State & Interaction 섹션

### 5-1. stateLayer (soft vs solid)
2×6 그리드 두 벌:
- **soft** 행: 옅은 타일(`neutral/background/default`) 위에 `functional/stateLayer/soft/{state}`를 겹쳐, default→hover→focused→pressed→activated→disabled 6칸.
- **solid** 행: 짙은 타일(`primary/background/default`) 위에 `functional/stateLayer/solid/{state}` 6칸.
- 캡션: "요소 밝기로 soft/solid를 고른다(테마 모드 무관). 배경색은 그대로 두고 위에 레이어만 올림."

### 5-2. stroke 상태
`neutral/stroke/default`와 `primary/stroke/default`의 6상태를 사각형 외곽선으로 나열:
default→hover→focused→pressed→activated→disabled.
- 캡션: "외곽선은 오버레이가 아니라 **색 자체**가 상태마다 바뀐다. default/static은 3:1 충족."

---

## 6. 만든 뒤 유지보수
- 코드/JSON에서 값이 바뀌면 → 해당 Semantic 변수 갱신(또는 JSON 재import). 페이지는 변수 바인딩이라 자동 반영.
- 토큰을 추가하면 → §3 스와치 + 관련 §4 예시에 한 칸 추가.
- 이 문서(`figma-doc-page.md`)는 페이지 구조의 단일 출처. 페이지를 바꾸면 여기도 갱신.
