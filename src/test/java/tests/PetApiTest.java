package tests;

import baseUrl.PetStoreBaseUrl;
import io.restassured.http.ContentType;
import models.Pet;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.lessThan;

public class PetApiTest extends PetStoreBaseUrl {

    private final int petId = 78909;  // constant id

            // ---------- POSITIVE TESTS ----------

    @Test(priority = 1, description = "Add new pet to the store")
    public void testAddNewPet() {
        Pet pet = new Pet(petId, "Puppy The Guardian", "available");

        getRequestSpecification()
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(petId))
                .body("name", equalTo("Puppy The Guardian"))
                .body("status", equalTo("available"))
                .body("id", instanceOf(Integer.class))
                .body("name", instanceOf(String.class))
                .time(lessThan(3500L))
                .log().ifValidationFails()
                .log().body();
    }

    @Test(priority = 2, description = "Get pet by ID")
    public void testGetPetById() {
        getRequestSpecification()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)  // Content-Type doğrulaması
                .body("id", equalTo(petId))
                .body("name", equalTo("Puppy The Guardian"))
                .body("status", equalTo("available"))
                .body("id", instanceOf(Integer.class))
                .body("name", instanceOf(String.class))
                .body("tags", is(empty()))  // tags alanının boş olmasını kontrol et
                .time(lessThan(3500L))  // Yanıt süresi kontrolü
                .header("Connection", containsString("keep-alive"))  // Response header doğrulaması
                .log().ifValidationFails()
                .log().body();
    }

    @Test(priority = 3, description = "Update an existing pet")
    public void testUpdatePet() {
        Pet updatedPet = new Pet(petId, "Flufy The Savage", "sold");

        getRequestSpecification()
                .body(updatedPet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)  // Content-Type doğrulaması
                .body("status", equalTo("sold"))
                .body("id", equalTo(petId))
                .body("name", equalTo("Flufy The Savage"))
                .body("id", instanceOf(Integer.class))
                .body("name", instanceOf(String.class))
                .time(lessThan(3500L))  // Yanıt süresi doğrulaması
                .header("Connection", containsString("keep-alive"))  // Response header doğrulaması
                .log().ifValidationFails()
                .log().body();
    }

    @Test(priority = 4, description = "Delete pet by ID")
    public void testDeletePet() {
        getRequestSpecification()
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(200)
                .time(lessThan(3500L))
                .log().ifValidationFails()
                .log().body();
    }

    // ---------- NEGATIVE TESTS ----------
    @Test(priority = 5, description = "Negative test: Get non-existing pet")
    public void testGetNonExistingPet() {
        int nonExistingPetId = 78909;

        getRequestSpecification()
                .pathParam("petId", nonExistingPetId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Pet not found"))
                .body("type", equalTo("error"))
                .time(lessThan(3500L))
                .log().ifValidationFails()
                .log().body();
    }

    @Test(priority = 6, description = "Negative test: Add pet with invalid body")
    public void testAddPetWithInvalidBody() {
        String invalidBody = "{invalidJson}";

        getRequestSpecification()
                .body(invalidBody)
                .when()
                .post("/pet")
                .then()
                .statusCode(400)
                .time(lessThan(3500L))
                .log().ifValidationFails()
                .log().body();
    }


}
