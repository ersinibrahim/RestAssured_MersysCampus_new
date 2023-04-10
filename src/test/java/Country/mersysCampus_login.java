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
import static org.hamcrest.Matchers.*;

public class mersysCampus_login {


    Cookies cookies;


    @BeforeClass
    public void loginBasqar() {
        baseURI = "https://test.mersys.io";

        Map<String, String> crenditial = new HashMap<>();
        crenditial.put("username", "turkeyts");
        crenditial.put("password", "TechnoStudy123");
        crenditial.put("rememberMe", "true");

        cookies =
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

    String randomGenName = RandomStringUtils.randomAlphabetic(6);
    String randomGenCode = RandomStringUtils.randomNumeric(3);

    String countryId;

    Country country = new Country();

    @Test
    public void createCountry() {


        country.setName(randomGenName);
        country.setCode(randomGenCode);


        countryId =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("/school-service/api/countries")


                        .then()
                        .statusCode(201)
                        .body("name", equalTo(randomGenName))
                        .log().body()
                        .extract().jsonPath().getString("id")


        ;


    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {


        country.setName(randomGenName);
        country.setCode(randomGenCode);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .post("/school-service/api/countries")


                .then()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \"" + randomGenName + "\" already exists."))


        ;


    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {
        String updateCountryName = RandomStringUtils.randomAlphabetic(6);

        Country country = new Country();
        country.setId(countryId);
        country.setName(updateCountryName);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .put("/school-service/api/countries")


                .then()
                .statusCode(200)
                .body("name", equalTo(updateCountryName))


        ;


    }


    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

        given()

                .cookies(cookies)
                .pathParam("countryId", countryId)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .delete("/school-service/api/countries/{countryId}")


                .then()
                .statusCode(200)
                .log().body()


        ;


    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

        given()

                .cookies(cookies)
                .pathParam("countryId", countryId)


                .when()
                .delete("/school-service/api/countries/{countryId}")


                .then()
                .statusCode(404)
                .log().body()


        ;


    }


}
