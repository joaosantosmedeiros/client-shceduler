package god.joaopedro.client_scheduler.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrmValidatorTest {

    @Nested
    class ItShouldReturnFalseIf {

        @Test
        void dtoIsNull() {
            assertFalse(CrmValidator.validate(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"  teste com tamanho maior  que 15", "1234567891012134578" })
        void crmLengthGreaterThanFifteen(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

        @ParameterizedTest
        @ValueSource(strings = {"123456", "abcde", "aiosjdoiajd"})
        void crmDontContainsBackslash(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

        @ParameterizedTest
        @ValueSource(strings = {"105/A/C", "//CDA/RN", "BYU15//"})
        void crmContainsMoreThanOneBackslash(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234/RNN", "123456/BC", "abcdef/XY"})
        void crmLastCharactersAreBiggerThanTwo(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

        @ParameterizedTest
        @ValueSource(strings = {"178/TY", "abcd/XZ", "ateae/PO"})
        void crmLastCharactersAreNotValid(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

        @ParameterizedTest
        @ValueSource(strings = {"178A/RN", "177?/RN", "abcde/PI"})
        void crmDigitsAreNotValid(String crm) {
            assertFalse(CrmValidator.validate(crm));
        }

    }

    @Nested
    class ItShouldReturnTrueIf{

        @ParameterizedTest
        @ValueSource(strings = {"123/am", "4567/RN", "77896/PB ", "123587/rj "})
        void crmIsValid(String crm) {
            assertTrue(CrmValidator.validate(crm));
        }
    }

}