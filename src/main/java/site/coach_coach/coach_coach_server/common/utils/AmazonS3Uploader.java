package site.coach_coach.coach_coach_server.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.validation.FileValidator;

@Slf4j
@RequiredArgsConstructor
@Component
public class AmazonS3Uploader {
	private final AmazonS3 amazonS3;
	private final FileValidator fileValidator;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadMultipartFile(MultipartFile multipartFile, String dirName) throws IOException {
		fileValidator.validate(multipartFile);
		File uploadFile = convertToFile(multipartFile)
			.orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CONVERT_FAIL));
		try {
			return uploadFileToS3(uploadFile, dirName);
		} finally {
			removeNewFile(uploadFile);
		}
	}

	private String uploadFileToS3(File uploadFile, String dirName) {
		String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
		return putS3(uploadFile, fileName);
	}

	private Optional<File> convertToFile(MultipartFile file) throws IOException {
		String originalFileName = getOriginalFileName(file);
		File convertFile = new File(System.getProperty("java.io.tmpdir"), originalFileName);

		try {
			if (convertFile.createNewFile()) {
				writeFile(file, convertFile);
				return Optional.of(convertFile);
			} else {
				log.error("File already exists: {}", convertFile.getAbsolutePath());
				return Optional.empty();
			}
		} catch (IOException e) {
			log.error("Failed to create or write to file: {}", convertFile.getAbsolutePath(), e);
			return Optional.empty();
		}
	}

	private String getOriginalFileName(MultipartFile file) {
		return Optional.ofNullable(file.getOriginalFilename())
			.filter(name -> !name.isBlank())
			.orElse(UUID.randomUUID().toString());
	}

	private void writeFile(MultipartFile file, File convertFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(convertFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			log.error("Failed to write bytes to file: {}", convertFile.getAbsolutePath(), e);
			if (convertFile.exists()) {
				convertFile.delete();
			}
			throw e; // Rethrow to handle it in the main method
		}
	}

	private String putS3(File uploadFile, String fileName) {
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일 삭제 성공");
		} else {
			log.error("파일 삭제 실패: {}", targetFile.getAbsolutePath());
		}
	}
}
