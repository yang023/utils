package cn.yang.util.select;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yang 2019/3/20
 */
public class SelectProvider {

    private Map<String, OptionProvider> providers;

    public SelectProvider() {
        this.providers = new ConcurrentHashMap<>();
    }

    public SelectProvider(Map<String, OptionProvider> providers) {
        this.providers = providers;
    }

    // TODO: lambda-->add

    public SelectProvider addProvider(OptionProvider provider) {
        this.providers.put(provider.type(), provider);
        return this;
    }


    public List<Option> options(String type, Map<String, Object> params) {
        OptionProvider provider = this.providers.get(type);
        return provider.fetch(params);
    }

}
