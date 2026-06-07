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
 * 상태(state) 처리
 * - 배경/채움: functional.stateLayer(반투명 오버레이)를 z축으로 올려서 표현 → background 토큰은 상태 미보유
 * - 외곽선: 색 자체가 바뀌므로 각 stroke 변형이 InteractionStates를 직접 보유
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
                    ),
                foreground =
                    BrandForeground(
                        default = Indigo600,
                        subtle = Indigo400,
                        onColor = White,
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
                    ),
                foreground =
                    BrandForeground(
                        default = Amber600,
                        subtle = Amber500,
                        onColor = Gray950,
                    ),
                stroke =
                    BrandStroke(
                        default =
                            InteractionStates(
                                default = Amber500,
                                hover = Amber600,
                                focused = Amber700,
                                pressed = Amber700,
                                activated = Amber600,
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
                        static = Gray950,
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
                        default =
                            InteractionStates(
                                default = Gray300,
                                hover = Gray400,
                                focused = Gray500,
                                pressed = Gray500,
                                activated = Gray400,
                                disabled = Gray200,
                            ),
                        static =
                            InteractionStates(
                                default = Gray400,
                                hover = Gray500,
                                focused = Gray600,
                                pressed = Gray600,
                                activated = Gray500,
                                disabled = Gray200,
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
                                onColor = White,
                            ),
                        negative =
                            FunctionalVariant(
                                default = Red600,
                                decorative = Red500,
                                subtle = Red50,
                                onColor = White,
                            ),
                        informative =
                            FunctionalVariant(
                                default = Blue600,
                                decorative = Blue500,
                                subtle = Blue50,
                                onColor = White,
                            ),
                        attention =
                            FunctionalVariant(
                                default = Amber500,
                                decorative = Amber400,
                                subtle = Amber50,
                                onColor = Gray950,
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
                                onColor = White,
                            ),
                        link =
                            FunctionalVariant(
                                default = Blue600,
                                decorative = Blue500,
                                subtle = Blue50,
                                onColor = White,
                            ),
                    ),
                // 흰 배경 위 → 검정 계열 오버레이를 옅게~진하게
                stateLayer =
                    InteractionStates(
                        default = Transparent,
                        hover = BlackAlpha08,
                        focused = BlackAlpha10,
                        pressed = BlackAlpha12,
                        activated = BlackAlpha16,
                        disabled = BlackAlpha38,
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
                        default = Indigo500,
                        subtle = Indigo950,
                        bold = Indigo400,
                        dimmed = Indigo900,
                    ),
                foreground =
                    BrandForeground(
                        default = Indigo400,
                        subtle = Indigo500,
                        onColor = White,
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
                    ),
                foreground =
                    BrandForeground(
                        default = Amber400,
                        subtle = Amber500,
                        onColor = Gray950,
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
                        default =
                            InteractionStates(
                                default = Gray600,
                                hover = Gray500,
                                focused = Gray400,
                                pressed = Gray400,
                                activated = Gray500,
                                disabled = Gray700,
                            ),
                        static =
                            InteractionStates(
                                default = Gray500,
                                hover = Gray400,
                                focused = Gray300,
                                pressed = Gray300,
                                activated = Gray400,
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
                                onColor = White,
                            ),
                        negative =
                            FunctionalVariant(
                                default = Red500,
                                decorative = Red400,
                                subtle = Red950,
                                onColor = White,
                            ),
                        informative =
                            FunctionalVariant(
                                default = Blue500,
                                decorative = Blue400,
                                subtle = Blue950,
                                onColor = White,
                            ),
                        attention =
                            FunctionalVariant(
                                default = Amber400,
                                decorative = Amber300,
                                subtle = Amber950,
                                onColor = Gray950,
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
                                onColor = White,
                            ),
                        link =
                            FunctionalVariant(
                                default = Blue400,
                                decorative = Blue300,
                                subtle = Blue950,
                                onColor = White,
                            ),
                    ),
                // 어두운 배경 위 → 흰 계열 오버레이를 옅게~진하게
                stateLayer =
                    InteractionStates(
                        default = Transparent,
                        hover = WhiteAlpha08,
                        focused = WhiteAlpha10,
                        pressed = WhiteAlpha12,
                        activated = WhiteAlpha16,
                        disabled = WhiteAlpha38,
                    ),
            ),
    )
