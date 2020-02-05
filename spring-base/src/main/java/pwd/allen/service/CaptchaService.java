package pwd.allen.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author pwd
 * @create 2018-08-12 10:33
 **/
@Service
public class CaptchaService {

    private static char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
            ,'0','1','2','3','4','5','6','7','8','9'};

    public String getRandomStr() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

}
