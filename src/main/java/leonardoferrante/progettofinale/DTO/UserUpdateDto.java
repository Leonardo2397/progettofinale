package leonardoferrante.progettofinale.DTO;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
