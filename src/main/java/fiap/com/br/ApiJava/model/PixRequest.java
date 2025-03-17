package fiap.com.br.ApiJava.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PixRequest {//8° passo criação do pix

    @NotBlank(message = "O numero da conta de origem é obrigatorio.")
    private String sourceAccountNumber;

    @NotBlank(message = "O numero da conta de destino é obrigatorio.")
    private String destinationAccountNumber;

    @Positive(message = "O valor do pix deve ser positivo.")
    private double amount;

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
