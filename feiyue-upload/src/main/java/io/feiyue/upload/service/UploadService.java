package io.feiyue.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg", "image/png");

    //import org.slf4j.Logger;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        //校验文件类型
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)) {
            LOGGER.info("文件类型不合法: {}", originalFilename);
            return null;
        }

        try {
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容给不合法: {}" + originalFilename);
            }


/*            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String type = StringUtils.substringAfterLast(originalFilename, ".");
            //保存到文件服务器
            file.transferTo(new File("D:\\Workspace\\IdeaProjects\\feiyue\\upload_image\\" + uuid + "." + type));
            //返回url,进行相应
            return "http://image.feiyue.io/" + uuid + "." + type;*/

            //保存到fastDFS文件服务器
            String    ext       = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            // 生成url地址，返回
            return "http://image.feiyue.io/" + storePath.getFullPath();

        } catch (IOException e) {
            LOGGER.info("服务器内部处理文件错误: " + originalFilename);

            e.printStackTrace();
        }

        return null;
    }
}
