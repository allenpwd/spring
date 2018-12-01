package pwd.allen.normal;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import pwd.allen.service.CaptchaService;
import pwd.allen.util.ExcelUtils;
import pwd.allen.util.GenerateXmlUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author pwd
 * @create 2018-08-12 20:44
 **/
public class Test {

    @org.junit.Test
    public void test() {
        CaptchaService captchaService = new CaptchaService();
        System.out.println(captchaService.getRandomStr());
    }

    @org.junit.Test
    public void testExcel() throws FileNotFoundException {
        // 创建excel表序号参数
        Long num = 0l;
        // 逐页查询数据,将所有数据导出到excel表中(注:此方法中不传p,l参数,使用的是service层中,默认的第1页开始,每页显示50条)
        Integer pp = 1;
        Long tt = 1l;
        List<List<Object>> datas = new ArrayList<>();
        File file = new File("C:\\Users\\Administrator\\Desktop\\excel-" + new Random().nextInt(1000) + ".xls");
        OutputStream out = new FileOutputStream(file);

        for (int i = 0; i < 6; i++) {
            List<Object> list = new ArrayList<Object>();
            Random random = new Random();
            for (int j = 0; j < 6; j++) {
                list.add(random.nextInt(10000));
            }
            datas.add(list);
        }

        // 设置表格表头
        List<String[]> listHeaders = new ArrayList<>();
//		String[] headers = new String[] { "序号", "入账时间", "内部交易流水号", "第三方流水号", "入账金额(元)", "第三方渠道" };
        listHeaders.add(new String[] { "序号", "入账时间", "交易流水号", "", "入账金额(元)", "第三方渠道" });
        listHeaders.add(new String[] { "", "", "内部交易流水号", "第三方流水号", "", "" });
        // 设置合并单元格
        List<Integer[]> merges = new ArrayList<>();
        merges.add(new Integer[] { 0, 0, 2, 3 });
        merges.add(new Integer[] { 0, 1, 0, 0 });
        merges.add(new Integer[] { 0, 1, 1, 1 });
        merges.add(new Integer[] { 0, 1, 4, 4 });
        merges.add(new Integer[] { 0, 1, 5, 5 });
        merges.add(new Integer[] { 3, 5, 0, 0 });
        // 设置副表头
        List<Integer[]> subheads = new ArrayList<>();
        subheads.add(new Integer[] { 3, 3, 0, 5 });
//		// 设置列宽
//		List<Integer[]> columnWidths = new ArrayList<>();
//		columnWidths.add(new Integer[] { 0, 50 });
//		columnWidths.add(new Integer[] { 2, 50 });
//		// 设置行高
//		List<Integer[]> rowHeights = new ArrayList<>();
//		rowHeights.add(new Integer[] { 0, 50 });
        // 导出
        try {
            Workbook book = GenerateXmlUtil.exportComplexExcel(listHeaders, datas, "第三方入账明细", merges, subheads, "宋体",
                    "宋体", 12, 10, true, false, true, true, "黄色", "灰色", true, true, true, 30, null,null);
//			Workbook book3 = GenerateXmlUtil.exportComplexExcel(listHeaders, datas, "第三方入账明细", merges, subheads, "宋体",
//					"宋体", 12, 10, true, false, true, true, "紫色", "灰色", true, true, true, null, columnWidths,rowHeights);
//			Workbook book1 = GenerateXmlUtil.exportComplexExcel(listHeaders, datas, "第三方入账明细", merges, subheads);
//			Workbook book2 = GenerateXmlUtil.exportComplexExcel(headers, datas, "第三方入账明细");
            String fileName = "第三方入账明细" + ".xls";
            book.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void fe_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "C:\\Users\\Administrator\\Desktop\\template\\template.xls");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2014-12-25");
        map.put("money", 2000000.00);
        map.put("upperMoney", "贰佰万");
        map.put("company", "执笔潜行科技有限公司");
        map.put("bureau", "财政局");
        map.put("person", "JueYue");
        map.put("phone", "1879740****");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 4; i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("id", i + 1 + "");
            lm.put("zijin", i * 10000 + "");
            lm.put("bianma", "A001");
            lm.put("mingcheng", "设计");
            lm.put("xiangmumingcheng", "EasyPoi " + i + "期");
            lm.put("quancheng", "开源项目");
            lm.put("sqje", i * 10000 + "");
            lm.put("hdje", i * 10000 + "");
            lm.put("yinghang", "银行账号");
            lm.put("kaihu", "开户");

            listMap.add(lm);
        }
        map.put("maplist", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\专项支出用款申请书_map.xls");
        workbook.write(fos);
        fos.close();
    }

    @org.junit.Test
    public void test3() {
        try {
            ExcelUtils.exec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test4() {

    }

}
