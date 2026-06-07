package com.jingom.calmong.core.designsystem.theme.color

/*
 * semantic 토큰 → primitive 매핑.
 *
 * 같은 토큰의 light/dark 값을 나란히 두어 매핑을 한눈에 비교할 수 있게 했다.
 * 값을 바꿀 때는 Figma semantic 토큰(`tokens/semantic.color.{light,dark}.json`)도 함께 맞춘다.
 *
 * brand: primary=Indigo, secondary=Amber.
 * functional: positive=Green, negative=Red, informative/link=Blue, attention=Amber, like=Rose.
 *
 * 콘텐츠 색 규칙 (onColor 폐기)
 * - 브랜드 채움 위 콘텐츠: brand.foreground.default (채움 위에서 접근성 충족하도록 매핑)
 * - functional은 "subtle 배경 + default 텍스트" 패턴 권장 (솔리드 채움 위 텍스트는 지양)
 * - 이미지/스크림/어두운 고정면 위: neutral.foreground.static (모드 독립 흰색)
 *
 * 상태(state) 처리
 * - 배경/채움: functional.stateLayer(soft=옅은 요소용 검정, solid=짙은 요소용 흰색)를 z축으로 올림
 * - 외곽선: 색 자체가 바뀌므로 각 stroke 변형이 InteractionStates 보유 (default/static은 3:1 충족)
 */

@Suppress("LongMethod")
fun lightCalMongColorScheme(): CalMongColorScheme =
    CalMongColorScheme(
        primary =
            BrandColors(
                background =
                    BrandBackground(
                        default = Indigo600,
                        subtle = Indigo50,
                        bold = Indigo700,
                        dimmed = Indigo100,
                        inverted = Indigo900,
                    ),
                foreground =
                    BrandForeground(
                        default = White,
                        subtle = Indigo200,
                        inverted = Indigo50,
                    ),
                stroke =
                    BrandStroke(
                        default =
                            InteractionStates(
                                default = Indigo600,
                                hover = Indigo700,
                                focused = Indigo800,
                                pressed = Indigo800,
                                activated = Indigo700,
                                disabled = Gray200,
                            ),
                        subtle =
                            InteractionStates(
                                default = Indigo200,
                                hover = Indigo300,
                                focused = Indigo400,
                                pressed = Indigo400,
                                activated = Indigo300,
                                disabled = Gray100,
                            ),
                    ),
            ),
        secondary =
            BrandColors(
                background =
                    BrandBackground(
                        default = Amber500,
                        subtle = Amber50,
                        bold = Amber600,
                        dimmed = Amber100,
                        inverted = Amber900,
                    ),
                foreground =
                    BrandForeground(
                        default = Gray950,
                        subtle = Gray800,
                        inverted = Amber50,
                    ),
                stroke =
                    BrandStroke(
                        default =
                            InteractionStates(
                                default = Amber600,
                                hover = Amber700,
                                focused = Amber800,
                                pressed = Amber800,
                                activated = Amber700,
                                disabled = Gray200,
                            ),
                        subtle =
                            InteractionStates(
                                default = Amber200,
                                hover = Amber300,
                                focused = Amber400,
                                pressed = Amber400,
                                activated = Amber300,
                                disabled = Gray100,
                            ),
                    ),
            ),
        neutral =
            NeutralColors(
                // light: 그림자로 elevation 표현 → 표면 색은 흰색 계열로 근접
                background =
                    NeutralBackground(
                        base = Gray50,
                        default = White,
                        raised1 = White,
                        raised2 = White,
                        dimmed = Gray100,
                        inverted = Gray900,
                    ),
                foreground =
                    NeutralForeground(
                        static = White,
                        default = Gray900,
                        subtle = Gray500,
                        decorative = Gray300,
                        alpha = BlackAlpha60,
                        inverted = White,
                    ),
                stroke =
                    NeutralStroke(
                        divider =
                            InteractionStates(
                                default = Gray100,
                                hover = Gray200,
                                focused = Gray300,
                                pressed = Gray300,
                                activated = Gray200,
                                disabled = Gray50,
                            ),
                        subtle =
                            InteractionStates(
                                default = Gray200,
                                hover = Gray300,
                                focused = Gray400,
                                pressed = Gray400,
                                activated = Gray300,
                                disabled = Gray100,
                            ),
                        // 컴포넌트 경계 → 3:1 충족 위해 Gray500부터
                        default =
                            InteractionStates(
                                default = Gray500,
                                hover = Gray600,
                                focused = Gray700,
                                pressed = Gray700,
                                activated = Gray600,
                                disabled = Gray200,
                            ),
                        static =
                            InteractionStates(
                                default = Gray600,
                                hover = Gray700,
                                focused = Gray800,
                                pressed = Gray800,
                                activated = Gray700,
                                disabled = Gray300,
                            ),
                    ),
            ),
        functional =
            FunctionalColors(
                common =
                    CommonFunctional(
                        positive =
                            FunctionalVariant(
                                default = Green600,
                                decorative = Green500,
                                subtle = Green50,
                            ),
                        negative =
                            FunctionalVariant(
                                default = Red600,
                                decorative = Red500,
                                subtle = Red50,
                            ),
                        informative =
                            FunctionalVariant(
                                default = Blue600,
                                decorative = Blue500,
                                subtle = Blue50,
                            ),
                        attention =
                            FunctionalVariant(
                                default = Amber500,
                                decorative = Amber400,
                                subtle = Amber50,
                            ),
                    ),
                general =
                    GeneralFunctional(
                        overlay = BlackAlpha50,
                        highlight = Indigo100,
                        shadow = BlackAlpha10,
                        disabled = Gray200,
                    ),
                specific =
                    SpecificFunctional(
                        like =
                            FunctionalVariant(
                                default = Rose500,
                                decorative = Rose400,
                                subtle = Rose50,
                            ),
                        link =
                            FunctionalVariant(
                                default = Blue600,
                                decorative = Blue500,
                                subtle = Blue50,
                            ),
                    ),
                stateLayer =
                    StateLayerColors(
                        // 옅은 요소 위 → 검정 알파 (모드 독립)
                        soft =
                            InteractionStates(
                                default = Transparent,
                                hover = BlackAlpha08,
                                focused = BlackAlpha10,
                                pressed = BlackAlpha12,
                                activated = BlackAlpha16,
                                disabled = BlackAlpha38,
                            ),
                        // 짙은 요소 위 → 흰색 알파 (모드 독립)
                        solid =
                            InteractionStates(
                                default = Transparent,
                                hover = WhiteAlpha08,
                                focused = WhiteAlpha10,
                                pressed = WhiteAlpha12,
                                activated = WhiteAlpha16,
                                disabled = WhiteAlpha38,
                            ),
                    ),
            ),
    )

@Suppress("LongMethod")
fun darkCalMongColorScheme(): CalMongColorScheme =
    CalMongColorScheme(
        primary =
            BrandColors(
                background =
                    BrandBackground(
                        // default를 Indigo600로 둬 흰 콘텐츠가 AA(6.29) 통과
                        default = Indigo600,
                        subtle = Indigo950,
                        bold = Indigo400,
                        dimmed = Indigo900,
                        inverted = Indigo100,
                    ),
                foreground =
                    BrandForeground(
                        default = White,
                        subtle = Indigo200,
                        inverted = Indigo900,
                    ),
                stroke =
                    BrandStroke(
                        default =
                            InteractionStates(
                                default = Indigo500,
                                hover = Indigo400,
                                focused = Indigo300,
                                pressed = Indigo300,
                                activated = Indigo400,
                                disabled = Gray700,
                            ),
                        subtle =
                            InteractionStates(
                                default = Indigo800,
                                hover = Indigo700,
                                focused = Indigo600,
                                pressed = Indigo600,
                                activated = Indigo700,
                                disabled = Gray800,
                            ),
                    ),
            ),
        secondary =
            BrandColors(
                background =
                    BrandBackground(
                        default = Amber400,
                        subtle = Amber950,
                        bold = Amber300,
                        dimmed = Amber900,
                        inverted = Amber100,
                    ),
                foreground =
                    BrandForeground(
                        default = Gray950,
                        subtle = Gray800,
                        inverted = Gray950,
                    ),
                stroke =
                    BrandStroke(
                        default =
                            InteractionStates(
                                default = Amber400,
                                hover = Amber300,
                                focused = Amber200,
                                pressed = Amber200,
                                activated = Amber300,
                                disabled = Gray700,
                            ),
                        subtle =
                            InteractionStates(
                                default = Amber800,
                                hover = Amber700,
                                focused = Amber600,
                                pressed = Amber600,
                                activated = Amber700,
                                disabled = Gray800,
                            ),
                    ),
            ),
        neutral =
            NeutralColors(
                // dark: 색으로 elevation 표현 → 떠오를수록 밝아짐
                background =
                    NeutralBackground(
                        base = Gray950,
                        default = Gray900,
                        raised1 = Gray800,
                        raised2 = Gray700,
                        dimmed = Black,
                        inverted = Gray50,
                    ),
                foreground =
                    NeutralForeground(
                        static = White,
                        default = Gray50,
                        subtle = Gray400,
                        decorative = Gray600,
                        alpha = WhiteAlpha60,
                        inverted = Gray900,
                    ),
                stroke =
                    NeutralStroke(
                        divider =
                            InteractionStates(
                                default = Gray800,
                                hover = Gray700,
                                focused = Gray600,
                                pressed = Gray600,
                                activated = Gray700,
                                disabled = Gray900,
                            ),
                        subtle =
                            InteractionStates(
                                default = Gray700,
                                hover = Gray600,
                                focused = Gray500,
                                pressed = Gray500,
                                activated = Gray600,
                                disabled = Gray800,
                            ),
                        // 컴포넌트 경계 → 3:1 충족 위해 Gray500부터
                        default =
                            InteractionStates(
                                default = Gray500,
                                hover = Gray400,
                                focused = Gray300,
                                pressed = Gray300,
                                activated = Gray400,
                                disabled = Gray700,
                            ),
                        static =
                            InteractionStates(
                                default = Gray400,
                                hover = Gray300,
                                focused = Gray200,
                                pressed = Gray200,
                                activated = Gray300,
                                disabled = Gray700,
                            ),
                    ),
            ),
        functional =
            FunctionalColors(
                common =
                    CommonFunctional(
                        positive =
                            FunctionalVariant(
                                default = Green500,
                                decorative = Green400,
                                subtle = Green950,
                            ),
                        negative =
                            FunctionalVariant(
                                default = Red500,
                                decorative = Red400,
                                subtle = Red950,
                            ),
                        informative =
                            FunctionalVariant(
                                default = Blue500,
                                decorative = Blue400,
                                subtle = Blue950,
                            ),
                        attention =
                            FunctionalVariant(
                                default = Amber400,
                                decorative = Amber300,
                                subtle = Amber950,
                            ),
                    ),
                general =
                    GeneralFunctional(
                        overlay = BlackAlpha70,
                        highlight = Indigo900,
                        shadow = BlackAlpha40,
                        disabled = Gray700,
                    ),
                specific =
                    SpecificFunctional(
                        like =
                            FunctionalVariant(
                                default = Rose400,
                                decorative = Rose300,
                                subtle = Rose950,
                            ),
                        link =
                            FunctionalVariant(
                                default = Blue400,
                                decorative = Blue300,
                                subtle = Blue950,
                            ),
                    ),
                stateLayer =
                    StateLayerColors(
                        soft =
                            InteractionStates(
                                default = Transparent,
                                hover = BlackAlpha08,
                                focused = BlackAlpha10,
                                pressed = BlackAlpha12,
                                activated = BlackAlpha16,
                                disabled = BlackAlpha38,
                            ),
                        solid =
                            InteractionStates(
                                default = Transparent,
                                hover = WhiteAlpha08,
                                focused = WhiteAlpha10,
                                pressed = WhiteAlpha12,
                                activated = WhiteAlpha16,
                                disabled = WhiteAlpha38,
                            ),
                    ),
            ),
    )
