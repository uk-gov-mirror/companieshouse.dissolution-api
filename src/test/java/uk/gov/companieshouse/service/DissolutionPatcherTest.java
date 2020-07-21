package uk.gov.companieshouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.fixtures.DissolutionFixtures;
import uk.gov.companieshouse.mapper.DirectorApprovalMapper;
import uk.gov.companieshouse.mapper.DissolutionResponseMapper;
import uk.gov.companieshouse.mapper.PaymentInformationMapper;
import uk.gov.companieshouse.model.db.dissolution.DirectorApproval;
import uk.gov.companieshouse.model.db.dissolution.Dissolution;
import uk.gov.companieshouse.model.db.dissolution.DissolutionDirector;
import uk.gov.companieshouse.model.db.payment.PaymentInformation;
import uk.gov.companieshouse.model.dto.dissolution.DissolutionPatchResponse;
import uk.gov.companieshouse.model.dto.payment.PaymentPatchRequest;
import uk.gov.companieshouse.model.enums.ApplicationStatus;
import uk.gov.companieshouse.model.enums.PaymentMethod;
import uk.gov.companieshouse.repository.DissolutionRepository;
import uk.gov.companieshouse.service.dissolution.DissolutionPatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.fixtures.PaymentFixtures.generatePaymentInformation;
import static uk.gov.companieshouse.fixtures.PaymentFixtures.generatePaymentPatchRequest;

@ExtendWith(MockitoExtension.class)
public class DissolutionPatcherTest {

    @InjectMocks
    private DissolutionPatcher patcher;

    @Mock
    private DissolutionRepository repository;

    @Mock
    private DissolutionResponseMapper responseMapper;

    @Mock
    private DirectorApprovalMapper approvalMapper;

    @Mock
    private PaymentInformationMapper paymentInformationMapper;

    private static final String COMPANY_NUMBER = "12345678";
    private static final String USER_ID = "1234";
    private static final String EMAIL = "user@mail.com";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String EMAIL_TWO = "two@mail.com";

    private Dissolution dissolution;
    private DissolutionPatchResponse response;
    private DirectorApproval approval;
    private ArgumentCaptor<Dissolution> dissolutionCaptor;

    @BeforeEach
    void init() {
        dissolution = DissolutionFixtures.generateDissolution();
        dissolution.getData().getDirectors().get(0).setEmail(EMAIL);
        response = DissolutionFixtures.generateDissolutionPatchResponse();
        approval = DissolutionFixtures.generateDirectorApproval();
        dissolutionCaptor = ArgumentCaptor.forClass(Dissolution.class);
    }

    @Test
    public void patch_addsApprovalToSingleDirector_savesInDatabase() {
        when(repository.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(java.util.Optional.of(dissolution));
        when(responseMapper.mapToDissolutionPatchResponse(COMPANY_NUMBER)).thenReturn(response);
        when(approvalMapper.mapToDirectorApproval(USER_ID, IP_ADDRESS)).thenReturn(approval);

        patcher.addDirectorApproval(COMPANY_NUMBER, USER_ID, IP_ADDRESS, EMAIL);

        verify(repository).save(dissolutionCaptor.capture());

        assertSame(dissolutionCaptor.getValue().getData().getDirectors().get(0).getDirectorApproval(), approval);
    }

    @Test
    public void patch_updatesStatusToPendingPayment_ifAllDirectorHaveApproved() {
        when(repository.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(java.util.Optional.of(dissolution));
        when(responseMapper.mapToDissolutionPatchResponse(COMPANY_NUMBER)).thenReturn(response);
        when(approvalMapper.mapToDirectorApproval(USER_ID, IP_ADDRESS)).thenReturn(approval);

        final DissolutionPatchResponse result = patcher.addDirectorApproval(COMPANY_NUMBER, USER_ID, IP_ADDRESS, EMAIL);

        verify(responseMapper).mapToDissolutionPatchResponse(COMPANY_NUMBER);
        verify(repository).save(dissolutionCaptor.capture());

        assertEquals(response, result);
        assertEquals(
                ApplicationStatus.PENDING_PAYMENT,
                dissolutionCaptor.getValue().getData().getApplication().getStatus()
        );
    }

    @Test
    public void patch_doesNotUpdateStatus_ifNotAllDirectorHaveApproved() {
        final List<DissolutionDirector> directors = DissolutionFixtures.generateDissolutionDirectorList();
        directors.get(0).setEmail(EMAIL);
        directors.get(1).setEmail(EMAIL_TWO);
        dissolution.getData().setDirectors(directors);

        when(repository.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(java.util.Optional.of(dissolution));
        when(responseMapper.mapToDissolutionPatchResponse(COMPANY_NUMBER)).thenReturn(response);
        when(approvalMapper.mapToDirectorApproval(USER_ID, IP_ADDRESS)).thenReturn(approval);

        patcher.addDirectorApproval(COMPANY_NUMBER, USER_ID, IP_ADDRESS, EMAIL);

        verify(repository).save(dissolutionCaptor.capture());

        assertEquals(
                ApplicationStatus.PENDING_APPROVAL,
                dissolutionCaptor.getValue().getData().getApplication().getStatus()
        );
    }

    @Test
    public void patch_updatesDissolutionWithPaymentInformation_savesInDatabase() {
        PaymentPatchRequest data = generatePaymentPatchRequest();
        PaymentInformation paymentInformation = generatePaymentInformation();

        when(repository.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(java.util.Optional.of(dissolution));
        when(paymentInformationMapper
                .mapToPaymentInformation(PaymentMethod.CREDIT_CARD, data.getPaymentReference(), data.getPaidAt()))
                .thenReturn(paymentInformation);

        patcher.updatePaymentInformation(data.getPaymentReference(), data.getPaidAt(), COMPANY_NUMBER);
        verify(repository).save(dissolutionCaptor.capture());

        assertEquals(paymentInformation, dissolutionCaptor.getValue().getPaymentInformation());
    }
}