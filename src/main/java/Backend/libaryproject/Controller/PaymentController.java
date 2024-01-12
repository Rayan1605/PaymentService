package Backend.libaryproject.Controller;

import Backend.libaryproject.Repository.PaymentRepository;
import Backend.libaryproject.RequestModels.PaymentInfoRequest;
import Backend.libaryproject.Service.PaymentService;
import Backend.libaryproject.Utils.ExtractJwt;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private PaymentService paymentService;
    private PaymentRepository paymentRepository;
//This code is creating a PaymentIntent,
// converting it to JSON, and returning it in an API response.
    @PostMapping("/secure/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws Exception {
        PaymentIntent paymentIntent= paymentService.createPaymentIntent(paymentInfoRequest);
        //toJson() converts the PaymentIntent object to a JSON string representation
        //Saves JSON string in paymentJson variable
        String Payment = paymentIntent.toJson();
        return new ResponseEntity<>(Payment, HttpStatus.OK);
    }

    @PutMapping("/secure/payment-Complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization")
                                                        String token) throws Exception {

        String userEmail = ExtractJwt.extractJwtExtraction(token,"\"sub\"");
        if (userEmail == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        return paymentService.stripePayment(userEmail);
    }

    @PostMapping("/save")
    Backend.libaryproject.Entity.Payment save(@RequestBody Backend.libaryproject.Entity.Payment payment){
        return paymentRepository.save(payment);
    }
    @PostMapping("/findByUserEmail")
    Backend.libaryproject.Entity.Payment findByUserEmail(@RequestBody String userEmail){
        return paymentRepository.findByUserEmail(userEmail);
    }

}
