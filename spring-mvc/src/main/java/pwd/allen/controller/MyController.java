package pwd.allen.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pwd.allen.service.MyService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 门那粒沙
 * @create 2020-02-11 21:30
 **/
@Log4j2
@RestController
@RequestMapping("my")
public class MyController {

    /**
     * handle方法的入参
     *  可以使用servlet API类型：处理器为 ServletRequestMethodArgumentResolver
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver
     *
     * @MatrixVariable 矩阵变量，每个变量用;分隔，多个值用,分隔；如/get/12;app=WeiBo;ids=1,2
     * 注意：默认这个功能没开启，开启方式：
     *  1）xml方式：<mvc:annotation-driven enable-matrix-variables="true"/>
     *  2）注解配置方式：?.setRemoveSemicolonContent(false)，?可以是RequestMappingHandlerMapping
     *
     * @param name
     * @param error
     * @param date
     * @param matrixVars
     * @return
     */
    @RequestMapping("param/{name}")
    public Object param(@PathVariable String name
            , @RequestParam(required = false) Integer error
            , Date date
            , @MatrixVariable(pathVar = "name") MultiValueMap matrixVars) {

        System.out.println(MvcUriComponentsBuilder.fromMappingName("MC#param").arg(0, "pwd").buildAndExpand("abc").toString());

        switch (error) {
            case 1:
                throw new RuntimeException("我出错了");
            case 2:
                System.out.println(10 / 0);
                break;
        }

        matrixVars.add("name", name);
        matrixVars.add("error", error);
        matrixVars.add("date", date);
        return matrixVars.toSingleValueMap();
    }

    /**
     * 上传文件
     *
     * 需要启用MultipartResolver
     *
     * @param file
     * @return
     */
    @PostMapping("upload")
    public Object upload(@RequestParam("file") MultipartFile file) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", file.getSize());
        map.put("originalFilename", file.getOriginalFilename());
        return map;
    }

    /**
     * 自定义参数类型转换
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    /**
     * 处理来自Controller handler的方法的ArithmeticException异常
     * 可参考 {@link org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver}
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public Object handleError(Exception e) {
        return Collections.singletonMap("error", e.toString());
    }

}
