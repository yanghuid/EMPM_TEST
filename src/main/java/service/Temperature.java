package service;

import net.sf.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public class Temperature {
    private HttpService httpService = new HttpService();

    /**
     * @param province 省
     * @param city     市
     * @param county   县
     * @return 返回该县的温度
     */
    public Optional<Integer>
    getTemperature(String province, String city, String county) throws Exception {
        // 根据省名称找到对应的省会代码id
        String provinceId = check(httpService.getRequest("province", ""), province);
        if ("NULL".equals(provinceId)) {
            throw new Exception("省会无法找到！");
        }
        // 根据省码找到对应的城市代码
        String cityId = check(httpService.getRequest("city", provinceId), city);
        if ("NULL".equals(cityId)) {
            throw new Exception("城市无法找到！");
        }
        // 根据城市找到对应的县城代码
        String countyId = check(httpService.getRequest("county", provinceId + cityId), county);
        if ("NULL".equals(countyId)) {
            throw new Exception("县或者区无法找到！");
        }
        // 根据县城代码找到对应的天气并且返回
        Integer temperature = getWeatherInfo(httpService.getRequest("temperature", provinceId + cityId + countyId));
        if (temperature == 99) {
            throw new Exception("无法获取该县或区的温度！");
        }
        Optional<Integer> opt = Optional.ofNullable(temperature);
        return opt;
    }

    /**
     * 获取该县的温度
     *
     * @param temperature
     * @return
     * @throws Exception
     */
    private Integer getWeatherInfo(Map<String, Object> temperature) throws Exception {
        String temp = "99";
        JSONObject o = (JSONObject) temperature.get("weatherinfo");
        if (!o.isEmpty()) {
            temp = (String) o.get("temp");
        }
        return Integer.valueOf(temp.substring(0, 2));
    }

    /**
     * 校验省会或者城市名字是否正确,成功返回省会城市代码，否则返回NULL用于异常抛出
     *
     * @param map
     * @param cityName
     * @return
     */
    private String check(Map<String, Object> map, String cityName) {
        // 根据名称获取代码id
        String id = "NULL";
        for (Map.Entry<String, Object> m : map.entrySet()) {
            String name = (String) m.getValue();
            if (cityName.equals(name)) {
                id = m.getKey();
                break;
            }
        }
        return id;
    }
}
