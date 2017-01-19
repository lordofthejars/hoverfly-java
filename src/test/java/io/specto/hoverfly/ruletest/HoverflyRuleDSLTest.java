package io.specto.hoverfly.ruletest;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.*;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class HoverflyRuleDSLTest {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode();

    private final RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setUp() throws Exception {

        hoverflyRule.simulate(dsl(
                service("www.my-test.com")

                        .post("/api/bookings").body("{\"flightId\": \"1\"}")
                        .willReturn(created("http://localhost/api/bookings/1"))

                        .get("/api/bookings/1")
                        .willReturn(success("{\"bookingId\":\"1\",\"origin\":\"London\",\"destination\":\"Singapore\",\"time\":\"2011-09-01T12:30\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/bookings/1\"}}}", "application/json")),

                service("www.other-anotherService.com")

                        .put("/api/bookings/1").body("{\"flightId\": \"1\", \"class\": \"PREMIUM\"}")
                        .willReturn(success())

                        .delete("/api/bookings/1")
                        .willReturn(noContent())

                        .get("/api/bookings")
                            .queryParam("class", "business", "premium")
                            .queryParam("destination", "new york")
                        .willReturn(success("{\"bookingId\":\"2\",\"origin\":\"London\",\"destination\":\"New York\",\"class\":\"BUSINESS\",\"time\":\"2011-09-01T12:30\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/bookings/2\"}}}", "application/json"))
                )
        );
    }

    @Test
    public void shouldBeAbleToAmendABookingUsingHoverfly() throws URISyntaxException {
        // Given
        final RequestEntity<String> bookFlightRequest = RequestEntity.put(new URI("http://www.other-anotherService.com/api/bookings/1"))
                .contentType(APPLICATION_JSON)
                .body("{\"flightId\": \"1\", \"class\": \"PREMIUM\"}");

        // When
        final ResponseEntity<String> bookFlightResponse = restTemplate.exchange(bookFlightRequest, String.class);

        // Then
        assertThat(bookFlightResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldBeAbleToDeleteBookingUsingHoverfly() throws Exception {
        // Given
        final RequestEntity<Void> bookFlightRequest = RequestEntity.delete(new URI("http://www.other-anotherService.com/api/bookings/1")).build();

        // When
        final ResponseEntity<Void> bookFlightResponse = restTemplate.exchange(bookFlightRequest, Void.class);

        // Then
        assertThat(bookFlightResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void shouldBeAbleToQueryBookingsUsingHoverfly() throws Exception {
        // When
        URI uri = UriComponentsBuilder.fromHttpUrl("http://www.other-anotherService.com")
                .path("/api/bookings")
                .queryParam("class", "business", "premium")
                .queryParam("destination", "new york")
                .build()
                .toUri();
        final ResponseEntity<String> getBookingResponse = restTemplate.getForEntity(uri, String.class);

        // Then
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(OK);
        assertThatJson(getBookingResponse.getBody()).isEqualTo("{" +
                "\"bookingId\":\"2\"," +
                "\"origin\":\"London\"," +
                "\"destination\":\"New York\"," +
                "\"class\":\"BUSINESS\"," +
                "\"time\":\"2011-09-01T12:30\"," +
                "\"_links\":{\"self\":{\"href\":\"http://localhost/api/bookings/2\"}}" +
                "}");

    }
}
