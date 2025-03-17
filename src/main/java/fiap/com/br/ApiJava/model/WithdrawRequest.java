package fiap.com.br.ApiJava.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class WithdrawRequest {//7° passo criação do saque

    @NotBlank(message = "O numero da conta é obrigatorio.")
    private String accountNumber;

    @Positive(message = "O valor do saque deve ser positivo.")
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
