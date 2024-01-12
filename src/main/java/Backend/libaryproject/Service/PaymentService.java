package Backend.libaryproject.Service;

import Backend.libaryproject.Entity.Payment;
import Backend.libaryproject.Repository.PaymentRepository;
import Backend.libaryproject.RequestModels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional

public class PaymentService {

    private final PaymentRepository paymentRepository;


    //This automatically injects any dependencies needed into the constructor.
    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          // Getting the secret key from the application.properties file
                          @Value("${stripe.key.secret}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> PaymentMethodTypes = new ArrayList<>();
        //The "card" payment method type is specifically added to the list of allowed payment
        // types for a couple reasons:
        //
        //To explicitly enable and allow card payments for this PaymentIntent.
        //By default, no payment method types are enabled unless specified.
        //So adding "card" indicates that credit/debit card payments should be accepted
        //// Basically This enables credit/debit card payments
        PaymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();

        params.put("amount", paymentInfoRequest.getAmount());
        params.put("currency", paymentInfoRequest.getCurrency());
        params.put("payment_method_types", PaymentMethodTypes);
        //The PaymentIntent object in Stripe represents a payment
        // that is intended to be made in the future.
        // It is used to handle customer payments in Stripe.
        return PaymentIntent.create(params);
    }

    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {
        Payment payment = paymentRepository.findByUserEmail(userEmail);

        if (payment == null) throw new Exception("Information is missing ");

        payment.setAmount(00.00);
        paymentRepository.save(payment);
        //Below will send the 200 status code to the client
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
