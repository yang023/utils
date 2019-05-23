package cn.yang.util.select;

import java.util.List;
import java.util.Map;

/**
 * @author yang 2019/3/20
 */
public abstract class OptionProvider {

    protected abstract String type();

    public abstract List<Option> fetch(Map<String, Object> param);

}
