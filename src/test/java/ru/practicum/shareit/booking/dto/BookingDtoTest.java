package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void dtoTest() throws IOException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusSeconds(1);
        BookingDto bookingDto = new BookingDto(1, start, end, 1L);

        JsonContent<BookingDto> resultDto = json.write(bookingDto);

        assertThat(resultDto).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(resultDto).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

}
