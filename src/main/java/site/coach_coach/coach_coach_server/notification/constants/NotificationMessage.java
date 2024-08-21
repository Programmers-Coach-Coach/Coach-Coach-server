package site.coach_coach.coach_coach_server.notification.constants;

import lombok.Getter;

@Getter
public enum NotificationMessage {
	REVIEW_MESSAGE("새로운 리뷰가 작성되었습니다."),
	ASK_MESSAGE("문의 메시지를 남겼습니다."),
	LIKE_MESSAGE("회원님을 관심 코치로 등록하였습니다."),
	USER_MESSAGE("님이 ");

	private final String message;

	NotificationMessage(String message) {
		this.message = message;
	}
}
