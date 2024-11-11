package jpabook.jpashop.repository;

import jakarta.persistence.GeneratedValue;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String memberName; // 회원 이름
    private OrderStatus orderState; // 주문 상태(ORDER, CANCEL)

}
