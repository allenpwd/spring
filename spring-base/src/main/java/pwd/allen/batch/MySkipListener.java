package pwd.allen.batch;

import org.springframework.batch.core.SkipListener;
import pwd.allen.entity.Person;

/**
 * job设置了跳过策略时可以监听跳过的过程
 * @author 门那粒沙
 * @create 2020-02-25 13:09
 **/
public class MySkipListener implements SkipListener<Person, Person> {

    /**
     * 读的时候跳过时回调
     * 为何read没有Item参数：要读对了才有Item，既然读都出错了那就没item了
     * @param t
     */
    @Override
    public void onSkipInRead(Throwable t) {

    }

    /**
     * 写的时候跳过时回调
     * @param item
     * @param t
     */
    @Override
    public void onSkipInWrite(Person item, Throwable t) {

    }

    /**
     * 处理的时候跳过时回调
     * @param item
     * @param t
     */
    @Override
    public void onSkipInProcess(Person item, Throwable t) {
        System.out.println(item.getId() + "got excception:" + t.toString());
    }
}
