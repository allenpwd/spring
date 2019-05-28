package pwd.allen.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pwd.allen.util.GenerateXmlUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pwd
 * @create 2018-08-12 10:31
 **/
@Controller
@RequestMapping("/help")
public class CaptchaController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Producer captchaProducer;

    @RequestMapping("getCaptcha.do")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);// 禁止server端缓存
        // 设置标准的 HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // 设置IE扩展 HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");// 设置标准 HTTP/1.0 不缓存图片
        response.setContentType("image/jpeg");// 返回一个 jpeg 图片，默认是text/html(输出文档的MIMI类型)
        String capText = captchaProducer.createText();// 为图片创建文本

        // 将文本保存在session中。这里就使用包中的静态变量吧
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        BufferedImage bi = captchaProducer.createImage("潘伟丹"); // 创建带有文本的图片
        ServletOutputStream out = response.getOutputStream();
        // 图片数据输出
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }

        System.out.println("Session 验证码是aasdfa：" + request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY));
    }


    /**
     * 第三方入账明细导出excel表格
     *
     * @param begintime
     * @param endtime
     * @param p
     * @param l
     * @param payType
     * @param k
     * @param response
     */
    @RequestMapping("exportPayLogExcel")
    public void exportPayLogExcel(String begintime, String endtime, Integer p, Integer l, Integer payType, String k,
                                  HttpServletResponse response) {
        // 创建excel表序号参数
        Long num = 0l;
        // 逐页查询数据,将所有数据导出到excel表中(注:此方法中不传p,l参数,使用的是service层中,默认的第1页开始,每页显示50条)
        Integer pp = 1;
        Long tt = 1l;
        List<List<Object>> datas = new ArrayList<>();
        OutputStream out = null;

        for (int i = 0; i < 4; i++) {
            List<Object> list = new ArrayList<Object>();
            list.add("234");
            list.add("3245");
            list.add("345");
            list.add("3453");
            list.add("3453");
            list.add("345");
            datas.add(list);
        }

        // 设置表格表头
        List<String[]> listHeaders = new ArrayList<>();
//		String[] headers = new String[] { "序号", "入账时间", "内部交易流水号", "第三方流水号", "入账金额(元)", "第三方渠道" };
        listHeaders.add(new String[] { "序号", "入账时间", "交易流水号", "", "入账金额(元)", "第三方渠道" });
        listHeaders.add(new String[] { "", "", "内部交易流水号", "第三方流水号", "", "" });
        // 设置合并单元格
        List<Integer[]> merges = new ArrayList<>();
        /*merges.add(new Integer[] { 0, 0, 2, 3 });
        merges.add(new Integer[] { 0, 1, 0, 0 });
        merges.add(new Integer[] { 0, 1, 1, 1 });
        merges.add(new Integer[] { 0, 1, 4, 4 });
        merges.add(new Integer[] { 0, 1, 5, 5 });*/
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
                    "宋体", 12, 10, true, false, true, true, "紫色", "灰色", true, true, true, 30, null,null);
//			Workbook book3 = GenerateXmlUtil.exportComplexExcel(listHeaders, datas, "第三方入账明细", merges, subheads, "宋体",
//					"宋体", 12, 10, true, false, true, true, "紫色", "灰色", true, true, true, null, columnWidths,rowHeights);
//			Workbook book1 = GenerateXmlUtil.exportComplexExcel(listHeaders, datas, "第三方入账明细", merges, subheads);
//			Workbook book2 = GenerateXmlUtil.exportComplexExcel(headers, datas, "第三方入账明细");
            String fileName = "第三方入账明细" + ".xls";
            response.reset(); // 清空response
            response.setContentType("application/x-msdownload");
            response.setHeader("Connection", "close"); // 表示不能用浏览器直接打开
            response.setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            response.setCharacterEncoding("UTF-8");
            out = response.getOutputStream();
            book.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/excel.do")
    private void excel (HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header");
        response.setHeader("Content-Disposition", "attachment;filename=abcasdfasdfsdf.xls");
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
        File file = new File("C:\\Users\\Administrator\\Desktop\\excel-884.xls");
        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(file));
        book.write(response.getOutputStream());
    }
}
