package com.polarbookshop.orderservice.book;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

class BookClientTests {

	private MockWebServer mockWebServer;
	private BookClient bookClient;

	@BeforeEach
	void setup() throws IOException {
		this.mockWebServer = new MockWebServer();
		this.mockWebServer.start();
		
		BookClientProperties bookClientProperties = new BookClientProperties();
		bookClientProperties.setCatalogServiceUrl(mockWebServer.url("/").uri());
		this.bookClient = new BookClient(bookClientProperties, WebClient.builder());
	}

	@AfterEach
	void clean() throws IOException {
		this.mockWebServer.shutdown();
	}

	@Test
	void whenBookExistsThenReturnBook() {
		String bookIsbn = "1234567890";

		MockResponse mockResponse = new MockResponse()
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setBody("{\"isbn\":" + bookIsbn + ", "
								+ "\"title\": \"Title\", "
								+ "\"author\": \"Author\", "
								+ "\"price\": 9.90, "
								+ "\"publisher\": \"Polarsophia\"}");
		mockWebServer.enqueue(mockResponse);

		Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);
		StepVerifier.create(book)
				.expectNextMatches(b -> b.getIsbn().equals(bookIsbn) && b.getTitle().equals("Title"))
				.verifyComplete();
	}

	@Test
	void whenBookNotExistsThenReturnEmpty() {
		String bookIsbn = "1234567891";

		MockResponse mockResponse = new MockResponse()
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setResponseCode(404);

		mockWebServer.enqueue(mockResponse);

		StepVerifier.create(bookClient.getBookByIsbn(bookIsbn))
				.expectNextCount(0)
				.verifyComplete();
	}
}