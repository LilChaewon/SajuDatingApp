package com.example.sajudatingapp.util;

import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.model.Message;
import com.example.sajudatingapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class StubDataUtil {

    public static String getTodayFortune() {
        return "오늘은 새로운 인연을 만날 수 있는 좋은 날입니다. 동쪽에서 오는 귀인을 놓치지 마세요.";
    }

    public static List<User> getTodayMatchCandidates() {
        List<User> users = new ArrayList<>();
        User user1 = new User("김민준", 28, "서울", "IT 엔지니어", "안녕하세요. 좋은 인연 찾고 싶어요.", 90);
        user1.setId(2L);
        users.add(user1);
        
        User user2 = new User("이서연", 25, "부산", "디자이너", "함께 성장할 수 있는 사람을 만나고 싶습니다.", 85);
        user2.setId(3L);
        users.add(user2);
        
        User user3 = new User("박현우", 30, "대구", "의사", "진중한 만남을 가지고 싶습니다.", 95);
        user3.setId(4L);
        users.add(user3);
        
        User user4 = new User("최지아", 26, "인천", "교사", "긍정적이고 밝은 분이면 좋겠어요.", 80);
        user4.setId(5L);
        users.add(user4);
        
        User user5 = new User("정태현", 29, "광주", "자영업", "함께 맛있는 음식을 즐길 분을 찾습니다.", 75);
        user5.setId(6L);
        users.add(user5);
        
        return users;
    }

    public static List<ChatRoom> getChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<User> candidates = getTodayMatchCandidates();

        if (candidates.size() > 3) {
            // 수락된 채팅방 (먼저 추가하여 우선 표시)
            ChatRoom room1 = new ChatRoom(candidates.get(1), "오늘 날씨 정말 좋네요!", false);
            room1.setId(1L);
            room1.setUserId(1L);
            if (candidates.get(1).getId() == null) {
                candidates.get(1).setId(3L);
            }
            room1.setPartnerId(candidates.get(1).getId());
            chatRooms.add(room1);
            
            // 또 다른 수락된 채팅방
            ChatRoom room2 = new ChatRoom(candidates.get(3), "네, 안녕하세요. 잘 부탁드려요.", false);
            room2.setId(2L);
            room2.setUserId(1L);
            if (candidates.get(3).getId() == null) {
                candidates.get(3).setId(5L);
            }
            room2.setPartnerId(candidates.get(3).getId());
            chatRooms.add(room2);
            
            // 수락 대기중인 채팅방
            ChatRoom room3 = new ChatRoom(candidates.get(0), "안녕하세요! 대화 요청 드려요.", true);
            room3.setId(3L);
            room3.setUserId(1L);
            if (candidates.get(0).getId() == null) {
                candidates.get(0).setId(2L);
            }
            room3.setPartnerId(candidates.get(0).getId());
            chatRooms.add(room3);
        }
        return chatRooms;
    }

    public static List<Message> getMessagesForChatRoom(ChatRoom chatRoom) {
        List<Message> messages = new ArrayList<>();
        if (!chatRoom.isPending()) {
            messages.add(new Message("안녕하세요! 프로필 보고 연락드렸어요.", false));
            messages.add(new Message("네, 안녕하세요. 반갑습니다.", true));
        }
        return messages;
    }

    public static int calculateCompatibility(User user1, User user2) {
        // Dummy logic
        return (user1.getName().length() + user2.getName().length()) * 5 % 100;
    }

    public static boolean requestChat(User user) {
        // Always succeeds for the prototype
        return true;
    }

    public static boolean sendMessage(String message) {
        // Always succeeds for the prototype
        return true;
    }

    public static boolean saveProfile() {
        // Always succeeds for the prototype
        return true;
    }
}
