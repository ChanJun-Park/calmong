# Codex 프로젝트 지침

이 저장소에는 Claude Code용 설정도 함께 들어 있다. Claude 전용 agents,
skills, commands, settings의 원본은 기존 `.claude/` 아래 파일로 유지하되,
Codex는 아래 프로젝트 규칙을 읽고 따라야 한다.

## 기본 프로젝트 맥락

- 프로젝트 개요, 아키텍처, 스택, 모듈 규칙, 테스트 규칙, 코드 스타일,
  커밋 포맷은 `CLAUDE.md`를 읽고 따른다.
- `CLAUDE.md`는 Claude 전용 문서가 아니라 이 저장소의 공통 작업 지침으로
  취급한다.
- 코드를 수정할 때는 `CLAUDE.md`에 적힌 기존 패턴을 우선한다:
  Clean Architecture, Kotlin/Android, Jetpack Compose, Material3, Hilt,
  Gradle Kotlin DSL, Version Catalog, `build-logic` convention plugin.

## 로컬 Claude 자산

`.claude/` 디렉터리는 Claude Code용으로 그대로 유지한다. 사용자가 명시적으로
요청하지 않는 한 Codex는 이 파일들을 옮기거나 다시 작성하지 않는다.

- `.claude/agents/`: 프로젝트 로컬 Claude agents.
- `.claude/commands/`: 프로젝트 로컬 Claude commands.
- `.claude/skills/`: 프로젝트 로컬 Claude skills.
- `.claude/settings.json`: 팀 공유 Claude Code settings.
- `.claude/settings.local.json`: 존재하는 경우 개인 Claude Code settings.

## 학습 노트 Skill

사용자가 `/learn`, "학습 자료 만들어줘", "방금 작업 설명해줘",
"이거 학습 노트로 정리해줘" 또는 이와 비슷한 요청을 하면 아래 파일의 지침을
따른다.

- `.claude/skills/learn/SKILL.md`

이 파일을 학습 노트 작성 workflow의 원본으로 사용한다. 요약하면 다음과 같다.

- 한글 학습 노트를 `.learning/<YYYY-MM-DD>/<topic-slug>.md` 아래에 만든다.
- 날짜는 세션 환경/context의 날짜를 사용한다. 임의로 추측하지 않는다.
- 실제로 수행한 작업의 why, what, how, 명령어, 파일, 결정 사항, 실패와 우회
  과정을 기록한다.
- `.learning/INDEX.md`를 갱신한다.
- 최종 응답에서는 생성한 경로와 다룬 주제만 짧게 보고하고, 문서 본문 전체를
  다시 출력하지 않는다.

## 언어

- 이 프로젝트의 사용자-facing 설명은 사용자가 따로 요청하지 않는 한 한국어로
  작성한다.
- 기술 식별자, 명령어, 파일 경로, 클래스명, dependency 이름은 원문 영어를
  유지한다.
