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
