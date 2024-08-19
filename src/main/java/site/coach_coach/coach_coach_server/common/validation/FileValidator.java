package site.coach_coach.coach_coach_server.common.validation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.InvalidFileException;

@Component
public class FileValidator {
	@Value("${file.upload.max-size}")
	private long maxFileSize;

	private final List<String> allowedExtensions = Arrays.asList(
		"jpg", "jpeg", "jfif", "png", "gif", "bmp", "webp", "tif", "tiff"
	);

	public void validate(MultipartFile file) {
		validateFileSize(file);
		validateFileExtension(file);
	}

	private void validateFileSize(MultipartFile file) {
		if (file.getSize() > maxFileSize) {
			throw new InvalidFileException(ErrorMessage.INVALID_FILE_SIZE);
		}
	}

	private void validateFileExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if (fileName == null || fileName.isBlank()) {
			throw new InvalidFileException(ErrorMessage.INVALID_FILE_EXTENSION);
		}

		String fileExtension = getFileExtension(fileName);
		if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
			throw new InvalidFileException(ErrorMessage.INVALID_FILE_NAME);
		}
	}

	private String getFileExtension(String fileName) {
		int lastIndexOfDot = fileName.lastIndexOf(".");
		if (lastIndexOfDot == -1) {
			return "";
		}
		return fileName.substring(lastIndexOfDot + 1);
	}
}
