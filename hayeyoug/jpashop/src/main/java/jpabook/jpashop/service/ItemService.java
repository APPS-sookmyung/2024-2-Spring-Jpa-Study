package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    @Transactional // 위에서 readOnly=true라고 했지만 여기에 애노테이션을 붙이면 메소드에 붙은 것이 더 우선권을 가짐
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() { // transactional 애노테이션 안 걸어 놨으니 클래스에 달아놓은게 적용됨 (readOnly=true)
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
