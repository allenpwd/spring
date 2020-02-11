package pwd.allen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author 门那粒沙
 * @create 2020-02-11 22:43
 **/
@RestController
public class ErrorController {

    @RequestMapping("/error")
    public Object handle(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", request.getAttribute("javax.servlet.error.status_code"));
        map.put("reason", request.getAttribute("javax.servlet.error.message"));
        return map;
    }
}
