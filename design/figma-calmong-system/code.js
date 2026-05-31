const pageName = "CalMong Design System";

const colors = {
  brandPrimary: "#6B4EE6",
  fab: "#3F3F46",
  sunday: "#E83E63",
  saturday: "#4A67E8",
  personal: "#C63D5A",
  work: "#2DAE91",
  family: "#F7CBD8",
  habit: "#6423C8",
  invite: "#FF8A70",
  yellow: "#F8D45C",
  surface: "#FFFFFF",
  subtle: "#F5F6F8",
  selected: "#E9ECEF",
  border: "#E6E8EB",
  text: "#202124",
  secondaryText: "#777A80",
  disabledText: "#C9CCD1",
  overlay: "#000000"
};

const typeStyles = [
  ["Display/Month", 32, 40, "Bold"],
  ["Title/Large", 28, 36, "Bold"],
  ["Title/Medium", 22, 30, "Bold"],
  ["Body/Large", 20, 30, "Regular"],
  ["Body/Medium", 18, 26, "Regular"],
  ["Label/Large", 16, 22, "Semi Bold"],
  ["Caption", 14, 20, "Regular"]
];

const swatches = [
  ["brand.primary", colors.brandPrimary],
  ["action.fab", colors.fab],
  ["calendar.sunday", colors.sunday],
  ["calendar.saturday", colors.saturday],
  ["calendar.personal", colors.personal],
  ["calendar.work", colors.work],
  ["calendar.family", colors.family],
  ["calendar.habit", colors.habit],
  ["calendar.invite", colors.invite],
  ["calendar.yellow", colors.yellow],
  ["surface.default", colors.surface],
  ["surface.subtle", colors.subtle],
  ["surface.selected", colors.selected],
  ["border.default", colors.border],
  ["text.primary", colors.text],
  ["text.secondary", colors.secondaryText],
  ["text.disabled", colors.disabledText]
];

const textStyleMap = {};
let fontFamily = "Inter";
let availableFontStyles = ["Regular"];

function hexToRgb(hex) {
  const value = hex.replace("#", "");
  return {
    r: parseInt(value.slice(0, 2), 16) / 255,
    g: parseInt(value.slice(2, 4), 16) / 255,
    b: parseInt(value.slice(4, 6), 16) / 255
  };
}

function solid(hex, opacity = 1) {
  return [{ type: "SOLID", color: hexToRgb(hex), opacity }];
}

function layout(node, direction = "VERTICAL", gap = 16, padding = 0) {
  node.layoutMode = direction;
  node.itemSpacing = gap;
  node.paddingTop = padding;
  node.paddingBottom = padding;
  node.paddingLeft = padding;
  node.paddingRight = padding;
  node.counterAxisSizingMode = "AUTO";
  node.primaryAxisSizingMode = "AUTO";
}

function fixedFrame(name, width, height, fill = colors.surface) {
  const frame = figma.createFrame();
  frame.name = name;
  frame.resize(width, height);
  frame.fills = solid(fill);
  frame.clipsContent = false;
  return frame;
}

function textNode(value, styleName = "Body/Medium", color = colors.text) {
  const node = figma.createText();
  const style = textStyleMap[styleName];
  node.fontName = style ? figma.getStyleById(style).fontName : { family: fontFamily, style: "Regular" };
  node.characters = value;
  if (style) node.textStyleId = style;
  node.fills = solid(color);
  return node;
}

function rect(name, width, height, fill, radius = 0, stroke) {
  const node = figma.createRectangle();
  node.name = name;
  node.resize(width, height);
  node.fills = fill ? solid(fill) : [];
  node.cornerRadius = radius;
  if (stroke) {
    node.strokes = solid(stroke);
    node.strokeWeight = 1;
  }
  return node;
}

function circle(name, size, fill, stroke) {
  const node = figma.createEllipse();
  node.name = name;
  node.resize(size, size);
  node.fills = fill ? solid(fill) : [];
  if (stroke) {
    node.strokes = solid(stroke);
    node.strokeWeight = 1.5;
  }
  return node;
}

function line(width, color = colors.border) {
  const node = figma.createLine();
  node.resize(width, 0);
  node.strokes = solid(color);
  node.strokeWeight = 1;
  return node;
}

function sectionTitle(title, caption) {
  const frame = fixedFrame(title, 720, 88);
  layout(frame, "VERTICAL", 6, 0);
  frame.fills = [];
  frame.appendChild(textNode(title, "Title/Large"));
  frame.appendChild(textNode(caption, "Caption", colors.secondaryText));
  return frame;
}

function createPaintStyles() {
  for (const [name, hex] of swatches) {
    const existing = figma.getLocalPaintStyles().find((style) => style.name === name);
    const style = existing || figma.createPaintStyle();
    style.name = name;
    style.paints = solid(hex);
  }
}

async function chooseFont() {
  const fonts = await figma.listAvailableFontsAsync();
  const candidates = ["Pretendard", "Noto Sans KR", "Apple SD Gothic Neo", "Inter", "Roboto"];
  for (const candidate of candidates) {
    if (fonts.some((font) => font.fontName.family === candidate)) {
      fontFamily = candidate;
      break;
    }
  }
  availableFontStyles = fonts
    .filter((font) => font.fontName.family === fontFamily)
    .map((font) => font.fontName.style);
  const required = ["Regular", "Bold", "Semi Bold"];
  for (const style of required) {
    const fallbackStyle = availableFontStyles.includes(style) ? style : availableFontStyles[0] || "Regular";
    await figma.loadFontAsync({ family: fontFamily, style: fallbackStyle });
  }
}

function fontStyle(styleName) {
  return availableFontStyles.includes(styleName) ? styleName : availableFontStyles[0] || "Regular";
}

function createTextStyles() {
  for (const [name, size, lineHeight, weight] of typeStyles) {
    const existing = figma.getLocalTextStyles().find((style) => style.name === name);
    const style = existing || figma.createTextStyle();
    style.name = name;
    style.fontName = { family: fontFamily, style: fontStyle(weight) };
    style.fontSize = size;
    style.lineHeight = { unit: "PIXELS", value: lineHeight };
    style.letterSpacing = { unit: "PIXELS", value: 0 };
    textStyleMap[name] = style.id;
  }
}

function colorBoard() {
  const frame = fixedFrame("Foundations / Color", 720, 1);
  layout(frame, "VERTICAL", 18, 0);
  frame.fills = [];
  frame.appendChild(sectionTitle("Colors", "Semantic tokens extracted from calendar, input, settings, diary screens."));

  const grid = fixedFrame("Color tokens", 720, 1);
  layout(grid, "HORIZONTAL", 16, 0);
  grid.layoutWrap = "WRAP";
  grid.fills = [];
  for (const [name, hex] of swatches) {
    const card = fixedFrame(name, 164, 132, colors.surface);
    layout(card, "VERTICAL", 10, 12);
    card.cornerRadius = 8;
    card.strokes = solid(colors.border);
    card.strokeWeight = 1;
    card.appendChild(rect("swatch", 140, 52, hex, 6));
    card.appendChild(textNode(name, "Caption"));
    card.appendChild(textNode(hex, "Caption", colors.secondaryText));
    grid.appendChild(card);
  }
  frame.appendChild(grid);
  return frame;
}

function typographyBoard() {
  const frame = fixedFrame("Foundations / Typography", 720, 1);
  layout(frame, "VERTICAL", 18, 0);
  frame.fills = [];
  frame.appendChild(sectionTitle("Typography", `${fontFamily} based scale for Compose sp and Figma px parity.`));
  for (const [name, size, lineHeight, weight] of typeStyles) {
    const row = fixedFrame(name, 720, 72, colors.surface);
    layout(row, "HORIZONTAL", 24, 16);
    row.counterAxisAlignItems = "CENTER";
    row.appendChild(textNode("가나다 ABC 123", name));
    row.appendChild(textNode(`${name} / ${size}px / ${lineHeight}px / ${weight}`, "Caption", colors.secondaryText));
    frame.appendChild(row);
  }
  return frame;
}

function spacingBoard() {
  const frame = fixedFrame("Foundations / Spacing + Radius", 720, 1);
  layout(frame, "VERTICAL", 18, 0);
  frame.fills = [];
  frame.appendChild(sectionTitle("Spacing, Radius, Elevation", "4dp grid, restrained card radius, shadow only for floating actions."));

  const row = fixedFrame("Spacing tokens", 720, 120);
  layout(row, "HORIZONTAL", 18, 16);
  row.fills = solid(colors.subtle);
  row.cornerRadius = 8;
  [4, 8, 12, 16, 20, 24, 32, 40, 48].forEach((size) => {
    const item = fixedFrame(`${size}`, 52, 88);
    layout(item, "VERTICAL", 8, 0);
    item.fills = [];
    item.counterAxisAlignItems = "CENTER";
    item.appendChild(rect("bar", size, 48, colors.brandPrimary, 2));
    item.appendChild(textNode(`${size}`, "Caption", colors.secondaryText));
    row.appendChild(item);
  });
  frame.appendChild(row);

  const radii = fixedFrame("Radius tokens", 720, 120);
  layout(radii, "HORIZONTAL", 24, 16);
  radii.fills = [];
  [["sm", 4], ["md", 8], ["lg", 16], ["full", 999]].forEach(([name, radius]) => {
    const item = fixedFrame(`radius.${name}`, 148, 88, colors.subtle);
    layout(item, "VERTICAL", 8, 12);
    item.cornerRadius = Math.min(radius, 28);
    item.appendChild(textNode(`radius.${name}`, "Caption"));
    item.appendChild(textNode(String(radius), "Caption", colors.secondaryText));
    radii.appendChild(item);
  });
  frame.appendChild(radii);
  return frame;
}

function appBarComponent(name, title, left, right) {
  const c = figma.createComponent();
  c.name = name;
  c.resize(360, 64);
  layout(c, "HORIZONTAL", 0, 16);
  c.counterAxisAlignItems = "CENTER";
  c.primaryAxisAlignItems = "SPACE_BETWEEN";
  c.fills = solid(colors.surface);
  c.appendChild(textNode(left, "Title/Large"));
  const center = textNode(title, title.includes(".") ? "Display/Month" : "Title/Large");
  c.appendChild(center);
  c.appendChild(textNode(right, "Title/Large"));
  return c;
}

function fabComponent() {
  const c = figma.createComponent();
  c.name = "Control / FAB / Create";
  c.resize(72, 72);
  c.cornerRadius = 36;
  c.fills = solid(colors.fab);
  c.effects = [{ type: "DROP_SHADOW", color: { r: 0, g: 0, b: 0, a: 0.2 }, offset: { x: 0, y: 8 }, radius: 18, spread: 0, visible: true, blendMode: "NORMAL" }];
  const plus = textNode("+", "Title/Large", colors.surface);
  plus.x = 25;
  plus.y = 15;
  c.appendChild(plus);
  return c;
}

function todayPillComponent() {
  const c = figma.createComponent();
  c.name = "Control / Today Pill";
  c.resize(92, 44);
  layout(c, "HORIZONTAL", 6, 16);
  c.counterAxisAlignItems = "CENTER";
  c.cornerRadius = 22;
  c.fills = solid(colors.surface);
  c.strokes = solid(colors.border);
  c.strokeWeight = 1;
  c.effects = [{ type: "DROP_SHADOW", color: { r: 0, g: 0, b: 0, a: 0.08 }, offset: { x: 0, y: 4 }, radius: 12, spread: 0, visible: true, blendMode: "NORMAL" }];
  c.appendChild(textNode("오늘", "Label/Large"));
  c.appendChild(textNode("›", "Body/Medium", colors.secondaryText));
  return c;
}

function dateCellComponent() {
  const c = figma.createComponent();
  c.name = "Calendar / Date Cell";
  c.resize(96, 112);
  layout(c, "VERTICAL", 8, 8);
  c.fills = solid(colors.surface);
  c.appendChild(textNode("25", "Title/Medium"));
  const dots = fixedFrame("event dots", 80, 12);
  layout(dots, "HORIZONTAL", 4, 0);
  dots.fills = [];
  dots.appendChild(circle("personal", 7, colors.personal));
  dots.appendChild(circle("family", 7, colors.family));
  c.appendChild(dots);
  return c;
}

function eventPillComponent() {
  const c = figma.createComponent();
  c.name = "Calendar / Event Pill";
  c.resize(132, 28);
  layout(c, "HORIZONTAL", 6, 0);
  c.counterAxisAlignItems = "CENTER";
  c.fills = solid("#FBEEF2");
  c.cornerRadius = 4;
  c.appendChild(rect("leading rail", 4, 28, colors.personal, 2));
  c.appendChild(textNode("일정명", "Caption", colors.text));
  return c;
}

function agendaRowComponent() {
  const c = figma.createComponent();
  c.name = "Calendar / Agenda Row";
  c.resize(360, 72);
  layout(c, "HORIZONTAL", 12, 0);
  c.counterAxisAlignItems = "CENTER";
  c.fills = solid(colors.surface);
  const time = fixedFrame("time", 76, 56);
  layout(time, "VERTICAL", 2, 0);
  time.fills = [];
  time.appendChild(textNode("오전 10:00", "Body/Medium"));
  time.appendChild(textNode("11:00", "Body/Medium", colors.secondaryText));
  c.appendChild(time);
  c.appendChild(rect("rail", 5, 56, colors.personal, 3));
  const text = fixedFrame("text", 240, 56);
  layout(text, "VERTICAL", 2, 0);
  text.fills = [];
  text.appendChild(textNode("부동산 계약", "Body/Large"));
  text.appendChild(textNode("최인수 부동산", "Body/Medium", colors.secondaryText));
  c.appendChild(text);
  return c;
}

function checkboxComponent() {
  const c = figma.createComponent();
  c.name = "Control / Checkbox";
  c.resize(36, 36);
  c.cornerRadius = 6;
  c.fills = solid(colors.work);
  c.appendChild(textNode("✓", "Title/Medium", colors.surface));
  c.children[0].x = 7;
  c.children[0].y = 1;
  return c;
}

function switchComponent() {
  const c = figma.createComponent();
  c.name = "Control / Switch / On";
  c.resize(54, 32);
  c.cornerRadius = 16;
  c.fills = solid(colors.brandPrimary);
  const knob = circle("knob", 30, colors.subtle);
  knob.x = 23;
  knob.y = 1;
  c.appendChild(knob);
  return c;
}

function settingsRowComponent() {
  const c = figma.createComponent();
  c.name = "List / Settings Row";
  c.resize(360, 64);
  layout(c, "HORIZONTAL", 12, 0);
  c.counterAxisAlignItems = "CENTER";
  c.primaryAxisAlignItems = "SPACE_BETWEEN";
  c.fills = solid(colors.surface);
  c.appendChild(textNode("완료 된 일정 및 할 일 보기", "Body/Large"));
  const sw = fixedFrame("switch", 54, 32, colors.brandPrimary);
  sw.cornerRadius = 16;
  const knob = circle("knob", 30, colors.subtle);
  knob.x = 23;
  knob.y = 1;
  sw.appendChild(knob);
  c.appendChild(sw);
  return c;
}

function calendarRowComponent() {
  const c = figma.createComponent();
  c.name = "List / Calendar Row";
  c.resize(360, 56);
  layout(c, "HORIZONTAL", 16, 0);
  c.counterAxisAlignItems = "CENTER";
  c.fills = solid(colors.surface);
  c.appendChild(circle("calendar color", 16, colors.personal));
  c.appendChild(textNode("[기본] 네이버에서 살아남기", "Body/Large"));
  return c;
}

function diaryCardComponent() {
  const c = figma.createComponent();
  c.name = "Diary / Card";
  c.resize(360, 188);
  layout(c, "VERTICAL", 14, 20);
  c.fills = solid(colors.surface);
  c.cornerRadius = 8;
  c.strokes = solid(colors.border);
  c.strokeWeight = 1;
  c.appendChild(textNode("3. 일", "Title/Medium", colors.sunday));
  c.appendChild(textNode("마인드 러닝 1일차", "Body/Large"));
  c.appendChild(textNode("무라카미 하루키가 매일 러닝을 하듯이", "Body/Medium"));
  c.appendChild(textNode("매일 일기를 쓰고, 스마트폰 사용량을 추적...", "Body/Medium"));
  return c;
}

function habitTemplateRowComponent() {
  const c = figma.createComponent();
  c.name = "Habit / Template Row";
  c.resize(360, 78);
  layout(c, "HORIZONTAL", 16, 18);
  c.counterAxisAlignItems = "CENTER";
  c.primaryAxisAlignItems = "SPACE_BETWEEN";
  c.fills = solid(colors.subtle);
  c.cornerRadius = 8;
  c.appendChild(textNode("+", "Title/Large", colors.brandPrimary));
  c.appendChild(textNode("습관 직접 만들기", "Body/Large"));
  c.appendChild(textNode("›", "Title/Medium", colors.secondaryText));
  return c;
}

function componentsBoard() {
  const frame = fixedFrame("Components", 760, 1);
  layout(frame, "VERTICAL", 24, 0);
  frame.fills = [];
  frame.appendChild(sectionTitle("Components", "Reusable component candidates generated as Figma components."));

  const grid = fixedFrame("Component grid", 760, 1);
  layout(grid, "HORIZONTAL", 24, 0);
  grid.layoutWrap = "WRAP";
  grid.fills = [];

  [
    appBarComponent("App Bar / Month", "2026. 5.", "☰", "⌕"),
    appBarComponent("App Bar / Modal", "일정", "×", "✓"),
    fabComponent(),
    todayPillComponent(),
    dateCellComponent(),
    eventPillComponent(),
    agendaRowComponent(),
    checkboxComponent(),
    switchComponent(),
    settingsRowComponent(),
    calendarRowComponent(),
    diaryCardComponent(),
    habitTemplateRowComponent()
  ].forEach((component) => {
    const wrapper = fixedFrame(component.name, 372, Math.max(component.height + 56, 132), colors.surface);
    layout(wrapper, "VERTICAL", 12, 12);
    wrapper.cornerRadius = 8;
    wrapper.strokes = solid(colors.border);
    wrapper.strokeWeight = 1;
    wrapper.appendChild(textNode(component.name, "Caption", colors.secondaryText));
    wrapper.appendChild(component);
    grid.appendChild(wrapper);
  });

  frame.appendChild(grid);
  return frame;
}

function monthPattern() {
  const screen = fixedFrame("Pattern / Month View", 390, 844, colors.surface);
  layout(screen, "VERTICAL", 0, 0);
  screen.cornerRadius = 24;
  screen.strokes = solid(colors.border);
  screen.strokeWeight = 1;

  const top = appBarComponent("instance / month app bar", "2026. 5.", "☰", "⌕");
  top.resize(390, 72);
  screen.appendChild(top);

  const weekdays = fixedFrame("weekdays", 390, 40);
  layout(weekdays, "HORIZONTAL", 0, 0);
  weekdays.fills = [];
  ["일", "월", "화", "수", "목", "금", "토"].forEach((day, index) => {
    const item = fixedFrame(day, 55, 40);
    item.fills = [];
    const color = index === 0 ? colors.sunday : index === 6 ? colors.saturday : colors.text;
    item.appendChild(textNode(day, "Label/Large", color));
    item.children[0].x = 20;
    item.children[0].y = 8;
    weekdays.appendChild(item);
  });
  screen.appendChild(weekdays);

  const grid = fixedFrame("calendar grid", 390, 420);
  layout(grid, "VERTICAL", 0, 0);
  grid.fills = [];
  const weeks = [
    ["26", "27", "28", "29", "30", "1", "2"],
    ["3", "4", "5", "6", "7", "8", "9"],
    ["10", "11", "12", "13", "14", "15", "16"],
    ["17", "18", "19", "20", "21", "22", "23"],
    ["24", "25", "26", "27", "28", "29", "30"],
    ["31", "1", "2", "3", "4", "5", "6"]
  ];
  weeks.forEach((week, rowIndex) => {
    const row = fixedFrame(`week ${rowIndex + 1}`, 390, 70);
    layout(row, "HORIZONTAL", 0, 0);
    row.fills = [];
    if (rowIndex > 0) row.appendChild(line(390));
    week.forEach((date, index) => {
      const cell = fixedFrame(date, 55, 70);
      cell.fills = [];
      const color = index === 0 ? colors.sunday : index === 6 ? colors.saturday : colors.text;
      const dateText = textNode(date, "Body/Large", rowIndex === 0 && index < 5 ? colors.disabledText : color);
      dateText.x = 20;
      dateText.y = 12;
      cell.appendChild(dateText);
      if (["5", "8", "16", "18", "21", "29", "30"].includes(date)) {
        const dot = circle("dot", 6, date === "16" || date === "18" ? colors.work : colors.personal);
        dot.x = 24;
        dot.y = 44;
        cell.appendChild(dot);
      }
      row.appendChild(cell);
    });
    grid.appendChild(row);
  });
  screen.appendChild(grid);

  const panel = fixedFrame("agenda panel", 390, 220, colors.surface);
  layout(panel, "VERTICAL", 16, 20);
  panel.strokes = solid(colors.border);
  panel.strokeWeight = 1;
  panel.appendChild(textNode("5.31. 일  음력 4.15.", "Title/Medium", colors.sunday));
  panel.appendChild(textNode("일정이 없습니다.", "Body/Large", colors.secondaryText));
  screen.appendChild(panel);
  return screen;
}

function diaryPattern() {
  const screen = fixedFrame("Pattern / Diary", 390, 844, colors.surface);
  layout(screen, "VERTICAL", 18, 20);
  screen.cornerRadius = 24;
  screen.strokes = solid(colors.border);
  screen.strokeWeight = 1;
  const appBar = appBarComponent("instance / diary app bar", "다이어리", "‹", "▦");
  appBar.resize(350, 64);
  screen.appendChild(appBar);
  const chip = fixedFrame("month chip", 92, 36, colors.selected);
  chip.cornerRadius = 18;
  const chipText = textNode("2023. 09.", "Caption", colors.secondaryText);
  chipText.x = 15;
  chipText.y = 8;
  chip.appendChild(chipText);
  screen.appendChild(chip);
  const card1 = diaryCardComponent();
  const card2 = diaryCardComponent();
  card2.children[0].characters = "24. 월";
  card2.children[0].fills = solid(colors.text);
  card2.children[1].characters = "pt 3회차";
  screen.appendChild(card1);
  screen.appendChild(card2);
  return screen;
}

function settingsPattern() {
  const screen = fixedFrame("Pattern / Settings", 390, 844, colors.surface);
  layout(screen, "VERTICAL", 18, 20);
  screen.cornerRadius = 24;
  screen.strokes = solid(colors.border);
  screen.strokeWeight = 1;
  const appBar = appBarComponent("instance / settings app bar", "설정", "×", "");
  appBar.resize(350, 64);
  screen.appendChild(appBar);
  screen.appendChild(textNode("캘린더", "Title/Medium"));
  ["완료 된 일정 및 할 일 보기", "완료 된 습관 보기", "AI 추천받기", "오늘의 브리핑 보기", "듀얼뷰 보기", "달력 좌우로 보기", "일 뷰에서 사진 보기"].forEach((label) => {
    const row = settingsRowComponent();
    row.children[0].characters = label;
    screen.appendChild(row);
  });
  return screen;
}

function patternsBoard() {
  const frame = fixedFrame("Screen Patterns", 1280, 1);
  layout(frame, "VERTICAL", 24, 0);
  frame.fills = [];
  frame.appendChild(sectionTitle("Screen Patterns", "Reference patterns to validate component composition before Compose implementation."));
  const row = fixedFrame("pattern row", 1280, 900);
  layout(row, "HORIZONTAL", 32, 0);
  row.fills = [];
  row.appendChild(monthPattern());
  row.appendChild(settingsPattern());
  row.appendChild(diaryPattern());
  frame.appendChild(row);
  return frame;
}

async function main() {
  await chooseFont();
  createPaintStyles();
  createTextStyles();

  let page = figma.root.children.find((child) => child.type === "PAGE" && child.name === pageName);
  if (!page) {
    page = figma.createPage();
    page.name = pageName;
  }
  figma.currentPage = page;
  for (const child of [...page.children]) child.remove();

  const root = fixedFrame("CalMong Design System", 2400, 2200, "#FAFAFB");
  layout(root, "HORIZONTAL", 48, 48);
  root.layoutWrap = "WRAP";
  page.appendChild(root);

  const intro = fixedFrame("Intro", 720, 220, colors.surface);
  layout(intro, "VERTICAL", 14, 28);
  intro.cornerRadius = 12;
  intro.strokes = solid(colors.border);
  intro.strokeWeight = 1;
  intro.appendChild(textNode("CalMong Design System", "Title/Large"));
  intro.appendChild(textNode("캘린더 앱 화면에서 반복되는 구조를 토큰, 컴포넌트, 화면 패턴으로 추출한 초안입니다.", "Body/Medium", colors.secondaryText));
  intro.appendChild(textNode("Generated from local screenshots: month, agenda, drawer, settings, schedule input, diary, habit flows.", "Caption", colors.secondaryText));
  root.appendChild(intro);
  root.appendChild(colorBoard());
  root.appendChild(typographyBoard());
  root.appendChild(spacingBoard());
  root.appendChild(componentsBoard());
  root.appendChild(patternsBoard());

  figma.viewport.scrollAndZoomIntoView([root]);
  figma.closePlugin("CalMong Design System page generated.");
}

main().catch((error) => {
  figma.closePlugin(`Failed: ${error.message}`);
});
