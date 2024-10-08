package example.springThymeleaf.controller;

import example.springThymeleaf.object.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    //HTTP API 검증 처리
    @PostMapping("add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return bindingResult.getAllErrors();
        }

        return form;
    }
}
