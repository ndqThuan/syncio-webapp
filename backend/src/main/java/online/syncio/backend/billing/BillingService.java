package online.syncio.backend.billing;

import online.syncio.backend.exception.NotFoundException;
import online.syncio.backend.label.Label;
import online.syncio.backend.label.LabelRepository;
import online.syncio.backend.user.User;
import online.syncio.backend.user.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billingRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public BillingService(BillingRepository billingRepository, UserRepository userRepository, LabelRepository labelRepository) {
        this.billingRepository = billingRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    // Map to DTO
    private BillingDTO mapToDTO(Billing billing, BillingDTO billingDTO) {
        billingDTO.setUserId(billing.getUser().getId());
        billingDTO.setLabelId(billing.getLabel().getId());
        billingDTO.setAmount(billing.getAmount());
        billingDTO.setCreatedDate(billing.getCreatedDate());
        return billingDTO;
    }

    // Map to Entity
    private Billing mapToEntity(BillingDTO billingDTO, Billing billing) {
        final User user = billingDTO.getUserId() == null ? null : userRepository.findById(billingDTO.getUserId())
                .orElseThrow(() -> new NotFoundException(User.class, "id", billingDTO.getUserId().toString()));
        final Label label = billingDTO.getLabelId() == null ? null : labelRepository.findById(billingDTO.getLabelId())
                .orElseThrow(() -> new NotFoundException(Label.class, "id", billingDTO.getLabelId().toString()));
        billing.setUser(user);
        billing.setLabel(label);
        billing.setAmount(billingDTO.getAmount());
        billing.setCreatedDate(billingDTO.getCreatedDate());
        return billing;
    }

    // Crud
    public List<BillingDTO> findAll(){
        List<Billing> billings = billingRepository.findAll(Sort.by("createdDate"));
        return billings.stream()
                .map(billing -> mapToDTO(billing, new BillingDTO()))
                .toList();
    }

    public void create(BillingDTO billingDTO) {
        Billing billing = new Billing();
        mapToEntity(billingDTO, billing);
        billingRepository.save(billing);
    }
}
