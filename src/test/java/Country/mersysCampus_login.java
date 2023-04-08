package Country;

import Country.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class mersysCampus_login {


    Cookies cookies;


    @BeforeClass
    public void loginBasqar()
    {
        baseURI="https://test.mersys.io";

        Map<String,String> crenditial=new HashMap<>();
        crenditial.put("username","turkeyts");
        crenditial.put("password","TechnoStudy123");
        crenditial.put("rememberMe","true");

        cookies=
                given()
                        .body(crenditial)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().detailedCookies();


        System.out.println("cookies = " + cookies);



    }


    @Test
    public void createCountry(){

        String randomGenName= RandomStringUtils.randomAlphabetic(6);
        String randomGenCode= RandomStringUtils.randomNumeric(3);

        Country country=new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);



        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")


                .then()
                .statusCode(201)
                .log().body()



        ;


    }






}
