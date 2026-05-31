# Gradle Convention Plugin 만들기 + `apply false` 패턴

> 작성일: 2026-05-31
> 관련 커밋: `9c2e4ca`
> 목표: 모든 Gradle 모듈이 공유하는 빌드 설정(Spotless·Detekt 등)을 한 곳에서 관리하기 위해 `build-logic`에 convention plugin을 만들고, 모듈에서는 plugin id 한 줄로 적용한다.

## 한눈에 보기

- `build-logic/`을 **별도 included build**로 만들어 그 안에 convention plugin을 정의
- `calmong.spotless`, `calmong.detekt` 두 plugin 등록 — 모듈은 `id("calmong.spotless")` 한 줄로 모든 설정을 받음
- convention plugin은 외부 plugin(Spotless/Detekt)을 `compileOnly`로 참조 → 런타임에는 빠짐
- **그래서** 루트 `build.gradle.kts`의 plugins {}에 두 외부 plugin을 `apply false`로 선언해 classpath만 확보
- 결과물 (이번 커밋 기준):
  - `build-logic/settings.gradle.kts`, `build-logic/convention/build.gradle.kts`
  - `build-logic/convention/src/main/kotlin/calmong/SpotlessConventionPlugin.kt`
  - `build-logic/convention/src/main/kotlin/calmong/DetektConventionPlugin.kt`
  - 루트 `settings.gradle.kts`에 `includeBuild("build-logic")` 추가
  - 루트 `build.gradle.kts`에 `alias(libs.plugins.spotless) apply false`, `alias(libs.plugins.detekt) apply false` 추가
  - `app/build.gradle.kts`에서 `id("calmong.spotless")`, `id("calmong.detekt")` 적용

## 1. Convention Plugin이란? 왜 만드는가

### 배경 / 왜

여러 모듈이 같은 빌드 설정(Spotless·Detekt·Hilt·Compose 등)을 반복 적용하면:
- 모듈마다 같은 코드 복붙
- 버전·옵션 변경 시 N곳 모두 수정 → 누락·불일치
- 새 모듈마다 보일러플레이트 작성

**선택지**:

| 방법 | 장점 | 단점 |
|---|---|---|
| `subprojects { ... }` (루트 build.gradle) | 가장 단순 | 모듈별 선택 적용 어려움, Gradle에서 점차 비권장 |
| **Convention plugin** | 재사용 가능, 모듈별 골라 적용, 정적 타입 검사 | 초기 셋업 코드량이 있음 |
| `apply from("*.gradle.kts")` | 빠른 시작 | 정적 검사 약함, IDE 지원 부족 |

→ **convention plugin** 선택. NiA(Now in Android) 표준 패턴이고 CLAUDE.md에 명시.

### 무엇을 했나

build-logic을 **included build**로 분리해 메인 빌드가 plugin 해석 시점에 사용할 수 있게 함.

```
build-logic/
├── settings.gradle.kts        # build-logic을 별도 Gradle 빌드로 선언
└── convention/
    ├── build.gradle.kts       # convention plugin 모듈의 빌드 스크립트
    └── src/main/kotlin/calmong/
        ├── SpotlessConventionPlugin.kt
        └── DetektConventionPlugin.kt
```

루트 `settings.gradle.kts`의 `pluginManagement {}` 안에 `includeBuild("build-logic")` 한 줄로 메인 빌드가 build-logic의 plugin을 인식한다.

### 직접 해보기

1. 루트에 `build-logic/` 디렉토리 생성.

2. `build-logic/settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
       repositories { google(); mavenCentral(); gradlePluginPortal() }
       versionCatalogs {
           create("libs") { from(files("../gradle/libs.versions.toml")) }
       }
   }
   rootProject.name = "build-logic"
   include(":convention")
   ```

3. `build-logic/convention/build.gradle.kts`:
   ```kotlin
   plugins { `kotlin-dsl` }
   group = "com.jingom.calmong.buildlogic"

   dependencies {
       compileOnly(libs.spotless.gradlePlugin)
       compileOnly(libs.detekt.gradlePlugin)
   }

   gradlePlugin {
       plugins {
           register("spotless") {
               id = "calmong.spotless"
               implementationClass = "calmong.SpotlessConventionPlugin"
           }
           register("detekt") {
               id = "calmong.detekt"
               implementationClass = "calmong.DetektConventionPlugin"
           }
       }
   }
   ```

4. plugin 본체 (`SpotlessConventionPlugin.kt`, `DetektConventionPlugin.kt`) — `Plugin<Project>` 구현. 내부에서 `pluginManager.apply("com.diffplug.spotless")` + `extensions.configure<SpotlessExtension> { ... }`로 외부 plugin을 적용하고 기본 설정.

5. 루트 `settings.gradle.kts`의 `pluginManagement {}` 첫 줄에 `includeBuild("build-logic")` 추가.

6. `gradle/libs.versions.toml`에 외부 plugin 버전·좌표 등록 (다음 섹션 참고).

7. `app/build.gradle.kts`에 `id("calmong.spotless")`, `id("calmong.detekt")` 추가.

### 알아두면 좋은 점

- `includeBuild`는 **두 종류 위치**가 있다. plugin 목적이면 반드시 `pluginManagement {}` **안**. 루트 레벨의 `includeBuild`는 일반 라이브러리 빌드 합치는 용도라 plugin 해석엔 못 씀.
- `versionCatalogs.create("libs") { from(...) }`을 통해 build-logic도 메인 catalog를 공유. 모든 버전을 한 파일에서 관리하는 핵심.
- convention plugin id에 `calmong.` 같은 프로젝트 prefix를 붙이면 외부 plugin id와 절대 충돌하지 않음 (NiA는 `nowinandroid.android.application` 식).
- 새 convention plugin 추가는 (a) `register("xxx")`, (b) `src/main/kotlin/calmong/XxxConventionPlugin.kt`, (c) 모듈에서 `id("calmong.xxx")` 적용 — 세 단계.

## 2. `compileOnly` + `apply false` 패턴

### 배경 / 왜

convention plugin의 `build.gradle.kts`에서 외부 plugin(Spotless/Detekt)을 어떻게 의존성으로 선언할지가 갈림길이다.

| 방식 | 동작 | 트레이드오프 |
|---|---|---|
| `implementation(libs.spotless.gradlePlugin)` | Spotless plugin이 convention plugin JAR의 **런타임 의존성**에 포함 | 셋업은 더 단순. 단, convention plugin들 간 동일 외부 plugin 버전이 갈리면 충돌. 의존성 트리가 불투명 |
| `compileOnly(libs.spotless.gradlePlugin)` | 컴파일만 통과, 런타임 의존성에서 제외 | 소비하는 쪽이 별도로 plugin classpath를 확보해야 함. 대신 **모든 모듈이 단일 catalog 버전만 사용 — 충돌 불가** |

→ `compileOnly` 선택 (NiA 패턴, 버전 단일화가 핵심 이점).

처음에 `compileOnly`만 쓰고 그대로 빌드 → **런타임 에러**:

```
> Failed to apply plugin 'calmong.spotless'.
   > Could not generate a decorated class for type SpotlessConventionPlugin.
      > com/diffplug/gradle/spotless/SpotlessExtension
```

원인: convention plugin 클래스가 `extensions.configure<SpotlessExtension> { ... }`를 호출 → JVM이 `SpotlessExtension` 클래스를 로드해야 함 → 런타임 classpath에 없음 → 폭발.

### 무엇을 했나

루트 `build.gradle.kts`의 plugins 블록에 외부 plugin을 **`apply false`로 선언**해 classpath에만 노출.

```kotlin
// 루트 build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // build-logic/convention 의 calmong.* 플러그인이 compileOnly로 참조
    // → 여기서 classpath에 노출
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.detekt) apply false
}
```

`apply false`의 의미:
- **plugin classpath에 추가는 함** (subproject들이 동일 classpath 공유)
- **루트 프로젝트 자체에는 적용 안 함** (루트엔 Spotless가 동작할 코드가 없으니까)

즉 "이 plugin 클래스를 빌드 어디서든 쓸 수 있게 준비만 해둬"라는 선언.

이후 모듈에서 `id("calmong.spotless")` → convention plugin 코드 실행 → 내부 `pluginManager.apply("com.diffplug.spotless")` 호출 시점에 SpotlessExtension 클래스가 이미 classpath에 있어 정상 동작.

### 직접 해보기

새 convention plugin이 외부 plugin 클래스를 참조하면 다음 4단계가 한 세트:

1. `gradle/libs.versions.toml`의 `[libraries]`에 plugin 아티팩트 추가:
   ```toml
   spotless-gradlePlugin = { group = "com.diffplug.spotless", name = "spotless-plugin-gradle", version.ref = "spotless" }
   ```

2. `build-logic/convention/build.gradle.kts`에 `compileOnly(libs.spotless.gradlePlugin)` 추가.

3. `gradle/libs.versions.toml`의 `[plugins]`에 plugin id 등록 (alias용):
   ```toml
   spotless = { id = "com.diffplug.spotless", version.ref = "spotless" }
   ```

4. **루트 `build.gradle.kts`의 plugins {}에 `alias(libs.plugins.spotless) apply false`** ← 빠뜨리면 위 런타임 에러.

이후 모듈에서 `id("calmong.spotless")`로 적용.

### `implementation`을 골랐다면 — 어떤 차이가 생기나

**먼저 메커니즘부터.** convention plugin의 `build.gradle.kts`에서 외부 plugin을 어떻게 의존성으로 선언하든, 외부 plugin의 실제 클래스 파일이 우리 convention JAR(`build-logic/convention/build/libs/convention.jar`) **안에 물리적으로 들어가지는 않는다.** JAR 안에는 우리가 작성한 `SpotlessConventionPlugin.class`, `DetektConventionPlugin.class`만 들어 있다.

차이는 JAR과 함께 출판되는 **메타데이터(.module 파일)** 에 기록되는 transitive 의존성 선언이다:

| 선언 | 메타데이터 효과 | 소비자(예: `:app`)에 미치는 영향 |
|---|---|---|
| `implementation(libs.spotless.gradlePlugin)` | "이 plugin을 적용한 소비자는 buildscript classpath에 Spotless도 함께 끌어와라" | `id("calmong.spotless")` 한 줄로 Spotless 전체 classpath가 자동 따라옴 |
| `compileOnly(libs.spotless.gradlePlugin)` | "컴파일만 통과시키면 됨. 소비자에게는 강제 안 함" | 소비자가 별도로 Spotless를 자기 buildscript classpath에 올려둬야 함 — 그래서 루트의 `apply false`가 필요 |

#### `compileOnly` + 루트 `apply false`의 실질적 이점

1. **소비자 buildscript classpath 통제** — convention plugin이 자기 의존성을 소비자에게 떠넘기지 않음. `calmong.spotless`만 적용한 모듈이 의도치 않게 AGP/Hilt 같은 다른 plugin 클래스에 노출되는 일이 없어 모듈 경계가 명확.

2. **외부 plugin 버전의 단일 진실 원천 강제** — buildscript classpath의 외부 plugin 버전은 **루트 `plugins {}` 블록 → `libs.versions.toml`의 `[plugins]` 한 곳**에서만 결정. convention plugin 안에서 catalog를 우회한 버전을 끼워 넣어도 소비자 classpath는 루트가 정한 버전이라 충돌이 명시적 에러로 드러남.

3. **convention plugin JAR이 가벼움** — 메타데이터에 transitive runtime dep이 안 적혀 소비 빌드의 dependency resolution이 단순함.

#### `implementation` 시 실제로 자주 생기는 문제

우리의 단일 `:convention` 서브프로젝트 구조에서도 충분히 나타날 수 있는 것:

- **의도치 않은 plugin이 transitive로 노출** — 미래에 `calmong.android.feature`가 내부에서 Hilt를 적용한다고 하자. `implementation(libs.hilt.gradlePlugin)`을 선언하면, `id("calmong.android.feature")`를 적용한 모듈은 Hilt를 직접 쓰지 않아도 `@HiltViewModel` 같은 어노테이션을 import할 수 있게 된다. 모듈 경계가 의존성 그래프상으로 흐릿해짐.

- **catalog 우회를 막을 수 없음** — convention plugin 내부에서 `implementation("com.diffplug.spotless:spotless-plugin-gradle:6.21.0")`처럼 catalog와 다른 버전을 직접 적어 넣어도 빌드 통과. 누가 어디서 어떤 버전을 쓰는지 추적이 어려워짐.

#### `build-logic`을 여러 서브프로젝트로 쪼갰을 때만 등장하는 문제

거의 일어나지 않는 시나리오. 만약 `build-logic`을 `build-logic/android/`, `build-logic/lint/` 식으로 분리하고 각각이 같은 외부 plugin을 다른 버전으로 `implementation`하면:

- **버전 격차** — 소비자 buildscript classpath에서 Gradle이 충돌 해소(보통 highest wins)에 따라 예상 못한 버전을 선택.
- **ClassCastException** — 같은 외부 plugin 클래스가 isolated classloader 두 곳에 로드되면 `cannot be cast to ...` 에러. 단일 `:convention` 구조에서는 사실상 발생하지 않음.

→ 우리는 단일 `:convention`이라 이 두 가지는 거의 무관. **처음부터 `compileOnly` + `apply false`를 채택하는 핵심 이유는 위 "자주 생기는 문제" 두 가지** — 모듈 경계 보존 + catalog 단일 진실 강제. JAR 경량화는 보너스.

#### 정리

| 관점 | `implementation` | `compileOnly` + 루트 `apply false` |
|---|---|---|
| 외부 plugin 클래스의 JAR 내 물리적 포함 | 없음 (둘 다 동일) | 없음 |
| 소비자 buildscript classpath에 외부 plugin이 transitive로 들어옴 | **예** (자동 노출) | 아니오 (루트가 명시 노출) |
| 외부 plugin 버전 단일 진실 원천 | catalog 우회 가능 | catalog가 단일 진실로 강제 |
| 의도치 않은 plugin 노출 | 가능 | 차단 |
| `build-logic` 분리 시 버전 충돌·ClassCastException | 가능 (드문 케이스) | 발생 불가 |

### 알아두면 좋은 점

- `implementation`을 썼다면 4번 단계가 불필요. 단점은 위 비교 표 참고. 초보 단계나 단일 plugin이면 `implementation`도 합리적 선택.
- `compileOnly` 패턴의 핵심 이점: **외부 plugin 버전을 catalog 한 곳에서만 관리**. 모든 의존자가 동일 plugin classpath 공유 → 버전 불일치 원천 차단.
- 비슷한 패턴이 Kotlin/AGP/Hilt/Compose에도 적용됨. NiA의 `build-logic/convention/build.gradle.kts`를 보면 `compileOnly`가 줄줄이 보임.
- "런타임 에러 났으니 그냥 `implementation`으로 바꿔" 가 가능은 하지만, NiA 패턴을 일관되게 따르는 게 장기적으로 유지보수 비용 ↓.

## 핵심 명령어·파일 모음

```bash
# 검증
./gradlew spotlessCheck detekt

# convention plugin만 컴파일 확인 (디버깅용)
./gradlew :build-logic:convention:compileKotlin
```

핵심 파일 경로:
- `build-logic/settings.gradle.kts`
- `build-logic/convention/build.gradle.kts`
- `build-logic/convention/src/main/kotlin/calmong/SpotlessConventionPlugin.kt`
- `build-logic/convention/src/main/kotlin/calmong/DetektConventionPlugin.kt`
- `settings.gradle.kts` — `pluginManagement { includeBuild("build-logic"); ... }`
- `build.gradle.kts` (루트) — `plugins { alias(libs.plugins.spotless) apply false; ... }`
- `gradle/libs.versions.toml` — `[versions]`, `[libraries]`(`*-gradlePlugin`), `[plugins]` 세 곳 갱신

## 함정 / 주의

1. **`compileOnly`만 쓰고 끝내면 런타임 에러** — `Could not generate a decorated class for type ...` 에러는 거의 항상 "루트의 `apply false` 누락" 신호.
2. **`includeBuild` 위치 헷갈리지 말 것** — plugin 목적이면 반드시 `pluginManagement {}` **안**. 그 바깥에 두면 plugin 해석에 못 쓰임.
3. **catalog 경로 오타** — `build-logic/settings.gradle.kts`의 `from(files("../gradle/libs.versions.toml"))` — 상대경로 `../`가 정확해야 함.
4. **체크리스트로 외우기** — 새 convention plugin 추가할 때 (a) catalog `[libraries]` (b) `compileOnly` (c) catalog `[plugins]` (d) 루트 `apply false` — 이 네 단계가 한 세트.
