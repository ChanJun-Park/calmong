package com.jingom.calmong.core.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * calmong semantic 색상 시스템.
 *
 * primitive([TailwindPalette])를 의미 단위로 매핑한 레이어. 화면/컴포넌트는 항상 이 토큰을 쓴다.
 * `CalMongTheme.colors.primary.background.default` 처럼 토큰 경로 그대로 접근한다.
 *
 * 층위(layer)
 * - 1단계: [primary] / [secondary] / [neutral] / [functional]
 * - 2단계: brand(primary·secondary)·neutral → background / foreground / stroke,
 *          functional → common / general / specific
 * - 3단계: 각 2단계의 variant (background.default, foreground.subtle, …)
 * - 4단계(선택): 상호작용 상태 — brand의 [BrandColors.interaction] ([InteractionStates])
 *
 * light/dark 두 벌은 [lightCalMongColorScheme] / [darkCalMongColorScheme]에 정의한다.
 */
@Immutable
data class CalMongColorScheme(
    val primary: BrandColors,
    val secondary: BrandColors,
    val neutral: NeutralColors,
    val functional: FunctionalColors,
)

// ---------------------------------------------------------------------------
// Brand (primary / secondary) — 브랜드 색상 집합
// ---------------------------------------------------------------------------

@Immutable
data class BrandColors(
    val background: BrandBackground,
    val foreground: BrandForeground,
    val stroke: BrandStroke,
)

@Immutable
data class BrandBackground(
    /** 기본 브랜드 채움 (filled 버튼/칩 배경) */
    val default: Color,
    /** 옅은 브랜드 틴트 표면 (강조 배너/선택 배경) */
    val subtle: Color,
    /** 더 진한 채움 (강한 강조) */
    val bold: Color,
    /** 흐려진 채움 (눌림/비활성 톤) */
    val dimmed: Color,
    /** 역상 브랜드 표면 (light는 어둡게, dark는 밝게) */
    val inverted: Color,
)

@Immutable
data class BrandForeground(
    /** background.default(브랜드 채움) 위에 올라가는 콘텐츠 — 채움 위에서 접근성 충족 */
    val default: Color,
    /** 채움 위 보조(덜 강조된) 콘텐츠 */
    val subtle: Color,
    /** background.inverted(역상 표면) 위에 올라가는 콘텐츠 */
    val inverted: Color,
)

@Immutable
data class BrandStroke(
    /** 브랜드 외곽선 (상태별 색) */
    val default: InteractionStates,
    /** 옅은 브랜드 외곽선 (상태별 색) */
    val subtle: InteractionStates,
)

// ---------------------------------------------------------------------------
// Neutral — 브랜드와 무관한 공통 표면/텍스트/선
// ---------------------------------------------------------------------------

@Immutable
data class NeutralColors(
    val background: NeutralBackground,
    val foreground: NeutralForeground,
    val stroke: NeutralStroke,
)

/**
 * 표면 elevation 사다리. light는 그림자로, dark는 색으로 높이를 표현한다.
 * `raised`(일반)는 [raised1]과 같다.
 */
@Immutable
data class NeutralBackground(
    /** 앱 최하단 배경 */
    val base: Color,
    /** 기본 표면(카드) */
    val default: Color,
    /** 1단계 떠오른 표면(시트/카드) */
    val raised1: Color,
    /** 2단계 떠오른 표면(메뉴/툴팁) */
    val raised2: Color,
    /** 가라앉은 표면(인셋/well) */
    val dimmed: Color,
    /** 반전 표면(스낵바 등) */
    val inverted: Color,
)

@Immutable
data class NeutralForeground(
    /** 최고 대비 고정 텍스트(이미지 위 등) */
    val static: Color,
    /** 기본 본문 텍스트 */
    val default: Color,
    /** 보조 텍스트 */
    val subtle: Color,
    /** 장식/비활성 텍스트·아이콘 */
    val decorative: Color,
    /** 반투명 오버레이 텍스트 */
    val alpha: Color,
    /** 반전 표면 위 텍스트 */
    val inverted: Color,
)

@Immutable
data class NeutralStroke(
    /** 헤어라인 구분선(가장 약함, 상태별 색) */
    val divider: InteractionStates,
    /** 옅은 선 (상태별 색) */
    val subtle: InteractionStates,
    /** 기본 외곽선 (상태별 색) */
    val default: InteractionStates,
    /** 강한 고정 외곽선 (상태별 색) */
    val static: InteractionStates,
)

// ---------------------------------------------------------------------------
// Functional — 기능/상태를 나타내는 색상
// ---------------------------------------------------------------------------

@Immutable
data class FunctionalColors(
    val common: CommonFunctional,
    val general: GeneralFunctional,
    val specific: SpecificFunctional,
    /**
     * 컴포넌트 위(z축)에 덧대는 반투명 상태 레이어. 알파로 상호작용을 표현한다.
     * 배경의 상태 처리는 이 레이어를 올려서 한다(배경색 자체를 바꾸지 않음).
     */
    val stateLayer: StateLayerColors,
)

/**
 * 반투명 상태 레이어 색. 요소의 밝기에 따라 고른다(테마 모드와 무관 — 둘 다 모드 독립).
 * - [soft]: 옅은 색 요소(흰/연한 표면, ghost 버튼) 위 → 어둡게(검정 알파)
 * - [solid]: 짙은 색 요소(브랜드 채움, 이미지) 위 → 밝게(흰색 알파)
 */
@Immutable
data class StateLayerColors(
    val soft: InteractionStates,
    val solid: InteractionStates,
)

@Immutable
data class CommonFunctional(
    val positive: FunctionalVariant,
    val negative: FunctionalVariant,
    val informative: FunctionalVariant,
    val attention: FunctionalVariant,
)

@Immutable
data class FunctionalVariant(
    /** 기본 채움/강조 */
    val default: Color,
    /** 장식용(아이콘/그래프 등) */
    val decorative: Color,
    /** 옅은 배경 틴트 — 권장: subtle 배경 + default 텍스트로 접근성 확보 */
    val subtle: Color,
)

/** 상태와 무관하게 공통으로 쓰이는 기능색. */
@Immutable
data class GeneralFunctional(
    /** 모달/바텀시트 뒤 스크림 */
    val overlay: Color,
    /** 선택/하이라이트 배경 */
    val highlight: Color,
    /** elevation 그림자 색 */
    val shadow: Color,
    /** 비활성 표면/채움 */
    val disabled: Color,
)

/** 앱 고유 의미를 갖는 기능색. */
@Immutable
data class SpecificFunctional(
    /** 좋아요/애정 표현 */
    val like: FunctionalVariant,
    /** 하이퍼링크 */
    val link: FunctionalVariant,
)

// ---------------------------------------------------------------------------
// 상호작용 상태 (4단계)
// ---------------------------------------------------------------------------

/**
 * 상호작용 상태별 색 묶음.
 * - [StateLayerColors]: 컴포넌트 위에 덧대는 반투명 오버레이(알파 값)
 * - stroke([BrandStroke]/[NeutralStroke]): 외곽선은 색 자체가 바뀌므로 불투명 색
 */
@Immutable
data class InteractionStates(
    val default: Color,
    val hover: Color,
    val focused: Color,
    /** 눌림(clicked) */
    val pressed: Color,
    /** 활성/선택 유지 */
    val activated: Color,
    val disabled: Color,
)

/**
 * 트리에 [CalMongColorScheme]가 제공되지 않은 채 접근하면 즉시 실패하도록 기본값을 두지 않는다.
 * 항상 `CalMongTheme`이 값을 채운다.
 */
internal val LocalCalMongColorScheme =
    staticCompositionLocalOf<CalMongColorScheme> {
        error("CalMongColorScheme가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
