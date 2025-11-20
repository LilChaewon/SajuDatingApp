# 프로젝트 요약 (Project Summary)

## 1. 개요 (Overview)
본 문서는 '사주 소개팅 앱' 프로젝트의 기술 스택과 주요 기능별 구현 내용을 요약하여 설명합니다.

---

## 2. 구현 스펙 (Technical Specifications)

### 2.1. 프론트엔드 (`app/`)
*   **기술:** Java, XML (Android Native)
*   **역할:** 사용자 인터페이스(UI), 사용자 경험(UX), 그리고 서버와의 데이터 통신을 담당합니다.

### 2.2. 백엔드 (`server/`)
*   **기술:** Node.js (Express 프레임워크)
*   **데이터베이스:** MySQL
*   **역할:** 비즈니스 로직, 데이터베이스 관리, 실시간 통신 등 앱의 핵심 기능을 담당합니다.

---

## 3. 폴더별 주요 기능 (Features by Folder)

### 3.1. `app/src/main/java/com/example/sajudatingapp/activity/`

*   `MainActivity.java`:
    *   **기능:** 앱의 메인 컨테이너 역할. 하단 탭 네비게이션 설정, 앱 시작 시 운세 팝업 및 병풍 애니메이션 제어, 상단 앱 바 설정을 담당합니다.
*   `ChatActivity.java`:
    *   **기능:** 1:1 실시간 채팅방 화면. 메시지 목록 표시, 메시지 전송, 그리고 상단 앱 바를 통해 사용자 신고 및 뒤로 가기 기능을 제공합니다.

### 3.2. `app/src/main/java/com/example/sajudatingapp/fragment/`

*   `MainFragment.java`:
    *   **기능:** 오늘의 추천 상대 목록 표시. 상하 스와이프를 통한 카드 넘기기 및 병풍 애니메이션을 담당합니다.
*   `ChatFragment.java`:
    *   **기능:** 참여 중인 모든 채팅방 목록을 표시합니다.
*   `ProfileFragment.java`:
    *   **기능:** 사용자 프로필(이름, 나이, 직업 등)을 조회하고 수정하는 기능을 제공합니다.
*   `FortuneDialogFragment.java`:
    *   **기능:** 앱 최초 실행 시 또는 메뉴를 통해 '오늘의 운세'를 팝업 형태로 보여줍니다.

### 3.3. `app/src/main/java/com/example/sajudatingapp/adapter/`

*   `MatchCardAdapter.java`:
    *   **기능:** 추천 상대(`User`) 데이터를 메인 화면의 카드 UI에 연결합니다.
*   `ChatListAdapter.java`:
    *   **기능:** 채팅방(`ChatRoom`) 데이터를 채팅 목록 UI에 연결합니다.
*   `MessageAdapter.java`:
    *   **기능:** 메시지(`Message`) 데이터를 채팅방의 대화 풍선 UI에 연결합니다.

### 3.4. `app/src/main/java/com/example/sajudatingapp/model/`
*   `User.java`, `ChatRoom.java`, `Message.java`: 앱에서 사용되는 핵심 데이터 모델(사용자, 채팅방, 메시지)을 정의합니다.

### 3.5. `app/src/main/java/com/example/sajudatingapp/util/`

*   `StubDataUtil.java`:
    *   **기능:** 서버가 구현되기 전, 프로토타입 단계에서 사용할 임시 데이터(사용자, 채팅방, 메시지 등)를 제공합니다.

### 3.6. `server/src/`

*   `index.js`:
    *   **기능:** 백엔드 서버의 시작점. Express 앱 설정, API 라우팅, WebSocket 및 MySQL 데이터베이스 연결을 관리합니다. 서버의 모든 요청이 이곳을 통해 분기됩니다.