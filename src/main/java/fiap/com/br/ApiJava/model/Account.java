package fiap.com.br.ApiJava.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class Account {//3° passo criação da Validação

    @NotBlank(message = "O numero da conta é obrigatorio.")
    private String accountNumber;

    @NotBlank(message = "A agencia é obrigatoria.")
    private String agency;

    @NotBlank(message = "O nome do titular é obrigatorio.")
    private String accountHolderName;

    @NotNull(message = "O CPF do titular é obrigatorio.")
    private Long accountHolderCpf;

    @PastOrPresent(message = "A data de abertura não pode ser no futuro.")
    private LocalDate openingDate;

    @PositiveOrZero(message = "O saldo inicial não pode ser negativo.")
    private double balance;

    private boolean active;

    @NotNull(message = "O tipo da conta é obrigatorio.")
    @Pattern(regexp = "corrente|poupança|salario", message = "O tipo deve ser 'corrente', 'poupança' ou 'salario'.")
    private String accountType;

   
    public Account() {
    }

    public Account(String accountNumber, String agency, String accountHolderName, Long accountHolderCpf,
                   LocalDate openingDate, double balance, boolean active, String accountType) {
        this.accountNumber = accountNumber;
        this.agency = agency;
        this.accountHolderName = accountHolderName;
        this.accountHolderCpf = accountHolderCpf;
        this.openingDate = openingDate;
        this.balance = balance;
        this.active = active;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAgency() {
        return agency;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public Long getAccountHolderCpf() {
        return accountHolderCpf;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public String getAccountType() {
        return accountType;
    }

    //Metodo para atualizar saldo 
    public void setBalance(double balance) {
        this.balance = balance;
    }

    //Metodo para atualizar o status, se vai esta ativo ou inativo
    public void setActive(boolean active) {
        this.active = active;
    }
}
