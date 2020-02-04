package pwd.allen.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pwd.allen.service.MyService;
import pwd.allen.service.PersonService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author pwd
 * @create 2018-08-12 10:31
 **/
@RestController
@RequestMapping("test")
public class CaptchaController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Producer captchaProducer;

    @Autowired
    private MyService myService;

    @Autowired
    private PersonService personService;

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

        BufferedImage bi = captchaProducer.createImage("李多海"); // 创建带有文本的图片
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

    @RequestMapping("my.do")
    public Object my(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {
        myService.printTwo(name);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        return map;
    }

    @PostMapping("upload.do")
    public Object upload(@RequestParam("file") MultipartFile file) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", file.getSize());
        map.put("originalFilename", file.getOriginalFilename());
        return map;
    }

}
