package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pwd.allen.service.MyService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 门那粒沙
 * @create 2020-02-11 21:30
 **/
@Log4j2
@RestController
@RequestMapping("my")
public class MyController {

    @Autowired
    private MyService myService;

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
    @CrossOrigin
    @GetMapping("param/{name}")
    public ResponseEntity<Map> param(@PathVariable String name
            , @RequestParam(required = false) Integer error
            , Date date
            , @MatrixVariable(pathVar = "name", required = false) MultiValueMap matrixVars) {

        System.out.println(MvcUriComponentsBuilder.fromMappingName("MC#param").arg(0, "pwd").buildAndExpand("abc").toString());

        switch (error) {
            case 1:
                throw new RuntimeException("我出错了");
            case 2:
                System.out.println(10 / 0);
                break;
        }
        myService.printThree(name);
        HashMap map = new HashMap<>(matrixVars);
        map.put("name", name);
        map.put("error", error);
        map.put("date", date);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(map);
    }

    /**
     * 使用apache的 commons-fileupload方式处理上传文件
     *  需要引入commons-fileupload.jar并注入 {@link org.springframework.web.multipart.commons.CommonsMultipartResolver}
     *
     *  MultipartResolver：{@link org.springframework.web.multipart.commons.CommonsMultipartResolver}
     *  MultipartHttpServletRequest：{@link org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest}
     *
     * 另一种上传方式：
     * Servlet3.0标准的上传方式
     *  需要配置multipart-config
     *  MultipartResolver：{@link org.springframework.web.multipart.support.StandardServletMultipartResolver}
     *  MultipartHttpServletRequest：{@link org.springframework.web.multipart.support.StandardMultipartHttpServletRequest}
     *
     * @param file
     * @return
     */
    @PostMapping("upload")
    public Object upload(MultipartHttpServletRequest request, @RequestParam("file") MultipartFile file) {
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", file.getSize());
        map.put("originalFilename", file.getOriginalFilename());
//        file.transferTo(new File());//这个方法只能调一次，调过后文件就没了
        return map;
    }

    /**
     * 使用PropertyEditor自定义参数类型转换，只对当前Controller有效
     * InitBinder的value属性用于限定要处理的方法参数，如果没有指定则每个参数都需要绑定一次
     *
     * @param binder
     */
//    @InitBinder("date")
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
