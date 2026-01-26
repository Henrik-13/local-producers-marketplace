package edu.bbte.localproducersmarketplace.dto.in;


import edu.bbte.localproducersmarketplace.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateDTO {

    @NotNull(message = "Status must not be null.")
    private OrderStatus status;
}
