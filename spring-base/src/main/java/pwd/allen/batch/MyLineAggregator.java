package pwd.allen.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.transform.LineAggregator;
import pwd.allen.entity.Person;

/**
 * 自定义行聚合器，将Person转成json
 * @author 门那粒沙
 * @create 2020-02-24 23:09
 **/
public class MyLineAggregator implements LineAggregator<Person> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String aggregate(Person item) {
        try {
            return mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize.", e);
        }
    }
}
