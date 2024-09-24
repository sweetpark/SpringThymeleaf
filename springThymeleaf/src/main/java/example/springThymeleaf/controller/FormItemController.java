package example.springThymeleaf.controller;

import example.springThymeleaf.object.DeliveryCode;
import example.springThymeleaf.object.Item;
import example.springThymeleaf.object.ItemType;
import example.springThymeleaf.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/form/items")
public class FormItemController {

    private final ItemRepository itemRepository;

    public FormItemController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping("add")
    public String addForm(Model model){
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("add")
    public String add(@ModelAttribute("item")Item item, Model model){
        itemRepository.save(item);
        log.info("item.open={}", item.getOpen());
        model.addAttribute("item", item);
        return "redirect:/form/items/"+item.getId();
    }

    @GetMapping("{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable("itemId") Long id, @ModelAttribute("item")Item item, Model model){
        itemRepository.update(id, item);
        model.addAttribute("item", item);
        return "redirect:/form/items/"+item.getId();
    }

    @GetMapping("{itemId}")
    public String detail(@PathVariable("itemId") Long id, Model model){
        Item item = itemRepository.findById(id);
        model.addAttribute("item",item);
        return "form/item";
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @ModelAttribute("regions")
    public Map<String, String> regions(){
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemType")
    public ItemType[] itemTypes(){
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST","빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL","보통 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW","느린 배송"));
        return deliveryCodes;
    }

    @PostConstruct
    public void init(){
        Item item1 = new Item("itemA",10000,1000);
        Item item2 = new Item("itemB",20000,2000);

        itemRepository.save(item1);
        itemRepository.save(item2);


    }
}
