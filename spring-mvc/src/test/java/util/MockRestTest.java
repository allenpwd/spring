package util;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

/**
 * 模拟请求
 *
 * @author 门那粒沙
 * @create 2020-02-06 15:51
 **/
public class MockRestTest {

    private MockRestServiceServer mockRestServiceServer;
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }

    /**
     * 模拟get请求：访问/hello并返回字符串
     */
    @Test
    public void testGet() {
        String responseBody = "this is a mocking request";
        this.mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("/hello"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(responseBody, MediaType.APPLICATION_JSON_UTF8));

        String hello = this.restTemplate.getForObject("/hello", String.class);
        System.out.println(hello);

        this.mockRestServiceServer.verify();
    }

}
