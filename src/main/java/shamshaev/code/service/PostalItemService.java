package shamshaev.code.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shamshaev.code.dto.PostalItemCreateDTO;
import shamshaev.code.dto.PostalItemDTO;
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.PostalItemMapper;
import shamshaev.code.repository.PostalItemRepository;

@Service
@AllArgsConstructor
public class PostalItemService {
    private final PostalItemRepository postalItemRepository;
    private final PostalItemMapper postalItemMapper;

    public Page<PostalItemDTO> getAll(int page, int size) {
        var postalItems = postalItemRepository.findAll(PageRequest.of(page - 1, size));

        return postalItems.map(postalItemMapper::map);
    }

    public PostalItemDTO findById(Long id) {
        var postalItem = postalItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostalItem with id " + id + " not found"));

        return postalItemMapper.map(postalItem);
    }

    public PostalItemDTO create(PostalItemCreateDTO postalItemData) {
        var postalItem = postalItemMapper.map(postalItemData);
        postalItemRepository.save(postalItem);

        return postalItemMapper.map(postalItem);
    }

    public PostalItemDTO update(PostalItemUpdateDTO postalItemData, Long id) {
        var postalItem = postalItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostalItem with id " + id + " not found"));
        postalItemMapper.update(postalItemData, postalItem);
        postalItemRepository.save(postalItem);

        return postalItemMapper.map(postalItem);
    }

    public void delete(Long id) {
        postalItemRepository.deleteById(id);
    }
}
