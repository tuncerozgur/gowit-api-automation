package baseUrl;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class PetStoreBaseUrl {

    public RequestSpecification spec;

    @BeforeClass
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2")
                .setContentType(ContentType.JSON)
                .build();
    }

    // Helper method to avoid repeating "given().spec(spec).contentType(ContentType.JSON)"
    public RequestSpecification getRequestSpecification() {
        return given().spec(spec).contentType(ContentType.JSON);
    }
}
