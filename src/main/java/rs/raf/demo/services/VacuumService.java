package rs.raf.demo.services;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.demo.entities.ErrorHistory;
import rs.raf.demo.entities.Status;
import rs.raf.demo.entities.User;
import rs.raf.demo.entities.Vacuum;
import rs.raf.demo.repositories.ErrorHistoryRepository;
import rs.raf.demo.repositories.VacuumRepository;

import java.util.*;

@Service
public class VacuumService {
    private VacuumRepository vacuumRepository;
    private StatusService statusService;
    private ErrorHistoryRepository errorHistoryRepository;
    private TaskScheduler taskScheduler;

    public VacuumService(VacuumRepository vacuumRepository, StatusService statusService, ErrorHistoryRepository errorHistoryRepository, TaskScheduler taskScheduler) {
        this.vacuumRepository = vacuumRepository;
        this.statusService = statusService;
        this.errorHistoryRepository = errorHistoryRepository;
        this.taskScheduler = taskScheduler;
    }

    public Vacuum addVacuum(Vacuum vacuum){
        return vacuumRepository.save(vacuum);
    }

    public List<Vacuum> getAllVacuums(User addedByUser){
        return vacuumRepository.findByAddedByUserAndActive(addedByUser, true);
    }

    public Vacuum getVacuumById(Long id){
        Optional<Vacuum> vacuumOptional = vacuumRepository.findById(id);
        if (vacuumOptional.isPresent()){
            return vacuumOptional.get();
        }
        return null;
    }

    public List<Vacuum> findVacuums(String name, Status status, Long dateFrom, Long dateTo, User u) {
        List<Vacuum> returnList = new ArrayList<>();
        if(name == null && status == null && dateFrom == null && dateTo == null){
            return returnList;
        }

        if (name != null && status != null && dateFrom != null && dateTo != null) {
            return vacuumRepository.findByAddedByUserAndNameContainingAndStatusAndCreationDateBetweenAndActive(u, name, status, dateFrom, dateTo, true);
        }

        List<Vacuum> nameList = new ArrayList<>();
        List<Vacuum> statusList = new ArrayList<>();
        List<Vacuum> dateList = new ArrayList<>();

        if (name != null){
            nameList = vacuumRepository.findByAddedByUserAndNameContainingAndActive(u, name, true);
        }
        if (status != null){
            statusList = vacuumRepository.findByAddedByUserAndStatusAndActive(u, status, true);
        }
        if (dateFrom != null && dateTo != null){
            dateList = vacuumRepository.findByAddedByUserAndCreationDateBetweenAndActive(u, dateFrom, dateTo, true);
        }else if(dateFrom != null){
            dateList = vacuumRepository.findByAddedByUserAndCreationDateAfterAndActive(u, dateFrom, true);
        }else if(dateTo != null){
            dateList = vacuumRepository.findByAddedByUserAndCreationDateBeforeAndActive(u, dateTo, true);
        }

        if (!nameList.isEmpty() && !statusList.isEmpty()) {
            returnList.addAll(nameList);
            returnList.retainAll(statusList);
        } else if (!nameList.isEmpty() && !dateList.isEmpty()) {
            returnList.addAll(nameList);
            returnList.retainAll(dateList);
        } else if (!statusList.isEmpty() && !dateList.isEmpty()) {
            returnList.addAll(statusList);
            returnList.retainAll(dateList);
        } else if (!nameList.isEmpty()) {
            returnList.addAll(nameList);
        } else if (!statusList.isEmpty()) {
            returnList.addAll(statusList);
        } else if (!dateList.isEmpty()) {
            returnList.addAll(dateList);
        }

        return returnList;
    }

    public void vacuumScheduler(Long delay, Vacuum v, Status s){
        this.taskScheduler.schedule(() -> {
            v.setStatus(s);
            vacuumRepository.save(v);
        }, new Date(System.currentTimeMillis() + delay));
    }

    public void vacuumSchedulerAdvanced(Long scheduled, Long vacuumId, Status s){
        if (new Date(scheduled).before(new Date(System.currentTimeMillis()))){
            ErrorHistory eh = new ErrorHistory("Zakazano vreme je u proslosti", this.getVacuumById(vacuumId).getAddedByUser(), this.getVacuumById(vacuumId), s);
            this.errorHistoryRepository.save(eh);
            return;
        }

        this.taskScheduler.schedule(() ->{
            Vacuum v = this.getVacuumById(vacuumId);    //uzimamo usisivac iz baze nad kojim je zakazana radnja
            User u = v.getAddedByUser();

            if(!v.isActive()){
                ErrorHistory eh = new ErrorHistory("Usisivac je obrisan", u, v, s);
                this.errorHistoryRepository.save(eh);
                return;
            }
            if(v.getStatus().getStatus().equals("PROCESSING")){
                ErrorHistory eh = new ErrorHistory("Usisivac je bio u fazi obrade zahteva", u, v, s);
                this.errorHistoryRepository.save(eh);
                return;
            }

            if(s.getStatus().equals("RUNNING")){
                if (v.getStatus().getStatus().equals("STOPPED")){
                    v.setCount(v.getCount()+1);
                    this.vacuumScheduler(500L, v, this.statusService.getStatusByName("PROCESSING"));
                    this.vacuumScheduler(10000L, v, s);
                } else {
                    ErrorHistory eh = new ErrorHistory("Usisivac nije bio u adekvatnom stanju", u, v, s);
                    this.errorHistoryRepository.save(eh);
                }
                return;
            }

            if(s.getStatus().equals("STOPPED")){
                if (v.getStatus().getStatus().equals("RUNNING")){
                    if(v.getCount() >= 3){
                        v.setCount(0);
                        this.vacuumScheduler(500L, v, this.statusService.getStatusByName("PROCESSING"));
                        this.vacuumScheduler(10000L, v, this.statusService.getStatusByName("DISCHARGING"));
                        this.vacuumScheduler(25000L, v, s);
                    }else{
                        this.vacuumScheduler(500L, v, this.statusService.getStatusByName("PROCESSING"));
                        this.vacuumScheduler(10000L, v, s);
                    }
                } else {
                    ErrorHistory eh = new ErrorHistory("Usisivac nije bio u adekvatnom stanju", u, v, s);
                    this.errorHistoryRepository.save(eh);
                }
                return;
            }

            if(s.getStatus().equals("DISCHARGING")){
                if (v.getStatus().getStatus().equals("STOPPED")){
                    this.vacuumScheduler(500L, v, this.statusService.getStatusByName("PROCESSING"));
                    this.vacuumScheduler(10000L, v, s);
                    this.vacuumScheduler(25000L, v, this.statusService.getStatusByName("STOPPED"));
                } else {
                    ErrorHistory eh = new ErrorHistory("Usisivac nije bio u adekvatnom stanju", u, v, s);
                    this.errorHistoryRepository.save(eh);
                }
                return;
            }
        }, new Date(scheduled));    //kraj
    }
}

