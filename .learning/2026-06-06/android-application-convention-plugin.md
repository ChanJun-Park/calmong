# Android application convention plugin 도입 + NiA 패턴 정착

> 작성일: 2026-06-06
> 관련 커밋: `40a3597` (application 추출), `0418db0` (NiA 패턴 재구조화), `e4a0669` (파일 구조 분리)
> 목표: `:app/build.gradle.kts`에 섞여 있던 빌드 인프라를 convention plugin으로 빼내고, 이미 도입돼 있던 library plugin과 함께 NiA(Now in Android) 구조에 맞게 정돈하기.

## 한눈에 보기
- `app/build.gradle.kts`의 `android { ... }` 블록 대부분을 `calmong.android.application` / `calmong.android.application.compose` 두 plugin으로 추출 → 61줄 → 33줄
- library/application 양쪽이 공유하는 Android baseline 로직을 `Project.configureKotlinAndroid(CommonExtension)` 확장함수로 추출 (`com/jingom/calmong/KotlinAndroid.kt`)
- Compose 의존성 주입을 `Project.configureAndroidCompose()` 확장함수로 추출 (`com/jingom/calmong/AndroidCompose.kt`)
- `compose` plugin이 baseline plugin을 transitive apply하던 구조 제거 → 모듈에서 `baseline + .compose` 두 plugin을 모두 명시 적용
- 파일 구조 정리: plugin 파일 6개는 `src/main/kotlin/` 루트(default 패키지), 헬퍼 3개는 `com/jingom/calmong/` 패키지로 분리

## 1. Application convention plugin 추출

### 배경 / 왜
- `app/build.gradle.kts`에 빌드 인프라(SDK 버전, Compose 활성화, BOM 등)와 app 정체성(applicationId, versionCode, ...)이 한 블록에 섞여 있었음.
- 이미 `:core:designsystem`을 위해 `calmong.android.library` / `calmong.android.library.compose`를 만들어 둔 상태였고, application 쪽에도 같은 패턴을 적용해 일관성을 맞추는 게 목적.

### 핵심 결정 (수정 포함)
| 항목 | 처음 선택 | 최종 |
|---|---|---|
| plugin 구조 | 단일 plugin (Compose 포함) | **2개로 분리** (baseline + `.compose`) — library와 대칭. 작업 중간에 사용자가 NiA 일치를 위해 변경 |
| `buildTypes.release` | plugin에 default 설정 (proguardFiles) | **plugin에서 제외, app에 잔존** — `getDefaultProguardFile()`이 plugin 컨텍스트에서 접근 불가 (아래 함정 참고). proguard/minify 정책은 모듈마다 다르므로 자연스러움 |
| material3 자동 주입 | ✗ | ✗ — `:app`이 직접 declare. 다른 Compose 모듈은 `:core:designsystem` wrapper만 쓰도록 유도 |
| `kotlin-android` plugin | 명시 적용 | **명시 적용 금지** — AGP 9부터 내장 |

### 무엇을 했나
- `build-logic/convention/src/main/kotlin/calmong/AndroidApplicationConventionPlugin.kt` 신규
  - `com.android.application` + `calmong.spotless` + `calmong.detekt` 적용
  - catalog 기반 compileSdk(major+minor), defaultConfig(minSdk/targetSdk/testInstrumentationRunner), compileOptions(Java 11)
- `AndroidApplicationComposeConventionPlugin.kt` 신규
  - `org.jetbrains.kotlin.plugin.compose` 적용 + `buildFeatures.compose = true` + Compose BOM/ui/tooling 자동 주입
- `build-logic/convention/build.gradle.kts`에 register 블록 2개 추가
- `app/build.gradle.kts` 축소 — plugin 1줄(나중에 2줄로) + namespace + applicationId/versionCode/versionName + buildTypes.release + dependencies만 잔존

### 직접 해보기
1. 새 plugin 클래스 작성 (`Plugin<Project>` 구현, `extensions.configure<ApplicationExtension>` 안에서 설정)
2. `build-logic/convention/build.gradle.kts`의 `gradlePlugin { plugins { register(...) } }`에 등록
3. `./gradlew :build-logic:convention:assemble`로 plugin 컴파일 검증
4. app/build.gradle.kts의 `plugins {}`를 새 plugin id로 교체, android 블록에서 추출된 항목 삭제
5. `./gradlew :app:assembleDebug`로 통합 검증

## 2. NiA 패턴으로 재구조화

### 배경 / 왜
- application/library 두 baseline plugin이 거의 같은 일을 하면서 catalog 읽기 + compileSdk/Java/Kotlin 설정 코드를 각자 가지고 있었음 → 중복
- NiA(Now in Android)는 `Project.configureKotlinAndroid(CommonExtension)` 같은 확장 함수로 공통 로직을 추출하고, plugin 파일과 헬퍼 파일을 패키지로 분리하는 패턴을 정착시켜 둠 → 그걸 그대로 따라옴
- 사용자가 `nowinandroid/` 디렉토리에 NiA 프로젝트 원본을 참고용으로 받아두었음

### 2-A. 공통 로직 → 확장 함수
- `com/jingom/calmong/KotlinAndroid.kt` — `Project.configureKotlinAndroid(commonExtension: CommonExtension)`
  - catalog에서 SDK·Java 버전 읽기
  - `commonExtension.apply { compileSdk { ... }; defaultConfig.apply { minSdk = ... }; compileOptions.apply { ... } }`
  - `extensions.configure<KotlinAndroidProjectExtension> { compilerOptions { jvmTarget.set(...) } }`
- `com/jingom/calmong/AndroidCompose.kt` — `Project.configureAndroidCompose()`
  - Compose BOM + ui / ui-graphics / ui-tooling-preview + debug deps 자동 주입
- `com/jingom/calmong/ProjectExtensions.kt` — `internal val Project.libs: VersionCatalog`
  - `extensions.getByType<VersionCatalogsExtension>().named("libs")` 보일러플레이트 제거

### 2-B. compose plugin의 transitive apply 제거
**Before:**
```kotlin
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("calmong.android.application")   // ← baseline을 transitive 적용
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        ...
    }
}
```

**After (NiA 패턴):**
```kotlin
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")                  // ← AGP plugin 직접 apply
            apply("org.jetbrains.kotlin.plugin.compose")
        }
        extensions.configure<ApplicationExtension> {
            buildFeatures.compose = true
        }
        configureAndroidCompose()
    }
}
```

모듈은 두 plugin을 모두 명시:
```kotlin
// app/build.gradle.kts
plugins {
    id("calmong.android.application")
    id("calmong.android.application.compose")
}
```

이렇게 하면 `calmong.android.application`(baseline)이 spotless/detekt/compileSdk 등을 채우고, `.compose`는 Compose 관련만 더한다. Gradle plugin manager가 `com.android.application` 중복 적용을 자동으로 처리하므로 충돌 없음.

### 2-C. 파일 구조 분리
**Before:** 모든 클래스가 `build-logic/convention/src/main/kotlin/calmong/` 한 패키지에 모여 있었음

**After (NiA 구조):**
```
build-logic/convention/src/main/kotlin/
├── SpotlessConventionPlugin.kt              ← default 패키지 (package 선언 없음)
├── DetektConventionPlugin.kt
├── AndroidApplicationConventionPlugin.kt
├── AndroidApplicationComposeConventionPlugin.kt
├── AndroidLibraryConventionPlugin.kt
├── AndroidLibraryComposeConventionPlugin.kt
└── com/jingom/calmong/
    ├── ProjectExtensions.kt    ← package com.jingom.calmong
    ├── KotlinAndroid.kt
    └── AndroidCompose.kt
```

- `build-logic/convention/build.gradle.kts`의 `implementationClass`도 `"calmong.AndroidApplicationConventionPlugin"` → `"AndroidApplicationConventionPlugin"`으로 단순화
- plugin 파일은 헬퍼를 `import com.jingom.calmong.{configureKotlinAndroid, configureAndroidCompose, libs}`로 명시

### 무엇을 했나 (요약)
1. `KotlinAndroid.kt`, `AndroidCompose.kt`, `ProjectExtensions.kt` 작성 (3 헬퍼)
2. 4개 convention plugin을 헬퍼 호출 + 차이점만 inline하는 구조로 재작성
3. `git mv`로 9개 파일을 새 위치로 이동, package 선언 일괄 갱신
4. `build-logic/convention/build.gradle.kts`의 implementationClass 단순화
5. `:app/build.gradle.kts`와 `:core:designsystem/build.gradle.kts`에 두 plugin 모두 명시 적용

## 핵심 명령어·파일 모음

```bash
# convention plugin 컴파일만 검증
./gradlew :build-logic:convention:assemble

# 전체 회귀 검증
./gradlew :app:assembleDebug :app:spotlessCheck :app:detekt \
          :core:designsystem:assembleDebug :core:designsystem:spotlessCheck :core:designsystem:detekt
```

핵심 파일:
- `build-logic/convention/build.gradle.kts` — plugin 등록 + compileOnly 의존성
- `gradle/libs.versions.toml` — `android-gradlePlugin = { ..., name = "gradle-api", ... }` (gradle 아님)
- `build-logic/convention/src/main/kotlin/com/jingom/calmong/KotlinAndroid.kt`
- `build-logic/convention/src/main/kotlin/com/jingom/calmong/AndroidCompose.kt`
- `build-logic/convention/src/main/kotlin/com/jingom/calmong/ProjectExtensions.kt`
- 모듈은 항상 baseline + .compose 두 plugin 모두 명시 (예: `core/designsystem/build.gradle.kts`)

## 함정 / 주의

### AGP 9의 `getDefaultProguardFile`이 plugin 컨텍스트에서 접근 불가
- `getDefaultProguardFile("proguard-android-optimize.txt")`은 build script DSL에서만 노출되는 함수. plugin 클래스 안에서는 못 부름.
- `com.android.build.gradle.internal.ProguardFiles` 같은 내부 클래스 경로 시도 → AGP 9에서 unresolved
- 결론: `buildTypes.release` 블록은 plugin에서 제외하고 `:app/build.gradle.kts`에 그대로 둠. NiA도 동일한 처리.

### AGP 9는 `org.jetbrains.kotlin.android` plugin 내장 — 명시 적용 시 즉시 실패
- 메시지: "The 'org.jetbrains.kotlin.android' plugin is no longer required for Kotlin support since AGP 9.0."
- 해결: `pluginManager.apply("org.jetbrains.kotlin.android")` 호출 자체를 제거. `KotlinAndroidProjectExtension`은 AGP 9 내장 Kotlin이 자동 등록해주므로 `extensions.configure<KotlinAndroidProjectExtension> { ... }`는 그대로 동작.

### `gradle` vs `gradle-api` 아티팩트
- AGP 9에서 DSL 인터페이스(`CommonExtension`, `ApplicationExtension`, `LibraryExtension`)는 `gradle-api` 아티팩트에 위치.
- 처음에 `compileOnly("com.android.tools.build:gradle")`로 시작했다가 일부 멤버 접근이 막혔음.
- 해결: `libs.versions.toml`에서 `android-gradlePlugin = { group = "com.android.tools.build", name = "gradle-api", version.ref = "agp" }`로 변경. NiA와 동일.

### `CommonExtension`의 일부 멤버가 abstract 경로로 접근 불가 (AGP 9.2.1)
- NiA(AGP 9.0.0)와 미묘하게 다른 부분. 우리 9.2.1에서는 다음이 `CommonExtension` 통해서 안 됨:
  - `compileOptions { ... }` 람다 폼 → `compileOptions.apply { ... }`로 우회
  - `defaultConfig { ... }` 람다 안의 `testInstrumentationRunner` → 헬퍼에서 빼고 각 plugin이 concrete extension에서 `defaultConfig.testInstrumentationRunner = "..."` 직접 설정
  - `buildFeatures.compose` → 헬퍼에서 빼고 각 compose plugin이 `extensions.configure<ApplicationExtension> { buildFeatures.compose = true }` 직접 설정
- 진단법: `unzip -p .../gradle-api-9.2.1.jar com/android/build/api/dsl/CommonExtension.class | javap -p -` 로 실제 멤버 확인 가능 (실제로 했음)
- 교훈: NiA를 그대로 복붙하면 안 됨. AGP 버전에 따라 일부 멤버의 노출 경로가 다르므로 검증하면서 적용.

### 모듈에서 `.compose` plugin만 적용 → 빌드 실패
- NiA 패턴에서 compose plugin은 baseline을 transitive apply하지 않음. baseline의 compileSdk 설정 등이 빠지면 AGP가 "compileSdk 미지정" 오류.
- 모듈 build.gradle.kts에는 baseline과 .compose **둘 다** 명시해야 함.

### `git mv` 직후 Edit 도구가 새 경로를 모름
- 파일을 옮긴 직후 새 경로로 Edit 호출하면 "File has not been read yet" 에러
- 해결: Read로 한 번 읽은 다음 Edit
