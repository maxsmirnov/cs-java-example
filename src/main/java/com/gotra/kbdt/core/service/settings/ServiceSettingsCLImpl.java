package com.gotra.kbdt.core.service.settings;

import com.gotra.kbdt.core.service.abs.ServiceSettings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author gotra
 */

public class ServiceSettingsCLImpl implements ServiceSettings {
    static Logger logger = Logger.getLogger(ServiceSettingsCLImpl.class.getName());
    private List<String> args = new ArrayList<String>();

    public ServiceSettingsCLImpl(String[] _args){
        for(String str: _args){
            args.add(str);
        }
    }

    private Object GetSettingValue(String source, String prefix){
        String tmp = source.substring(source.indexOf('=') + 1);
        return (Object)tmp;
    }

    public String GetStringParam(String paramName){
        for(String str: args){
            if(str.contains(paramName + "="))
                return (String)GetSettingValue(str, paramName + "=");
        }
        return "";
    }
    public Integer GetNumericParam(String paramName){
        for(String str: args){
            if(str.contains(paramName + "="))
                return Integer.parseInt((String) GetSettingValue(str, paramName + "="));
        }
        return new Integer(-1);
    }
    public Boolean GetBooleanParam(String paramName){
        for(String str: args){
            if(str.contains(paramName + "="))
                return Boolean.parseBoolean((String) GetSettingValue(str, paramName + "="));
        }
        return false;
    }
    public Double GetFloatParam(String paramName){
        for(String str: args){
            if(str.contains(paramName + "="))
                return Double.parseDouble((String) GetSettingValue(str, paramName + "="));
        }
        return new Double(0);
    }
    public Map<String, Object> GetListParam(String paramName){
        Map<String, Object> map = new HashMap<String, Object>();
        for(String str: args){
            if(str.contains(paramName + "="))
                map.put(paramName, GetSettingValue(str, paramName + "="));
        }

        return map;
    }
}
