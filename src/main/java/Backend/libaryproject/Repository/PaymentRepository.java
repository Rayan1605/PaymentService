package Backend.libaryproject.Repository;

import Backend.libaryproject.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PaymentRepository extends JpaRepository<Payment ,Long> {
    Payment findByUserEmail(String userEmail);
}
