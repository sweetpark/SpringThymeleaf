package example.springThymeleaf.controller;


import example.springThymeleaf.object.*;
import example.springThymeleaf.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation/v4/items")
public class ValidationItemControllerV4 {
    private final ItemRepository itemRepository;

    @Autowired
    public ValidationItemControllerV4(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping("/add")
    public String addForm(Model model){
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors",errors);
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    // validator 호출x , annotaion 사용
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //Object error (@scriptAssert 대신해서 java로 표현하는게 더 직관적임)
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }

        //검증에 걸렸으면, 다시 입력폼으로 이동 (오류가 있으면)
        if(bindingResult.hasErrors()){
            return "validation/v4/addForm";
        }

        //성공 로직
        Item item = new Item(form.getItemName(), form.getPrice(), form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v4/items/{itemId}";

    }

    @GetMapping("{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable("itemId") Long id, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult){

        //Object error (@scriptAssert 대신해서 java로 표현하는게 더 직관적임)
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }

        //검증에 걸렸으면, 다시 입력폼으로 이동 (오류가 있으면)
        if(bindingResult.hasErrors()){
            return "validation/v4/editForm";
        }

        //성공 로직
        Item itemParam = new Item(form.getItemName(), form.getPrice(), form.getQuantity());
        itemRepository.update(id, itemParam);

        return "redirect:/validation/v4/items/{itemId}";

    }

    @GetMapping("{itemId}")
    public String detail(@PathVariable("itemId") Long id, Model model){
        Item item = itemRepository.findById(id);
        model.addAttribute("item",item);
        return "validation/v4/item";
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }




    @PostConstruct
    public void init(){
        Item item1 = new Item("itemC",20000,3000);
        Item item2 = new Item("itemD",30000,4000);

        itemRepository.save(item1);
        itemRepository.save(item2);


    }
}
