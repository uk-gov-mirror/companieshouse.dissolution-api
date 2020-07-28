package uk.gov.companieshouse.service;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.fixtures.CompanyProfileFixtures;
import uk.gov.companieshouse.fixtures.DissolutionFixtures;
import uk.gov.companieshouse.model.enums.CompanyStatus;
import uk.gov.companieshouse.model.enums.CompanyType;
import uk.gov.companieshouse.service.CompanyClosableValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompanyClosableValidatorTest {

    private final CompanyClosableValidator mapper = new CompanyClosableValidator();

    @Test
    public void mapCompanyDetailsToClosable_companyHasTypeLtdAndIsActive_returnsTrue() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.LTD.getValue());
        company.setCompanyStatus(CompanyStatus.ACTIVE.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertTrue(isClosable);
    }

    @Test
    public void mapCompanyDetailsToClosable_companyHasTypePlcAndIsActive_returnsTrue() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.PLC.getValue());
        company.setCompanyStatus(CompanyStatus.ACTIVE.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertTrue(isClosable);
    }

    @Test
    public void mapCompanyDetailsToClosable_companyHasTypeLtdAndIsDissolved_returnsFalse() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.LTD.getValue());
        company.setCompanyStatus(CompanyStatus.DISSOLVED.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertFalse(isClosable);
    }

    @Test
    public void mapCompanyDetailsToClosable_companyHasTypePlcAndIsDissolved_returnsFalse() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.PLC.getValue());
        company.setCompanyStatus(CompanyStatus.DISSOLVED.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertFalse(isClosable);
    }

    @Test
    public void mapCompanyDetailsToClosable_companyHasNonClosableTypeAndIsActive_returnsFalse() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.EEIG.getValue());
        company.setCompanyStatus(CompanyStatus.ACTIVE.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertFalse(isClosable);
    }

    @Test
    public void mapCompanyDetailsToClosable_companyHasNonClosableTypeAndIsDissolved_returnsFalse() {
        final CompanyProfileApi company = CompanyProfileFixtures.generateCompanyProfileApi();
        company.setType(CompanyType.EEIG.getValue());
        company.setCompanyStatus(CompanyStatus.DISSOLVED.getValue());

        final boolean isClosable = mapper.isCompanyClosable(company);

        assertFalse(isClosable);
    }
}
