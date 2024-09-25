package example.springThymeleaf.controller;

import example.springThymeleaf.object.DeliveryCode;
import example.springThymeleaf.object.Item;
import example.springThymeleaf.object.ItemType;
import example.springThymeleaf.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Slf4j
@Controller
@RequestMapping("/form/items")
public class FormItemController {

    private final ItemRepository itemRepository;

    public FormItemController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

//    @GetMapping("add")
//    public String addForm(Model model){
//        model.addAttribute("item", new Item());
//        return "form/addForm";
//    }

    @GetMapping("add")
    public String addForm(Model model){
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors",errors);
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }
//    @PostMapping("add")
//    public String add(@ModelAttribute("item")Item item, Model model){
//        itemRepository.save(item);
//        log.info("item.open={}", item.getOpen());
//        model.addAttribute("item", item);
//        return "redirect:/form/items/"+item.getId();
//    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model){

        // 검증 오류 보관
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다");
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999){
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = "+ resultPrice);
            }
        }

        //검증에 걸렸으면, 다시 입력폼으로 이동 (오류가 있으면)
        if(!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v1/items/{itemId}";

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


    /***
     * UI 버튼관련 ModelAttribute
     * select
     * 단일/다중
     * Radio
     */
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
