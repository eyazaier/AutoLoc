package tn.esprit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repositories.ReservationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationServiceImp implements IReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImp(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Reservation save(Reservation entity) {
        return reservationRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
