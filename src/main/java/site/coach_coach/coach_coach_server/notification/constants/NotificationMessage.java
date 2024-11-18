package site.coach_coach.coach_coach_server.notification.constants;

import lombok.Getter;

@Getter
public enum NotificationMessage {
	REVIEW_MESSAGE("새로운 리뷰가 작성되었습니다!"),
	ASK_MESSAGE("코치 매칭을 요청했어요!"),
	LIKE_MESSAGE("회원님을 관심 코치로 등록했어요!"),
	MATCH_MESSAGE("코치 매칭을 수락했어요!"),
	MATCH_REQUEST_MESSAGE(" 님께 코치 매칭을 요청했어요! 답변이 오면 빠르게 알려드릴게요 :)"),
	REFUSAL_MESSAGE("매칭 요청을 거절하였습니다."),
	CANCEL_MESSAGE("매칭을 취소하였습니다."),
	ROUTINE_MESSAGE("새로운 루틴을 등록했어요."),
	USER_MESSAGE(" 님이 ");

	private final String message;

	NotificationMessage(String message) {
		this.message = message;
	}
}
