package com.sparta.javafeed.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.ImgFileType;
import com.sparta.javafeed.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Util {

    // AWS S3 클라이언트
    private final AmazonS3 amazonS3;
    // 파일 확장자 변조 체크
    private final Tika tika = new Tika();

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    /**
     * AWS S3 파일 업로드
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        // 파일 확장자 체크
        validImgFile(file);

        String originName = file.getOriginalFilename(); // 원본 파일명
        String extension = StringUtils.getFilenameExtension(originName); // 확장자
        String saveName = generateSaveFilename(originName);

        ObjectMetadata metadata = new ObjectMetadata(); // 메타데이터
        metadata.setContentType(Mimetypes.getInstance().getMimetype(saveName));
        metadata.setContentLength(file.getSize());

        try {
            // AWS S3 파일 업로드
            PutObjectResult putObjectResult = amazonS3.putObject(
                    new PutObjectRequest(bucketName, saveName, file.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorType.UPLOAD_FAILED);
        }

        // 데이터베이스에 저장할 파일이 저장된 주소
        return amazonS3.getUrl(bucketName, saveName).toString();
    }

    /**
     * 파일 확장자 체크
     * @param file 파일
     */
    private void validImgFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String mimeType = tika.detect(inputStream);

            ImgFileType.getImgFileType(mimeType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 저장할 파일명 생성
     * @param filename 원본 파일명
     * @return 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }
}
