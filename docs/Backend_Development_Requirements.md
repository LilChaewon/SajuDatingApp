# 백엔드 개발 상세 요건 (2차 개발 가이드)

## 1. 개요
본 문서는 '사주 소개팅 앱'의 2차 개발 단계에서 필요한 백엔드 시스템의 아키텍처, 데이터베이스 스키마, API 엔드포인트 등을 상세히 기술합니다. Node.js 또는 Python 기반의 RESTful API 서버를 구축하는 것을 전제로 합니다.

## 2. 시스템 아키텍처
- **서버 프레임워크:** Node.js (Express) 또는 Python (FastAPI / Django)
- **데이터베이스:** PostgreSQL 또는 MySQL
- **인증:** JWT (JSON Web Token) 기반 인증
- **실시간 통신:** WebSocket (Socket.IO 등)을 통한 실시간 채팅 구현

## 3. 데이터베이스 스키마
테이블명은 복수형(plural)을 사용합니다.
- **`users`**
  - `id`: INT, Primary Key, Auto Increment
  - `email`: VARCHAR(255), Unique, Not Null
  - `password_hash`: VARCHAR(255), Not Null
  - `name`: VARCHAR(100), Not Null
  - `birth_date`: DATE, Not Null
  - `gender`: ENUM('male', 'female', 'other'), Not Null
  - `location`: VARCHAR(255)
  - `job`: VARCHAR(255)
  - `bio`: TEXT
  - `profile_img_url`: VARCHAR(2048)
  - `created_at`: DATETIME, Default CURRENT_TIMESTAMP

- **`matches`**
  - `id`: INT, PK, Auto Increment
  - `requester_id`: INT, Foreign Key to `users.id`
  - `receiver_id`: INT, Foreign Key to `users.id`
  - `status`: ENUM('requested', 'matched', 'declined'), Not Null, Default 'requested'
  - `compatibility_score`: INT
  - `created_at`: DATETIME, Default CURRENT_TIMESTAMP
  - UNIQUE (`requester_id`, `receiver_id`)

- **`chats`**
  - `id`: INT, PK, Auto Increment
  - `match_id`: INT, Foreign Key to `matches.id`, Unique
  - `created_at`: DATETIME, Default CURRENT_TIMESTAMP

- **`messages`**
  - `id`: INT, PK, Auto Increment
  - `chat_id`: INT, Foreign Key to `chats.id`
  - `sender_id`: INT, Foreign Key to `users.id`
  - `content`: TEXT, Not Null
  - `sent_at`: DATETIME, Default CURRENT_TIMESTAMP

## 4. API 엔드포인트 상세

### 4.1. 인증 (Authentication)
- **JWT 발급:** `POST /api/users/login` 성공 시, Access Token과 Refresh Token을 발급합니다.
- **토큰 전달:** 클라이언트는 이후 모든 요청의 `Authorization` 헤더에 `Bearer <Access-Token>` 형태로 토큰을 포함하여 전송해야 합니다.
- **토큰 갱신:** Access Token 만료 시, Refresh Token을 사용하여 `POST /api/auth/refresh`를 통해 새로운 Access Token을 발급받습니다.

### 4.2. API 명세

#### **Users**
- **`POST /api/users/register`** (회원가입)
  - **Request Body:** `{ "email", "password", "name", "birth_date", "gender" }`
  - **Success (201 Created):** `{ "user_id", "name", "email" }`
- **`GET /api/users/me`** (내 프로필 조회)
  - **Headers:** `Authorization: Bearer <token>`
  - **Success (200 OK):** `{ "user_id", "name", "email", "birth_date", ... }`
- **`PUT /api/users/me`** (내 프로필 수정)
  - **Headers:** `Authorization: Bearer <token>`
  - **Request Body:** `{ "location", "job", "bio", "profile_img_url" }`
  - **Success (200 OK):** `{ "message": "Profile updated successfully" }`

#### **Matches**
- **`GET /api/matches/recommendations`** (오늘의 추천 상대 조회)
  - **Headers:** `Authorization: Bearer <token>`
  - **Success (200 OK):** `[ { "user_id", "name", "age", "location", "bio", "profile_img_url", "compatibility_score" }, ... ]`
- **`POST /api/matches/request`** (대화 요청)
  - **Headers:** `Authorization: Bearer <token>`
  - **Request Body:** `{ "receiver_id": <user_id> }`
  - **Success (201 Created):** `{ "match_id", "status": "requested" }`
  - **Error (409 Conflict):** `{ "error": "Match request already exists" }`
- **`POST /api/matches/{match_id}/accept`** (대화 요청 수락)
  - **Headers:** `Authorization: Bearer <token>`
  - **Success (200 OK):** `{ "match_id", "status": "matched", "chat_id" }` (채팅방 자동 생성)

#### **Chats & Messages**
- **`GET /api/chats`** (채팅방 목록 조회)
  - **Headers:** `Authorization: Bearer <token>`
  - **Success (200 OK):** `[ { "chat_id", "partner": { "user_id", "name", "profile_img_url" }, "last_message", "unread_count" }, ... ]`
- **`GET /api/chats/{chat_id}/messages`** (메시지 목록 조회)
  - **Headers:** `Authorization: Bearer <token>`
  - **Query Params:** `?limit=30&offset=0` (페이지네이션)
  - **Success (200 OK):** `[ { "message_id", "sender_id", "content", "sent_at" }, ... ]`

---
*참고: 메시지 전송(`POST /api/chats/{chat_id}/messages`)은 실시간 성격을 고려하여 REST API가 아닌 WebSocket 이벤트(`'sendMessage'`)로 처리하는 것이 더 효율적입니다.*
