package cz.muni.fi.airportmanager.passengerservice.repository;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.entity.Passenger;
import cz.muni.fi.airportmanager.passengerservice.model.NotificationDto;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class PassengerRepositoryTest {

    @Inject
    PassengerRepository passengerRepository;

    private Passenger createTestPassenger() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setEmail("johndoe@example.com");
        passenger.setFlightId(1L);
        return passenger;
    }

    private Notification createTestNotification() {
        Notification notification = new Notification();
        notification.message = "Test notification message";
        return notification;
    }

    @Test
    @TestReactiveTransaction
    void shouldFindNotificationsForPassenger(UniAsserter asserter) {
        // TODO implement this test
        // create a passenger and a notification, add the notification to the passenger and test if the notification is found
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> {
                    passenger.addNotification(notification);
                    return passengerRepository.persist(passenger);
                })
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> {
                            assertEquals(1, notifications.size());
                            assertEquals(notification.message, notifications.get(0).message);
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldAddNotificationByFlightId(UniAsserter asserter) {
        // TODO implement this test
        // It should test that the notification is added to the appropriate passengers
        // call addNotificationByFlightId and then check if the notification is present in the passenger's notifications
        // using findNotificationsForPassenger method
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> passengerRepository.addNotificationByFlightId(passenger.getFlightId(), notification))
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> assertTrue(notifications.stream().anyMatch(n -> n.message.equals(notification.message)))
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldFindPassengersForFlight(UniAsserter asserter) {
        // TODO implement this test
        // Persist a passenger and then get the its record by flight id
        // test findPassengersForFlight method

        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        () -> passengerRepository.findPassengersForFlight(passenger.getFlightId()),
                        passengers -> {
                            assertEquals(1, passengers.size());
                            assertEquals(passenger.getId(), passengers.get(0).getId());
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldFindNotificationsWithEmail(UniAsserter asserter) {
        // TODO implement this test
        // this test should find all notifications with the email of the passenger
        // test findNotificationsWithEmail method
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();
        passenger.addNotification(notification);

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        passengerRepository::findNotificationsWithEmail,
                        notificationDtos -> {
                            assertEquals(1, notificationDtos.size());
                            NotificationDto dto = notificationDtos.get(0);
                            assertEquals(notification.message, dto.message);
                            assertEquals(passenger.getEmail(), dto.email);
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleNoNotificationsForPassenger(UniAsserter asserter) {
        // TODO implement this test
        // test findNotificationsForPassenger method
        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> assertEquals(0, notifications.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleInvalidPassengerIdForNotifications(UniAsserter asserter) {
        // TODO implement this test
        // test findNotificationsForPassenger method
        asserter.assertThat(
                () -> passengerRepository.findNotificationsForPassenger(-1L),
                notifications -> assertEquals(0, notifications.size())
        );
    }

    @Test
    @TestReactiveTransaction
    void shouldNotAddNotificationToNonExistentFlight(UniAsserter asserter) {
        // TODO implement this test
        // test addNotificationByFlightId method
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.addNotificationByFlightId(-1L, notification))
                .assertThat(
                        () -> passengerRepository.findNotificationsWithEmail(),
                        notifications -> assertEquals(0, notifications.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleNoPassengersForFlight(UniAsserter asserter) {
        // TODO implement this test
        // test findPassengersForFlight method
        asserter
                .assertThat(
                        () -> passengerRepository.findPassengersForFlight(-1L),
                        passengers -> assertEquals(0, passengers.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleEmptyPassengerRepositoryForNotificationsWithEmail(UniAsserter asserter) {
        // TODO implement this test
        // test findNotificationsWithEmail method
        asserter
                .assertThat(
                        passengerRepository::findNotificationsWithEmail,
                        notifications -> assertEquals(0, notifications.size())
                );
    }


    @Test
    @TestReactiveTransaction
    void shouldNotFindNonExistentPassenger(UniAsserter asserter) {
        // TODO implement this test
        // test findById method
        asserter
                .assertThat(
                        () -> passengerRepository.findById(-1L),
                        Assertions::assertNull
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldDeletePassenger(UniAsserter asserter) {
        // TODO implement this test
        // test deleteById method
        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> passengerRepository.deleteById(passenger.getId()))
                .assertThat(
                        () -> passengerRepository.findById(passenger.getId()),
                        Assertions::assertNull
                );
    }
}
