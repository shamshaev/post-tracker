package shamshaev.code.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shamshaev.code.dto.StatusCreateDTO;
import shamshaev.code.dto.StatusDTO;
import shamshaev.code.dto.StatusUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.StatusMapper;
import shamshaev.code.repository.StatusRepository;

@Service
@AllArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    public Page<StatusDTO> getAll(int page, int size) {
        var statuses = statusRepository.findAll(PageRequest.of(page - 1, size));

        return statuses.map(statusMapper::map);
    }

    public StatusDTO findById(Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found"));

        return statusMapper.map(status);
    }

    public StatusDTO create(StatusCreateDTO statusData) {
        var status = statusMapper.map(statusData);
        statusRepository.save(status);

        return statusMapper.map(status);
    }

    public StatusDTO update(StatusUpdateDTO statusData, Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found"));
        statusMapper.update(statusData, status);
        statusRepository.save(status);

        return statusMapper.map(status);
    }

    public void delete(Long id) {
        statusRepository.deleteById(id);
    }
}
