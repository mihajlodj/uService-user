package ftn.userservice.domain.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ftn.userservice.domain.mappers.LocalDateTimeDeserializer;
import ftn.userservice.domain.mappers.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private UUID id;
    private UUID lodgeId;
    private UUID guestId;
    private UUID ownerId;
    private double price;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateFrom;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTo;
    private int numberOfGuests;
    private ReservationStatus status;

    public enum ReservationStatus {
        ACTIVE,
        CANCELED,
    }

}
