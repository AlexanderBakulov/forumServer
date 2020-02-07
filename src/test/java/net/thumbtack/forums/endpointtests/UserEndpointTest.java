package net.thumbtack.forums.endpointtests;

import net.thumbtack.forums.dao.CommonDao;
import net.thumbtack.forums.errors.ForumException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@TestPropertySource(locations="classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserEndpointTest {

    @Autowired
    CommonDao commonDao;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach()
    public void clearDatabase() throws ForumException {
        commonDao.clear();
    }

//    @Test
//    public void testCreateUser() {
//        CreateUserDtoRequest request = new CreateUserDtoRequest("User", "user@mail.ru", "123456aA");
//        assertThat(this.restTemplate.postForObject("http://localhost:9090/api/users",
//                request, CommonUserDtoResponse.class).getName()).isEqualTo(request.getName());
//    }



}
