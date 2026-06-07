# calmong

목표 달성을 도와주는 AI agent pet이 함께하는 캘린더 앱.

## 스택
- 언어/플랫폼: Kotlin, Android (compileSdk·minSdk·targetSdk는 `gradle/libs.versions.toml`을 단일 소스로 둔다)
- UI: Jetpack Compose + Material3, 타입 안전 Navigation
- 비동기: Coroutines + Flow
- DI: Dagger/Hilt
- 영속화: Room (관계형), DataStore (Preferences)
- 로깅: Timber
- 빌드: Gradle Kotlin DSL + Version Catalog + `build-logic` convention plugin
- 정적 분석/포맷: Spotless Gradle plugin(ktlint 적용) + Detekt
- 테스트: JUnit, MockK, Turbine, Compose UI Test

## 아키텍처
**Clean Architecture** — 의존 방향은 `presentation → domain ← data`. 안쪽 레이어(domain)는 바깥을 모른다.

- **presentation**: Compose UI + ViewModel. `StateFlow<UiState>` 단일 상태를 노출하고, 이벤트는 함수 호출(intent)로 받는 단방향 데이터 흐름(UDF).
- **domain**: 순수 Kotlin. UseCase + 도메인 모델 + Repository **인터페이스**. Android 의존 금지.
- **data**: Repository 구현, Room/DataStore/Network DataSource. domain 인터페이스를 구현해 외부에서 주입.

### DI (Hilt)
- 모듈별 `@Module @InstallIn(...)`로 의존성 제공.
- Coroutine Dispatcher는 `DispatcherProvider`로 추상화해 주입 (테스트에서 `TestDispatcher`로 교체).
- `@HiltViewModel` / `@AndroidEntryPoint`는 프레임워크 클래스에만 적용.

## 모듈 구조
Now in Android 구조를 참고한다. **처음부터 다 만들지 말고 실제로 필요해질 때 생성**한다.

- `:app` — 엔트리, 네비게이션 그래프, 테마 조립
- `:core:designsystem` — Material3 토큰·테마·원자 컴포넌트
- `:core:ui` — feature 간 공유 Compose 컴포넌트
- `:core:common` — 유틸리티·`DispatcherProvider`
- `:core:domain` — feature 간 공유 도메인 (필요 시점에만)
- `:core:data` — 공통 Repository 구현
- `:core:database` / `:core:datastore` — 영속화 인프라
- `:core:testing` — 테스트 픽스처, fake 구현
- `:feature:<name>` — 화면 단위 feature (presentation 위주, 필요 시 자체 domain/data 보유)

새 feature는 `:feature:<name>` 모듈을 추가하고 필요한 `:core:*`만 의존한다.

## 빌드 / Convention Plugin
- 모든 모듈이 공유하는 Gradle 설정·의존성은 `build-logic/convention/`의 convention plugin으로 정의해 재사용한다.
- 권장 plugin 네이밍:
  - `calmong.android.application`, `calmong.android.library`, `calmong.android.feature`
  - `calmong.android.library.compose`, `calmong.android.hilt`
  - `calmong.jvm.library` (순수 Kotlin 모듈: domain 등)
  - `calmong.android.test`, `calmong.jvm.test`
- 모든 의존성과 버전은 `gradle/libs.versions.toml`로 단일화한다. 모듈 빌드 스크립트는 catalog alias만 참조 (직접 좌표/버전 명시 금지).

## 테스트 / TDD
- **사이클**: 실패하는 테스트 작성 → 최소 코드로 통과 → 리팩토링.
- **피라미드**
  - 단위 테스트(JVM, 빠르고 다수): domain / data / ViewModel
  - 통합 테스트: Repository ↔ DataSource 결합
  - UI 테스트(Compose UI Test): 화면 핵심 시나리오만 (탐색적 UI 작업까지 강제 TDD는 적용하지 않음)
- **도구**: JUnit, MockK, Turbine, Compose UI Test, Robolectric(필요 시).
- **규칙**
  - 도메인/데이터 로직은 Android 의존 없이 JVM 테스트로 검증.
  - 코루틴 테스트는 `runTest` + `TestDispatcher` 조합 사용.
  - 테스트 더블은 fake(`:core:testing`에 위치) > mock 순으로 선호.

## 코드 규칙
- **Kotlin 관용구**
  - 불변성 기본 (`val`, `data class`, 읽기 전용 컬렉션).
  - 상태/이벤트는 `sealed class` / `sealed interface` + 망라적 `when`.
  - 실패는 예외 대신 `Result` 또는 도메인 `Either` 타입으로 표현하고 UI 경계에서 매핑.
- **Compose**
  - 상태 호이스팅: stateless composable이 기본, 상태는 위로 끌어올린다.
  - 파라미터 안정성 확보 — 불변 자료형, 필요 시 `@Immutable`/`@Stable`.
  - 컬렉션은 `kotlinx.collections.immutable` 권장.
  - 사용자 노출 문자열은 항상 `stringResource(...)`로 (i18n 대비).
- **로깅**: Timber 사용. `Timber.plant(...)`는 `:app` 진입점에서 한 번만 (Debug 빌드는 `DebugTree`, Release 빌드는 별도 트리). 사용자 노출 메시지는 로깅과 별개로 string resource로.
- **원칙**: SOLID / KISS / DRY / YAGNI. 단, **추측성 추상화 금지** — 중복이 실제로 아플 때 추상화한다.
- **정적 분석 / 포맷**: Spotless Gradle plugin으로 ktlint를 적용 + Detekt. 둘 다 convention plugin으로 묶어 모든 모듈에 일관 적용하고, CI에서 검증.

## 디자인 시스템 (`:core:designsystem`)
모든 UI는 `:core:designsystem`의 **semantic 토큰만** 사용한다. 색·치수를 화면에 직접 박지 않는다.
Tailwind primitive(raw 팔레트/스케일)는 `internal`이라 직접 못 쓰며, 항상 의미(역할) 토큰을 거친다.

- **진입점**: 화면을 `CalMongTheme { }`로 감싸고, 토큰은 다음 accessor로 접근한다.
  - `CalMongTheme.colors` · `.shapes` · `.elevations` · `.spacings` · `.layout` · `.windowWidthClass`
- **5개 토큰 시스템** — 역할·매핑·사용 규칙은 각 가이드가 단일 출처:
  - 색상: `core/designsystem/COLOR_SYSTEM.md`
  - radius/shape: `core/designsystem/RADIUS_SYSTEM.md`
  - elevation: `core/designsystem/ELEVATION_SYSTEM.md`
  - spacing: `core/designsystem/SPACING_SYSTEM.md`
  - layout(WindowWidthClass·contentMaxWidth): `core/designsystem/LAYOUT_SYSTEM.md`
  - 토큰 구조·Figma 동기화 절차: `core/designsystem/tokens/README.md`

### 사용 규칙
- **색**: `Color(0x…)`나 Material 색 직접 대신 `CalMongTheme.colors.*`. 채움 위 콘텐츠는 `brand.foreground.default`, 상태 표현은 `functional.stateLayer`(soft/solid)와 `stroke`의 상태색을 쓴다.
- **치수**: `RoundedCornerShape(…dp)`·`padding(…dp)` 직접 대신 `CalMongTheme.shapes.*` / `CalMongTheme.spacings.*`(gap·inset·section 역할).
- **깊이**: 그림자 수치 대신 `CalMongTheme.elevations.*`(flat~modal). Light는 그림자, Dark는 surface 색으로 깊이 표현.
- **반응형**: 임의 dp 비교 금지. `CalMongTheme.windowWidthClass`(Compact/Medium/Expanded/Large)로 골격을 분기하고, 넓은 화면 콘텐츠 폭은 `layout.contentMaxWidth`(form/prose/wide)로 제한한다.
- **Material3 컴포넌트**(Button/Card 등)는 `CalMongTheme`이 `MaterialTheme.colorScheme`/`shapes`로 매핑해 자동으로 브랜드를 따른다. 고유 토큰이 필요할 때만 `CalMongTheme.*`를 직접 쓴다.

### 토큰 추가가 필요할 때
spacing·shape·layout(및 color·elevation)에 **역할이 부족하면 새 요소를 정의해도 된다**. 단 추측성 추가는 금지하고(실제로 필요할 때만), 다음을 함께 갱신해 단일 출처를 유지한다.
1. (필요 시) primitive 스케일 — `theme/<domain>/CalMong*.kt`
2. semantic 역할 — 해당 data class에 역할 추가
3. Figma 변수 + `core/designsystem/tokens/semantic.<domain>.json`
4. 해당 `*_SYSTEM.md` 가이드
값의 진실은 코드(`theme/<domain>/…`)와 토큰 JSON이며, 한쪽을 바꾸면 다른 쪽도 맞춘다.

## 작업 규칙

### 커밋
단위 작업이 끝나면 다음 포맷으로 커밋한다.

```
#{작업 종류} #{이슈 번호} {간단한 작업 메시지}
  - (선택) {작업 상세 메시지}
  - (선택) {여러 줄 가능}
```

- **작업 종류**(소문자 단어): `feature`, `bugfix`, `refactoring`, `design`, `test`, `docs`, `chore`, `style`, `perf`, `build`, `ci`, `revert` 등. 새 종류가 필요하면 이 목록에 추가한 뒤 사용한다.
- **이슈 번호**: Jira 키(`#CALMONG-123`) 또는 GitHub 이슈 번호(`#42`). 연결된 이슈가 없으면 이 토큰은 생략한다.
- **간단한 작업 메시지**: 한 줄, 50자 내외, 명령형. 마침표 없음.
- **상세 메시지**: 필요할 때만. `  - `(공백 2칸 + 하이픈 + 공백)으로 들여쓰기. *왜* 했는지 위주로 적고, *무엇*은 diff가 말하게 둔다.

예시:
```
#feature #CALMONG-12 주간 캘린더 뷰 추가
  - Compose의 LazyHorizontalGrid 기반으로 구현
  - 주간 스와이프 제스처는 다음 PR에서 처리
```
```
#bugfix #57 펫 다이얼로그 닫는 도중 발생하는 크래시 수정
```
```
#refactoring CalendarRepository를 data 모듈로 이동
```

### 커밋 단위
- 한 커밋은 하나의 논리적 변경만 담는다. 리팩토링과 기능 추가를 섞지 않는다.
- 작업이 끝났다고 판단되면 즉시 커밋한다. 여러 단위를 한꺼번에 모아 커밋하지 않는다.
- 커밋 전 빌드/테스트가 통과하는 상태인지 확인한다.

## 에이전트 / 스킬 / 커맨드
- 프로젝트 전용 정의는 `.claude/agents/`, `.claude/skills/`, `.claude/commands/`에 둔다
- 개인 설정은 `.claude/settings.local.json` (gitignore됨), 팀 공유 설정은 `.claude/settings.json`
