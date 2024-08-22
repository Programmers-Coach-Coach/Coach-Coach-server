package site.coach_coach.coach_coach_server.completedcategory.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;
import site.coach_coach.coach_coach_server.completedcategory.repository.CompletedCategoryRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CompletedCategoryService {
	private final CompletedCategoryRepository completedCategoryRepository;
	private final UserRepository userRepository;

	public void createCompletedCategory(Long userIdByJwt, Category category, Date record_date) {
		User user = userRepository.getReferenceById(userIdByJwt);

		CompletedCategory completedCategory = CompletedCategory.builder()
			.user(user)
			.category(category)
			.recordDate(record_date)
			.build();
		completedCategoryRepository.save(completedCategory);
	}

	public void deleteCompletedCategory(Long completedCategoryId) {
		
	}

}
