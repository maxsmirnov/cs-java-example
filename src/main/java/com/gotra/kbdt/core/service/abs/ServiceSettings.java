package com.gotra.kbdt.core.service.abs;

import java.util.Map;

/**
 * @author gotra
 */

public interface ServiceSettings {
    public String GetStringParam(String paramName);
    public Integer GetNumericParam(String paramName);
    public Boolean GetBooleanParam(String paramName);
    public Double GetFloatParam(String paramName);
    public Map<String, Object> GetListParam(String paramName);
}
