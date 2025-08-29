package com.techie.microservices.order_service;

import com.techie.microservices.order_service.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Testcontainers
class OrderServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

	@LocalServerPort
	private int port;

	// Mahazo ny port WireMock avy amin'ny Spring property
	@Value("${wiremock.server.port}")
	private int wiremockPort;

	static {
		postgreSQLContainer.start();
	}

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;

		// Override ny inventory.url amin'ny port WireMock
		System.setProperty("inventory.url", "http://localhost:" + wiremockPort);
	}

	@Test
	void shouldPlaceOrder() {
		String requestBody = """
        {
          "id": "1",
          "orderNumber": "ignored",
          "skuCode": "iphone_15",
          "price": 1000,
          "quantity": 100
        }
        """;

		InventoryClientStub.stubInventoryCall("iphone_15", 100);

		RestAssured.given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201)
				.body(Matchers.equalTo("Order Placed Successfully"));
	}
}
