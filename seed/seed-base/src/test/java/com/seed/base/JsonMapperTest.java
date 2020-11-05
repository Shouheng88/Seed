package com.seed.base;

import com.seed.base.model.enums.DeviceType;
import com.seed.base.utils.JsonMapper;
import lombok.Data;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 15:40
 */
public class JsonMapperTest {

    @Test
    public void testToString() {
        TestOuterClass mapperTest = new TestOuterClass("the name", "the value", new Date(),
                new TestOuterClass.TestInnerClass("the inner name", "the inner value"));
        Assert.assertNotNull(JsonMapper.nonDefaultMapper().toJson(mapperTest));
    }

    public static void main(String...args) {
        TestOuterClass testOuterClass = new TestOuterClass("the name", "the value", new Date(),
                new TestOuterClass.TestInnerClass("the inner name", "the inner value"));
        testOuterClass.list = Arrays.asList("A", "B", "C");
        TestOuterClass testOuterClass2 = new TestOuterClass("the name 2", "the value 2", new Date(),
                new TestOuterClass.TestInnerClass("the inner name 2", "the inner value 2"));
        testOuterClass.list = Arrays.asList("A 2", "B 2", "C 2");
        testOuterClass.nonDefault = null;
        testOuterClass.deviceType = DeviceType.ANDROID;
        String outJson1 = JsonMapper.nonDefaultMapper().toJson(testOuterClass);
        System.out.println(outJson1);
        System.out.println(JsonMapper.nonEmptyMapper().toJson(testOuterClass));
        System.out.println(JsonMapper.nonNullMapper().toJson(testOuterClass));
        System.out.println(JsonMapper.nonNullAndDefaultDateFormatMapper().toJson(testOuterClass));
        String outJson2 = JsonMapper.nonEmptyMapper().toJson(Arrays.asList(testOuterClass, testOuterClass2));
        TestOuterClass outerClass = JsonMapper.nonDefaultMapper().fromJson(outJson1, TestOuterClass.class);
        System.out.println(outerClass);
        System.out.println(outerClass.innerClass);
        List<TestOuterClass.TestInnerClass> list = JsonMapper.nonDefaultMapper().fromJson(outJson2, List.class, TestOuterClass.class);
        System.out.println(list);
    }

    @Data
    @ToString
    public static class TestOuterClass {

        private String name;

        private String value;

        private Date date;

        private String nullValue;

        private String emptyValue;

        private String nonDefault = "";

        private DeviceType deviceType;

        private TestInnerClass innerClass;

        private List<String> list;

        public TestOuterClass() {
        }

        public TestOuterClass(String name, String value, Date date, TestInnerClass testInnerClass) {
            this.name = name;
            this.value = value;
            this.date = date;
            this.innerClass = testInnerClass;
        }

        @Data
        @ToString
        public static class TestInnerClass {

            private String name;

            private String value;

            public TestInnerClass() {
            }

            public TestInnerClass(String name, String value) {
                this.name = name;
                this.value = value;
            }
        }
    }
}
