package com.seed.portal.config;

import cn.hutool.extra.mail.MailUtil;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.seed.base.utils.DozerBeanUtil;
import com.seed.base.utils.MailUtils;
import com.seed.base.utils.SeedSecurity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.unit.DataSize;
import org.thymeleaf.TemplateEngine;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * SpringBoot basic configuration.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 14:21
 */
@Configuration
@EnableSwagger2
@MapperScan(basePackages = {"com.seed.data.dao"})
@ComponentScan(basePackages = {"com.seed.data.manager"})
public class BootConfig {

    @Resource
    private JavaMailSender mailSender;

    @Bean
    public DozerBeanUtil dozerBeanUtil() {
        return DozerBeanUtil.get();
    }

    @Bean
    public SeedSecurity seedSecurity() {
        return SeedSecurity.get();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        List<Filter> filterList = new ArrayList<>();
        filterList.add(wallFilter());
        filterList.add(new StatFilter());
        filterList.add(new Slf4jLogFilter());
        druidDataSource.setProxyFilters(filterList);
        return druidDataSource;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter=new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig(){
        WallConfig config = new WallConfig();
        // Allow to execute multiple statements at once
        config.setMultiStatementAllow(true);
        // Allow none base statement
        config.setNoneBaseStatementAllow(true);
        return config;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(2));
        factory.setMaxRequestSize(DataSize.ofMegabytes(2));
        return factory.createMultipartConfig();
    }

    @Bean
    public MailUtils mailUtils(TemplateEngine templateEngine, @Value("${mail.from.address}") String emailAddress) {
        return new MailUtils(mailSender, templateEngine, emailAddress);
    }

}
