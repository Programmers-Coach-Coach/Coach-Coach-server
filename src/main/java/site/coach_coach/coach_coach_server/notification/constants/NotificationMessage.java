package site.coach_coach.coach_coach_server.notification.constants;

import lombok.Getter;

@Getter
public enum NotificationMessage {
	REVIEW_MESSAGE("새로운 리뷰가 작성되었습니다."),
	ASK_MESSAGE("매칭을 신청하였습니다."),
	LIKE_MESSAGE("회원님을 관심 코치로 등록하였습니다."),
	MATCH_MESSAGE("회원님의 매칭 신청을 수락하셨습니다."),
	REFUSAL_MESSAGE(" 매칭 신청을 거절하였습니다."),
	USER_MESSAGE("님이 ");

	private final String message;

	NotificationMessage(String message) {
		this.message = message;
	}
}
