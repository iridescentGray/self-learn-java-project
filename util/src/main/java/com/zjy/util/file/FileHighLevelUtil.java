package com.zjy.util.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author zjy
 * @date 2022/3/13
 */
public class FileHighLevelUtil {

    public static void doWithFileUnderPath(String path, FileCallback fc, FileFilter fileFilter) {
        File[] ls = FileUtil.ls(path);
        for (File file : ls) {
            if (fileFilter != null && !fileFilter.matches(file)) {
                continue;
            }
            try {
                fc.doWith(file);
            } catch (Exception ex) {
                throw new IllegalStateException("文件操作异常 '" + file.getName() + "': " + ex);
            }
        }
    }

    public static String defaultSavePath(File sourceFile, String pathName) {
        return defaultSavePath(sourceFile, pathName, "");
    }

    /**
     * 生成保存文件的默认路径
     *
     * @param sourceFile
     * @param pathName
     * @param nameDisturbance
     * @return
     */
    public static String defaultSavePath(File sourceFile, String pathName, String nameDisturbance) {
        Assert.notNull(sourceFile);
        Assert.notBlank(pathName);
        Assert.notNull(nameDisturbance);
        return StrUtil.format("{}/{}/{}{}", sourceFile.getParent(), pathName, nameDisturbance, sourceFile.getName());
    }

    /**
     * 以默认的方式保存文件
     *
     * @param sourceFile
     * @return
     */
    public static File defaultSaveFile(File sourceFile) {
        Assert.notNull(sourceFile);
        return defaultSaveFile(sourceFile, null);
    }

    /**
     * 以默认的方式保存文件
     *
     * @param sourceFile
     * @param pathName
     * @return
     */
    public static File defaultSaveFile(File sourceFile, String pathName) {
        Assert.notNull(sourceFile);
        if (StrUtil.isBlank(pathName)) {
            pathName = "defaultGenerate";
        }
        return FileUtil.touch(defaultSavePath(sourceFile, pathName));
    }

    /**
     * 文件操作
     */
    @FunctionalInterface
    public interface FileCallback {

        void doWith(File file) throws IOException;
    }


    /**
     * 文件过滤
     */
    @FunctionalInterface
    public interface FileFilter {

        boolean matches(File file);
    }

}
