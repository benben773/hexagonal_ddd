package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartListRowEntity {
  @Id
  @Column( length = 16, nullable = false, unique = true )
  private UUID   id;
  private int    count;
  private String total;
}
