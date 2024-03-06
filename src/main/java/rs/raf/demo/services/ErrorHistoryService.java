package rs.raf.demo.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.raf.demo.entities.ErrorHistory;
import rs.raf.demo.entities.User;
import rs.raf.demo.repositories.ErrorHistoryRepository;

import java.util.List;

@Service
public class ErrorHistoryService {
    private ErrorHistoryRepository errorHistoryRepository;

    public ErrorHistoryService(ErrorHistoryRepository errorHistoryRepository) {
        this.errorHistoryRepository = errorHistoryRepository;
    }

    public List<ErrorHistory> paginate(Integer page, Integer size,User user){
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        return errorHistoryRepository.findByUser(pageable, user);
    }
}
