package au.sydney.soft3202.task1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

class Item extends RepresentationModel<Item>  {
    public Integer id;
    public String name;
    public String user;
    public int count;
    public Double cost;
    public Item(int id, String name, String user, int count, Double cost) {
           this.id = id;
           this.name = name;
           this.user = user;
           this.count = count;
           this.cost = cost;
    }
}

class BasketItem  {
    public String name;
    public int count;

    public BasketItem(String name, int count) {
        this.name = name;
        this.count = count;
    }
}


@RestController
@RequestMapping("/api/shop")
public class ShoppingController {

    Map<String, Double> costs = new HashMap<String, Double>();

    @Autowired
    private ShoppingBasketRepository shoppingBasket;

    @GetMapping("viewall")
    public List<Item> getBasket() {
        List<ShoppingBasket> fs = shoppingBasket.findAll();
        List<Item> bs = new LinkedList<Item>();
        for (ShoppingBasket f : fs) {
            if (!costs.containsKey(f.getName())) {
                costs.put(f.getName(), 0.00);
            }
            bs.add(new Item(f.getId(), f.getName(), f.getUser(), f.getCount(), costs.get(f.getName())));
        }
        return bs;
    }

    @GetMapping("users")
    public List<String> getUsers() {
        List<ShoppingBasket> fs = shoppingBasket.findAll();
        List<String> users = new LinkedList<String>();
        for (ShoppingBasket f : fs) {
            if (!users.contains(f.getUser())){
                users.add(f.getUser());

            }
        }

        return users;
    }

    @GetMapping("costs")
    public Map<String, Double> getCosts() {
        return this.costs;
    }

    @GetMapping("/users/{username}")
    public List<BasketItem> getUserByUsername(@PathVariable String username) {
        List<ShoppingBasket> fs = shoppingBasket.findAll();
        List<BasketItem> bs = new LinkedList<BasketItem>();
        for (ShoppingBasket f : fs) {
            if (f.getUser().equals(username)) {
                bs.add(new BasketItem(f.getName(), f.getCount()));
            }
        }
        return bs;
    }

    @GetMapping("/users/{username}/total")
    public Double getTotalCostForUser(@PathVariable String username) {
        Double total = 0.0;
        List<ShoppingBasket> fs = shoppingBasket.findAll();
        for (ShoppingBasket f : fs) {
            if (f.getUser().equals(username)){
                try {
                    total += f.getCount() * costs.get(f.getName());
                } catch (Exception e ){
                    continue;
                }

            }
        }
        return total;
    }

    @PostMapping("/users/{username}/add")
    public String addItem(@PathVariable String username, @RequestBody Map<String, Object> newItem) {
        try {
            String name = (String) newItem.get("name");
            Integer count = Integer.valueOf((String) newItem.get("count"));

            boolean updated = false;
            List<ShoppingBasket> fs = shoppingBasket.findAll();
            for (ShoppingBasket f : fs) {
                if (f.getUser().equals(username) && f.getName().equals(name)){
                    f.setCount(count + f.getCount());
                    shoppingBasket.save(f);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                ShoppingBasket newItemSb = new ShoppingBasket();
                newItemSb.setName(name);
                newItemSb.setCount(count);
                newItemSb.setUser(username);
                shoppingBasket.save(newItemSb);
            }

            return "Successfully added " + name + " to " + username + "'s basket.";
        } catch (Exception e) {
            return "An error occurred try again";
        }

    }

    @PutMapping("/users/{username}/basket/{name}")
    public String editBasket(@PathVariable String username, @PathVariable String name, @RequestBody Map<String, Object> newCount) {
        try {
            Integer count = Integer.valueOf((String) newCount.get("count"));
            List<ShoppingBasket> fs = shoppingBasket.findAll();
            for (ShoppingBasket f : fs) {
                if (f.getName().equals(name) && f.getUser().equals(username)) {
                    f.setCount(count);
                    shoppingBasket.save(f);
                    return "Successfully updated " + name + " for " + username;
                }
            }
            return "Username or item does not exist";
        } catch (Exception e) {
            return "An error occurred try again";
        }
    }

    @DeleteMapping("/users/{username}")
    public String deleteBasket(@PathVariable String username) {
        try {
            List<ShoppingBasket> fs = shoppingBasket.findAll();
            List<ShoppingBasket> toDelete = new LinkedList<ShoppingBasket>();
            for (ShoppingBasket f : fs) {
                if (f.getUser().equals(username)) {
                    toDelete.add(f);
                }
            }
            if (!toDelete.isEmpty()) {
                shoppingBasket.deleteAll(toDelete);
                return "Successfully deleted " + username;
            } else {
                return "No such user";
            }
        } catch (Exception e) {
            return "An error occurred try again";
        }
    }

    @DeleteMapping("/users/{username}/basket/{name}")
    public String deleteItem(@PathVariable String username, @PathVariable String name) {
        try {
            List<ShoppingBasket> fs = shoppingBasket.findAll();
            ShoppingBasket toDelete = null;
            for (ShoppingBasket f : fs) {
                if (f.getUser().equals(username) && f.getName().equals(name)) {
                    toDelete = f;
                    break;
                }
            }
            if (toDelete != null) {
                shoppingBasket.delete(toDelete);
                return "Successfully deleted " + username;
            } else {
                return "No such user or item";
            }

        } catch (Exception e) {
            return "An error occurred try again";
        }
    }

    @PostMapping("/costs")
    public String addItemCost(@RequestBody Map<String, Object> newCost) {
        try {
            String name = (String) newCost.get("name");
            Double cost = Double.valueOf((String) newCost.get("cost"));


            this.costs.put(name, cost);
            return "Successfully added " + name;
        } catch (Exception e) {
            return "An error occurred try again";
        }

    }

    @PutMapping("/costs/{itemName}")
    public String updateItemCost(@PathVariable String itemName, @RequestBody Map<String, Object> newCost) {
        try {

            String name = (String) newCost.get("name");
            Double cost = Double.valueOf((String) newCost.get("cost"));

            if (!this.costs.containsKey(itemName)) {
                throw new IllegalArgumentException("Name Does Not Exist");
            }

            this.costs.put(itemName, cost);
            return "Successfully updated " + name;
        } catch (Exception e) {
            return e.getMessage();
        }

    }










}
