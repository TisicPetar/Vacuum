package rs.raf.demo.services;

import org.springframework.stereotype.Service;
import rs.raf.demo.entities.Status;
import rs.raf.demo.repositories.StatusRepository;

@Service
public class StatusService {
    private StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status getStatusByName(String statusName){
        return statusRepository.findByStatus(statusName);
    }
}
