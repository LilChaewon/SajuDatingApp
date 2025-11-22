# 사주 소개팅 앱: 제품 요구사항 명세서 (PRD)

### 1. 개요 (Overview)
본 문서는 안드로이드 플랫폼 기반의 '사주 소개팅 앱'의 최종 제품 요구사항을 정의합니다. 이 앱은 사용자의 사주 정보를 기반으로 매일 개인화된 운세를 제공하고, 사주 궁합을 분석하여 최적의 상대를 추천하는 신개념 데이팅 애플리케이션입니다.

**제품 컨셉:** 전통적인 '사주'와 '궁합'을 현대적인 데이팅 앱에 접목하여, 단순한 외모나 스펙을 넘어선 깊이 있는 관계를 찾는 사용자에게 신뢰도 높은 매칭 경험을 제공합니다.

### 2. 목표 (Goals)
- **정확한 사주 기반 서비스 제공:** 사용자의 생년월일시 정보를 바탕으로 신뢰성 있는 오늘의 운세 및 남녀간의 궁합 점수를 계산하고 제공합니다.
- **고품질 매칭 시스템 구축:** 사주 궁합, 사용자 선호도, 위치 정보 등을 종합적으로 고려한 매칭 알고리즘을 통해 매일 최대 5명의 최적의 상대를 추천합니다.
- **실시간 소통 기능 활성화:** 매칭된 사용자 간에 실시간으로 메시지를 주고받을 수 있는 안정적인 채팅 환경을 제공합니다.
- **직관적이고 매력적인 사용자 경험 제공:** 한국적인 디자인 테마와 '병풍 애니메이션' 등 독창적인 UX를 통해 사용자에게 인상적인 경험을 선사합니다.

### 3. 타겟 사용자 (Target Audience)
- 자신의 사주(운세, 궁합)에 관심이 많고 이를 관계의 중요한 요소로 생각하는 20-30대 남녀
- 기존 데이팅 앱의 가벼운 만남보다 의미 있는 관계를 찾고자 하는 사용자
- 한국적인 문화와 디자인에 매력을 느끼는 사용자

### 4. 사용자 흐름 (User Flow)

1.  **앱 최초 실행:** 사용자가 앱을 처음 실행하면 로그인/회원가입 화면이 나타납니다.
2.  **회원가입:** 사용자는 이메일, 비밀번호, 이름, 생년월일, 성별 등의 정보를 입력하여 회원가입을 완료합니다. 성공 시, 서버는 JWT 인증 토큰을 발급합니다.
3.  **로그인:**
    *   기존 사용자는 이메일과 비밀번호로 로그인합니다. 성공 시, 서버는 JWT 인증 토큰을 발급합니다.
    *   앱은 발급받은 토큰을 `SharedPreferences`에 안전하게 저장합니다.
4.  **자동 로그인:** 앱 재실행 시, 저장된 토큰이 유효하면 자동으로 로그인되어 메인 화면으로 이동합니다. 토큰이 없거나 만료된 경우 로그인 화면으로 이동합니다.
5.  **메인 기능 접근:** 로그인 후, 사용자는 메인 화면에서 오늘의 추천 상대를 확인하고, 채팅 및 프로필 관리 기능을 자유롭게 이용할 수 있습니다.

### 5. 주요 기능 (Core Features)

#### 5.1. 하단 네비게이션 (Bottom Navigation Bar)
- **메인 탭:** **백엔드 API(`GET /api/matches`)를 통해** 오늘의 추천 상대 목록을 받아와 핵심 화면에 표시합니다.
- **채팅방 탭:** **백엔드 API(`GET /api/chat/rooms`)를 통해** 사용자가 참여 중인 실시간 채팅방 목록을 보여줍니다.
- **프로필 탭:** **백엔드 API(`GET /api/users/me`)를 통해** 사용자의 프로필 정보를 확인하고, 수정을 **요청(`PUT /api/users/me`)**합니다.

#### 5.2. 앱 실행 및 오늘의 운세
- **최초 로그인 후:** 사용자의 생년월일에 기반해 서버(**`GET /api/fortune/today`**)에서 계산된 '오늘의 운세' 정보가 팝업으로 표시됩니다.
- **메인 화면:** 사용자는 상단 앱 바의 아이콘을 통해 언제든지 '오늘의 운세'를 다시 확인할 수 있습니다.

#### 5.3. 메인 화면 (상대 추천)
- 백엔드 매칭 시스템이 하루 최대 5명의 추천 상대를 **API를 통해** 클라이언트로 전달합니다.
- **카드 구성 요소:**
    - 프로필 사진, 이름, 나이, 자기소개 등 **서버에서 받은** 실제 사용자 정보
    - **궁합 점수:** **실시간 API 호출(`GET /api/matches/{userId}/compatibility`)**을 통해 두 사용자의 궁합 점수를 계산하여 표시합니다.
    - **"대화 요청" 버튼**

#### 5.4. 채팅 기능 (Chat)
- "대화 요청" 버튼 클릭 시, **서버에 채팅방 생성을 요청(`POST /api/chat/request/{userId}`)**합니다.
- 상대방이 요청을 수락하면, 서버는 두 사용자 간의 채팅방을 활성화하고 양측 클라이언트에 이를 알립니다.
- 채팅은 **Socket.IO와 연동된** 실시간 통신으로 구현되며, 모든 메시지는 서버 데이터베이스에 영구적으로 저장됩니다.

#### 5.5. 프로필 화면 (Profile)
- 사용자는 자신의 프로필 정보(사진, 이름, 나이, 직업, 위치 등)를 수정할 수 있습니다.
- '저장' 버튼 클릭 시, 수정된 정보가 **서버로 전송되어 데이터베이스에 업데이트**됩니다.

### 6. 디자인 및 테마 (Design & Theme)
- **주요 색상:** 베이지색, 연한 갈색을 메인 컬러로 사용하여 차분하고 한국적인 분위기를 연출합니다.
- **전체적인 느낌:** 단아하고 고급스러운 디자인을 지향합니다.
- **핵심 애니메이션:** 병풍이 열리는 애니메이션을 통해 앱의 독창적인 정체성을 강조합니다.

### 6. 기술 요구사항 (Technical Requirements)
- **Frontend (Android):**
  - 개발 언어: Java, XML
  - 아키텍처: MVVM (Model-View-ViewModel) 패턴 권장
  - 라이브러리: Android Jetpack (Navigation, ViewModel, LiveData), Retrofit2 (API 통신), Glide/Picasso (이미지 로딩)
- **Backend (Server):**
  - 개발 언어: Node.js (Express)
  - **추가 요구사항: Python 런타임 (TensorFlow, numpy 등 포함)**
  - 데이터베이스: **MySQL**
  - 인증: JWT (JSON Web Token) 기반 인증
  - 실시간 통신: WebSocket (Socket.IO)
- **핵심 로직:**
  - **사주 분석 엔진 (Saju Analysis Engine):**
    - **오늘의 운세:** `legacy` 폴더의 Python 코드(`daily_woonse.py`)를 분석, Node.js로 로직을 포팅하여 구현. 사용자의 일간(日干)과 오늘의 일진(日辰)을 비교하여 '십성(十星)'을 계산하고, 이를 미리 정의된 CSV 데이터와 매칭하여 운세 텍스트를 제공.
    - **궁합 점수:** `legacy` 폴더의 Python 코드(`hd2.ipynb`)를 기반으로 구현. Node.js 서버는 두 사용자의 사주 정보를 Python 스크립트에 전달하고, 스크립트는 사전 훈련된 Keras/TensorFlow 모델(`.h5`)을 이용해 궁합 점수를 계산한 후 결과를 반환.
  - **매칭 알고리즘:** 위에서 계산된 궁합 점수를 핵심 지표로 사용하여, 사용자 필터, 거리 등을 종합적으로 고려해 추천 목록을 생성.

### 7. 백엔드 API 명세 (Backend API Specification)

#### 7.1. 사용자 인증 (Authentication)
- `POST /api/auth/register`
  - **설명:** 신규 사용자 회원가입
  - **요청 본문:** `email`, `password`, `name`, `birthDate`, `gender` 등
  - **응답:** `accessToken` (JWT)
- `POST /api/auth/login`
  - **설명:** 기존 사용자 로그인
  - **요청 본문:** `email`, `password`
  - **응답:** `accessToken` (JWT)

#### 7.2. 사용자 프로필 (User Profile)
- `GET /api/users/me`
  - **설명:** 자신의 프로필 정보 조회
  - **헤더:** `Authorization: Bearer {accessToken}`
- `PUT /api/users/me`
  - **설명:** 자신의 프로필 정보 수정
  - **헤더:** `Authorization: Bearer {accessToken}`
  - **요청 본문:** 수정할 프로필 정보

#### 7.3. 운세 및 매칭 (Fortune & Matching)
- `GET /api/fortune/today`
  - **설명:** 오늘의 운세 정보 조회
  - **헤더:** `Authorization: Bearer {accessToken}`
- `GET /api/matches`
  - **설명:** 오늘의 추천 상대 목록 조회 (최대 5명)
  - **헤더:** `Authorization: Bearer {accessToken}`
- `GET /api/matches/{userId}/compatibility`
  - **설명:** 특정 사용자와의 궁합 점수 조회
  - **헤더:** `Authorization: Bearer {accessToken}`

#### 7.4. 채팅 (Chat)
- `POST /api/chat/request/{userId}`
  - **설명:** 특정 사용자에게 대화 요청 보내기
  - **헤더:** `Authorization: Bearer {accessToken}`
- `GET /api/chat/rooms`
  - **설명:** 참여 중인 모든 채팅방 목록 조회
  - **헤더:** `Authorization: Bearer {accessToken}`
- **WebSocket (Socket.IO) Events:**
  - `connection`: 소켓 연결
  - `join_room`: 특정 채팅방 참여
  - `send_message`: 메시지 전송
  - `receive_message`: 메시지 수신

#### 7.5. 신고 (Reporting)
- `POST /api/reports`
  - **설명:** 특정 사용자 신고하기
  - **헤더:** `Authorization: Bearer {accessToken}`
  - **요청 본문:** `reportedUserId`, `reason` (선택 사항)
  - **응답:** 성공 또는 실패 메시지
