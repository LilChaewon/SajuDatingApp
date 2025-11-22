실제 채팅방 활성화 가이드

현재 앱은 더미 데이터만 사용하도록 설정되어 있습니다.

1. 백엔드 서버 준비사항

API 서버 URL 설정
app/src/main/java/com/example/sajudatingapp/api/ApiClient.java 파일에서 서버 URL을 설정하세요:

private static final String BASE_URL = "https://your-backend-server.com/api/";

필요한 API 엔드포인트
- POST /chat/request - 대화 요청
- GET /chat/rooms/{userId} - 채팅방 목록 조회
- GET /chat/rooms/{chatRoomId}/messages - 메시지 목록 조회
- POST /chat/rooms/{chatRoomId}/messages - 메시지 전송
- PUT /chat/rooms/{chatRoomId}/accept - 대화 요청 수락
- PUT /chat/rooms/{chatRoomId}/reject - 대화 요청 거절
- DELETE /chat/rooms/{chatRoomId} - 채팅방 삭제

WebSocket 서버 URL
실시간 메시지 수신을 위해 WebSocket 서버가 필요합니다:
- ws://your-backend-server.com/chat 또는 wss://your-backend-server.com/chat

2. 코드 활성화 단계

2.1 ChatFragment 활성화

app/src/main/java/com/example/sajudatingapp/fragment/ChatFragment.java 파일에서:

1. onViewCreated 메서드 (59-86줄):
   - loadDummyDataFirst(); 주석 처리
   - // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화 아래의 주석 블록(/* ... */) 제거

2. onResume 메서드 (98-113줄):
   - loadDummyDataFirst(); 주석 처리
   - 주석 블록 제거

3. loadChatRooms 메서드 (115-159줄):
   - loadDummyDataFirst(); 주석 처리
   - 주석 블록 제거
   - currentUserId를 실제 사용자 ID로 변경 (SharedPreferences 등에서 가져오기)

2.2 ChatActivity 활성화

app/src/main/java/com/example/sajudatingapp/activity/ChatActivity.java 파일에서:

1. onCreate 메서드 (60-79줄):
   - 주석 블록 제거

2. initializeViews 메서드 (107-128줄):
   - 더미 데이터 로드 부분 주석 처리
   - 실제 채팅방 로드 코드 활성화

3. sendButton 클릭 리스너 (130-154줄):
   - 더미 모드 코드 주석 처리
   - 실제 메시지 전송 코드 활성화

4. onResume 메서드 (314-325줄):
   - 주석 블록 제거

2.3 사용자 ID 설정

실제 사용자 ID를 가져오는 방법:

// SharedPreferences에서 사용자 ID 가져오기
SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
Long currentUserId = prefs.getLong("user_id", -1L);

// 또는 로그인 세션에서 가져오기
// Long currentUserId = SessionManager.getInstance().getCurrentUserId();

ChatFragment와 ChatActivity에서 currentUserId를 설정하는 부분을 찾아 실제 사용자 ID로 변경하세요.

3. WebSocket 연결 설정

ChatManager를 초기화한 후 WebSocket을 연결하세요:

chatManager.connectWebSocket("wss://your-backend-server.com/chat");

이 코드는 MainActivity의 onCreate나 사용자 로그인 후에 호출하세요.

4. 테스트 체크리스트

- API 서버 URL이 올바르게 설정되었는지 확인
- 모든 주석 블록(/* ... */)이 제거되었는지 확인
- 더미 데이터 로드 코드가 주석 처리되었는지 확인
- currentUserId가 실제 사용자 ID로 설정되었는지 확인
- WebSocket 서버 URL이 올바르게 설정되었는지 확인
- 인터넷 권한이 AndroidManifest.xml에 추가되었는지 확인
- 백엔드 서버가 실행 중인지 확인

5. 에러 처리

서버 연결 실패 시 더미 데이터로 폴백하도록 이미 구현되어 있습니다. 
ChatManager의 콜백에서 onError가 호출되면 자동으로 더미 데이터를 표시합니다.

6. 주의사항

- 실제 서버로 전환하기 전에 백엔드 API가 모두 구현되어 있는지 확인하세요
- WebSocket 연결이 실패해도 앱이 크래시하지 않도록 예외 처리가 되어 있습니다
- 로컬 Room 데이터베이스는 서버 데이터와 동기화됩니다
