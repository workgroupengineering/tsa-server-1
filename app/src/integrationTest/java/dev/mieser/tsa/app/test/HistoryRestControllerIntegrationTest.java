package dev.mieser.tsa.app.test;

import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mieser.tsa.web.dto.datatable.Column;
import dev.mieser.tsa.web.dto.datatable.DatatablesPagingRequest;
import dev.mieser.tsa.web.dto.datatable.Order;
import dev.mieser.tsa.web.dto.datatable.SortDirection;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HistoryRestControllerIntegrationTest {

    private final int serverPort;

    private final ObjectMapper objectMapper;

    HistoryRestControllerIntegrationTest(@LocalServerPort int serverPort) {
        this.serverPort = serverPort;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void returnsResponseHistoryInExpectedOrder() throws Exception {
        // given
        DatatablesPagingRequest pagingRequest = new DatatablesPagingRequest(1, 0, 100, List.of(new Order(0, SortDirection.DESC)),
            List.of(new Column("serialNumber", "serialNumber")));

        // when / then
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pagingRequest))
            .when()
            .post("/api/history")
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .rootPath("data")
            .body("size()", is(5))
            .body("id", is(List.of(4, 5, 3, 2, 1)));
    }

    @Test
    void returnsResponseHistoryWithExpectedPage() throws Exception {
        // given
        DatatablesPagingRequest pagingRequest = new DatatablesPagingRequest(1337, 2, 1, emptyList(),
            List.of(new Column("id", "id")));

        // when / then
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pagingRequest))
            .when()
            .post("/api/history")
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .body("draw", is(1337))
            .body("recordsTotal", is(5))
            .body("recordsFiltered", is(5))
            .body("data.size()", is(1))
            .body("data.id", is(List.of(3)));
    }

    @Test
    void serialNumbersAreRepresentedAsUppercaseHexStrings() throws Exception {
        // given
        DatatablesPagingRequest pagingRequest = new DatatablesPagingRequest(0, 0, 1, List.of(new Order(0, SortDirection.ASC)),
            List.of(new Column("id", "id")));

        // when / then
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pagingRequest))
            .when()
            .post("/api/history")
            .then()
            .assertThat()
            .statusCode(SC_OK)
            .rootPath("data")
            .body("serialNumber", is(List.of("A13")));
    }

}
