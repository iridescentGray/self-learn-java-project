package com.zjy.runner.excel;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.zjy.util.excel.ExcelHighLevelUtil;
import com.zjy.util.file.FileHighLevelUtil;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zjy
 * @date 2022/3/13
 */
public class ExcelTest {

    public static void main(String[] args) {
        FileHighLevelUtil.doWithFileUnderPath("/Users/zjy/Desktop/test"
                , file -> ExcelHighLevelUtil.doWithSheetsBySax(file, new ExcelHighLevelUtil.RowHandlerCreator() {
                    final LinkedList<Object> sheetData = new LinkedList<>();
                    final LinkedList<Object> sheetHeader = new LinkedList<>();
                    final AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public RowHandler create() {
                        return (sheetIndex, rowIndex, rowCells) -> {
                            if (rowIndex == 0) {
                                sheetHeader.add(rowCells);
                            } else {
                                sheetData.add(rowCells);
                            }
                            if (sheetData.size() == 500) {
                                sheetData.addFirst(sheetHeader.getFirst());
                                ExcelHighLevelUtil.writeBigExcelByCollection(FileHighLevelUtil.defaultSavePath(file, "newExcel", "sheet-" + sheetIndex + "-" + atomicInteger.incrementAndGet()), sheetData);
                                sheetData.clear();
                            }
                            Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowCells);
                        };
                    }

                    @Override
                    public void convergence(int sheetIndex) {
                        ExcelHighLevelUtil.writeBigExcelByCollection(FileHighLevelUtil.defaultSavePath(file, "newExcel", "sheet-" + sheetIndex + "-" + atomicInteger.incrementAndGet()), sheetData);
                        sheetData.clear();
                    }
                })
                , ExcelHighLevelUtil.EXCEL_FILTER);
    }

}
