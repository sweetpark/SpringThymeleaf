package example.springThymeleaf.object;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class Item {
    private Long id;

    @NotBlank(message="공백은 입력할 수 없습니다.")
    private String itemName;

    @NotNull
    @Range(min=1000, max=1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    private Boolean open; // 판매 여부
    private List<String> regions; // 등록 지역
    private ItemType itemType; // 상품 종류
    private String deliveryCode;

    public Item(){}

    public Item(String itemName, int price, Integer quantity){
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
