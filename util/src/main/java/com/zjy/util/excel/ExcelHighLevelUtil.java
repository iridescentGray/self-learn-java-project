package com.zjy.util.excel;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.zjy.util.file.FileHighLevelUtil;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author zjy
 * @date 2022/3/13
 */
public class ExcelHighLevelUtil {

    public static FileHighLevelUtil.FileFilter EXCEL_FILTER = file -> file != null && !file.isDirectory() && ("xlsx".equals(FileTypeUtil.getType(file)));

    /**
     * 迭代每个Sheet页
     * ®
     *
     * @param excelFile     excel文件
     * @param sheetConsumer 消费每一个Sheet
     */
    public static void doWithSheets(File excelFile, BiConsumer<String, ExcelReader> sheetConsumer) {
        Assert.notNull(excelFile);
        ExcelReader sheetSizeReader = ExcelUtil.getReader(FileUtil.getInputStream(excelFile));
        int sheetSize = sheetSizeReader.getSheets().size();
        List<String> sheetNames = sheetSizeReader.getSheetNames();
        sheetSizeReader.close();
        for (int i = 0; i < sheetSize; i++) {
            ExcelReader sheetReader = ExcelUtil.getReader(excelFile, i);
            sheetConsumer.accept(sheetNames.get(i), sheetReader);
            sheetReader.close();
        }
    }

    public static void doWithSheetsBySax(File excelFile, RowHandler rowHandler) {
        Assert.notNull(excelFile);
        ExcelReader sheetSizeReader = ExcelUtil.getReader(FileUtil.getInputStream(excelFile));
        int sheetSize = sheetSizeReader.getSheets().size();
        sheetSizeReader.close();
        for (int i = 0; i < sheetSize; i++) {
            ExcelUtil.readBySax(excelFile, 0, rowHandler);
        }
    }

    public static void doWithSheetsBySax(File excelFile, RowHandlerCreator rowHandlerCreator) {
        Assert.notNull(excelFile);
        ExcelReader sheetSizeReader = ExcelUtil.getReader(FileUtil.getInputStream(excelFile));
        int sheetSize = sheetSizeReader.getSheets().size();
        sheetSizeReader.close();
        for (int i = 0; i < sheetSize; i++) {
            ExcelUtil.readBySax(excelFile, i, rowHandlerCreator.create());
            rowHandlerCreator.convergence(i);
        }
    }

    /**
     * 写出Excel --根据集合
     */
    public static void writeBigExcelByCollection(String writePath, Collection<?> collection) {
        BigExcelWriter writer = ExcelUtil.getBigWriter(writePath);
        writer.write(collection);
        writer.close();
    }


    /**
     * 行阅读器 创建者
     */
    @FunctionalInterface
    public interface RowHandlerCreator {

        RowHandler create();

        /**
         * 收敛函数  负责善后
         *
         * @param sheet
         */
        default void convergence(int sheet) {
        }
    }

}
