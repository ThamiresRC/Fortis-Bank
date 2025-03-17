package fiap.com.br.ApiJava.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class DepositRequest { //6° passo criação do deposito

    @NotBlank(message = "O numero da conta é obrigatorio.")
    private String accountNumber;

    @Positive(message = "O valor do deposito deve ser positivo.")
    private double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
