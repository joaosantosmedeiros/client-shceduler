package god.joaopedro.client_scheduler.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidatorTest {

    @Nested
    class ItShouldReturnFalse {

        @Test
        public void whenCpfIsNull() {
            assertFalse(CpfValidator.validate(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "123", "113.392.134-511", "12i3ojo"})
        public void ifCpfLengthIsInvalid(String cpf) {
            assertFalse(CpfValidator.validate(cpf));
        }

        @ParameterizedTest
        @ValueSource(strings = {"113.392.134-5a", "aaa.bbb.ccc-dd"})
        public void ifNonDigitsAreInserted(String cpf) {
            assertFalse(CpfValidator.validate(cpf));
        }

        @ParameterizedTest
        @ValueSource(strings = {"113.392.134-41", "731.675.460-60", "180.329.140-95", "263.337.170-20"})
        public void ifTenthDigitIsInvalid(String cpf) {
            assertFalse(CpfValidator.validate(cpf));
        }

        @ParameterizedTest
        @ValueSource(strings = {"113.392.134-52", "939.218.910-95", "802.156.730-94", "448.973.650-97"})
        public void ifEleventhDigitIsInvalid(String cpf) {
            assertFalse(CpfValidator.validate(cpf));
        }

        @ParameterizedTest
        @ValueSource(strings = {"111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44"})
        public void ifCpfIsMadeOfASingleNumber(String cpf) {
            assertFalse(CpfValidator.validate(cpf));
        }
    }

    @Nested
    class ItShouldReturnTrue {

        @ParameterizedTest
        @ValueSource(strings = {"113.392.134-51", "431.533.590-83", "167.456.600-08", "448.973.650-90"})
        public void ifCpfIsValid(String cpf) {
            assertTrue(CpfValidator.validate(cpf));
        }
    }
}