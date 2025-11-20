# 프로젝트 마일스톤 (Phase 1: Frontend Prototype)

## M1: 프로젝트 기본 설정 및 코어 UI
- [x] `build.gradle.kts`에 Material, Navigation 등 의존성 설정
- [x] `colors.xml`, `themes.xml`에 앱 기본 테마 및 색상 정의
- [x] `MainActivity`에 `BottomNavigationView` 설정
- [x] `nav_graph.xml`을 이용한 3개 탭(메인, 채팅, 프로필) 프래그먼트 연결

## M2: 데이터 모델 및 스텁 유틸리티 구현
- [x] `model` 패키지에 `User`, `ChatRoom`, `Message` 등 데이터 클래스 생성
- [x] `util` 패키지에 `StubDataUtil` 클래스 생성 및 모든 더미 데이터/함수 구현

## M3: 메인 기능 구현 (매칭)
- [x] `FortuneDialogFragment` 구현 (오늘의 운세 팝업)
- [x] 병풍 열기 애니메이션 구현
- [x] `ViewPager2`와 `MatchCardAdapter`를 이용한 상대 추천 카드 UI 구현
- [x] 메인 화면과 `StubDataUtil` 연동

## M4: 서브 기능 구현 (채팅 및 프로필)
- [x] `ChatFragment`의 채팅 목록 UI 구현
- [x] `ChatActivity`의 채팅방 UI 구현
- [x] `ProfileFragment`의 프로필 수정 UI 구현
- [x] 각 기능과 `StubDataUtil` 연동

## M5: 최종 검토 및 안정화
- [x] 전반적인 UI 폴리싱 및 버그 수정
- [x] 코드 정리 및 주석 추가
- [x] 최종 프로토타입 동작 검토

## Phase 2: 백엔드 API 서버 구축
- [ ] **M1: 프로젝트 설정 및 DB 모델링:** Node.js (Express) 프로젝트 생성, MySQL 데이터베이스 스키마(사용자, 채팅방, 메시지 등) 설계 및 모델링.
- [ ] **M2: 사용자 인증 및 프로필 API 개발:** JWT 기반 회원가입, 로그인, 로그아웃 기능 구현. 사용자 프로필 조회 및 수정 API 엔드포인트 개발.
- [ ] **M3: 사주 분석 및 운세 API 개발:** 사주 정보 분석 로직 구현 및 오늘의 운세 제공 API 개발.
- [ ] **M4: 핵심 매칭 및 궁합 API 개발:** 사용자 추천 알고리즘 구현 및 매일 추천 상대 목록 제공 API 개발. 두 사용자 간의 궁합 점수 계산 API 개발.
- [ ] **M5: 실시간 채팅 기능 구현:** WebSocket(Socket.IO)을 이용한 채팅 서버 구축. 채팅방 생성, 메시지 전송 및 수신 기능 구현.
- [ ] **M6: API 문서화 및 테스트:** Swagger/OpenAPI를 이용한 API 문서 자동화 및 Postman/Jest 등을 이용한 API 기능 테스트.
- [ ] **M7: 신고 시스템 백엔드 구현:** 신고 내역 저장을 위한 `reports` 테이블을 데이터베이스에 설계 및 추가. `POST /api/reports` API 엔드포인트 개발.
- [ ] **M8: 신고 시스템 프론트엔드 연동:** `ChatActivity`에서 신고 아이콘 클릭 시 확인 다이얼로그를 표시하고, `POST /api/reports` API를 호출하여 신고 기능 연동.
