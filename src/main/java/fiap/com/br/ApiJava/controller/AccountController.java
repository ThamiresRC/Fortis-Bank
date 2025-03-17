package fiap.com.br.ApiJava.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fiap.com.br.ApiJava.model.Account;
import fiap.com.br.ApiJava.model.DepositRequest;
import fiap.com.br.ApiJava.model.PixRequest;
import fiap.com.br.ApiJava.model.WithdrawRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final List<Account> accountsRepository = new ArrayList<>();

    @PostMapping("/create") //2° passo criaçao do cadastro da conta
    public ResponseEntity<?> createAccount(@RequestBody @Valid Account account, BindingResult result) {
        if (account.getAccountHolderName() == null || account.getAccountHolderName().isEmpty()) {
            result.addError(new FieldError("account", "accountHolderName", "Nome do titular é obrigatorio"));
        }
        if (account.getAccountHolderCpf() == null) {
            result.addError(new FieldError("account", "accountHolderCpf", "CPF do titular é obrigatorio"));
        }
        if (account.getOpeningDate() == null || account.getOpeningDate().isAfter(LocalDate.now())) {
            result.addError(new FieldError("account", "openingDate", "Data de abertura não pode ser no futuro"));
        }
        if (account.getBalance() < 0) {
            result.addError(new FieldError("account", "balance", "Saldo inicial não pode ser negativo"));
        }
        if (account.getAccountType() == null || !account.getAccountType().matches("corrente|poupança|salário")) {
            result.addError(new FieldError("account", "accountType", "Tipo de conta deve ser 'conta corrente', 'conta poupança' ou 'conta salario'"));
        }

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        accountsRepository.add(account);
        System.out.println("Cadastrando conta: " + account.getAccountHolderName());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping //4° passo criação buscas (retorna tds as contas cadastradas)
    public List<Account> getAllAccounts() {
        return accountsRepository;
    }

    @GetMapping("/{accountNumber}") //4° passo criação buscas (retorna conta por id)
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}") //4° passo criação buscas (retorna conta por cpf)
    public ResponseEntity<Account> getAccountByCpf(@PathVariable Long cpf) {
        return accountsRepository.stream()
                .filter(acc -> acc.getAccountHolderCpf().equals(cpf))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    @PutMapping("/close/{accountNumber}")//5° passo criação encerrar conta (marcar como inativa)
    public ResponseEntity<?> closeAccount(@PathVariable String accountNumber) {
        Account account = accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
        }

        account.setActive(false);
        return ResponseEntity.ok(account);
    }

    
    @PostMapping("/deposit") //6° passo criação deposito
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
        Account account = accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(depositRequest.getAccountNumber()))
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Conta não encontrada.");
        }

        if (depositRequest.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O valor do depósito deve ser positivo.");
        }

        account.setBalance(account.getBalance() + depositRequest.getAmount());

        return ResponseEntity.ok(account);
    }
    
   
    @PostMapping("/withdraw")//7° passo criação do saque
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Account account = accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(withdrawRequest.getAccountNumber()))
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
        }

        if (withdrawRequest.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O valor do saque deve ser positivo.");
        }

        if (account.getBalance() < withdrawRequest.getAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Saldo insuficiente para saque.");
        }

        account.setBalance(account.getBalance() - withdrawRequest.getAmount());

        return ResponseEntity.ok(account);
    }
    
    
    @PostMapping("/pix")//8° passo criação do pix
    public ResponseEntity<?> pix(@RequestBody @Valid PixRequest pixRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        
        // Buscar a conta de origem
        Account sourceAccount = accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(pixRequest.getSourceAccountNumber()))
                .findFirst()
                .orElse(null);
        if (sourceAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta de origem não encontrada.");
        }
        
        // Buscar a conta de destino
        Account destinationAccount = accountsRepository.stream()
                .filter(acc -> acc.getAccountNumber().equals(pixRequest.getDestinationAccountNumber()))
                .findFirst()
                .orElse(null);
        if (destinationAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta de destino não encontrada.");
        }
        
        // Verificar se o valor do pix é positivo
        if (pixRequest.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor do pix deve ser positivo.");
        }
        
        // Verificar se a conta de origem possui saldo suficiente
        if (sourceAccount.getBalance() < pixRequest.getAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente para realizar o pix.");
        }
        
        // Realizar a transferencia: descontar da conta de origem e creditar na conta de destino
        sourceAccount.setBalance(sourceAccount.getBalance() - pixRequest.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + pixRequest.getAmount());
        
        // Retornar os dados atualizados da conta de origem
        return ResponseEntity.ok(sourceAccount);
    }
}
