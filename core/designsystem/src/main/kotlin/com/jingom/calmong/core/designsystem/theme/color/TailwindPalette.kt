package com.jingom.calmong.core.designsystem.theme.color

import androidx.compose.ui.graphics.Color

/*
 * Primitive(원시) 색상 토큰.
 *
 * Figma에서 Tailwind Tokens 플러그인으로 만든 variable(`core/designsystem/color.json`)을
 * 코드로 옮긴 1:1 미러. 이 레이어는 "raw 팔레트"일 뿐 의미(semantic)를 갖지 않는다.
 * 화면/컴포넌트에서 직접 쓰지 말고 항상 CalMongColorScheme의 semantic 토큰을 거친다.
 *
 * semantic 레이어가 참조하는 7개 패밀리만 옮겼다(gray/indigo/amber/green/red/blue/rose).
 * 다른 팔레트가 필요해지면 그때 추가한다(YAGNI).
 */

// Gray
internal val Gray50 = Color(0xFFF9FAFB)
internal val Gray100 = Color(0xFFF3F4F6)
internal val Gray200 = Color(0xFFE5E7EB)
internal val Gray300 = Color(0xFFD1D5DB)
internal val Gray400 = Color(0xFF9CA3AF)
internal val Gray500 = Color(0xFF6B7280)
internal val Gray600 = Color(0xFF4B5563)
internal val Gray700 = Color(0xFF374151)
internal val Gray800 = Color(0xFF1F2937)
internal val Gray900 = Color(0xFF111827)
internal val Gray950 = Color(0xFF030712)

// Indigo (primary brand)
internal val Indigo50 = Color(0xFFEEF2FF)
internal val Indigo100 = Color(0xFFE0E7FF)
internal val Indigo200 = Color(0xFFC7D2FE)
internal val Indigo300 = Color(0xFFA5B4FC)
internal val Indigo400 = Color(0xFF818CF8)
internal val Indigo500 = Color(0xFF6366F1)
internal val Indigo600 = Color(0xFF4F46E5)
internal val Indigo700 = Color(0xFF4338CA)
internal val Indigo800 = Color(0xFF3730A3)
internal val Indigo900 = Color(0xFF312E81)
internal val Indigo950 = Color(0xFF1E1B4B)

// Amber (secondary brand)
internal val Amber50 = Color(0xFFFFFBEB)
internal val Amber100 = Color(0xFFFEF3C7)
internal val Amber200 = Color(0xFFFDE68A)
internal val Amber300 = Color(0xFFFCD34D)
internal val Amber400 = Color(0xFFFBBF24)
internal val Amber500 = Color(0xFFF59E0B)
internal val Amber600 = Color(0xFFD97706)
internal val Amber700 = Color(0xFFB45309)
internal val Amber800 = Color(0xFF92400E)
internal val Amber900 = Color(0xFF78350F)
internal val Amber950 = Color(0xFF451A03)

// Green (functional: positive)
internal val Green50 = Color(0xFFF0FDF4)
internal val Green100 = Color(0xFFDCFCE7)
internal val Green200 = Color(0xFFBBF7D0)
internal val Green300 = Color(0xFF86EFAC)
internal val Green400 = Color(0xFF4ADE80)
internal val Green500 = Color(0xFF22C55E)
internal val Green600 = Color(0xFF16A34A)
internal val Green700 = Color(0xFF15803D)
internal val Green800 = Color(0xFF166534)
internal val Green900 = Color(0xFF14532D)
internal val Green950 = Color(0xFF052E16)

// Red (functional: negative)
internal val Red50 = Color(0xFFFEF2F2)
internal val Red100 = Color(0xFFFEE2E2)
internal val Red200 = Color(0xFFFECACA)
internal val Red300 = Color(0xFFFCA5A5)
internal val Red400 = Color(0xFFF87171)
internal val Red500 = Color(0xFFEF4444)
internal val Red600 = Color(0xFFDC2626)
internal val Red700 = Color(0xFFB91C1C)
internal val Red800 = Color(0xFF991B1B)
internal val Red900 = Color(0xFF7F1D1D)
internal val Red950 = Color(0xFF450A0A)

// Blue (functional: informative / link)
internal val Blue50 = Color(0xFFEFF6FF)
internal val Blue100 = Color(0xFFDBEAFE)
internal val Blue200 = Color(0xFFBFDBFE)
internal val Blue300 = Color(0xFF93C5FD)
internal val Blue400 = Color(0xFF60A5FA)
internal val Blue500 = Color(0xFF3B82F6)
internal val Blue600 = Color(0xFF2563EB)
internal val Blue700 = Color(0xFF1D4ED8)
internal val Blue800 = Color(0xFF1E40AF)
internal val Blue900 = Color(0xFF1E3A8A)
internal val Blue950 = Color(0xFF172554)

// Rose (functional: like)
internal val Rose50 = Color(0xFFFFF1F2)
internal val Rose100 = Color(0xFFFFE4E6)
internal val Rose200 = Color(0xFFFECDD3)
internal val Rose300 = Color(0xFFFDA4AF)
internal val Rose400 = Color(0xFFFB7185)
internal val Rose500 = Color(0xFFF43F5E)
internal val Rose600 = Color(0xFFE11D48)
internal val Rose700 = Color(0xFFBE123C)
internal val Rose800 = Color(0xFF9F1239)
internal val Rose900 = Color(0xFF881337)
internal val Rose950 = Color(0xFF4C0519)

internal val White = Color(0xFFFFFFFF)
internal val Black = Color(0xFF000000)

internal val Transparent = Color(0x00000000)

// Alpha (primitive 팔레트에 대응값이 없는 반투명 토큰용. 이름 끝 숫자는 불투명도 %)
internal val BlackAlpha08 = Color(0x14000000)
internal val BlackAlpha10 = Color(0x1A000000)
internal val BlackAlpha12 = Color(0x1F000000)
internal val BlackAlpha16 = Color(0x29000000)
internal val BlackAlpha38 = Color(0x61000000)
internal val BlackAlpha40 = Color(0x66000000)
internal val BlackAlpha50 = Color(0x80000000)
internal val BlackAlpha60 = Color(0x99000000)
internal val BlackAlpha70 = Color(0xB3000000)
internal val WhiteAlpha08 = Color(0x14FFFFFF)
internal val WhiteAlpha10 = Color(0x1AFFFFFF)
internal val WhiteAlpha12 = Color(0x1FFFFFFF)
internal val WhiteAlpha16 = Color(0x29FFFFFF)
internal val WhiteAlpha38 = Color(0x61FFFFFF)
internal val WhiteAlpha60 = Color(0x99FFFFFF)
