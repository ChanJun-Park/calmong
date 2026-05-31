# calmong

목표 달성을 도와주는 AI agent pet이 함께하는 캘린더 앱.

## 스택
- Android (Kotlin), Gradle Kotlin DSL
- 자세한 의존성·아키텍처는 확정되는 대로 이 문서에 누적한다

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
