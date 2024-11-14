package shamshaev.code.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shamshaev.code.dto.TrackStatusCreateDTO;
import shamshaev.code.dto.TrackStatusDTO;
import shamshaev.code.dto.TrackStatusUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.mapper.TrackStatusMapper;
import shamshaev.code.repository.TrackStatusRepository;

@Service
@AllArgsConstructor
public class TrackStatusService {
    private final TrackStatusRepository trackStatusRepository;
    private final TrackStatusMapper trackStatusMapper;

    public Page<TrackStatusDTO> getAll(int page, int size) {
        var trackStatuses = trackStatusRepository.findAll(PageRequest.of(page - 1, size));

        return trackStatuses.map(trackStatusMapper::map);
    }

    public TrackStatusDTO findById(Long id) {
        var trackStatus = trackStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found"));

        return trackStatusMapper.map(trackStatus);
    }

    public TrackStatusDTO create(TrackStatusCreateDTO trackStatusData) {
        var trackStatus = trackStatusMapper.map(trackStatusData);
        trackStatusRepository.save(trackStatus);

        return trackStatusMapper.map(trackStatus);
    }

    public TrackStatusDTO update(TrackStatusUpdateDTO trackStatusData, Long id) {
        var trackStatus = trackStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " not found"));
        trackStatusMapper.update(trackStatusData, trackStatus);
        trackStatusRepository.save(trackStatus);

        return trackStatusMapper.map(trackStatus);
    }

    public void delete(Long id) {
        trackStatusRepository.deleteById(id);
    }
}
