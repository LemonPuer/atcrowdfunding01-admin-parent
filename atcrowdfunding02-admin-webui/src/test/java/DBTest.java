import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * @author Lemon
 * @create 2023-02-16-16:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml"})
public class DBTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminMapper mapper;

    @Test
    public void test1() throws SQLException {
        System.out.println(ds.getConnection());
    }

    @Test
    public void test2() {
        adminService.saveUser(new Admin(null, "tom", "123456", "tom", "123@qq.com", "2000-1-1"));
    }

    @Test
    public void test3() {
        Logger logger = LoggerFactory.getLogger(DBTest.class);
        logger.info(CrowdUtil.md5("123456"));
    }

    @Test
    public void test4() {
        for (int i = 0; i < 200; i++) {
            mapper.insert(new Admin(null, "acct" + i, "pswd" + i, "uname" + i, "email" + i, null));
        }
    }
}
