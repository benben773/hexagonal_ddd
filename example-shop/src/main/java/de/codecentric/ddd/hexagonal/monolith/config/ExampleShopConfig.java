package de.codecentric.ddd.hexagonal.monolith.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ProductValidationService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ShoppingCartsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.order.persistence.OrderCrudRepository;
import de.codecentric.ddd.hexagonal.monolith.order.persistence.OrderPositionCrudRepository;
import de.codecentric.ddd.hexagonal.monolith.order.persistence.OrderRepositoryJpa;
import de.codecentric.ddd.hexagonal.monolith.product.persistence.ProductCrudRepository;
import de.codecentric.ddd.hexagonal.monolith.product.persistence.ProductRepositoryJpa;
import de.codecentric.ddd.hexagonal.monolith.shoppingcart.persistence.ShoppingCartCrudRepository;
import de.codecentric.ddd.hexagonal.monolith.shoppingcart.persistence.ShoppingCartItemCrudRepository;
import de.codecentric.ddd.hexagonal.monolith.shoppingcart.persistence.ShoppingCartRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExampleShopConfig {
  @Bean
  ProductsApi createProductApi( @Autowired final ProductCrudRepository jpaRepository ) {
    return new ProductsApiImpl( new ProductRepositoryJpa( jpaRepository ) );
  }

  @Bean
  OrdersApi createOrdersApi( @Autowired final OrderCrudRepository orderRepository,
                             @Autowired final OrderPositionCrudRepository positionsRepository ) {
    return new OrdersApiImpl( new OrderRepositoryJpa( orderRepository, positionsRepository ) );
  }

  @Bean
  ShoppingCartsApi createShoppingCartsApi( @Autowired final OrdersApi ordersApi,
                                           @Autowired final ProductsApi productsApi,
                                           @Autowired final ShoppingCartCrudRepository cartRepository,
                                           @Autowired final ShoppingCartItemCrudRepository cartItemRepository ) {
    return new ShoppingCartsApiImpl( new OrdersCheckoutPolicyService( ordersApi ),
                                     new ProductValidationService( productsApi ),
                                     new ShoppingCartRepositoryJpa( cartRepository, cartItemRepository ) );
  }

  @Bean
  @Primary
  public static ObjectMapper createObjectMapper() {
    ObjectMapper objectmapper = new ObjectMapper();
    objectmapper.setVisibility( FIELD, ANY );
    objectmapper.registerModule( new ParameterNamesModule() );
    objectmapper.registerModule( new PackagingTypeModule() );
    objectmapper.registerModule( new AmountModule() );
    objectmapper.registerModule( new MoneyModule() );
    return objectmapper;
  }
}
