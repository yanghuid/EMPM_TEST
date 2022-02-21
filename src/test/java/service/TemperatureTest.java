package service;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@RunWith(Parameterized.class)
public class TemperatureTest extends TestCase {
    private static Temperature temperature;
    private String province;
    private String city;
    private String county;

    @Before
    public void setUp() throws Exception {
        temperature = new Temperature();
    }

    public TemperatureTest(String province, String city, String county) {
        this.province = province;
        this.city = city;
        this.county = county;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"江苏", "苏州", "苏州"},
                {"江苏", "上海", "苏州"},
                // 边缘数据
                {"江苏", "", ""},
                {"江苏", "苏州", ""},
                {"", "", ""},
        });
    }


    @Test(expected = IOException.class)
    public void testGetTemperature() throws Exception {
        Optional<Integer> opt = Optional.ofNullable(23);
        // 测试用例测试
        assertEquals(opt, temperature.getTemperature(province, city, county));
    }

}