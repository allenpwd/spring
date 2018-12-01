package pwd.allen.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * POI导出excel表格
 * @author lv617
 * @version 1.0
 */
public class GenerateXmlUtil {
    private static SimpleDateFormat DATE_FORAMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出最精简的excel表格
     * @param headers
     * @param datas
     * @param name
     * @return
     */
    public static Workbook generateCreateXsl(String headers[], List<List<Object>> datas, String name) {
        if (headers == null || headers.length < 1 || CollectionUtils.isEmpty(datas) || !StringUtils.hasText(name)) {
            return null;
        }
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFSheet sheet = work.createSheet(name);
        // 设置标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        for (List<Object> data : datas) {
            row = sheet.createRow(datas.indexOf(data) + 1);
            for (int i = 0; i < data.size(); i++) {
                HSSFCell cell = row.createCell(i);
                GenerateXmlUtil.setValue(cell, data.get(i));
            }
        }
        return work;
    }

    /**
     * 导出复杂的excel表格(自定义格式)
     * @param listHeaders  复杂表头数据(结合合并单元格使用,实现复杂表头)
     * @param datas 表格内容数据
     * @param name  表格中sheet的名称
     * @param merges 合并单元格; 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
     * @param subheads 将表格数据设置为副表头; 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
     * @param hFontName 表格表头字体格式
     * @param dFontName 表格内容字体格式
     * @param hSize 表格表头字体大小
     * @param dSize 表格内容字体大小
     * @param hBold  表头字体加粗
     * @param dBold  内容字体加粗
     * @param hCenter 表头字体居中
     * @param dCenter 内容字体居中
     * @param hForegroundColor  表头背景色; 例:红色,黄色,绿色,蓝色,紫色,灰色.
     * @param dForegroundColor 内容背景色; 例:红色,黄色,绿色,蓝色,紫色,灰色.
     * @param hWrapText 表头自动换行
     * @param dWrapText 内容自动换行
     * @param borderBottom  表格添加边框
     * @param autoWidths 设置自适应列宽的最大展示宽度
     * @param columnWidths  自定义列宽设置; 例:参数1：哪一列 参数2：列宽值
     * @param rowHeights  自定义行高设置; 例:参数1：哪一行 参数2：行高值
     * @return
     */
    public static Workbook exportComplexExcel(List<String[]> listHeaders, List<List<Object>> datas, String name, List<Integer[]> merges, List<Integer[]> subheads, String hFontName, String dFontName,
                                              Integer hSize, Integer dSize, boolean hBold, boolean dBold, boolean hCenter, boolean dCenter, String hForegroundColor, String dForegroundColor, boolean hWrapText, boolean dWrapText,
                                              boolean borderBottom, Integer autoWidths, List<Integer[]> columnWidths, List<Integer[]> rowHeights) {
        if (listHeaders == null || listHeaders.get(0) == null || listHeaders.get(0).length < 1 || CollectionUtils.isEmpty(datas) || !StringUtils.hasText(name)) {
            return null;
        }
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFSheet sheet = work.createSheet(name);
        // 设置列宽
        if (columnWidths != null && columnWidths.size() > 0 && columnWidths.get(0).length == 2) {
            for (Integer[] columnWidth : columnWidths) {
                sheet.setColumnWidth(columnWidth[0], columnWidth[1] * 256);
            }
        }
        // 设置自适应列宽
        if (autoWidths != null) {
            List<Integer> maxCalls = GenerateXmlUtil.getMaxCall(listHeaders, datas);
            for (int i = 0, j = maxCalls.size(); i < j; i++) {
                // 最大列宽设置
                if (maxCalls.get(i) > autoWidths) {
                    sheet.setColumnWidth(i, autoWidths * 256);
                } else {
                    sheet.setColumnWidth(i, maxCalls.get(i) * 256);
                }
            }
        }
        // 设置合并单元格
        if (merges != null && merges.size() > 0 && merges.get(0).length == 4) {
            // 设置合并单元格//参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
            for (Integer[] merge : merges) {
                CellRangeAddress region = new CellRangeAddress(merge[0], merge[1], merge[2], merge[3]);
                sheet.addMergedRegion(region);
            }
        }
        // 设置行高
        if (rowHeights != null && rowHeights.size() > 0 && rowHeights.get(0).length == 2) {
            // 设置表头格式
            HSSFCellStyle style = GenerateXmlUtil.setStyle(work, hFontName, hSize, hBold, hCenter, hForegroundColor, hWrapText, borderBottom);
            // 设置 表格内容格式
            HSSFCellStyle style2 = GenerateXmlUtil.setStyle(work, dFontName, dSize, dBold, dCenter, dForegroundColor, dWrapText, borderBottom);
            for (Integer[] rowHeight : rowHeights) {
                // 插入表格表头数据
                GenerateXmlUtil.addHeader(listHeaders, sheet, style, rowHeight);
                // 插入表格内容数据
                GenerateXmlUtil.addData(listHeaders, datas, subheads, work, sheet, style2, rowHeight, style);
            }
        } else {
            // 设置表头格式
            HSSFCellStyle style = GenerateXmlUtil.setStyle(work, hFontName, hSize, hBold, hCenter, hForegroundColor, hWrapText, borderBottom);
            // 设置 表格内容格式
            HSSFCellStyle style2 = GenerateXmlUtil.setStyle(work, dFontName, dSize, dBold, dCenter, dForegroundColor, dWrapText, borderBottom);
            // 插入表格表头数据
            GenerateXmlUtil.addHeader(listHeaders, sheet, style, null);
            // 插入表格内容数据
            GenerateXmlUtil.addData(listHeaders, datas, subheads, work, sheet, style2, null, style);
        }
        return work;
    }

    /**
     * 导出复杂的excel表格
     * (复杂表头固定格式:表头字体,宋体12号,加粗,紫色背景色;内容字体,宋体10号;字体居中,自动换行,自适应列宽(最大30 ),表格加边框)
     * @param listHeaders  复杂表头数据(结合合并单元格使用,实现复杂表头)
     * @param datas  表格内容数据
     * @param name  表格中sheet的名称
     * @param merges  合并单元格; 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
     * @param subheads  将表格数据设置为副表头; 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
     * @return
     */
    public static Workbook exportComplexExcel(List<String[]> listHeaders, List<List<Object>> datas, String name, List<Integer[]> merges, List<Integer[]> subheads) {
        if (listHeaders == null || listHeaders.get(0) == null || listHeaders.get(0).length < 1 || CollectionUtils.isEmpty(datas) || !StringUtils.hasText(name)) {
            return null;
        }
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFSheet sheet = work.createSheet(name);
        // 设置自适应列宽
        List<Integer> maxCalls = GenerateXmlUtil.getMaxCall(listHeaders, datas);
        for (int i = 0, j = maxCalls.size(); i < j; i++) {
            // 最大列宽设置
            if (maxCalls.get(i) > 30) {
                sheet.setColumnWidth(i, 30 * 256);
            } else {
                sheet.setColumnWidth(i, maxCalls.get(i) * 256);
            }
        }
        // 设置标题行格式
        HSSFCellStyle style = GenerateXmlUtil.setStyle(work, "宋体", 12, true, true, "紫色", true, true);
        // 设置表格内容行格式
        HSSFCellStyle style2 = GenerateXmlUtil.setStyle(work, "宋体", 10, false, true, null, true, true);
        // 设置合并单元格
        if (merges != null && merges.size() > 0 && merges.get(0).length == 4) {
            // 设置合并单元格//参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
            for (Integer[] merge : merges) {
                CellRangeAddress region = new CellRangeAddress(merge[0], merge[1], merge[2], merge[3]);
                sheet.addMergedRegion(region);
            }
        }
        // 插入表格表头数据
        GenerateXmlUtil.addHeader(listHeaders, sheet, style, null);
        // 插入表格内容数据
        GenerateXmlUtil.addData(listHeaders, datas, subheads, work, sheet, style2, null, style);
        return work;
    }

    /**
     * 导出复杂的excel表格
     * (单行表头固定格式:表头字体,宋体12号,加粗,紫色背景色;内容字体,宋体10号;字体居中,自动换行,自适应列宽(最大30 ),表格加边框)
     * @param headers  单行表头数据
     * @param datas 表格内容数据
     * @param name  表格中sheet的名称
     * @return
     */
    public static Workbook exportComplexExcel(String headers[], List<List<Object>> datas, String name) {
        if (headers == null || headers.length < 1 || CollectionUtils.isEmpty(datas) || !StringUtils.hasText(name)) {
            return null;
        }
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFSheet sheet = work.createSheet(name);
        List<String[]> listHeaders = new ArrayList<>();
        listHeaders.add(headers);
        // 设置自适应列宽
        List<Integer> maxCalls = GenerateXmlUtil.getMaxCall(listHeaders, datas);
        for (int i = 0, j = maxCalls.size(); i < j; i++) {
            // 最大列宽设置
            if (maxCalls.get(i) > 30) {
                sheet.setColumnWidth(i, 30 * 256);
            } else {
                sheet.setColumnWidth(i, maxCalls.get(i) * 256);
            }
        }
        // 设置标题行格式
        HSSFCellStyle style = GenerateXmlUtil.setStyle(work, "宋体", 12, true, true, "紫色", true, true);
        // 设置表格内容行格式
        HSSFCellStyle style2 = GenerateXmlUtil.setStyle(work, "宋体", 10, false, true, null, true, true);
        // 插入表格表头数据
        for (int m = 0, n = listHeaders.size(); m < n; m++) {
            HSSFRow row = sheet.createRow(m);
            String[] header = listHeaders.get(m);
            for (int i = 0; i < header.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(header[i]);
                cell.setCellValue(text);
                // 单元格格式设置
                cell.setCellStyle(style);
            }
        }
        // 插入表格内容数据
        for (List<Object> data : datas) {
            Integer rowNum = datas.indexOf(data) + listHeaders.size();
            HSSFRow row = sheet.createRow(rowNum);
            for (int i = 0, j = data.size(); i < j; i++) {
                HSSFCell cell = row.createCell(i);
                GenerateXmlUtil.setValue(cell, data.get(i));
                // 单元格格式设置
                cell.setCellStyle(style2);
            }
        }
        return work;
    }

    /************************************* 手动分割线 ******************************************************/
    /**
     * 插入表格的表头数据
     * @param listHeaders  复杂表头数据(结合合并单元格使用,实现复杂表头)
     * @param sheet HSSFSheet
     * @param style 表格格式
     * @param rowHeight 设置行高; 参数1：哪一行 参数2：行高值
     */
    private static void addHeader(List<String[]> listHeaders, HSSFSheet sheet, HSSFCellStyle style, Integer[] rowHeight) {
        for (int m = 0, n = listHeaders.size(); m < n; m++) {
            HSSFRow row = sheet.createRow(m);
            if (rowHeight != null && rowHeight[0] == m) {
                row.setHeightInPoints(rowHeight[1]);
            } else {
                // 设置默认行高
                sheet.setDefaultRowHeightInPoints(100);
            }
            String[] headers = listHeaders.get(m);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
                // 单元格格式设置
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 插入表格的内容数据
     * @param listHeaders  复杂表头数据(结合合并单元格使用,实现复杂表头)
     * @param datas  表格内容数据
     * @param subheads 将表格数据设置为副表头; 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
     * @param work HSSFWorkbook
     * @param sheet HSSFSheet
     * @param style2  表格格式
     * @param rowHeight 设置行高; 参数1：哪一行 参数2：行高值
     */
    private static void addData(List<String[]> listHeaders, List<List<Object>> datas, List<Integer[]> subheads, HSSFWorkbook work, HSSFSheet sheet, HSSFCellStyle style2, Integer[] rowHeight,
                                HSSFCellStyle style3) {
        for (List<Object> data : datas) {
            Integer rowNum = datas.indexOf(data) + listHeaders.size();
            HSSFRow row = sheet.createRow(rowNum);
            if (rowHeight != null && rowHeight[0] == rowNum) {
                row.setHeightInPoints(rowHeight[1]);
            } else {
                // 设置默认行高
                sheet.setDefaultRowHeightInPoints(50);
            }
            for (int i = 0, j = data.size(); i < j; i++) {
                HSSFCell cell = row.createCell(i);
                GenerateXmlUtil.setValue(cell, data.get(i));
                // 设置副表头格式
                if (subheads != null && subheads.size() > 0 && subheads.get(0).length == 4) {
                    // 设置副表头格式
                    for (Integer[] subhead : subheads) {
                        for (int w = 0, v = subhead.length; w < v; w++) {
                            for (int m = subhead[0], n = subhead[1]; m <= n; m++) {
                                if (m == rowNum) {
                                    for (int x = subhead[2], y = subhead[3]; x <= y; x++) {
                                        if (x == i) {
                                            // 单元格格式设置
                                            cell.setCellStyle(style3);
                                        }
                                    }
                                    break;
                                }
                                // 单元格格式设置
                                cell.setCellStyle(style2);
                            }
                        }
                    }
                } else {
                    // 单元格格式设置
                    cell.setCellStyle(style2);
                }
            }
        }
    }

    /**
     * 设置表格格式
     * @param work HSSFWorkbook
     * @param fontName 字体样式
     * @param size 字体大小
     * @param bold 加粗
     * @param center 居中
     * @param foregroundColor 背景色
     * @param wrapText 自动换行
     * @param borderBottom 边框
     * @return
     */
    private static HSSFCellStyle setStyle(HSSFWorkbook work, String fontName, Integer size, boolean bold, boolean center, String foregroundColor, boolean wrapText, boolean borderBottom) {
        // 创建字体样式
        HSSFFont font = work.createFont();
        // 创建格式
        HSSFCellStyle style = work.createCellStyle();
        // 设置字体
        if (fontName != null) {
            font.setFontName("宋体");
        }
        // 设置字体加粗
        if (bold) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        // 设置字体大小
        if (size != null) {
            // 设置字体高度
            font.setFontHeightInPoints(size.shortValue());
            // 设置字体宽度
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        // 设置字体格式
        style.setFont(font);
        // 设置居中
        if (center) {
            style.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        }
        // 设置背景颜色
        if (foregroundColor != null) {
            if ("黄色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("绿色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("红色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("蓝色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("紫色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("灰色".equals(foregroundColor)) {
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
        }
        // 设置边框
        if (borderBottom) {
            style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
            style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
            style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
            style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        }
        // 设置自动换行
        if (wrapText) {
            style.setWrapText(true);
        }
        return style;
    }

    /**
     * 最大列宽
     * @param headers
     * @param datas
     * @return
     */
    private static List<Integer> getMaxCall(List<String[]> listHeaders, List<List<Object>> datas) {
        // 创建最大列宽集合
        List<Integer> maxCall = new ArrayList<>();
        List<List<Integer>> lss = new ArrayList<>();
        // 计算标题行数据的列宽
        for (int i = 0, j = listHeaders.size(); i < j; i++) {
            List<Integer> hls = new ArrayList<>();
            for (int m = 0, n = listHeaders.get(i).length; m < n; m++) {
                int length = listHeaders.get(i)[m].getBytes().length;
                hls.add(length);
                if (i == 0) {
                    // 最大列宽赋初值
                    maxCall.add(0);
                }
            }
            lss.add(hls);
        }
        // 计算内容行数据的列宽
        for (int i = 0, j = datas.size(); i < j; i++) {
            List<Integer> dls = new ArrayList<>();
            for (int m = 0, n = datas.get(i).size(); m < n; m++) {
                Object obj = datas.get(i).get(m);
                if (obj.getClass() == Date.class) {
                    // 日期格式类型转换
                    obj = GenerateXmlUtil.DATE_FORAMT.format(obj);
                }
                int length = obj.toString().getBytes().length;
                dls.add(length);
            }
            lss.add(dls);
        }
        // 根据列宽计算出每列的最大宽度
        for (int i = 0, j = lss.size(); i < j; i++) {
            for (int m = 0, n = lss.get(i).size(); m < n; m++) {
                Integer a = lss.get(i).get(m);
                Integer b = maxCall.get(m);
                if (a > b) {
                    maxCall.set(m, a);
                }
            }
        }
        return maxCall;
    }

    /**
     * 表格数据类型转换
     * @param cell
     * @param value
     */
    private static void setValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value.getClass() == String.class) {
            cell.setCellValue((String) value);
        } else if (value.getClass() == Integer.class) {
            cell.setCellValue((Integer) value);
        } else if (value.getClass() == Double.class) {
            cell.setCellValue((Double) value);
        } else if (value.getClass() == Date.class) {
            cell.setCellValue(GenerateXmlUtil.DATE_FORAMT.format(value));
        } else if (value.getClass() == Long.class) {
            cell.setCellValue((Long) value);
        }
    }
	/*TODO	
	 private static String getValue(Cell cell) {
			String value = "";
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = String.valueOf(cell.getNumericCellValue());
			}
			return value;
		}*/
}