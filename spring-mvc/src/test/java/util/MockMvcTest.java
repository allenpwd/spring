package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pwd.allen.config.AOPConfig;
import pwd.allen.config.MainConfig;
import pwd.allen.config.MvcConfig;
import pwd.allen.controller.CaptchaController;
import pwd.allen.filter.MyFilter;
import pwd.allen.service.MyService;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author 门那粒沙
 * @create 2020-02-03 13:26
 **/
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextHierarchy({
        @ContextConfiguration(classes = {MainConfig.class, AOPConfig.class, MvcConfig.class})
//        , @ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml")
})
public class MockMvcTest {

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void mockGet() throws Exception {
        MockMvc mockMvc = null;
        ConfigurableMockMvcBuilder mockMvcBuilder = null;

        //region 构建MockMvc模拟
        //方法一
        mockMvcBuilder = MockMvcBuilders.webAppContextSetup(wac);
        //方法二
        CaptchaController captchaController = new CaptchaController();
        //使用Mockito模拟下MyService服务依赖
        MyService myService = Mockito.mock(MyService.class);
        ReflectionTestUtils.setField(captchaController, "myService", myService);
        mockMvcBuilder = MockMvcBuilders.standaloneSetup(captchaController);
        //配置期望所有响应中的状态为200
        mockMvcBuilder.alwaysExpect(status().isOk());
        //添加过滤器
        mockMvcBuilder.addFilter(new MyFilter(), "/*");
        mockMvc = mockMvcBuilder.build();
        //endregion

        String name = "pwd";
        mockMvc.perform(get("/test/my")
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("pwd"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试上传文件
     * @throws Exception
     */
    @Test
    public void mockUpload() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultRequest(get("/").contextPath("/pwd-web"))//设置通用的默认请求属性
                .build();
        String filePath = "db.properties";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .fileUpload("/pwd-web/test/upload")
                .file(new MockMultipartFile("file", filePath, "text/plain", new ClassPathResource(filePath).getInputStream())))
                .andReturn();
        try {
            System.out.println(mvcResult.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
