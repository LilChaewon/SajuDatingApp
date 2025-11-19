# 프론트엔드 개발 상세 요건

## 1. 개요
본 문서는 '사주 소개팅 앱' 1차 개발의 프론트엔드 상세 요건을 기술합니다. 모든 기능은 시각적 프로토타입 완성을 목표로 하며, 모든 데이터는 `StubDataUtil` 클래스를 통해 제공됩니다.

## 2. 개발 환경 및 의존성
- **언어:** Java
- **빌드 시스템:** Gradle
- **핵심 의존성 (app/build.gradle.kts에 추가 필요):**
  ```kotlin
  // Material Design
  implementation("com.google.android.material:material:1.12.0")

  // Navigation Component
  implementation("androidx.navigation:navigation-fragment:2.7.7")
  implementation("androidx.navigation:navigation-ui:2.7.7")

  // ViewPager2 for card stack
  implementation("androidx.viewpager2:viewpager2:1.1.0")
  ```

## 3. 프로젝트 구조
`java/com/example/sajudatingapp/` 내부는 아래와 같이 구성합니다.
- **activity:** `MainActivity`
- **fragment:** `MainFragment`, `ChatFragment`, `ProfileFragment`, `FortuneDialogFragment`
- **adapter:** `MatchCardAdapter`, `ChatListAdapter`, `MessageAdapter`
- **model:** `User`, `ChatRoom`, `Message`
- **util:** `StubDataUtil`

## 4. 공통 UI 요소
- **`res/values/colors.xml`**:
  ```xml
  <color name="beige">#F5F5DC</color>
  <color name="light_brown">#D2B48C</color>
  <color name="dark_brown">#8B4513</color>
  <color name="white">#FFFFFFFF</color>
  <color name="black">#FF000000</color>
  ```
- **`res/menu/bottom_nav_menu.xml`**:
  ```xml
  <menu xmlns:android="http://schemas.android.com/apk/res/android">
      <item android:id="@+id/navigation_main" android:icon="@drawable/ic_home" android:title="메인"/>
      <item android:id="@+id/navigation_chat" android:icon="@drawable/ic_chat" android:title="채팅"/>
      <item android:id="@+id/navigation_profile" android:icon="@drawable/ic_profile" android:title="프로필"/>
  </menu>
  ```
- **Navigation Graph (`res/navigation/nav_graph.xml`)**:
  - `MainFragment`, `ChatFragment`, `ProfileFragment`를 وجهة (destination)으로 하는 네비게이션 그래프를 설정합니다.
  - `MainActivity`의 `BottomNavigationView`와 연동합니다.

## 5. 기능별 상세 구현

### 5.1. 유틸리티: StubDataUtil.java
`com.example.sajudatingapp.util` 패키지 내에 생성합니다.
```java
package com.example.sajudatingapp.util;

import com.example.sajudatingapp.model.User;
import java.util.ArrayList;
import java.util.List;

public class StubDataUtil {

    public static String getTodayFortune() {
        return "오늘은 새로운 인연을 만날 수 있는 좋은 날입니다. 동쪽에서 오는 귀인을 놓치지 마세요.";
    }

    public static List<User> getTodayMatchCandidates() {
        List<User> users = new ArrayList<>();
        users.add(new User("김민준", 28, "서울", "IT 엔지니어", "안녕하세요. 좋은 인연 찾고 싶어요.", 90));
        users.add(new User("이서연", 25, "부산", "디자이너", "함께 성장할 수 있는 사람을 만나고 싶습니다.", 85));
        users.add(new User("박현우", 30, "대구", "의사", "진중한 만남을 가지고 싶습니다.", 95));
        users.add(new User("최지아", 26, "인천", "교사", "긍정적이고 밝은 분이면 좋겠어요.", 80));
        users.add(new User("정태현", 29, "광주", "자영업", "함께 맛있는 음식을 즐길 분을 찾습니다.", 75));
        return users;
    }
    
    // 다른 스텁 함수들도 필요에 따라 추가...
    public static boolean requestChat(User user) { return true; }
    public static boolean sendMessage(String message) { return true; }
    public static boolean saveProfile() { return true; }
}
```
*User 모델 클래스는 위 데이터에 맞게 생성자가 구현되어야 합니다.*

### 5.2. 운세 팝업 및 병풍 애니메이션
- **`FortuneDialogFragment.java`**: `DialogFragment`로 구현, `StubDataUtil.getTodayFortune()` 호출.
- **애니메이션**: `MainActivity`에서 `FortuneDialogFragment`가 dismiss될 때, `ObjectAnimator`나 `ViewPropertyAnimator`를 사용하여 병풍 `ImageView`들의 `translationX`와 `alpha`를 조절하여 사라지게 합니다.

### 5.3. 메인 화면: 상대 추천 카드
- **`item_match_card.xml` 레이아웃 예시**:
  ```xml
  <com.google.android.material.card.MaterialCardView ...>
      <LinearLayout android:orientation="vertical" ...>
          <ImageView android:id="@+id/iv_profile_picture" ... />
          <TextView android:id="@+id/tv_name_age" ... />
          <TextView android:id="@+id/tv_bio" ... />
          <TextView android:id="@+id/tv_compatibility_score" ... />
          <Button android:id="@+id/btn_request_chat" android:text="대화 요청" ... />
      </LinearLayout>
  </com.google.android.material.card.MaterialCardView>
  ```
- **`MatchCardAdapter`**: `onBindViewHolder`에서 `User` 객체를 받아 각 View에 데이터를 설정합니다. '대화 요청' 버튼 클릭 시 `StubDataUtil.requestChat()`을 호출하고 "요청을 보냈습니다" `Toast`를 표시합니다.

### 5.4. 채팅
- **`ChatFragment`**: `RecyclerView`와 `ChatListAdapter`를 사용하여 가상의 채팅방 목록을 표시합니다.
- **`ChatActivity`**: `RecyclerView`와 `MessageAdapter`를 사용하여 메시지 목록을 표시. 전송 버튼 클릭 시 입력된 텍스트로 `Message` 객체를 만들어 `MessageAdapter`에 추가하고 `RecyclerView`를 갱신합니다.

### 5.5. 프로필
- **`ProfileFragment`**: `EditText`와 `Button`으로 구성. 저장 버튼 클릭 시 `StubDataUtil.saveProfile()`을 호출하고 "저장되었습니다" `Toast`를 표시합니다.
