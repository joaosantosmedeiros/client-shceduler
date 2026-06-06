package god.joaopedro.client_scheduler.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidatorTest {

    @Test
    public void itShouldReturnFalseOnNull() {
        assertFalse(CpfValidator.validate(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "113.392.134-511", "12i3ojo"})
    public void itShouldReturnFalseOnInvalidLength(String cpf) {
        assertFalse(CpfValidator.validate(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"113.392.134-5a", "aaa.bbb.ccc-dd"})
    public void itShouldReturnFalseIfNonDigitsAreInserted(String cpf) {
        assertFalse(CpfValidator.validate(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"113.392.134-41", "731.675.460-60", "180.329.140-95", "263.337.170-20"})
    public void itShouldReturnFalseIfTenthDigitIsInvalid(String cpf) {
        assertFalse(CpfValidator.validate(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"113.392.134-52", "939.218.910-95", "802.156.730-94", "448.973.650-97"})
    public void itShouldReturnFalseIfEleventhDigitIsInvalid(String cpf) {
        assertFalse(CpfValidator.validate(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44"})
    public void itShouldReturnFalseIfCpfIsMadeOfASingleNumber(String cpf) {
        assertFalse(CpfValidator.validate(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"113.392.134-51", "431.533.590-83", "167.456.600-08", "448.973.650-90"})
    public void itShouldReturnTrueIfCpfIsValid(String cpf) {
        assertTrue(CpfValidator.validate(cpf));
    }
}