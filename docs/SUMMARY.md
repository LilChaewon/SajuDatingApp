# 프로젝트 요약 (Project Summary)

## 1. 구현 스펙 (Technical Specifications)

*   **프론트엔드 (`app/`):**
    *   **기술:** Java, XML (Android Native)
    *   **네트워킹:** Retrofit2, Socket.IO 클라이언트
*   **백엔드 (`server/`):**
    *   **기술:** Node.js (Express 프레임워크)
    *   **실시간 통신:** Socket.IO
    *   **데이터베이스:** MySQL
    *   **추가 환경:** Python 런타임 (TensorFlow, numpy)

## 2. 폴더별 주요 기능 (Features by Folder)

### 2.1. `app/` (안드로이드 클라이언트)
안드로이드 앱의 모든 소스 코드와 리소스를 포함합니다.
*   **`activity/`, `fragment/`**: 앱의 각 화면(메인, 채팅, 프로필 등) UI와 사용자 상호작용을 관리합니다.
*   **`adapter/`**: 추천 카드, 채팅 목록 등 리스트 형태의 UI에 데이터를 표시하는 역할을 담당합니다.
*   **`network/`**: Retrofit2와 Socket.IO를 사용하여 백엔드 서버와 통신합니다.
*   **`model/`**: API 통신에 사용되는 데이터 모델(User, Message 등)을 정의합니다.
*   **`util/`**: `SessionManager` 등 앱 전반에 사용되는 유틸리티 클래스를 포함합니다.

### 2.2. `server/` (백엔드 서버)
Node.js로 구현된 API 서버입니다.
*   **`src/routes/`**: `/api/auth`, `/api/users` 등 기능별 API 엔드포인트를 정의하고, HTTP 요청을 컨트롤러로 라우팅합니다.
*   **`src/controllers/`**: API 요청에 대한 회원가입, 매칭, 채팅 등의 실제 비즈니스 로직을 처리합니다.
*   **`src/utils/`**: `fortune.js`(운세 계산), `compatibility.js`(궁합 점수 계산) 등 특정 비즈니스 로직을 모듈화하여 관리합니다.
*   **`src/python/`**: 궁합 점수 계산에 사용되는 Python 스크립트와 머신러닝 모델(`.h5`)을 포함합니다.
*   **`src/socket.js`**: 실시간 채팅을 위한 Socket.IO 서버의 연결 및 이벤트 핸들링 로직을 담당합니다.
*   **`src/db.js`**: MySQL 데이터베이스 커넥션 풀을 생성하고 관리합니다.

### 2.3. `database/`
*   `schema.sql`: 프로젝트에 필요한 모든 데이터베이스 테이블(`users`, `matches`, `chats`, `messages`)의 구조(DDL)를 정의합니다.

### 2.4. `docs/`
*   `PRD.md`, `MILESTONES.md` 등 프로젝트의 기획, 요구사항, 개발 단계에 대한 문서를 포함합니다.

### 2.5. `legacy/`
*   이식 대상이었던 궁합 점수, 운세 계산 로직의 원본 Python 코드와 Jupyter Notebook 파일이 보관되어 있습니다. 개발의 참조 자료로 사용되었습니다.

---

## 3. 현재 개발 상태 (Current Development State)

*   **백엔드 API 서버 구축 완료:**
    - 기획 문서(`Backend_Development_Requirements.md`)에 명시된 모든 백엔드 기능이 구현되었습니다.
    - 데이터베이스 설정, 사용자 인증, 프로필 관리, 궁합 점수 계산, 매칭, 실시간 채팅, 오늘의 운세 API가 모두 준비되었습니다.

*   **향후 과제 (Next Steps):**
    - **안드로이드 앱과 백엔드 API 연동이 가장 중요하고 시급한 다음 단계입니다.**
    - 현재 안드로이드 앱은 실제 네트워크 통신 없이 임시 데이터(`StubDataUtil`)를 사용하고 있습니다. 아래와 같이 모든 기능을 실제 API와 연동하는 작업이 필요합니다.
        - **메인 화면:** 추천 상대를 `/api/matches/recommendations` API로 불러오기
        - **채팅:** 채팅방 목록과 메시지를 `/api/chats` API로 불러오고, 실시간 메시지 송수신을 Socket.IO와 연동
        - **프로필:** 사용자 정보를 `/api/users/me` API로 불러오고 저장하기
        - **오늘의 운세:** 운세 정보를 `/api/fortune/today` API로 불러오기
    - API 연동 후, 전반적인 UI/UX 개선 및 버그 수정, 그리고 실제 서비스 배포를 위한 준비가 필요합니다.
