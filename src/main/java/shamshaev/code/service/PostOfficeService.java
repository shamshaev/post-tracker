package shamshaev.code.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shamshaev.code.dto.PostOfficeCreateDTO;
import shamshaev.code.dto.PostOfficeDTO;
import shamshaev.code.dto.PostOfficeUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.PostOfficeMapper;
import shamshaev.code.repository.PostOfficeRepository;

@Service
@AllArgsConstructor
public class PostOfficeService {
    private final PostOfficeRepository postOfficeRepository;
    private final PostOfficeMapper postOfficeMapper;

    public Page<PostOfficeDTO> getAll(int page, int size) {
        var postOffices = postOfficeRepository.findAll(PageRequest.of(page - 1, size));

        return postOffices.map(postOfficeMapper::map);
    }

    public PostOfficeDTO findById(Long id) {
        var postOffice = postOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostOffice with id " + id + " not found"));

        return postOfficeMapper.map(postOffice);
    }

    public PostOfficeDTO create(PostOfficeCreateDTO postOfficeData) {
        var postOffice = postOfficeMapper.map(postOfficeData);
        postOfficeRepository.save(postOffice);

        return postOfficeMapper.map(postOffice);
    }

    public PostOfficeDTO update(PostOfficeUpdateDTO postOfficeData, Long id) {
        var postOffice = postOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostOffice with id " + id + " not found"));
        postOfficeMapper.update(postOfficeData, postOffice);
        postOfficeRepository.save(postOffice);

        return postOfficeMapper.map(postOffice);
    }

    public void delete(Long id) {
        postOfficeRepository.deleteById(id);
    }
}
