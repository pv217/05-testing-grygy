package cz.muni.fi.airportmanager.passengerservice.resource;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.entity.Passenger;
import cz.muni.fi.airportmanager.passengerservice.model.CreatePassengerDto;
import cz.muni.fi.airportmanager.passengerservice.service.PassengerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(PassengerResource.class)
class PassengerResourceTest {

    @InjectMock
    PassengerService passengerService;

    @Test
    void shouldGetEmptyListOfPassengers() {
        // TODO implement this test
        // mock the passengerService.listAll() method to return an empty list
        Mockito.when(this.passengerService.listAll()).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldGetListOfPassengers() {
        // TODO implement this test
        // mock the passengerService.listAll() method to return a list with one passenger
        Mockito.when(this.passengerService.listAll()).thenReturn(Uni.createFrom().item(List.of(createPassenger())));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void shouldCreatePassenger() {
        // TODO implement this test
        // mock the passengerService.createPassenger() method to return a passenger with ID 1
        CreatePassengerDto testPassenger = createTestPassengerDto();
        Passenger responsePassenger = Passenger.fromDto(testPassenger);
        responsePassenger.setId(1L);
        Mockito.when(this.passengerService.createPassenger(Mockito.any(CreatePassengerDto.class))).thenReturn(Uni.createFrom().item(responsePassenger));


        given().contentType("application/json")
                .body(testPassenger)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("email", equalTo(responsePassenger.getEmail()))
                .body("id", equalTo(responsePassenger.getId().intValue()));
    }

    @Test
    void shouldGetExistingPassenger() {
        // TODO implement this test
        // mock the passengerService.getPassenger() method to return a passenger with ID 1
        Passenger testPassenger = createPassenger();
        Mockito.when(this.passengerService.getPassenger(testPassenger.getId())).thenReturn(Uni.createFrom().item(testPassenger));

        given().when()
                .get("/" + testPassenger.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(testPassenger.getId().intValue()));
    }

    @Test
    void shouldNotGetNonexistingPassenger() {
        // TODO implement this test
        // mock the passengerService.getPassenger() method to return null
        Mockito.when(this.passengerService.getPassenger(Mockito.anyLong())).thenReturn(Uni.createFrom().nullItem());

        given().when()
                .get("/99") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteExistingPassenger() {
        // TODO implement this test
        // mock the passengerService.deletePassenger() method to return true
        Passenger testPassenger = createPassenger();
        Mockito.when(this.passengerService.deletePassenger(testPassenger.getId())).thenReturn(Uni.createFrom().item(true));

        given().when()
                .delete("/" + testPassenger.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotDeleteNonexistingPassenger() {
        // TODO implement this test
        // mock the passengerService.deletePassenger() method to return false
        Mockito.when(this.passengerService.deletePassenger(Mockito.anyLong())).thenReturn(Uni.createFrom().item(false));

        given().when()
                .delete("/99") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }


    @Test
    void shouldDeleteAllPassengers() {
        // TODO implement this test
        // mock the passengerService.deleteAllPassengers() method to return 0
        Mockito.when(this.passengerService.deleteAllPassengers()).thenReturn(Uni.createFrom().item(0L));

        given().when()
                .delete()
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotFindPassengersForNonexistentFlight() {
        // TODO implement this test
        // mock the passengerService.getPassengersForFlight() method to return an empty list
        Mockito.when(this.passengerService.getPassengersForFlight(Mockito.anyLong())).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get("/flight/99")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }


    @Test
    void shouldGetPassengersForFlight() {
        // TODO implement this test
        // mock the passengerService.getPassengersForFlight() method to return a list with one passenger
        Long flightId = 1L;
        List<Passenger> passengers = List.of(createPassenger());
        Mockito.when(passengerService.getPassengersForFlight(flightId)).thenReturn(Uni.createFrom().item(passengers));

        given().when()
                .get("/flight/" + flightId)
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", equalTo(passengers.get(0).getId().intValue()))
                .body("[0].email", equalTo(passengers.get(0).getEmail()));
    }

    @Test
    void shouldGetEmptyListOfPassengersForFlightWhenNoPassengers() {
        // TODO implement this test
        // mock the passengerService.getPassengersForFlight() method to return an empty list
        Long flightId = 1L;
        Mockito.when(passengerService.getPassengersForFlight(flightId)).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get("/flight/" + flightId)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldGetNotificationsForPassenger() {
        // TODO implement this test
        // mock the passengerService.findNotificationsForPassenger() method to return a list with one notification
        Long passengerId = 1L;
        List<Notification> notifications = List.of(createNotification());
        Mockito.when(passengerService.findNotificationsForPassenger(passengerId)).thenReturn(Uni.createFrom().item(notifications));

        given().when()
                .get("/" + passengerId + "/notifications")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].message", equalTo(notifications.get(0).message));
    }

    @Test
    void shouldGetEmptyListOfNotificationsForPassengerWhenNoNotifications() {
        // TODO implement this test
        // mock the passengerService.findNotificationsForPassenger() method to return an empty list
        Long passengerId = 1L;
        Mockito.when(passengerService.findNotificationsForPassenger(passengerId)).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get("/" + passengerId + "/notifications")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldNotFindNotificationsForNonexistentPassenger() {
        // TODO implement this test
        // mock the passengerService.findNotificationsForPassenger() method to return an empty list
        Long invalidPassengerId = 99L;
        Mockito.when(passengerService.findNotificationsForPassenger(invalidPassengerId)).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get("/" + invalidPassengerId + "/notifications")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    // Helper methods
    private Notification createNotification() {
        Notification notification = new Notification();
        notification.message = "Test notification";
        return notification;
    }


    private Passenger createPassenger() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setNotifications(Collections.emptyList());
        passenger.setEmail("johndoe@gmail.com");
        passenger.setFlightId(1L);
        return passenger;
    }

    private CreatePassengerDto createTestPassengerDto() {
        CreatePassengerDto passengerDto = new CreatePassengerDto();
        passengerDto.firstName = "John";
        passengerDto.lastName = "Doe";
        passengerDto.flightId = 1L;
        passengerDto.email = "john@gmail.com";
        return passengerDto;
    }
}

