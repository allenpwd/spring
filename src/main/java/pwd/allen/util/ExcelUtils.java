package pwd.allen.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pwd
 * @create 2018-08-27 9:41
 **/
public class ExcelUtils {

    private static SimpleDateFormat DATE_FORAMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Workbook exec() throws Exception {
        List<Map<String, Object>> list_data = new ArrayList<Map<String, Object>>();
        int begin_row = 3, begin_col = 0;
        int data_num_row = 8, data_num_col = 5;
        for (int i = 0; i < 8; i++) {
            //Map<String, Object> map_row = new HashMap<String, Object>();
            //map_row.put("rowHeight", 20);
            //List<Map<String, Object>> list_data = new ArrayList<Map<String, Object>>();

            Map<String, Object> map_cell = null;
            map_cell = new HashMap<String, Object>();
            map_cell.put("row", i + begin_row);
            map_cell.put("col", 0 + begin_col);
            map_cell.put("text", "陈光存");
            if (i%2 == 0) {
                map_cell.put("merge", new Integer[] {1, 0});
            }
            list_data.add(map_cell);
            map_cell = new HashMap<String, Object>();
            map_cell.put("text", "出勤");
            map_cell.put("color", "黄色");
            map_cell.put("row", i + begin_row);
            map_cell.put("col", 1 + begin_col);
            list_data.add(map_cell);
            map_cell = new HashMap<String, Object>();
            map_cell.put("text", "产假");
            map_cell.put("row", i + begin_row);
            map_cell.put("col", 2 + begin_col);
            list_data.add(map_cell);
            map_cell = new HashMap<String, Object>();
            map_cell.put("text", "轮休");
            map_cell.put("row", i + begin_row);
            map_cell.put("col", 3 + begin_col);
            list_data.add(map_cell);
            map_cell = new HashMap<String, Object>();
            map_cell.put("text", "事假");
            map_cell.put("row", i + begin_row);
            map_cell.put("col", 4 + begin_col);
            list_data.add(map_cell);

        }

        HashMap<String, Object> map_cell_t = null;

        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "姓名 日期");
        map_cell_t.put("width", 40);
        map_cell_t.put("height", 40);
        map_cell_t.put("anchor", true);
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 0);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "26");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 1);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "27");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 2);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "28");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 3);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "29");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 4);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "30");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 5);
        list_data.add(map_cell_t);

        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "广西大藤峡水利枢纽开发有限责任公司2018年08月员工考勤表");
        map_cell_t.put("bold", true);
        map_cell_t.put("height", 50);
        map_cell_t.put("row", 0);
        map_cell_t.put("col", 0);
        map_cell_t.put("merge", new Integer[]{0, 6});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "部门：综合部");
        map_cell_t.put("row", 1);
        map_cell_t.put("col", 0);
        map_cell_t.put("merge", new Integer[]{0, 1});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        //map_cell_t.put("text", "");
        map_cell_t.put("row", 1);
        map_cell_t.put("col", 2);
        map_cell_t.put("merge", new Integer[]{0, 4});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "考勤员");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 5);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("row", 3);
        map_cell_t.put("col", 5);
        map_cell_t.put("merge", new Integer[]{9, 0});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "员工签字");
        map_cell_t.put("row", 2);
        map_cell_t.put("col", 6);
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("row", 3);
        map_cell_t.put("col", 6);
        map_cell_t.put("merge", new Integer[]{1, 0});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("row", 5);
        map_cell_t.put("col", 6);
        map_cell_t.put("merge", new Integer[]{1, 0});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("row", 7);
        map_cell_t.put("col", 6);
        map_cell_t.put("merge", new Integer[]{1, 0});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("row", 9);
        map_cell_t.put("col", 6);
        map_cell_t.put("merge", new Integer[]{1, 0});
        list_data.add(map_cell_t);

        //考勤符号
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "考勤符号");
        map_cell_t.put("row", 11);
        map_cell_t.put("col", 0);
        map_cell_t.put("merge", new Integer[]{1, 0});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "出勤");
        map_cell_t.put("row", 11);
        map_cell_t.put("col", 1);
        map_cell_t.put("merge", new Integer[]{0, 1});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "——");
        map_cell_t.put("row", 12);
        map_cell_t.put("col", 1);
        map_cell_t.put("merge", new Integer[]{0, 1});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "出差");
        map_cell_t.put("row", 11);
        map_cell_t.put("col", 3);
        map_cell_t.put("merge", new Integer[]{0, 1});
        list_data.add(map_cell_t);
        map_cell_t = new HashMap<String, Object>();
        map_cell_t.put("text", "——");
        map_cell_t.put("row", 12);
        map_cell_t.put("col", 3);
        map_cell_t.put("merge", new Integer[]{0, 1});
        list_data.add(map_cell_t);

        //File excelFile = new File("C:\\Users\\Administrator\\Desktop\\excel-template.xls");
        //InputStream is = new FileInputStream(excelFile);
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFSheet sheet = work.createSheet("测试");

        //套数据
        for (int i = 0; i < list_data.size(); i++) {
            Map<String, Object> map_cell = list_data.get(i);
            Integer num_row = (Integer)map_cell.get("row");
            Integer num_col = (Integer)map_cell.get("col");
            if (num_row != null && num_col != null) {
                HSSFRow row = sheet.getRow(num_row);
                if (row == null) row = sheet.createRow(num_row);
                HSSFCell cell = row.getCell(num_col);
                if (cell == null) cell = row.createCell(num_col);
                ExcelUtils.setCell(cell, map_cell);
            }
        }


        File file = new File("C:\\Users\\Administrator\\Desktop\\excel-" + new Random().nextInt(1000) + ".xls");
        OutputStream out = new FileOutputStream(file);
        work.write(out);

        return work;
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
            cell.setCellValue(ExcelUtils.DATE_FORAMT.format(value));
        } else if (value.getClass() == Long.class) {
            cell.setCellValue((Long) value);
        }
    }


    public static void setCell(HSSFCell cell, Map<String, Object> map_cell) {

        // 创建字体样式
        HSSFFont font = cell.getSheet().getWorkbook().createFont();
        // 创建格式
        HSSFCellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        // 设置字体
        if (map_cell.get("fontName") != null && !"".equals(map_cell.get("fontName"))) {
            font.setFontName((String)map_cell.get("fontName"));
        } else {
            font.setFontName("宋体");
        }
        // 设置字体加粗
        if (map_cell.get("bold") != null) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        // 设置字体大小
        if (map_cell.get("size") != null && !"".equals(map_cell.get("size"))) {
            // 设置字体高度
            font.setFontHeightInPoints(((Integer)map_cell.get("size")).shortValue());
            // 设置字体宽度
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        // 设置字体格式
        style.setFont(font);
        // 设置居中
        //if (map_cell.get("center") != null) {
            style.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        //}
        // 设置背景颜色
        String color = (String)map_cell.get("color");
        if (color != null && !"".equals(color)) {
            if ("黄色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("绿色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("红色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("蓝色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("紫色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("灰色".equals(color)) {
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        }
        // 设置边框
        if (map_cell.get("borderBottom") != null) {
            style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
            style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
            style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
            style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        }
        // 设置自动换行
        //if (map_cell.get("wrapText") != null) {
            style.setWrapText(true);
        //}
        cell.setCellStyle(style);
        //单元格合并
        Integer[] arr_merge = (Integer[])map_cell.get("merge");
        if (arr_merge != null && arr_merge.length == 2) {
            CellRangeAddress region = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + arr_merge[0], cell.getColumnIndex(), cell.getColumnIndex() + arr_merge[1]);
            cell.getSheet().addMergedRegion(region);
        }
        //设置行高
        if (map_cell.get("height") != null) {
            cell.getRow().setHeightInPoints((Integer)map_cell.get("height"));
        }
        //设置宽
        if (map_cell.get("width") != null) {
            cell.getSheet().setColumnWidth(cell.getColumnIndex(), (Integer)map_cell.get("width") * 64);
        }
        //设置斜线
        if (map_cell.get("anchor") != null) {
            HSSFClientAnchor anchor = new HSSFClientAnchor();
            anchor.setAnchor((short) cell.getColumnIndex(), cell.getRowIndex(), 0, 0, (short)(cell.getColumnIndex() + 1), cell.getRowIndex() + 1, 0, 0);
            HSSFPatriarch patriarch = cell.getSheet().createDrawingPatriarch();
            HSSFSimpleShape line1 = patriarch.createSimpleShape(anchor);
            line1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            line1.setLineStyle(HSSFShape.LINESTYLE_SOLID);
            //在NPOI中线的宽度12700表示1pt,所以这里是0.5pt粗的线条。
            line1.setLineWidth(6350);
        }
        ExcelUtils.setValue(cell, map_cell.get("text"));

    }

    public static void setDefaultStyle(HSSFSheet sheet) {
        // 创建字体样式
        HSSFFont font = sheet.getWorkbook().createFont();
        // 创建格式
        HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        // 设置字体
        font.setFontName("宋体");
        // 设置字体加粗
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 设置字体大小
        // 设置字体宽度
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 设置字体格式
        style.setFont(font);
        sheet.getRow(1).setRowStyle(style);
    }

}


