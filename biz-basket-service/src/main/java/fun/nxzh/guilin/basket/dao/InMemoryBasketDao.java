package fun.nxzh.guilin.basket.dao;

import fun.nxzh.guilin.basket.model.Basket;
import fun.nxzh.guilin.basket.model.BasketItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBasketDao implements BasketDao {

  private Map<String, Basket> basketMap = new ConcurrentHashMap<>();

  public InMemoryBasketDao() {
    Basket basket1 = new Basket();
    basket1.setBuyerId("12345");
    basket1.setItems(new ArrayList<BasketItem>() {{
      add(new BasketItem("BSKT-ITEM-1", "PRD-1", "Spring T-Shirt 01", new BigDecimal(20.00), new BigDecimal(20.00), 1));
      add(new BasketItem("BSKT-ITEM-2", "PRD-2", "Spring T-Shirt 02", new BigDecimal(25.00), new BigDecimal(25.00), 2));
    }});
    basketMap.put("12345", basket1);

    Basket basket2 = new Basket();
    basket2.setBuyerId("23456");
    basket2.setItems(new ArrayList<BasketItem>() {{
      add(new BasketItem("BSKT-ITEM-2", "PRD-2", "Spring T-Shirt 02", new BigDecimal(25.00), new BigDecimal(25.00), 3));
      add(new BasketItem("BSKT-ITEM-3", "PRD-3", "Spring T-Shirt 03", new BigDecimal(30.00), new BigDecimal(30.00), 4));
    }});
    basketMap.put("23456", basket2);
  }

  @Override
  public Basket findBasketByUserId(String userId) {
    return basketMap.get(userId);
  }
}
