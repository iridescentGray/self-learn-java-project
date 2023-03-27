package com.zjy.util.image;

import cn.hutool.core.io.FileTypeUtil;
import com.zjy.util.file.FileHighLevelUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * 压缩者
 *
 * @author zjy
 * @date 2022/3/12
 */
@Slf4j
public class ImageUtil {

    public static FileHighLevelUtil.FileFilter IMAGE_FILTER = file -> file != null && ("png".equals(FileTypeUtil.getType(file)) || "jpg".equals(FileTypeUtil.getType(file)));

    /**
     * 旋转图片
     *
     * @param sourceFile 源文件
     * @param rotate     旋转角度
     */
    public static File rotateImage(File sourceFile, int rotate) throws IOException {
        return rotateImage(sourceFile, null, rotate);
    }

    public static File rotateImage(File sourceFile, File targetFile, int rotate) throws IOException {
        if (sourceFile == null) {
            return null;
        }
        Thumbnails.of(sourceFile).scale(1f).rotate(rotate).toFile(targetFile != null ? targetFile : (targetFile = FileHighLevelUtil.defaultSaveFile(sourceFile)));
        return targetFile;
    }


    /**
     * 图片
     *
     * @param sourceFile 源文件
     * @param scale      缩放比率
     */
    public static File scaleImage(File sourceFile, float scale) throws IOException {
        return compressAndScaleImage(sourceFile, null, scale, 1f);
    }

    /**
     * 降低图片大小
     *
     * @param sourceFile 源文件
     * @param quality    降低目标大小
     */
    public static File compressFile(File sourceFile, float quality) throws IOException {
        return compressAndScaleImage(sourceFile, null, 1f, quality);
    }

    /**
     * 同比例缩放图片并降低图片大小
     *
     * @param sourceFile 源文件
     * @param quality    降低目标大小
     */
    public static File compressAndScaleImage(File sourceFile, float scale, float quality) throws IOException {
        return compressAndScaleImage(sourceFile, null, scale, quality);
    }

    public static File compressAndScaleImage(File sourceFile, File targetFile, float scale, float quality) throws IOException {
        if (sourceFile == null) {
            return null;
        }
        Thumbnails.of(sourceFile).scale(scale).outputQuality(quality).toFile(targetFile != null ? targetFile : (targetFile = FileHighLevelUtil.defaultSaveFile(sourceFile)));
        return targetFile;
    }

}
