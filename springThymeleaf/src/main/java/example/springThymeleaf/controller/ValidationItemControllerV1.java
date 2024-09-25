package example.springThymeleaf.controller;

import example.springThymeleaf.object.Item;
import example.springThymeleaf.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class ValidationItemControllerV1 {
    private final ItemRepository itemRepository;

    @Autowired
    public ValidationItemControllerV1(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }


    @GetMapping(value="/validation/v1/items/{itemId}")
    public String validationV1(@PathVariable("itemId") Long itemId, Model model){
        log.info("validation V1");
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }
}
